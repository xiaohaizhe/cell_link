package com.hydata.intelligence.platform.service.ServiceImpl;

import com.hydata.intelligence.platform.service.MqttService;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author: Jasmine
 * @createTime:
 * @description: <Paho MQTT Implementation>
 * @modified:
 */
@Service
public class MqttServiceImpl implements MqttService {

    @Resource
    private MqttPahoMessageHandler mqttHandler;

    @Override
    //待修改
    public void send(String topic, String content) {
        // 构建消息
        Message<String> messages = MessageBuilder.withPayload(content).setHeader(MqttHeaders.TOPIC, topic).build();
        // 发送消息
        mqttHandler.handleMessage(messages);
    }
}
