package com.hydata.intelligence.platform;

import com.hydata.intelligence.platform.service.CommandService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application{
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	//HTTP消费端初始化
	//MQTT消费端初始化
}