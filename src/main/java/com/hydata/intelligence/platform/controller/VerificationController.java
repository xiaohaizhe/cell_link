package com.hydata.intelligence.platform.controller;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hydata.intelligence.platform.service.VerificationService;

/**
 * @author pyt
 * @createTime 2018年10月29日上午11:23:33
 */
@Transactional
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/verification")
public class VerificationController {
	@Autowired
	private VerificationService webserviceService;
	
	@RequestMapping(value = "/verification",method=RequestMethod.GET)
	public Map<String, Object> verification(String phone){
		return webserviceService.sendSms(phone);
	}
	
	@RequestMapping(value = "/vertifySms",method =RequestMethod.GET )
	public Map<String, Object> vertifySms(Integer user_id,String phone,String code){
		return webserviceService.vertifySms(user_id,phone, code);
	}
	
	@RequestMapping(value = "/sendEmail" ,method=RequestMethod.GET)
	public Map<String, Object> sendEmail(Integer user_id,String email){		
		return webserviceService.sendEmail(user_id,email);
	}
	
	@RequestMapping(value = "/vertifyEmail" ,method=RequestMethod.GET)
	public Map<String, Object> vertifyEmail(Integer user_id,String email,String code){		
		return webserviceService.vertifyEmail(user_id,email,code);
	}
}

