package com.hydata.intelligence.platform.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hydata.intelligence.platform.repositories.OperationLogsRepository;

/**
 * @author pyt
 * @createTime 2018年11月6日下午4:46:41
 */
@Transactional
@Service
public class OperationLogsService {
	@Autowired
	private OperationLogsRepository operationLogsRepository;
	
}

