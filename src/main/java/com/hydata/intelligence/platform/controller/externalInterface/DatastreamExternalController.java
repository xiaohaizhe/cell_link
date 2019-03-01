package com.hydata.intelligence.platform.controller.externalInterface;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.service.DeviceService;
import com.hydata.intelligence.platform.service.HttpService;
import com.hydata.intelligence.platform.utils.CheckParams;

/**
 * @author pyt
 * @createTime 2018年12月25日上午10:07:14
 */
@RestController
@RequestMapping("/api/external/device")
public class DatastreamExternalController {
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private HttpService httpSevice;
	
	@RequestMapping(value="/{device_sn}/datastream",method=RequestMethod.GET)	
	public JSONObject getDeviceDatastream(@PathVariable Long device_id,HttpServletRequest request) {
		String api_key = httpSevice.resolveHttpHeader(request);
		JSONObject params = new JSONObject();
		params.put("device_id", device_id);
		params.put("key", api_key);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return deviceService.getDeviceDatastream(device_id, api_key);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value="/{device_sn}/datastream/{name}",method=RequestMethod.GET)
	public JSONObject getDeviceData(@PathVariable Long device_id,@PathVariable String name,Date start,Date end,HttpServletRequest request){
		String api_key = httpSevice.resolveHttpHeader(request);
		JSONObject params = new JSONObject();
		params.put("device_id", device_id);
		params.put("key", api_key);
		params.put("name", name);
		params.put("start", start);
		params.put("end", end);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return deviceService.getDeviceDatastreamData(device_id,name,start,end,api_key);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}	
}

