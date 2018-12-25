package com.hydata.intelligence.platform.controller.externalInterface;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.TriggerModel;
import com.hydata.intelligence.platform.service.HttpService;
import com.hydata.intelligence.platform.service.TriggerService;

/**
 * @author pyt
 * @createTime 2018年12月19日下午3:35:16
 */
/*@RestController
@EnableAutoConfiguration
@RequestMapping("/api/trigger")*/
public class TriggerController {
	/*@Autowired
	private TriggerService triggerService;
	
	@Autowired
	private HttpService httpSevice;
	
	@RequestMapping(value="/add",method = RequestMethod.POST)
	public JSONObject addTrigger(@RequestBody TriggerModel trigger,HttpServletRequest request) {
		String api_key = httpSevice.resolveHttpHeader(request);
		return triggerService.addTrigger(trigger,api_key);		
	}*/
}

