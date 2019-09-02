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

    public JSONObject getDevice(Device device){
        JSONObject object = new JSONObject();
        object.put("deviceId",device.getDeviceId());
        object.put("deviceName",device.getDeviceName());
        object.put("devicesn",device.getDevicesn());
        object.put("longitude",device.getLongitude());
        object.put("latitude",device.getLatitude());
        object.put("description",device.getDescription());
        object.put("status",device.getStatus());
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
     * @param device 设备
     * @param br 校验结果
     * @return 结果
     */
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
                    if (device.getLongitude()!=null && device.getLongitude()!= deviceOld.getLongitude()){
                        deviceOld.setLongitude(device.getLongitude());
                    }
                    if (device.getLatitude()!=null && device.getLatitude()!= deviceOld.getLatitude()){
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
     * @param deviceId 设备id
     * @return 结果
     */
    public JSONObject delete(Long deviceId){
        if (deviceRepository.existsById(deviceId)){
            deviceRepository.deleteById(deviceId);
            return RESCODE.SUCCESS.getJSONRES();
        }return RESCODE.DEVICE_NOT_EXIST.getJSONRES();
    }

    /**
     * 根据设备id获取设备详情
     * @param deviceId 设备id
     * @return 结果
     */
    public JSONObject findById(Long deviceId){
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if (deviceOptional.isPresent()){
            Device device = deviceOptional.get();
            return RESCODE.SUCCESS.getJSONRES(getDevice(device));
        }return RESCODE.DEVICE_NOT_EXIST.getJSONRES();
    }
}
