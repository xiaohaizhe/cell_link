package com.hydata.intelligence.platform.controller;

import com.hydata.intelligence.platform.dto.TriggerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
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
	public JSONObject delTrigger(Integer id) {
		return triggerService.delTrigger(id);
	}
	
	@RequestMapping(value = "/modify",method = RequestMethod.POST)
	public JSONObject modifyTrigger(@RequestBody TriggerModel trigger) {
		return triggerService.modifyTrigger(trigger);
	}
	
	@RequestMapping(value="/get_by_productId",method=RequestMethod.GET)
	public JSONObject getTriggersByProductId(Integer productId) {
		return triggerService.getTriggersByProductId(productId);
	}
	
	@RequestMapping(value="/get_associated_triggers",method=RequestMethod.GET)
	public JSONObject getTriggersByDeviceSn(String device_sn,Integer page,Integer number) {
		return triggerService.getAssociatedTriggers(device_sn,page,number);
	}
	
	@RequestMapping(value="/get_associated_devices",method=RequestMethod.GET)
	public JSONObject getAssociatedDevices(Integer trigger_id,Integer page,Integer number) {
		return triggerService.getAssociatedDevices(trigger_id, page, number);
	}
	
	@RequestMapping(value="/get_not_associated_devices",method=RequestMethod.GET)
	public JSONObject getNotAssociatedDevices(Integer product_id,Integer trigger_id,Integer page,Integer number) {
		return triggerService.getNotAssociatedDevices(product_id,trigger_id, page, number);
	}
	
	@RequestMapping(value="/get_by_name",method=RequestMethod.GET)
	public JSONObject getByName(Integer product_id,String name,Integer page,Integer number) {
		return triggerService.getByName(product_id, name, page, number);
	}
	
	@RequestMapping(value="/trigger_associated_device",method=RequestMethod.GET)
	public JSONObject triggerAssociatedDevice(long trigger_id,String device_sn) {
		return triggerService.triggerAssociatedDevice(trigger_id, device_sn);
	}	
	
}

