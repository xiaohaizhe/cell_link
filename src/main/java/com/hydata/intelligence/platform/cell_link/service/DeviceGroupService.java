package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import com.hydata.intelligence.platform.cell_link.entity.Scenario;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.DeviceGroupRepository;
import com.hydata.intelligence.platform.cell_link.repository.ScenarioRepository;
import com.hydata.intelligence.platform.cell_link.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

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
        return object;
    }

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
                    DeviceGroup deviceGroupNew = deviceGroupRepository.save(deviceGroup);
                    return RESCODE.SUCCESS.getJSONRES(deviceGroupNew);
                }
            }
            return RESCODE.SCENARIO_NOT_EXIST.getJSONRES();
        }
        return object;
    }

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
                return RESCODE.SUCCESS.getJSONRES(getDeviceGroup(deviceGroupNew));
            }
            return RESCODE.DEVICE_GROUP_NOT_EXIST.getJSONRES();
        }
        return object;
    }
}
