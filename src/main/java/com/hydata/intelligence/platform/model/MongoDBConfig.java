package com.hydata.intelligence.platform.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author pyt
 * @createTime 2019年1月2日下午6:02:14
 */
@SpringBootConfiguration
public class MongoDBConfig {	
	@Value("${mongodb.username}")
	private String username;
	@Value("${mongodb.password}")
	private String password;
	@Value("${mongodb.server.host}")
	private String host;
	@Value("${mongodb.server.port}")
	private int port;
	
	@Bean
	public MongoDB mongoDB() {
		return MongoDB.options()
				.setHost(host)
				.setPassword(password)
				.setPort(port)
				.setUsername(username)
				.Builder();
	}
}

