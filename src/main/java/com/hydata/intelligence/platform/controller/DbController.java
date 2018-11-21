package com.hydata.intelligence.platform.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.service.MongoDBService;

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
		
		@Autowired
		private MongoDBService mongoDBService;
		
		
	    @RequestMapping(value = "/testMongoDB",produces="application/json;charset=UTF-8")
	    public JSONObject testMongoDB(){
	    	/*JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("name", "test1");
	    	jsonObject.put("date", new Date());
	    	mongoDBService.insert(jsonObject);*/
	    	//mongoDBService.find();
	    	return null;
	    }
}
