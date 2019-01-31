package com.hydata.intelligence.platform.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.service.DeviceService;
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
    /**
     *
     * @param device_sn: 设备鉴权码
     * @param object： 设备实时上传信息流
     * @return
     */
    @RequestMapping(value="/{device_sn}/resolve_data",method = RequestMethod.POST)
    public JSONObject resolveDeviceData(@PathVariable String device_sn,@RequestBody JSONObject object) {
        return deviceService.resolveDeviceData(device_sn,object);
    }
}