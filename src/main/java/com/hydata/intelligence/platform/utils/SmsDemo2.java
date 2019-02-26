package com.hydata.intelligence.platform.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.hydata.intelligence.platform.model.RESCODE;


/**
 * @author pyt
 * @createTime 2019年2月13日下午4:19:15
 */
@Component
public class SmsDemo2 {
	@Value("${leancloud.appId}")
	private String appId;
	
	@Value("${leancloud.appKey}")
	private String appKey;
	
	@Value("${leancloud.masterKey}")
	private String masterKey;
	
	@Value("${vertify.time}")
	private Integer vertifytime;
	
	private static final Logger log = LogManager.getLogger(SmsDemo.class);
	
	public JSONObject sendSms(String phone) {
		AVOSCloud.initialize(appId,appKey,masterKey);
		try {
			AVOSCloud.requestSMSCode(phone, "cell link", "验证码", vertifytime);
			return RESCODE.SUCCESS.getJSONRES();
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
			return RESCODE.FAILURE.getJSONRES(e.getMessage());
		}
	}
	
	public JSONObject verifySMSCode(String phone,String code) {
		AVOSCloud.initialize(appId,appKey,masterKey);
		try {
			AVOSCloud.verifySMSCode(code, phone);
			return RESCODE.SUCCESS.getJSONRES();
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
			return RESCODE.FAILURE.getJSONRES(e.getMessage());
		}
	}

}

