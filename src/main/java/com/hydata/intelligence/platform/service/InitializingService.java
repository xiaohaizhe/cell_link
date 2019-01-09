package com.hydata.intelligence.platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author pyt
 * @createTime 2018年12月13日下午3:24:16
 */
@Component
public class InitializingService implements CommandLineRunner {

	@Autowired
	private MqttReceiveConfig mqttReceiveConfig;


	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		mqttReceiveConfig.init();
	}

}

