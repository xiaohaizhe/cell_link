package com.hydata.intelligence.platform.service;

public interface MqttService {


    /**
     * 发送消息
     * @param topic 主题
     * @param content 内容
     */

    void send(String topic, String content);

    /**
     * MQTT消费端初始化
     */
    static void MqttInit(){

    }

}
