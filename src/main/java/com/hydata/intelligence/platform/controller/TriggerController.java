package com.hydata.intelligence.platform.controller;

import com.hydata.intelligence.platform.dto.TriggerModel;
import com.hydata.intelligence.platform.model.RESCODE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.service.TriggerService;
import com.hydata.intelligence.platform.utils.CheckParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

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
	public JSONObject addTrigger(@RequestBody @Validated TriggerModel trigger, BindingResult br) {
		if(br.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			sb.append(br.getObjectName()+":");
			List<FieldError> errors  = br.getFieldErrors();
			for (FieldError error : errors){
				sb.append("["+error.getField() + ":"+error.getDefaultMessage()+"].");
			}
			return RESCODE.PARAM_ERROR.getJSONRES(sb.toString());
		}
		return triggerService.addTrigger(trigger);
	}

	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	public JSONObject delTrigger(Long id) {
		JSONObject params = new JSONObject();
		params.put("id", id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return triggerService.delTrigger(id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
	}

	@RequestMapping(value = "/modify",method = RequestMethod.POST)
	public JSONObject modifyTrigger(@RequestBody TriggerModel trigger) {
		return triggerService.modifyTrigger(trigger);
	}

	@RequestMapping(value="/get_by_productId",method=RequestMethod.GET)
	public JSONObject getTriggersByProductId(Long productId) {
		JSONObject params = new JSONObject();
		params.put("productId", productId);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return triggerService.getTriggersByProductId(productId);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}

	}

	@RequestMapping(value="/get_associated_triggers",method=RequestMethod.GET)
	public JSONObject getTriggersByDeviceSn(Long device_id,String name,Integer page,Integer number) {
		JSONObject params = new JSONObject();
		params.put("deviceId", device_id);
		params.put("page", page);
		params.put("number", number);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return triggerService.getAssociatedTriggers(device_id,name,page,number);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
	}

	@RequestMapping(value="/get_associated_triggers_overview",method=RequestMethod.GET)
	public JSONObject getTriggersOverviewByDeviceSn(Long device_id) {
		JSONObject params = new JSONObject();
		params.put("deviceId", device_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return triggerService.getAssociatedTriggers(device_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
	}

	@RequestMapping(value="/get_associated_devices",method=RequestMethod.GET)
	public JSONObject getAssociatedDevices(Long trigger_id,String name,Integer page,Integer number,Integer sort) {
		JSONObject params = new JSONObject();
		params.put("triggerId", trigger_id);
		params.put("page", page);
		params.put("number", number);
		params.put("sort", sort);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return triggerService.getAssociatedDevices(trigger_id, name,page, number,sort);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}

	}

	@RequestMapping(value="/get_not_associated_devices",method=RequestMethod.GET)
	public JSONObject getNotAssociatedDevices(Long product_id,Long trigger_id,String name,Integer page,Integer number,Integer sort) {
		JSONObject params = new JSONObject();
		params.put("productId", product_id);
		params.put("triggerId", trigger_id);
		params.put("page", page);
		params.put("number", number);
		params.put("sort", sort);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return triggerService.getNotAssociatedDevices(product_id,trigger_id, name==null?"":name,page, number,sort);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}


	}

	@RequestMapping(value="/get_by_name",method=RequestMethod.GET)
	public JSONObject getByName(Long product_id,String name,Integer page,Integer number) {
		JSONObject params = new JSONObject();
		params.put("productId", product_id);
		params.put("page", page);
		params.put("number", number);

		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return triggerService.getByName(product_id, name, page, number);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}

	}

	@RequestMapping(value="/trigger_associated_device",method=RequestMethod.GET)
	public JSONObject triggerAssociatedDevice(long trigger_id,Long device_id) {
		JSONObject params = new JSONObject();
		params.put("triggerId", trigger_id);
		params.put("deviceId", device_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return triggerService.triggerAssociatedDevice(trigger_id, device_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
	}

	@RequestMapping(value="/trigger_associated_all_device",method=RequestMethod.GET)
	public JSONObject triggerAssociatedAllDevices(long trigger_id) {
		JSONObject params = new JSONObject();
		params.put("triggerId", trigger_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return triggerService.triggerAssociatedAllDevices(trigger_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
	}

	@RequestMapping(value="/trigger_disconnected_device",method = RequestMethod.GET)
	public JSONObject triggerDisconnectedDevice(Long trigger_id,Long device_id){
		JSONObject params = new JSONObject();
		params.put("trigger_id", trigger_id);
		params.put("device_id", device_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return triggerService.triggerDisconnectedDevice(trigger_id,device_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
	}

	@RequestMapping(value="/trigger_disconnected_all_device",method = RequestMethod.GET)
	public JSONObject triggerDisconnectedDevice(Long trigger_id){
		JSONObject params = new JSONObject();
		params.put("trigger_id", trigger_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return triggerService.triggerDisconnectedAllDevices(trigger_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
	}

	@RequestMapping(value= "/get_increment",method = RequestMethod.GET)
	public JSONObject getIncrement(Long trigger_id,String start ,String end) throws ParseException {
		JSONObject params = new JSONObject();
		params.put("trigger_id", trigger_id);
		params.put("start", start);
		params.put("end", end);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return triggerService.getIncrement(trigger_id, sdf.parse(start), sdf.parse(end));
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
	}

	@RequestMapping(value= "/get_totalincrement",method = RequestMethod.GET)
	public JSONObject getTotalIncrement(long product_id, String start ,String end) throws ParseException {
		JSONObject params = new JSONObject();
		params.put("product_id", product_id);
		params.put("start", start);
		params.put("end", end);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return triggerService.getTotalIncrement(product_id, sdf.parse(start), sdf.parse(end));
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
	}



}

