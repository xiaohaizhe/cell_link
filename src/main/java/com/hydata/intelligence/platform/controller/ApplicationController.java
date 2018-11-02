package com.hydata.intelligence.platform.controller;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hydata.intelligence.platform.model.ApplicationModel;
import com.hydata.intelligence.platform.service.ApplicationService;

/**
 * @author pyt
 * @createTime 2018年11月1日下午5:34:07
 */
@Transactional
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/application")
public class ApplicationController {
	@Autowired
	private ApplicationService applicationService;
	
	@RequestMapping(value="/addChartApp",method=RequestMethod.POST)
	public Map<String, Object> addApplication(@RequestBody ApplicationModel applicationModel){
		return applicationService.addApplication(applicationModel);
	}
	
	@RequestMapping(value="/delChartApp",method=RequestMethod.GET)
	public Map<String, Object> delChartApp(Integer id){
		return applicationService.delChartApp(id);
	}
	
	@RequestMapping(value = "/queryByProductId",method=RequestMethod.GET)
	public Map<String, Object> queryByProductId(Integer product_id){
		return applicationService.queryByProductId(product_id);
	}
	
	@RequestMapping(value= "/getChartAppDetail" ,method = RequestMethod.GET)
	public Map<String, Object> getChartAppDetail(Integer app_id){
		return applicationService.getChartAppDetail(app_id);
	}
	
	@RequestMapping(value = "/getAppByName" , method = RequestMethod.GET)
	public Map<String, Object> getAppByProductIdAndName(Integer product_id, String app_name){
		return applicationService.getAppByProductIdAndName(product_id, app_name);
	}
 }

