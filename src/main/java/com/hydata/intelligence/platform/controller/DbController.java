package com.hydata.intelligence.platform.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	    @RequestMapping(value = "/testMongoDB")
	    public String testMongoDB(){
	    	logger.debug("test");
	    	System.out.println("hello!");	
	    	return "test";
	    }
}
