package com.hydata.intelligence.platform.service;

import java.util.Enumeration;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.User;
import com.hydata.intelligence.platform.repositories.UserRepository;

/**
 * @author pyt
 * @createTime 2018年12月18日下午5:07:53
 */
@Transactional
@Service
public class HttpService {
	@Autowired
	private UserRepository userRepository;
	
	private static Logger logger = LogManager.getLogger(HttpService.class);
	
	public String resolveHttpHeader(HttpServletRequest request) {
		//获取header中的api_key值
		Enumeration<String> names = request.getHeaderNames();
		String api_key = "";
		while(names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getHeader(name);
			logger.debug(name+":"+value);
			if(name.equals("api_key")) {
				api_key = value;
			}
		}
		return api_key;
	}

}

