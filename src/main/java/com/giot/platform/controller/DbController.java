package com.giot.platform.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.giot.platform.dao.SelfTestMapper;
import com.giot.platform.dao.UserMapper;
import com.giot.platform.pojo.SelfTest;
import com.giot.platform.pojo.User;
/**
 * 
 * @author pyt
 *
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/userCon")
public class DbController {
	 @Autowired
	 public SelfTestMapper mapper;
	    @RequestMapping(value = "/addUser" , method = RequestMethod.POST)
	    public void addUser(@RequestBody SelfTest user){
	    	user.setIsvalid(true);
	    	mapper.insert(user);
	    	System.out.println("用户名："+user.getName());
	    	System.out.println("hello");
	    	    
	    }
}
