package com.hydata.intelligence.platform.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
