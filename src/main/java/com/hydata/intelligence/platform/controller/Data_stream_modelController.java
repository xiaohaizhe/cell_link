package com.hydata.intelligence.platform.controller;

import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hydata.intelligence.platform.pojo.DatastreamModel;

/**
 * @author pyt
 * @createTime 2018年10月22日下午5:08:01
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/DSMController")
public class Data_stream_modelController {
	private static Logger logger = LogManager.getLogger(Data_stream_modelController.class);
	 @RequestMapping(value = "/add" ,method = RequestMethod.POST)
	    public String testMongoDB(@RequestBody DatastreamModel dsmodel){
	    	logger.debug("test");
	    	System.out.println("hello!");	
	    	return "test";
	    }
}

