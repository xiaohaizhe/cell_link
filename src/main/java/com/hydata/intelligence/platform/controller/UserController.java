package com.hydata.intelligence.platform.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.User;
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
	public JSONObject login(String name,String password){
		return userService.login(name, password);		
	}
	
	@RequestMapping(value= "/logout",method = RequestMethod.GET)
	public JSONObject logout(Integer id){
		return userService.logout(id);
	}
	
	@RequestMapping(value = "/vertifyName",method = RequestMethod.GET)
	public JSONObject vertifyName(String name){
		return userService.vertifyName(name);
	}
	
	@RequestMapping(value="/vertifyAndModifyUserPhone",method=RequestMethod.GET)
	public JSONObject vertifyAndModifyUserPhone(Integer user_id,String newPhone, String code){
		return userService.vertifyAndModifyUserPhone(user_id, newPhone, code);
	}
	
	@RequestMapping(value="/modify",method = RequestMethod.POST)
	public JSONObject modifyUser(@RequestBody User user) {
		return userService.modifyUser(user);
	}

}

