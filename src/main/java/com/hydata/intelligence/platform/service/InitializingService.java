package com.hydata.intelligence.platform.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @author pyt
 * @createTime 2018年12月13日下午3:24:16
 */
@Component
public class InitializingService implements CommandLineRunner {

	@Autowired
	private MqttReceiveConfig mqttReceiveConfig;

	@Value("${spring.data.mongodb.uri}")
	private String mongouri;
	
	@Value("${spring.datasource.url}")
	private String mysqlurl;
	
	private static Logger logger = LogManager.getLogger(InitializingService.class);
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		logger.info("MongoDB数据库地址：");
		logger.info(mongouri);
		logger.info("mysql数据库地址：");
		logger.info(mysqlurl);
		
		mqttReceiveConfig.init();
	}

}

