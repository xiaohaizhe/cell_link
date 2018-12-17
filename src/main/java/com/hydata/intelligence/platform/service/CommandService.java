package com.hydata.intelligence.platform.service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Transactional
@Service
public class CommandService {
	@Autowired
	private ProductRepository productRepository;

    //@Resource
    //private MqttPahoMessageHandler mqttHandler;

    //待修改
    public void send(String topic, String content) {
    	
    	/**
    	 * haizhe
    	 * (1）根据topic判断其通讯方式
    	 * （2）根据通讯方式，调用不同的send方法，
    	 * 若为mqtt，调用mqtt的方法
    	 */


		try {
			// 创建客户端
			
			// 创建消息
			MqttMessage message = new MqttMessage(content.getBytes());
			int qos = 2;
			// 设置消息的服务质量
			message.setQos(qos);
			// 发布消息
			MqttReceiveConfig.sendClient.publish(topic, message);
			
			/**
			 * haizhe
			 * (1) 存入指令log，
			 * TODO
			 * 此处不需要disconnect
			 */




			// 断开连接
			//MqttReceiveConfig.sendClient.disconnect();
			// 关闭客户端
			//sampleClient.close();
			//System.exit(0);
		} catch (MqttException me) {
			System.err.println("reason " + me.getReasonCode());
			System.err.println("msg " + me.getMessage());
			System.err.println("loc " + me.getLocalizedMessage());
			System.err.println("cause " + me.getCause());
			System.err.println("excep " + me);
			me.printStackTrace();
		}

		//存储命令日志

		//接受回执信息
//      // 构建消息
//      Message<String> messages = MessageBuilder.withPayload(content).setHeader(MqttHeaders.TOPIC, topic).build();
//      // 发送消息
//      mqttHandler.handleMessage(messages);

	}
    	


}
