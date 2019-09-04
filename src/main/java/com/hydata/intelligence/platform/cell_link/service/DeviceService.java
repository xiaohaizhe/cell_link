package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.Device;
import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.DeviceGroupRepository;
import com.hydata.intelligence.platform.cell_link.repository.DeviceRepository;
import com.hydata.intelligence.platform.cell_link.utils.Constants;
import com.hydata.intelligence.platform.cell_link.utils.PageUtils;
import com.hydata.intelligence.platform.cell_link.utils.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    @CacheEvict(cacheNames = "device", key = "#p0.deviceId", allEntries = true)
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
    @CacheEvict(cacheNames = "device", allEntries = true)
    public JSONObject delete(Long deviceId) {
        if (deviceRepository.existsById(deviceId)) {
            deviceRepository.deleteById(deviceId);
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
    public JSONObject findByDeviceName(Long user_id, String device_name, Integer page, Integer number, String sorts,
                                       Long scenario_id, Long dg_id, String start, String end, Integer status) {

        Pageable pageable = PageUtils.getPage(page, number, sorts);
        Page<Device> devicePage = null;
        devicePage = deviceRepository.findAll((Specification<Device>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (user_id != null && user_id >= 0) {
                logger.info("userId:"+user_id);
                predicateList.add(
                        criteriaBuilder.equal(
                                root.get("userId").as(Long.class),
                                user_id));
            }
            if (device_name != null && !device_name.equals("")) {
                logger.info("device_name:"+device_name);
                predicateList.add(
                        //like：模糊匹配，跟SQL是一样的
                        criteriaBuilder.like(
                                //user表里面有个String类型的name
                                root.get("deviceName").as(String.class),
                                //映射规则
                                "%" + device_name + "%"));
            }
            if (scenario_id != null && scenario_id >= 0) {
                logger.info("scenario_id:"+scenario_id);
                predicateList.add(
                        criteriaBuilder.equal(
                                root.get("scenarioId").as(Long.class),
                                scenario_id));
            }
            if (dg_id != null && dg_id >= 0) {
                logger.info("dg_id:"+dg_id);
                predicateList.add(
                        criteriaBuilder.equal(
                                root.get("deviceGroup").as(DeviceGroup.class),
                                dg_id));
            }
            if (status != null && status >= 0) {
                logger.info("status:"+status);
                predicateList.add(
                        criteriaBuilder.equal(
                                root.get("status").as(Integer.class),
                                status));
            }
            if (start != null && !start.equals("") && end != null && !end.equals("")) {
                logger.info("start:"+start);
                logger.info("end:"+end);
                Date s = StringUtil.getDate(start);
                Date e = StringUtil.getDate(end);
                if (s != null && e != null) {
                    predicateList.add(
                            criteriaBuilder.between(
                                    root.get("created").as(Date.class),s,e));
                }
            }
            Predicate[] predicates = new Predicate[predicateList.size()];
            return criteriaBuilder.and(predicateList.toArray(predicates));
        }, pageable);
        List<JSONObject> deviceList = new ArrayList<>();
        for (Device device : devicePage.getContent()) {
            deviceList.add(getDevice(device));
        }
        return RESCODE.SUCCESS.getJSONRES(deviceList, devicePage.getTotalPages(), devicePage.getTotalElements());
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
}
