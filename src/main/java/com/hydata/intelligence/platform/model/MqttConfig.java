package com.hydata.intelligence.platform.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author pyt
 * @createTime 2018年12月27日下午2:48:06
 */
@SpringBootConfiguration
public class MqttConfig {
	 @Value("${mqtt.serverURI}")
	 private String broker;

    @Value("${mqtt.username}")
 	private String userName;

    @Value("${mqtt.password}")
	private String password;

    @Value("${mqtt.serverURI}")
	private String hostUrl;

    @Value("${mqtt.clientId}")
	private String clientId;

    @Value("${mqtt.defaultTopic}")
	private String defaultTopic;

    @Value("${mqtt.completionTimeout}")
	private int completionTimeout ;   //连接超时

	@Value("${mqtt.defaultQos}")
	private int qos;

	@Value("${mqtt.cleanSession}")
	private String cleanSession;

    @Bean
    public MQTT mqtt(){
        return MQTT.options()
        		.setBroker(broker)
        		.setUserName(userName)
        		.setPassword(password)
        		.setHostUrl(hostUrl)
        		.setClientId(clientId)
        		.setDefaultTopic(defaultTopic)
        		.setCompletionTimeout(completionTimeout)
				.setQos(qos)
				.setCleanSession(cleanSession.equals("true"))
        		.build();
    }
}

