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
	public JSONObject verification(String phone){
		return verificationService.sendSms(phone);
	}
	
	@RequestMapping(value = "/userVertifySms",method =RequestMethod.GET )
	public JSONObject vertifySms(Integer user_id,String phone,String code){
		return verificationService.vertifySms(user_id,phone, code);
	}
	
	@RequestMapping(value = "/vertifySms",method =RequestMethod.GET )
	public JSONObject vertifySms(String phone,String code){
		return verificationService.vertifySms(phone, code);
	}
	
	@RequestMapping(value = "/sendEmail" ,method=RequestMethod.GET)
	public JSONObject sendEmail(Integer user_id,String email){		
		return verificationService.sendEmail(user_id,email);
	}
	
	@RequestMapping(value = "/vertifyEmail" ,method=RequestMethod.GET)
	public JSONObject vertifyEmail(Integer user_id,String email,String code){		
		return verificationService.vertifyEmail(user_id,email,code);
	}
	
	@RequestMapping(value = "vertifyEmailForUrl",method = RequestMethod.GET)
	public JSONObject vertifyEmailForUrl(Integer id,String code) {
		return verificationService.vertifyEmailForUrl(id, code);
	}
	
}

