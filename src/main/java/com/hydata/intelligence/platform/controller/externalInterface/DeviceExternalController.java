package com.hydata.intelligence.platform.controller.externalInterface;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.service.CommandService;
import com.hydata.intelligence.platform.service.DeviceService;
import com.hydata.intelligence.platform.service.HttpService;

/**
 * @author pyt
 * @createTime 2018年10月31日上午11:31:11
 */
@RestController
@RequestMapping("/api/external/device")
public class DeviceExternalController {
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private HttpService httpSevice;
	
	@Autowired
	private CommandService commandService;
	
	@RequestMapping(value="/{device_sn}",method=RequestMethod.GET)
	public JSONObject getDeviceDetail(@PathVariable String device_sn,HttpServletRequest request){
		String api_key = httpSevice.resolveHttpHeader(request);
		return deviceService.getDeviceDetail(device_sn,api_key);
	}
	@RequestMapping(value="/{device_sn}/sendcmd",method=RequestMethod.POST)
	public JSONObject sendcmd(@PathVariable String device_sn,JSONObject object,HttpServletRequest request, int type) {
		return commandService.send(device_sn, object.toJSONString(),type);
	} 
	
}

