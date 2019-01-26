package com.hydata.intelligence.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.service.VerificationService;
import com.hydata.intelligence.platform.utils.CheckParams;

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
		JSONObject params = new JSONObject();
		params.put("user_id", user_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return verificationService.sendSms(user_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value = "/send_code",method=RequestMethod.GET)
	public JSONObject sendCode(Long user_id,String phone) {
		JSONObject params = new JSONObject();
		params.put("user_id", user_id);
		params.put("phone", phone);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return verificationService.sendCode(user_id, phone);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value = "/user_vertify_sms",method =RequestMethod.GET )
	public JSONObject vertifySms(Long user_id,String code){
		JSONObject params = new JSONObject();
		params.put("user_id", user_id);
		params.put("code", code);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return verificationService.vertifySms(user_id, code);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value = "/vertify_sms",method =RequestMethod.GET )
	public JSONObject vertifySms1(Long user_id,String code){
		JSONObject params = new JSONObject();
		params.put("user_id", user_id);
		params.put("code", code);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return verificationService.vertifySms1(user_id, code);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value = "/vertify_code",method =RequestMethod.GET )
	public JSONObject vertifyCode(Long user_id,String phone,String code) {
		JSONObject params = new JSONObject();
		params.put("user_id", user_id);
		params.put("code", code);
		params.put("phone", phone);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return verificationService.vertifyCode(user_id, phone, code);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value = "/send_email" ,method=RequestMethod.GET)
	public JSONObject sendEmail(Long user_id,String email){
		JSONObject params = new JSONObject();
		params.put("user_id", user_id);
		params.put("email", email);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return verificationService.sendEmail(user_id,email);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value = "/vertify_email" ,method=RequestMethod.GET)
	public JSONObject vertifyEmail(Long user_id,String email,String code){
		JSONObject params = new JSONObject();
		params.put("user_id", user_id);
		params.put("email", email);
		params.put("code", code);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return verificationService.vertifyEmail(user_id,email,code);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value = "/vertify_for_trigger",method = RequestMethod.GET)
	public JSONObject vertifyEmailForUrl(Long id,String code) {
		JSONObject params = new JSONObject();
		params.put("id", id);
		params.put("code", code);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return verificationService.vertifyEmailForUrl(id, code);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
}

