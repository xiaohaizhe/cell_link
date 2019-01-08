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
	private ProductService productService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private OperationLogsService operationLogsService;
	
	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private MqttReceiveConfig mqttReceiveConfig;


	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		productService.setProtocol();
		adminService.addAdmin();
		operationLogsService.setOperationType();
		applicationService.setChart();
		mqttReceiveConfig.init();
	}

}

