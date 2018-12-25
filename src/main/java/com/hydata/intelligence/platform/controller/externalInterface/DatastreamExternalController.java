package com.hydata.intelligence.platform.controller.externalInterface;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.service.DeviceService;
import com.hydata.intelligence.platform.service.HttpService;

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
	public JSONObject getDeviceDatastream(@PathVariable String device_sn,HttpServletRequest request) {
		String api_key = httpSevice.resolveHttpHeader(request);
		return deviceService.getDeviceDatastream(device_sn, api_key);
	}
	
	@RequestMapping(value="/{device_sn}/datastream/{name}",method=RequestMethod.GET)
	public JSONObject getDeviceData(@PathVariable String device_sn,@PathVariable String name,Date start,Date end,HttpServletRequest request){
		String api_key = httpSevice.resolveHttpHeader(request);
		return deviceService.getDeviceDatastreamData(device_sn,name,start,end,api_key);
	}	
}

