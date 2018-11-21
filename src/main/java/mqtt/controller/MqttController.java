package mqtt.controller;

import mqtt.service.MqttService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * <MQTT发送消息>
 */
@RestController
public class MqttController {

    @Resource
    private MqttService mqttService;

    @RequestMapping("/send")
    public String sendMessage(String topic, String content) {

        mqttService.send(topic, content);

        return "发送成功";
    }

}
