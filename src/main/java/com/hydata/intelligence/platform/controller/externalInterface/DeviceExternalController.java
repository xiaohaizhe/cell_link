package com.hydata.intelligence.platform.controller.externalInterface;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.hydata.intelligence.platform.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

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
	private DeviceRepository deviceRepository;
	
	@Autowired
	private CommandService commandService;
	
	@RequestMapping(value="/{device_id}",method=RequestMethod.GET)
	public JSONObject getDeviceDetail(@PathVariable Long device_id,HttpServletRequest request){
		String api_key = httpSevice.resolveHttpHeader(request);
		return deviceService.getDeviceDetail(device_id,api_key);
	}
	@RequestMapping(value="/{device_id}/sendcmd",method=RequestMethod.POST)
	public JSONObject sendcmd(@PathVariable Long device_id,JSONObject object,HttpServletRequest request, int type,long user_id) {
		return commandService.send(device_id, object.toJSONString(),type,user_id);
	}

	@RequestMapping(value= "/auto_add_device",method = RequestMethod.POST)
	public JSONObject autoAddDevice(@RequestBody JSONObject info, HttpServletRequest request){
		String api_key = httpSevice.resolveHttpHeader(request);
		String registration_code = (String) info.get("reg_code");
		String device_sn = (String) info.get("device_sn");
		return deviceService.autoAdd(registration_code, device_sn, api_key);
	}
	
}

