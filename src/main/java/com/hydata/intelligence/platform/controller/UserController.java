package com.hydata.intelligence.platform.controller;

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
	public JSONObject logout(long id){
		return userService.logout(id);
	}
	
	@RequestMapping(value = "/vertify_name",method = RequestMethod.GET)
	public JSONObject vertifyName(String name){
		return userService.vertifyName(name);
	}
	
	@RequestMapping(value="/vertify_modify_phone",method=RequestMethod.GET)
	public JSONObject vertifyAndModifyUserPhone(long user_id,String newPhone, String code){
		return userService.vertifyAndModifyUserPhone(user_id, newPhone, code);
	}
	
	@RequestMapping(value="/modify",method = RequestMethod.POST)
	public JSONObject modifyUser(@RequestBody User user) {
		return userService.modifyUser(user);
	}
	
	@RequestMapping(value="/get_global_statistics",method=RequestMethod.GET)
	public JSONObject getGlobalStatistics() {
		return userService.getGlobalStatistics();
	}
	
	@RequestMapping(value = "/get_product_quantity",method=RequestMethod.GET)
	public JSONObject getProductQuantity(Integer user_id) {
		return userService.getProductQuantity(user_id);
	}
	
	@RequestMapping(value = "/get_operation_logs",method = RequestMethod.GET)
	public JSONObject get_operation_logs(Integer user_id,String key_word) {
		return userService.getOperationLogs(user_id, key_word);
	}

}

