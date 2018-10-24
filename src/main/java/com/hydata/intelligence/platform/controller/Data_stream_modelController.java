package com.hydata.intelligence.platform.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hydata.intelligence.platform.dto.DatastreamModel;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.service.Data_stream_model_Service;

/**
 * @author pyt
 * @createTime 2018年10月22日下午5:08:01
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/dsmCon")
public class Data_stream_modelController {
	private static Logger logger = LogManager.getLogger(Data_stream_modelController.class);
	@Autowired
	private Data_stream_model_Service dsmService;
	
	@RequestMapping(value = "/add" ,method = RequestMethod.GET)
	    public Map<String, Object> add(int product_id,String dsm_name,String unit_name,String unit_symbol){		
	    	return dsmService.addData_stream_model(product_id, dsm_name, unit_name, unit_symbol);
		/*logger.debug("test");
    	System.out.println("hello!");	
    	return RESCODE.SUCCESS.getJSONRES();*/
		}
	 
	 @RequestMapping(value = "/delete" ,method = RequestMethod.GET)
	    public String delete(@RequestBody DatastreamModel dsmodel){
	    	logger.debug("test");
	    	System.out.println("hello!");	
	    	return "test";
	    }
	 @RequestMapping(value = "/modify" ,method = RequestMethod.GET)
	    public String modify(@RequestBody DatastreamModel dsmodel){
	    	logger.debug("test");
	    	System.out.println("hello!");	
	    	return "test";
	    }
	 @RequestMapping(value = "/query" ,method = RequestMethod.GET)
	    public String query(@RequestBody DatastreamModel dsmodel){
	    	logger.debug("test");
	    	System.out.println("hello!");	
	    	return "test";
	    }
}

