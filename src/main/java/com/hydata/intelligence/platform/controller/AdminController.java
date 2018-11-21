package com.hydata.intelligence.platform.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
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

/**
 * @author pyt
 * @createTime 2018年10月31日下午5:48:41
 */
@Transactional
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/admin")
public class AdminController {
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private UserService userService; 
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public JSONObject login(String name,String pwd){
		return adminService.login(name, pwd);
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public JSONObject logout(String name){
		return adminService.logout(name);
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public JSONObject addUser(@RequestBody User user){
		return userService.addAccount(user);
	}
	
	@RequestMapping(value="/modifyAdminPwd",method = RequestMethod.GET)
	public JSONObject modifyAdminPwd(String name,String newPwd){
		return adminService.modifyAdminPwd(name, newPwd);
	}
	
	@RequestMapping(value="/modifyAdminPhone",method = RequestMethod.GET)
	public JSONObject modifyAdminPhone(String name,String newPhone){
		return adminService.modifyAdminPhone(name, newPhone);
	}
	
	@RequestMapping(value="/modifyAdmin",method = RequestMethod.POST)
	public JSONObject modifyAdmin(@RequestBody Admin admin) {
		return adminService.modifyAdmin(admin);
	}
	
	@RequestMapping(value="/vertifyAndModifyAdminPhone",method=RequestMethod.GET)
	public JSONObject vertifyAndModifyAdminPhone(String name,String newPhone,String code){
		return adminService.vertifyAndModifyAdminPhone(name, newPhone, code);
	}
	
	@RequestMapping(value="/delete",method = RequestMethod.GET )
	public JSONObject deleteUser(Integer user_id,String admin_name){
		return adminService.deleteUser(user_id,admin_name);
	}
	
	@RequestMapping(value="/query",method = RequestMethod.GET)
	public JSONObject queryUser(Integer page,Integer number){
		Page<User> result = adminService.queryUser(page, number);
		return RESCODE.SUCCESS.getJSONRES(result.getContent(), result.getTotalPages(), result.getTotalElements());
	}
	
	@RequestMapping(value="/query_by_uname",method=RequestMethod.GET)
	public JSONObject queryUserByUser_name(String user_name,Integer page,Integer number){
		Page<User> result = adminService.queryUserByUser_name(user_name, page, number);
		return RESCODE.SUCCESS.getJSONRES(result.getContent(), result.getTotalPages(), result.getTotalElements());
	}
	@RequestMapping(value="/change_effectiveness",method=RequestMethod.GET)
	public JSONObject changeUserEffectiveness(Integer user_id,String admin_name){
		return adminService.changeUserEffectiveness(user_id, admin_name);
	}
	@RequestMapping(value = "/modify",method = RequestMethod.POST)
	public JSONObject modifyUser(@RequestBody User user) {
		return userService.adminModifyUser(user);
	}
}

