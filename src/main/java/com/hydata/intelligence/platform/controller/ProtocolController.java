package com.hydata.intelligence.platform.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.service.DeviceService;
import com.hydata.intelligence.platform.service.MqttHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Jasmine
 * @createTime: 2019-01-22 14:05
 * @description: 对于不同协议设备信息流的接入处理
 * @modified:
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/protocol")
public class ProtocolController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private MqttHandler mqttHandler;

    /**
     *
     * @param device_id: 设备id
     * @param object： 设备实时上传信息流
     * @return
     */
    @RequestMapping(value="/{device_id}/resolve_data",method = RequestMethod.POST)
    public JSONObject resolveDeviceData(@PathVariable Long device_id,@RequestBody JSONObject object) {
        return deviceService.resolveDeviceData(device_id,object);
    }

    @RequestMapping(value="/mqtt_connection",method = RequestMethod.GET)
    public JSONObject mqttConnection(Long productId) {
        return mqttHandler.brokerStatus();
    }
    @RequestMapping(value="/mqtt_reconnect",method = RequestMethod.GET)
    public JSONObject mqttReconnect(Long productId) {
        return mqttHandler.reconnect();
    }

}