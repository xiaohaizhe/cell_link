package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.Datapoint;
import com.hydata.intelligence.platform.cell_link.entity.Device;
import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.*;
import com.hydata.intelligence.platform.cell_link.utils.Constants;
import com.hydata.intelligence.platform.cell_link.utils.ExcelUtils;
import com.hydata.intelligence.platform.cell_link.utils.PageUtils;
import com.hydata.intelligence.platform.cell_link.utils.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @ClassName DeviceService
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/30 10:24
 * @Version
 */
@Transactional
@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceGroupRepository deviceGroupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OplogService oplogService;
    @Autowired
    private DatapointRepository datapointRepository;

    private static Logger logger = LogManager.getLogger(DeviceService.class);

    public JSONObject getDevice(Device device) {
        JSONObject object = new JSONObject();
        object.put("deviceId", device.getDeviceId());
        object.put("deviceName", device.getDeviceName());
        object.put("devicesn", device.getDevicesn());
        object.put("longitude", device.getLongitude());
        object.put("latitude", device.getLatitude());
        object.put("description", device.getDescription());
        object.put("status", device.getStatus());
        object.put("created", device.getCreated());
        object.put("modified", device.getModified());
        object.put("protocol", device.getDeviceGroup().getProtocol().getProtocolName());
        object.put("dgId", device.getDeviceGroup().getDgId());
        object.put("deviceGroupName", device.getDeviceGroup().getDeviceGroupName());
        object.put("scenarioId", device.getDeviceGroup().getScenario().getScenarioId());
        object.put("scenarioName", device.getDeviceGroup().getScenario().getScenarioName());
        return object;
    }

    /**
     * 添加设备
     * 设备名称与鉴权信息不能重复
     *
     * @param device 设备
     * @param br     校验结果
     * @return 结果
     */
    @CacheEvict(cacheNames = {"deviceGroup", "device", "log"}, allEntries = true)
    public JSONObject add(Device device, BindingResult br) {
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            if (device.getDeviceGroup() != null
                    && device.getDeviceGroup().getDgId() != null) {
                Optional<DeviceGroup> deviceGroupOptional = deviceGroupRepository.findById(device.getDeviceGroup().getDgId());
                if (deviceGroupOptional.isPresent()) {
                    List<Device> deviceList = deviceRepository.findByDeviceNameAndDeviceGroup(device.getDeviceName(),
                            device.getDeviceGroup().getDgId());
                    if (deviceList.size() > 0) {
                        return RESCODE.DEVICE_NAME_EXIST_IN_DEVICE_GROUP.getJSONRES();
                    }
                    List<Device> deviceList1 = deviceRepository.findByDevicesnAndDeviceGroup(device.getDevicesn(),
                            device.getDeviceGroup().getDgId());
                    if (deviceList1.size() > 0) {
                        return RESCODE.DEVICESN_EXIST_IN_DEVICE_GROUP.getJSONRES();
                    }
                    DeviceGroup deviceGroup = deviceGroupOptional.get();
                    device.setScenarioId(deviceGroup.getScenario().getScenarioId());
                    device.setUserId(deviceGroup.getScenario().getUser().getUserId());
                    device.setStatus(1);
                    Device deviceNew = deviceRepository.save(device);
                    oplogService.device(deviceNew.getUserId(), "添加设备:" + deviceNew.getDeviceName());
                    return RESCODE.SUCCESS.getJSONRES(deviceNew);
                }
                return RESCODE.DEVICE_GROUP_NOT_EXIST.getJSONRES();
            }
            return RESCODE.DEVICE_GROUP_NOT_EXIST.getJSONRES();
        }
        return object;
    }

    /**
     * 修改设备：
     * 名称、描述、经纬度可修改
     *
     * @param device 设备
     * @param br     校验结果
     * @return 结果
     */
    @CacheEvict(cacheNames = {"device", "deviceGroup", "datastream", "log"}, allEntries = true)
    public JSONObject update(Device device, BindingResult br) {
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            if (device.getDeviceId() != null) {
                Optional<Device> deviceOptional = deviceRepository.findById(device.getDeviceId());
                if (deviceOptional.isPresent()) {
                    Device deviceOld = deviceOptional.get();
                    if (device.getDeviceName() != null
                            && !device.getDeviceName().equals(deviceOld.getDeviceName())) {
                        List<Device> deviceList = deviceRepository.findByDeviceNameAndDeviceGroup(device.getDeviceName(),
                                deviceOld.getDeviceGroup().getDgId());
                        if (deviceList.size() > 0) {
                            return RESCODE.DEVICE_NAME_EXIST_IN_DEVICE_GROUP.getJSONRES();
                        }
                        deviceOld.setDeviceName(device.getDeviceName());
                    }
                    if (device.getDescription() != null
                            && !device.getDescription().equals(deviceOld.getDescription())) {
                        deviceOld.setDescription(device.getDescription());
                    }
                    if (device.getLongitude() != null && device.getLongitude() != deviceOld.getLongitude()) {
                        deviceOld.setLongitude(device.getLongitude());
                    }
                    if (device.getLatitude() != null && device.getLatitude() != deviceOld.getLatitude()) {
                        deviceOld.setLatitude(device.getLatitude());
                    }
                    Device deviceNew = deviceRepository.saveAndFlush(deviceOld);
                    oplogService.device(deviceNew.getUserId(), "修改设备:" + deviceNew.getDeviceName());
                    return RESCODE.SUCCESS.getJSONRES(getDevice(deviceNew));
                }
            }
            return RESCODE.DEVICE_NOT_EXIST.getJSONRES();
        }
        return object;
    }

    /**
     * 根据设备id删除设备
     *
     * @param deviceId 设备id
     * @return 结果
     */
    @CacheEvict(cacheNames = {"device", "deviceGroup", "datastream", "log"}, allEntries = true)
    public JSONObject delete(Long deviceId) {
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            deviceRepository.deleteById(deviceId);
            oplogService.device(device.getUserId(), "删除设备:" + device.getDeviceName());
            return RESCODE.SUCCESS.getJSONRES();
        }
        return RESCODE.DEVICE_NOT_EXIST.getJSONRES();
    }

    /**
     * 根据设备id获取设备详情
     *
     * @param deviceId 设备id
     * @return 结果
     */
    @Cacheable(cacheNames = "device")
    public JSONObject findById(Long deviceId) {
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            return RESCODE.SUCCESS.getJSONRES(getDevice(device));
        }
        return RESCODE.DEVICE_NOT_EXIST.getJSONRES();
    }

    /**
     * 根据设备名称/场景/设备组查询设备分页
     *
     * @param user_id     用户id 必填
     * @param device_name 设备名称
     * @param page        页码 必填
     * @param number      每页显示数量 必填
     * @param sorts       排序条件
     * @param scenario_id 场景id
     * @param dg_id       设备组id
     * @param start       设备创建开始时间
     * @param end         设备创建结束时间
     * @param status      设备状态
     * @return 结果
     */
    @Cacheable(cacheNames = "device", keyGenerator = "myKeyGenerator")
    public JSONObject findByDeviceName(Long user_id, String device_name, Integer page, Integer number, String sorts,
                                       Long scenario_id, Long dg_id, String start, String end, Integer status) {

        Pageable pageable = PageUtils.getPage(page, number, sorts);
        Page<Device> devicePage = null;
        devicePage = deviceRepository.findAll(getSpecification(user_id, device_name, scenario_id, dg_id, start, end, status), pageable);
        List<JSONObject> deviceList = new ArrayList<>();
        for (Device device : devicePage.getContent()) {
            deviceList.add(getDevice(device));
        }
        return RESCODE.SUCCESS.getJSONRES(deviceList, devicePage.getTotalPages(), devicePage.getTotalElements());
    }

    private Specification<Device> getSpecification(Long userId, String deviceName, Long scenarioId, Long dgId, String start, String end, Integer status) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (userId != null && userId >= 0) {
                logger.info("userId:" + userId);
                predicateList.add(
                        criteriaBuilder.equal(
                                root.get("userId").as(Long.class),
                                userId));
            }
            if (deviceName != null && !deviceName.equals("")) {
                logger.info("device_name:" + deviceName);
                predicateList.add(
                        //like：模糊匹配，跟SQL是一样的
                        criteriaBuilder.like(
                                //user表里面有个String类型的name
                                root.get("deviceName").as(String.class),
                                //映射规则
                                "%" + deviceName + "%"));
            }
            if (scenarioId != null && scenarioId >= 0) {
                logger.info("scenario_id:" + scenarioId);
                predicateList.add(
                        criteriaBuilder.equal(
                                root.get("scenarioId").as(Long.class),
                                scenarioId));
            }
            if (dgId != null && dgId >= 0) {
                logger.info("dg_id:" + dgId);
                predicateList.add(
                        criteriaBuilder.equal(
                                root.get("deviceGroup").as(DeviceGroup.class),
                                dgId));
            }
            if (status != null && status >= 0) {
                logger.info("status:" + status);
                predicateList.add(
                        criteriaBuilder.equal(
                                root.get("status").as(Integer.class),
                                status));
            }
            if (start != null && !start.equals("") && end != null && !end.equals("")) {
                logger.info("start:" + start);
                logger.info("end:" + end);
                Date s = StringUtil.getDate(start);
                Date e = StringUtil.getDate(end);
                if (s != null && e != null) {
                    predicateList.add(
                            criteriaBuilder.between(
                                    root.get("created").as(Date.class), s, e));
                }
            }
            Predicate[] predicates = new Predicate[predicateList.size()];
            return criteriaBuilder.and(predicateList.toArray(predicates));
        };
    }

    public void export(Long userId, String deviceName, Long scenarioId, Long dgId, String start, String end, Integer status,
                       HttpServletRequest request, HttpServletResponse response) {
        List<Device> deviceList = deviceRepository.findAll(
                getSpecification(userId, deviceName, scenarioId, dgId, start, end, status));
        List<Map<String, Object>> list = new ArrayList<>();
        for (Device device : deviceList) {
            Map map = new HashMap();
            map.put("设备名称", device.getDeviceName());
            map.put("设备id", device.getDeviceId().toString());
            map.put("设备鉴权信息", device.getDevicesn());
            map.put("状态",device.getStatus()==1?"正常":"异常");
            map.put("创建时间",device.getCreated());
            list.add(map);
        }
        ExcelUtils.exportExcel("device", list, request, response);
    }

    /*    public JSONObject findByDeviceName(Long user_id, String device_name, Integer page, Integer number, String sorts) {

            Pageable pageable = PageUtils.getPage(page, number, sorts);
            Page<Device> devicePage = deviceRepository.findByUserIdAndDeviceName(user_id, device_name, pageable);
            List<JSONObject> deviceList = new ArrayList<>();
            for (Device device : devicePage.getContent()) {
                deviceList.add(getDevice(device));
            }
            return RESCODE.SUCCESS.getJSONRES(deviceList, devicePage.getTotalPages(), devicePage.getTotalElements());
        }*/
    @Autowired
    private AppRepository appRepository;

    /**
     * 设备概况-用户下设备组和应用数量 -设备异常总览
     *
     * @param userId
     * @return
     */
    @Cacheable(cacheNames = "device", keyGenerator = "myKeyGenerator")
    public JSONObject getOverview(Long userId) {
        if (userRepository.existsById(userId)) {
            Long dgSum = deviceGroupRepository.findByUserId(userId);
            Long appSum = appRepository.findByUserId(userId);
            JSONObject object = new JSONObject();
            object.put("dgSum", dgSum);
            object.put("appSum", appSum);
            List<Device> deviceList = deviceRepository.findByUserId(userId);
            int deviceSum = deviceList.size();
            int deviceSum_normal = 0;
            for (Device device :
                    deviceList) {
                if (device.getStatus() != null && device.getStatus() == 1) deviceSum_normal++;
            }
            int deviceSum_abnormal = deviceSum - deviceSum_normal;
            JSONObject deviceStatus = new JSONObject();
            deviceStatus.put("deviceSum", deviceSum);
            deviceStatus.put("deviceSum_normal", deviceSum_normal);
            deviceStatus.put("deviceSum_abnormal", deviceSum_abnormal);
            object.put("device", deviceStatus);
            return RESCODE.SUCCESS.getJSONRES(object);
        }
        return RESCODE.USER_NOT_EXIST.getJSONRES();
    }

    /**
     * 设备趋势分析
     *
     * @param userId
     * @return
     */
    @Cacheable(cacheNames = "device", keyGenerator = "myKeyGenerator")
    public JSONObject getIncrement(Long userId, String start, String end) {
        if (userRepository.existsById(userId)) {
            Date s = StringUtil.getDate(start);
            Date e = StringUtil.getDate(end);
            if (e.getTime() > new Date().getTime()) {
                e = new Date();
            }
            List<Device> deviceList = deviceRepository.findByUserIdAndCreatedBetween(userId, s, e);
            int length = (int) ((e.getTime() - s.getTime()) / 1000 / 60 / 60 / 24);
            logger.debug("共需循环" + (length + 1) + "次");
            //		趋势分析图表数据
            JSONArray array = new JSONArray();

            Date sdate = (Date) s.clone();
            Date edate = new Date();
            for (int i = 0; i < length; i++) {
                logger.debug("第" + (i + 1) + "次");
                edate = (Date) sdate.clone();
                edate.setDate(edate.getDate() + 1);
                edate.setHours(0);
                edate.setMinutes(0);
                edate.setSeconds(0);
                logger.debug("开始：" + StringUtil.getDateString(sdate));
                logger.debug("结束：" + StringUtil.getDateString(edate));
                array.add(getOneDayData(deviceList, sdate, edate));
                sdate = (Date) edate.clone();
            }
            logger.debug("最后一次");
            edate = StringUtil.getDate(end);
            logger.debug("开始：" + StringUtil.getDateString(sdate));
            logger.debug("结束：" + StringUtil.getDateString(edate));
            array.add(getOneDayData(deviceList, sdate, edate));
            return RESCODE.SUCCESS.getJSONRES(array);
        }
        return RESCODE.USER_NOT_EXIST.getJSONRES();
    }

    @Cacheable(cacheNames = "device", keyGenerator = "myKeyGenerator")
    public JSONObject findByDgId(Long dgId){
        Optional<DeviceGroup> deviceGroupOptional = deviceGroupRepository.findById(dgId);
        if (deviceGroupOptional.isPresent()){
            List<Device> deviceList = deviceRepository.findByDeviceGroup(dgId);
            JSONArray array = new JSONArray();
            for (Device device:
                 deviceList) {
                array.add(getDevice(device));
            }
            return RESCODE.SUCCESS.getJSONRES(array);
        }return RESCODE.DEVICE_NOT_EXIST.getJSONRES();
    }

    private JSONObject getOneDayData(List<Device> deviceList, Date sdate, Date edate) {
        JSONObject object = new JSONObject();
        int count = 0;
        for (Device device : deviceList) {
            if (device.getCreated().getTime() >= sdate.getTime() && device.getCreated().getTime() < edate.getTime()) {
                count++;
            }
        }
        object.put("time", sdate);
        object.put("value", count);
        return object;
    }
    private boolean checkDevice(String name,String devicesn,Long dgId){
        List<Device> deviceList = deviceRepository.findByDeviceNameOrDevicesnAndDeviceGroup(name,devicesn,dgId);
        return deviceList.size() <= 0;
    }

    public JSONObject importFile(MultipartFile file, Long dgId, HttpServletRequest request){
        logger.debug("解析表格中的设备信息并保存");
        JSONObject result = new JSONObject();
        JSONObject failMsg = new JSONObject();

        JSONObject fail1 = new JSONObject();
        fail1.put("msg","设备名或鉴权信息已存在");
        JSONArray failData1 = new JSONArray();

        JSONObject fail2 = new JSONObject();
        fail2.put("msg","鉴权信息必须包含数字和字母，长度为4-32");
        JSONArray failData2 = new JSONArray();

        Optional<DeviceGroup> deviceGroupOptional = deviceGroupRepository.findById(dgId);
        if (deviceGroupOptional.isPresent()) {
            DeviceGroup deviceGroup = deviceGroupOptional.get();
            JSONObject objectReturn = ExcelUtils.importExcel(file);
            if ((Integer) objectReturn.get("code") == 0) {
                JSONArray array = objectReturn.getJSONArray("data");
                int count = 0;
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Set<String> keys = object.keySet();
                    Iterator<String> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        JSONObject value = (JSONObject) object.get(key);
                        Set<String> names = value.keySet();
                        Iterator<String> it = names.iterator();
                        while (it.hasNext()) {
                            String name = it.next();
                            String devicesn = (String) value.get(name);
                            logger.debug(name + ":" + devicesn);
                            //Optional<Device> deviceOptional = deviceRepository.findByProductIdAndDeviceSn(productId, devicesn);
                            boolean isExist = checkDevice(name,devicesn, dgId);
                            logger.info("检查添加设备的鉴权信息是否重复");
                            if (!isExist) {
                                count++;
                                failData1.add(key);
//                                failMsg.put(key, "鉴权信息已存在");
                                logger.info("编号为：" + key + "的设备数据鉴权信息重复");
                                continue;
                            }
                            if (StringUtil.check(devicesn)) {
                                count++;
//                                failMsg.put(key, "鉴权信息包含数字以外字符");
                                failData2.add(key);
                                logger.info("编号为：" + key + "的设备数据鉴权信息包含数字以外字符");
                                continue;
                            }
                            logger.info("编号为：" + key + "的设备数据鉴权信息有效");
                            Device device = new Device();
                            device.setDevicesn(devicesn);
                            device.setDeviceName(name);
                            device.setDeviceGroup(deviceGroup);
                            device.setUserId(deviceGroup.getUserId());
                            device.setScenarioId(deviceGroup.getScenario().getScenarioId());
                            device.setStatus(1);
                            deviceRepository.save(device);
                            /*if (product.getProtocolId() == 1) {
                                logger.debug("设备协议id为1，即MQTT");
                                try {
                                    mqttHandler.mqttAddDevice(device.getId().toString());
                                } catch (MqttException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }*/
                        }
                        fail1.put("data",failData1);
                        fail2.put("data",failData2);
                        JSONArray errNo = new JSONArray();
                        errNo.add(fail1);
                        errNo.add(fail2);
                        failMsg.put("errNo",errNo);
                        failMsg.put("sum", count);
                    }
                }
                result.put("sum", array.size());
                result.put("success", array.size() - count);
                result.put("fail", failMsg);

                oplogService.device(deviceGroup.getUserId(),"批量添加设备");
                return RESCODE.SUCCESS.getJSONRES(result);
            } else {
                return objectReturn;
            }
        }
        return RESCODE.DEVICE_GROUP_NOT_EXIST.getJSONRES();
    }

    /**
     * 判断近100个数据点异常情况
     *
     * @param ds_id: data_history_id
     * @return object: 返回数据流
     */
    public JSONObject checkStatus(long ds_id) {
        Pageable pageable = new PageRequest(0, 100, Sort.Direction.DESC, "create_time");
        Page<Datapoint> data_historyPage = datapointRepository.findByfindByDatastreamId(ds_id, pageable);

        if (data_historyPage != null) {
            //logger.info("开始判断最近100条数据流的异常情况");
            List<Datapoint> data_histories = data_historyPage.getContent();
            if (data_histories.size() < 100) {
                return RESCODE.INSUFFICIENT_DATA.getJSONRES();
            }
            Date last = data_histories.get(0).getCreated();
            Date curr;
            long total = 0;
            for (int i = 1; i < 101; i++) {
                Datapoint data_history = data_histories.get(i);
                curr = data_history.getCreated();
                long delta = last.getTime() - curr.getTime();
                last = curr;
                total += delta;
            }
            long freq = total / 100;
            //logger.info("最近100条数据流的平均频率是" + freq + "毫秒");
            last = data_histories.get(0).getCreated();
            for (int i = 1; i < 101; i++) {
                Datapoint data_history = data_histories.get(i);
                curr = data_history.getCreated();
                long delta = last.getTime() - curr.getTime();
                if (delta < freq * 0.5) {
                    data_history.setStatus(1);
                } else if (delta > freq * 1.5) {
                    data_history.setStatus(2);
                } else {
                    data_history.setStatus(0);
                }
                datapointRepository.save(data_history);
                last = curr;
            }
            data_historyPage = datapointRepository.findByDd_id(ds_id, pageable);
            //logger.info("数据流诊断结果：" + data_historyPage.getContent());
            return RESCODE.SUCCESS.getJSONRES(data_historyPage.getContent());

        }
        return RESCODE.DATASTREAM_NOT_EXIST.getJSONRES(ds_id);

    }

}
