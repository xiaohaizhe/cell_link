package com.hydata.intelligence.platform.controller;

import java.util.Map;

import javax.jws.soap.SOAPBinding.Use;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hydata.intelligence.platform.repositories.UserRepository;
import com.hydata.intelligence.platform.service.UserService;

/**
 * @author pyt
 * @createTime 2018年10月29日上午9:22:03
 */
@Transactional
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public Map<String, Object> login(String name,String password){
		return userService.login(name, password);		
	}
	
	@RequestMapping(value= "/logout",method = RequestMethod.GET)
	public Map<String, Object> logout(Integer id){
		return userService.logout(id);
	}
	
	@RequestMapping(value = "/vertifyName",method = RequestMethod.GET)
	public Map<String, Object> vertifyName(String name){
		return userService.vertifyName(name);
	}

}

