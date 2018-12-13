package com.hydata.intelligence.platform.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.python.antlr.PythonParser.list_for_return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hydata.intelligence.platform.dto.OperationLogs;
import com.hydata.intelligence.platform.dto.OperationType;
import com.hydata.intelligence.platform.repositories.OperationLogsRepository;
import com.hydata.intelligence.platform.repositories.OperationTypeRepository;

/**
 * @author pyt
 * @createTime 2018年11月6日下午4:46:41
 */
@Transactional
@Service
public class OperationLogsService {
	
	@Autowired
	private OperationTypeRepository operationTypeRepository;
	
	public void setOperationType() {
		String[] names = {"登陆","注销","验证手机","验证邮箱","产品"};
		List<OperationType> types = operationTypeRepository.findAll();
		List<String> typeName = new ArrayList<>();
		for(OperationType type : types) {
			typeName.add(type.getName());
		}
		for(int i = 0 ; i <names.length ; i++) {
			if(typeName.contains(names[i])==false) {
				OperationType operationType = new OperationType();
				operationType.setName(names[i]);
				operationTypeRepository.save(operationType);
			}
		}
		
	}
	
}

