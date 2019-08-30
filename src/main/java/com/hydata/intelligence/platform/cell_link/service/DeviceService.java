package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.Device;
import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.DeviceGroupRepository;
import com.hydata.intelligence.platform.cell_link.repository.DeviceRepository;
import com.hydata.intelligence.platform.cell_link.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

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

    public JSONObject add(Device device, BindingResult br) {
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            if (device.getDeviceGroup()!=null
                    && device.getDeviceGroup().getDgId()!=null){
                Optional<DeviceGroup> deviceGroupOptional = deviceGroupRepository.findById(device.getDeviceGroup().getDgId());
                if (deviceGroupOptional.isPresent()){
                    List<Device> deviceList = deviceRepository.findByDeviceNameAndDeviceGroup(device.getDeviceName(),
                            device.getDeviceGroup().getDgId());
                    if (deviceList.size()>0){
                        return RESCODE.DEVICE_EXIST_IN_DEVICE_GROUP.getJSONRES();
                    }
                    DeviceGroup deviceGroup = deviceGroupOptional.get();
                    device.setScenarioId(deviceGroup.getScenario().getScenarioId());
                    device.setUserId(deviceGroup.getScenario().getUser().getUserId());
                    device.setStatus(1);
                    Device deviceNew = deviceRepository.save(device);
                    return RESCODE.SUCCESS.getJSONRES(deviceNew);
                }return RESCODE.DEVICE_GROUP_NOT_EXIST.getJSONRES();
            }return RESCODE.DEVICE_GROUP_NOT_EXIST.getJSONRES();
        }
        return object;
    }
}
