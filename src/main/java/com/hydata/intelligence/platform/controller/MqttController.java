package com.hydata.intelligence.platform.controller;

import com.hydata.intelligence.platform.service.MqttService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 *  @author: Jasmine
 *  @createTime:
 *  @description: <MQTT发送消息>
 *  @modified:
 */
@RestController
public class MqttController {

    @Resource
    private MqttService mqttService;

    @RequestMapping("/api/mqtt/send")
    //待修改
    public String sendMessage(String topic, String content) {

        mqttService.send(topic, content);

        return "发送成功";
    }

}
