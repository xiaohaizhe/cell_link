package com.hydata.intelligence.platform.controller;

import com.hydata.intelligence.platform.dto.TriggerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.service.TriggerService;

/**
 * @author pyt
 * @createTime 2018年11月5日下午3:28:45
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/trigger")
public class TriggerController {
	@Autowired
	private TriggerService triggerService;
	
	@RequestMapping(value="/add",method = RequestMethod.POST)
	public JSONObject addTrigger(@RequestBody TriggerModel trigger) {
		return triggerService.addTrigger(trigger);		
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	public JSONObject delTrigger(Integer Id) {
		return triggerService.delTrigger(Id);
	}
	
	@RequestMapping(value = "/modify",method = RequestMethod.POST)
	public JSONObject modifyTrigger(@RequestBody TriggerModel trigger) {
		return triggerService.modifyTrigger(trigger);
	}
	
	@RequestMapping(value="/get_by_productId",method=RequestMethod.GET)
	public JSONObject getTriggersByProductId(Integer productId) {
		return triggerService.getTriggersByProductId(productId);
	}

}

