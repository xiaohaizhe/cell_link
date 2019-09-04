package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.Datastream;
import com.hydata.intelligence.platform.cell_link.entity.Device;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.DatastreamRepository;
import com.hydata.intelligence.platform.cell_link.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @ClassName DatastreamService
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/4 18:09
 * @Version
 */
@Service
@Transactional
public class DatastreamService {
    @Autowired
    private DatastreamRepository datastreamRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    public JSONObject add(Datastream datastream){
        if (datastream.getDevice()!=null && datastream.getDevice().getDeviceId()!=null){
            System.out.println(datastream);
            System.out.println(datastream.getDevice().getDeviceId());
            Optional<Device> deviceOptional = deviceRepository.findById(datastream.getDevice().getDeviceId());
            if (deviceOptional.isPresent()){
                Device device = deviceOptional.get();
                datastream.setUserId(device.getUserId());
                datastream.setScenarioId(device.getScenarioId());
                datastream.setDgId(device.getDeviceGroup().getDgId());
                datastream.setDeviceName(device.getDeviceName());
                datastreamRepository.save(datastream);
                return RESCODE.SUCCESS.getJSONRES();
            }
        }return RESCODE.DEVICE_NOT_EXIST.getJSONRES();
    }
}
