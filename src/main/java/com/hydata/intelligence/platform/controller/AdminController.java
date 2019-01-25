package com.hydata.intelligence.platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.Admin;
import com.hydata.intelligence.platform.dto.User;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.service.AdminService;
import com.hydata.intelligence.platform.service.UserService;
import com.hydata.intelligence.platform.utils.CheckParams;

/**
 * @author pyt
 * @createTime 2018年10月31日下午5:48:41
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/admin")
public class AdminController {
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private UserService userService; 
	
	@RequestMapping(value="/add_admin",method=RequestMethod.GET)
	public void addAdmin() {
		adminService.addAdmin();
	}
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public JSONObject login(String name,String pwd){
		JSONObject params = new JSONObject();
		params.put("name", name);
		params.put("pwd", pwd);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return adminService.login(name, pwd);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public JSONObject logout(String name){
		JSONObject params = new JSONObject();
		params.put("name", name);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return adminService.logout(name);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public JSONObject addUser(@RequestBody @Validated User user,BindingResult br){
		if(br.hasErrors()) {
			StringBuilder sb = new StringBuilder();
            sb.append(br.getObjectName()+":");
            List<FieldError> errors  = br.getFieldErrors();
            for (FieldError error : errors){
                sb.append("["+error.getField() + ":"+error.getDefaultMessage()+"].");
            }
            return RESCODE.PARAM_ERROR.getJSONRES(sb.toString());
		}
		return userService.addAccount(user);
	}
	
	@RequestMapping(value="/modifyAdminPwd",method = RequestMethod.GET)
	public JSONObject modifyAdminPwd(String name,String newPwd){
		
		JSONObject params = new JSONObject();
		params.put("name", name);
		params.put("newPwd", newPwd);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return adminService.modifyAdminPwd(name, newPwd);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value="/modifyAdminPhone",method = RequestMethod.GET)
	public JSONObject modifyAdminPhone(String name,String newPhone){
		JSONObject params = new JSONObject();
		params.put("name", name);
		params.put("newPhone", newPhone);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return adminService.modifyAdminPhone(name, newPhone);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value="/modifyAdmin",method = RequestMethod.POST)
	public JSONObject modifyAdmin(@RequestBody Admin admin) {
		return adminService.modifyAdmin(admin);
	}
	
	@RequestMapping(value="/vertifyAndModifyAdminPhone",method=RequestMethod.GET)
	public JSONObject vertifyAndModifyAdminPhone(String name,String newPhone,String code){
		JSONObject params = new JSONObject();
		params.put("name", name);
		params.put("newPhone", newPhone);
		params.put("code", code);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return adminService.vertifyAndModifyAdminPhone(name, newPhone, code);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}		
	}
	
	@RequestMapping(value="/delete",method = RequestMethod.GET )
	public JSONObject deleteUser(Integer user_id,String admin_name){
		JSONObject params = new JSONObject();
		params.put("user_id", user_id);
		params.put("admin_name", admin_name);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return adminService.deleteUser(user_id,admin_name);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value="/query",method = RequestMethod.GET)
	public JSONObject queryUser(Integer page,Integer number){
		JSONObject params = new JSONObject();
		params.put("page", page);
		params.put("number", number);

		JSONObject result1 = CheckParams.checkParams(params);
		if((Integer)result1.get("code")==0) {
			Page<User> result = adminService.queryUser(page, number);
			return RESCODE.SUCCESS.getJSONRES(result.getContent(), result.getTotalPages(), result.getTotalElements());
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result1.get("data"));
		}
		
	}
	
	@RequestMapping(value="/query_by_uname",method=RequestMethod.GET)
	public JSONObject queryUserByUser_name(String user_name,Integer page,Integer number){
		JSONObject params = new JSONObject();
		params.put("user_name", user_name);
		params.put("page", page);
		params.put("number", number);
		JSONObject result1 = CheckParams.checkParams(params);
		if((Integer)result1.get("code")==0) {
			Page<User> result = adminService.queryUserByUser_name(user_name, page, number);
			return RESCODE.SUCCESS.getJSONRES(result.getContent(), result.getTotalPages(), result.getTotalElements());
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result1.get("data"));
		}
		
	}
	@RequestMapping(value="/change_effectiveness",method=RequestMethod.GET)
	public JSONObject changeUserEffectiveness(Integer user_id,String admin_name){
		JSONObject params = new JSONObject();
		params.put("user_id", user_id);
		params.put("admin_name", admin_name);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return adminService.changeUserEffectiveness(user_id, admin_name);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	@RequestMapping(value = "/modify",method = RequestMethod.POST)
	public JSONObject modifyUser(@RequestBody @Validated User user,BindingResult br) {
		if(br.hasErrors()) {
			StringBuilder sb = new StringBuilder();
            sb.append(br.getObjectName()+":");
            List<FieldError> errors  = br.getFieldErrors();
            for (FieldError error : errors){
                sb.append("["+error.getField() + ":"+error.getDefaultMessage()+"].");
            }
            return RESCODE.PARAM_ERROR.getJSONRES(sb.toString());
		}
		return userService.adminModifyUser(user);
	}
}

