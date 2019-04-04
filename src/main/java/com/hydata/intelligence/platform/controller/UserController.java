package com.hydata.intelligence.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.User;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.service.UserService;
import com.hydata.intelligence.platform.utils.CheckParams;

import java.util.List;

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
		JSONObject params = new JSONObject();
		params.put("name", name);
		params.put("password", password);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return userService.login(name, password);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
				
	}
	
	@RequestMapping(value= "/logout",method = RequestMethod.GET)
	public JSONObject logout(long id){
		
		JSONObject params = new JSONObject();
		params.put("id", id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return userService.logout(id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value = "/vertify_name",method = RequestMethod.GET)
	public JSONObject vertifyName(String name){

		JSONObject params = new JSONObject();
		params.put("name", name);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return userService.vertifyName(name);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value="/vertify_modify_phone",method=RequestMethod.GET)
	public JSONObject vertifyAndModifyUserPhone(long user_id,String newPhone, String code){
		JSONObject params = new JSONObject();
		params.put("user_id", user_id);
		params.put("newPhone", newPhone);
		params.put("code", code);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return userService.vertifyAndModifyUserPhone(user_id, newPhone, code);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
		
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
	public JSONObject getProductQuantity(Long user_id) {
		JSONObject params = new JSONObject();
		params.put("user_id", user_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return userService.getProductQuantity(user_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value = "/get_operation_logs",method = RequestMethod.GET)
	public JSONObject get_operation_logs(Long user_id,String key_word) {
		JSONObject params = new JSONObject();
		params.put("user_id", user_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return userService.getOperationLogs(user_id, key_word);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}

}

