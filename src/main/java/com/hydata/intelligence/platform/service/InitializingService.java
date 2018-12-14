package com.hydata.intelligence.platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * @author pyt
 * @createTime 2018年12月13日下午3:24:16
 */

public class InitializingService implements CommandLineRunner {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private OperationLogsService operationLogsService;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		productService.setProtocol();
		adminService.addAdmin();
		operationLogsService.setOperationType();
		applicationService.setChart();
	}

}
