package com.hydata.intelligence.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.service.VerificationService;

/**
 * @author pyt
 * @createTime 2018年10月29日上午11:23:33
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/verification")
public class VerificationController {
	@Autowired
	private VerificationService verificationService;
	
	@RequestMapping(value = "/verification",method=RequestMethod.GET)
	public JSONObject verification(Long user_id){
		return verificationService.sendSms(user_id);
	}
	
	@RequestMapping(value = "/send_code",method=RequestMethod.GET)
	public JSONObject sendCode(Long user_id,String phone) {
		return verificationService.sendCode(user_id, phone);
	}
	
	@RequestMapping(value = "/user_vertify_sms",method =RequestMethod.GET )
	public JSONObject vertifySms(Long user_id,String code){
		return verificationService.vertifySms(user_id, code);
	}
	
	@RequestMapping(value = "/vertify_sms",method =RequestMethod.GET )
	public JSONObject vertifySms1(Long user_id,String code){
		return verificationService.vertifySms1(user_id, code);
	}
	
	@RequestMapping(value = "/vertify_code",method =RequestMethod.GET )
	public JSONObject vertifyCode(Long user_id,String phone,String code) {
		return verificationService.vertifyCode(user_id, phone, code);
	}
	
	@RequestMapping(value = "/send_email" ,method=RequestMethod.GET)
	public JSONObject sendEmail(Long user_id,String email){		
		return verificationService.sendEmail(user_id,email);
	}
	
	@RequestMapping(value = "/vertify_email" ,method=RequestMethod.GET)
	public JSONObject vertifyEmail(Long user_id,String email,String code){		
		return verificationService.vertifyEmail(user_id,email,code);
	}
	
	@RequestMapping(value = "/vertify_for_trigger",method = RequestMethod.GET)
	public JSONObject vertifyEmailForUrl(Long id,String code) {
		return verificationService.vertifyEmailForUrl(id, code);
	}
	
}

