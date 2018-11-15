package com.hydata.intelligence.platform.controller;

import java.util.Map;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
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
	private VerificationService verificationService;
	
	@RequestMapping(value = "/verification",method=RequestMethod.GET)
	public JSONObject verification(Integer user_id,String phone){
		return verificationService.sendSms(user_id,phone);
	}
	
	@RequestMapping(value = "/user_vertify_sms",method =RequestMethod.GET )
	public JSONObject vertifySms(Integer user_id,String phone,String code){
		return verificationService.vertifySms(user_id,phone, code);
	}
	
	@RequestMapping(value = "/vertify_sms",method =RequestMethod.GET )
	public JSONObject vertifySms1(Integer user_id,String phone,String code){
		return verificationService.vertifySms1(user_id,phone, code);
	}
	
	@RequestMapping(value = "/send_email" ,method=RequestMethod.GET)
	public JSONObject sendEmail(Integer user_id,String email){		
		return verificationService.sendEmail(user_id,email);
	}
	
	@RequestMapping(value = "/vertify_email" ,method=RequestMethod.GET)
	public JSONObject vertifyEmail(Integer user_id,String email,String code){		
		return verificationService.vertifyEmail(user_id,email,code);
	}
	
	@RequestMapping(value = "/vertify_for_trigger",method = RequestMethod.GET)
	public JSONObject vertifyEmailForUrl(Integer id,String code) {
		return verificationService.vertifyEmailForUrl(id, code);
	}
	
}

