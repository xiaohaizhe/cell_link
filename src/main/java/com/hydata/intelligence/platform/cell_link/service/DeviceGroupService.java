package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.Device;
import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import com.hydata.intelligence.platform.cell_link.entity.Scenario;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.DeviceGroupRepository;
import com.hydata.intelligence.platform.cell_link.repository.DeviceRepository;
import com.hydata.intelligence.platform.cell_link.repository.ScenarioRepository;
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

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * @ClassName DeviceGroupService
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/28 14:46
 * @Version
 */
@Transactional
@Service
public class DeviceGroupService {
    @Autowired
    private ScenarioRepository scenarioRepository;
    @Autowired
    private DeviceGroupRepository deviceGroupRepository;
    @Autowired
    private OplogService oplogService;

    private static Logger logger = LogManager.getLogger(DeviceGroupService.class);

    private JSONObject getDeviceGroup(DeviceGroup deviceGroup){
        JSONObject object = new JSONObject();
        object.put("dgId",deviceGroup.getDgId());
        object.put("deviceGroupName",deviceGroup.getDeviceGroupName());
        object.put("description",deviceGroup.getDescription());
        object.put("serialNumber",deviceGroup.getSerialNumber());
        object.put("factory",deviceGroup.getFactory());
        object.put("specification",deviceGroup.getSpecification());
        object.put("parameters",deviceGroup.getParameters());
        object.put("protocol",deviceGroup.getProtocol());
        object.put("created",deviceGroup.getCreated());
        object.put("registrationCode",deviceGroup.getRegistrationCode());
        return object;
    }

    @CacheEvict(value = {"deviceGroup","user","device"},allEntries = true)
    public JSONObject add(DeviceGroup deviceGroup, BindingResult br) {
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            logger.info(deviceGroup);
            if (deviceGroup.getScenario() != null && deviceGroup.getScenario().getScenarioId() != null) {
                Optional<Scenario> scenarioOptional = scenarioRepository.findById(deviceGroup.getScenario().getScenarioId());
                if (scenarioOptional.isPresent()) {
                    List<DeviceGroup> deviceGroupList = deviceGroupRepository.findByScenarioAndDeviceGroupName(
                            deviceGroup.getScenario().getScenarioId(), deviceGroup.getDeviceGroupName());
                    if (deviceGroupList.size() > 0) {
                        return RESCODE.DEVICE_GROUP_NAME_EXIST_IN_SCENARIO.getJSONRES();
                    }
                    Scenario scenario = scenarioOptional.get();
                    deviceGroup.setUserId(scenario.getUser().getUserId());
                    deviceGroup.setRegistrationCode(UUID.randomUUID().toString());
                    DeviceGroup deviceGroupNew = deviceGroupRepository.save(deviceGroup);
                    oplogService.deviceGroup(deviceGroupNew.getUserId(),"添加设备组:"+deviceGroupNew.getDeviceGroupName());
                    return RESCODE.SUCCESS.getJSONRES(deviceGroupNew);
                }
            }
            return RESCODE.SCENARIO_NOT_EXIST.getJSONRES();
        }
        return object;
    }

    @CacheEvict(cacheNames = {"deviceGroup","datastream"},allEntries = true)
    public JSONObject update(DeviceGroup deviceGroup, BindingResult br) {
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            Optional<DeviceGroup> deviceGroupOptional = deviceGroupRepository.findById(deviceGroup.getDgId());
            if (deviceGroupOptional.isPresent()) {
                DeviceGroup deviceGroupOld = deviceGroupOptional.get();
                if (deviceGroup.getDeviceGroupName() != null
                        && !deviceGroup.getDeviceGroupName().equals(deviceGroupOld.getDeviceGroupName())) {
                    List<DeviceGroup> deviceGroupList = deviceGroupRepository.findByScenarioAndDeviceGroupName(
                            deviceGroupOld.getScenario().getScenarioId(), deviceGroup.getDeviceGroupName());
                    if (deviceGroupList.size() > 0) {
                        return RESCODE.DEVICE_GROUP_NAME_EXIST_IN_SCENARIO.getJSONRES();
                    }
                    deviceGroupOld.setDeviceGroupName(deviceGroup.getDeviceGroupName());
                }
                if (deviceGroup.getDescription() != null
                        && !deviceGroup.getDescription().equals(deviceGroupOld.getDescription())) {
                    deviceGroupOld.setDescription(deviceGroup.getDescription());
                }
                if (deviceGroup.getSerialNumber() != null
                        && !deviceGroup.getSerialNumber().equals(deviceGroupOld.getSerialNumber())) {
                    deviceGroupOld.setSerialNumber(deviceGroup.getSerialNumber());
                }
                if (deviceGroup.getFactory() != null
                        && !deviceGroup.getFactory().equals(deviceGroupOld.getFactory())) {
                    deviceGroupOld.setFactory(deviceGroup.getFactory());
                }
                if (deviceGroup.getSpecification()!=null
                        && !deviceGroup.getSpecification().equals(deviceGroupOld.getSpecification())){
                    deviceGroupOld.setSpecification(deviceGroup.getSpecification());
                }
                if (deviceGroup.getParameters()!=null
                        && !deviceGroup.getParameters().equals(deviceGroupOld.getParameters())){
                    deviceGroupOld.setParameters(deviceGroup.getParameters());
                }
                DeviceGroup deviceGroupNew = deviceGroupRepository.saveAndFlush(deviceGroupOld);
                oplogService.deviceGroup(deviceGroupNew.getUserId(),"修改设备组:"+deviceGroupNew.getDeviceGroupName());
                return RESCODE.SUCCESS.getJSONRES(getDeviceGroup(deviceGroupNew));
            }
            return RESCODE.DEVICE_GROUP_NOT_EXIST.getJSONRES();
        }
        return object;
    }

    @CacheEvict(cacheNames = {"deviceGroup","user","device","datastream"},allEntries = true)
    public JSONObject delete(Long dgId){
        Optional<DeviceGroup> deviceGroupOptional = deviceGroupRepository.findById(dgId);
        if (deviceGroupOptional.isPresent()){
            DeviceGroup deviceGroup = deviceGroupOptional.get();
            deviceGroupRepository.deleteById(dgId);
            oplogService.deviceGroup(deviceGroup.getUserId(),"删除设备组:"+deviceGroup.getDeviceGroupName());
            return RESCODE.SUCCESS.getJSONRES();
        }return RESCODE.DEVICE_GROUP_NOT_EXIST.getJSONRES();
    }

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceService deviceService;
    //设备组详情和设备分页数据
    @Cacheable(cacheNames = "deviceGroup",keyGenerator = "myKeyGenerator")
    public JSONObject findById(Long dgId,String deviceName,Integer status,String start,String end,Integer page,Integer number,String sorts){
        Optional<DeviceGroup> deviceGroupOptional = deviceGroupRepository.findById(dgId);
        if (deviceGroupOptional.isPresent()){
            DeviceGroup deviceGroup = deviceGroupOptional.get();
            Pageable pageable = PageUtils.getPage(page, number, sorts);
            Page<Device> devicePage = null;
            devicePage = deviceRepository.findAll((Specification<Device>) (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicateList = new ArrayList<>();
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
                predicateList.add(
                        criteriaBuilder.equal(
                                root.get("deviceGroup").as(DeviceGroup.class),
                                dgId));
                if (deviceName != null && !deviceName.equals("")) {
                    logger.info("deviceName:" + deviceName);
                    predicateList.add(
                            //like：模糊匹配，跟SQL是一样的
                            criteriaBuilder.like(
                                    //user表里面有个String类型的name
                                    root.get("deviceName").as(String.class),
                                    //映射规则
                                    "%" + deviceName + "%"));
                }
                if (status != null && status >= 0) {
                    logger.info("status:" + status);
                    predicateList.add(
                            criteriaBuilder.equal(
                                    root.get("status").as(Integer.class),
                                    status));
                }

                Predicate[] predicates = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(predicates));
            }, pageable);
            List<JSONObject> deviceList = new ArrayList<>();
            for (Device device:devicePage.getContent()){
                deviceList.add(deviceService.getDevice(device));
            }
            JSONObject result = new JSONObject();
            result.put("deviceList",deviceList);
            result.put("deviceGroup",getDeviceGroup(deviceGroup));
            return RESCODE.SUCCESS.getJSONRES(result,devicePage.getTotalPages(),devicePage.getTotalElements());
        }return RESCODE.DEVICE_GROUP_NOT_EXIST.getJSONRES();
    }

    @Cacheable(cacheNames = "deviceGroup",keyGenerator = "myKeyGenerator")
    public JSONObject findListByScenario(Long scenarioId){
        List<DeviceGroup> deviceGroups= deviceGroupRepository.findByScenario(scenarioId);
        List<JSONObject> deviceGroupList = new ArrayList<>();
        for (DeviceGroup deviceGroup:deviceGroups){
            deviceGroupList.add(getDeviceGroup(deviceGroup));
        }
        return RESCODE.SUCCESS.getJSONRES(deviceGroupList);
    }

    @Cacheable(cacheNames = "deviceGroup",keyGenerator = "myKeyGenerator")
    public JSONObject findByScenario(Long scenario_id, Integer page, Integer number,String sorts, String device_group_name){
        if (scenarioRepository.existsById(scenario_id)){
            Pageable pageable = PageUtils.getPage(page, number, sorts);
            Page<DeviceGroup> deviceGroupPage = deviceGroupRepository.findByScenarioAndDeviceGroupName(
                    scenario_id,device_group_name,pageable);
            List<JSONObject> deviceGroupList = new ArrayList<>();
            for (DeviceGroup deviceGroup:deviceGroupPage.getContent()){
                deviceGroupList.add(getDeviceGroup(deviceGroup));
            }
            return RESCODE.SUCCESS.getJSONRES(deviceGroupList,deviceGroupPage.getTotalPages(),deviceGroupPage.getTotalElements());
        }return RESCODE.SCENARIO_NOT_EXIST.getJSONRES();
    }
}
