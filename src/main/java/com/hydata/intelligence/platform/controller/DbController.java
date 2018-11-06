package com.hydata.intelligence.platform.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author pyt
 *
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/userCon")
public class DbController {
		private static Logger logger = LogManager.getLogger(DbController.class);
	    @RequestMapping(value = "/testMongoDB",produces="application/json;charset=UTF-8")
	    public JSONObject testMongoDB(){
	    	logger.debug("test");
	    	System.out.println("hello!");
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("test", "test");
	    	System.out.println(jsonObject);
	    	return jsonObject;
	    }
}
