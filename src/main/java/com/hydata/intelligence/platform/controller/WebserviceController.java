package com.hydata.intelligence.platform.controller;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.service.WebserviceService;

/**
 * @author pyt
 * @createTime 2018年10月29日上午11:23:33
 */
@Transactional
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/webService")
public class WebserviceController {
	@Autowired
	private WebserviceService webserviceService;
	
	@RequestMapping(value = "/verification",method=RequestMethod.GET)
	public Map<String, Object> verification(String phone){
		return webserviceService.sendSms(phone);
	}
	
	@RequestMapping(value = "vertifySms",method =RequestMethod.GET )
	public Map<String, Object> vertifySms(String phone,String code){
		return webserviceService.vertifySms(phone, code);
	}
	@RequestMapping(value = "vertifyEmail" ,method=RequestMethod.GET)
	public Map<String, Object> vertifyEmail(String email){		
		return webserviceService.vertifyEmail(email);
	}

}

