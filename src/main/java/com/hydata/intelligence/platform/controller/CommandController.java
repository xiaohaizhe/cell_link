package com.hydata.intelligence.platform.controller;

import com.hydata.intelligence.platform.service.CommandService;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 *  @author: Jasmine
 *  @createTime:
 *  @description: <MQTT发送消息>
 *  @modified: haizhe
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/command")
public class CommandController {

    @Resource
    private CommandService commandService;

    @RequestMapping("/sendcmd")
    public String sendMessage(String topic, String content) {
        commandService.send(topic, content);
        return "发送成功";
    }

}
