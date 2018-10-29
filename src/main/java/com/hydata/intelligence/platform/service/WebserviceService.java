package com.hydata.intelligence.platform.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse.SmsSendDetailDTO;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.utils.Aliyunproperties;
import com.hydata.intelligence.platform.utils.SmsDemo;



/**
 * @author pyt
 * @createTime 2018年10月29日上午11:23:50
 */
@Transactional
@Service
public class WebserviceService {
	@Autowired 
	private Aliyunproperties aliyunproperties;
	
	private Logger logger = LogManager.getLogger(WebserviceService.class);
	
	/**
	 * 向手机发送验证码
	 * @param phone
	 * @return
	 */
	public Map<String, Object> sendSms(String phone) {
		Integer code = getRandom();
		System.out.println("验证码："+code);
		SendSmsResponse sendsmsresponse = null;
		try {
			sendsmsresponse = SmsDemo.sendSms(phone,String.valueOf(code));
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		if(sendsmsresponse.getCode().equals("OK")){
			return RESCODE.SUCCESS.getJSONRES();
		}else {
			return RESCODE.FAILURE.getJSONRES(sendsmsresponse);
		}
	}
	
	/**
	 * 生成随机验证码
	 */
	public int getRandom() {
		double i = Math.random();		
		int code = (int) Math.round(i*1000000);
		if(code == 1000000 || code <100000) {
			code = getRandom();
		}
		return code;
	}
	
	/**
	 * 根据手机号获取该手机号下最新的验证码
	 * @param phone
	 * @return
	 */
	public SmsSendDetailDTO getCode(String phone) {
		List<SmsSendDetailDTO> codelist = new ArrayList<>();
		try {
			QuerySendDetailsResponse response = SmsDemo.querySendDetails(phone);
			System.out.println(response.getMessage());
			codelist = response.getSmsSendDetailDTOs();
			logger.debug(codelist.get(0).getContent());				
			logger.debug("接收到的时间为："+codelist.get(0).getReceiveDate() +".");	
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return codelist.get(0);
	}
	
	/**
	 * 验证手机号验证码
	 * @param phone
	 * @param smscode
	 * @return
	 */
	public Map<String, Object> vertifySms(String phone,String smscode){
		SmsSendDetailDTO smsDetail = getCode(phone);
		if(smsDetail!=null) {
			logger.debug("手机号："+phone+"下有发送验证码");
			//最新短息消息
			String code = smsDetail.getOutId();
			logger.debug("最新验证码为："+code);
			String receiveDate = smsDetail.getReceiveDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int min =0;
			try {
				Date date = sdf.parse(receiveDate);
				Date now = new Date();
				long cost = now.getTime()-date.getTime();
				min = (int) (cost/1000/60);				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(min<aliyunproperties.getMin()) {//短信有效时间
				logger.debug("短信在有效期内");
				if(smscode.equals(code)) {
					logger.debug("手机号:"+phone + ",验证码:" + smscode + " 验证成功。。。");
					return RESCODE.VERTIFY_SMS_SUCCESS.getJSONRES();
				}else {
					return RESCODE.VERTIFY_SMS_FAIL.getJSONRES();
				}				
			}else {
				return RESCODE.VERTIFY_SMS_TIMEOUT.getJSONRES();
			}
		}else {
			return RESCODE.VERTIFY_SMS_NULL.getJSONRES();
		}
	}
	
	public Map<String, Object> vertifyEmail(String email){
		/*
		 * 1.正则判断
		 * 2.。。。。
		 */
		boolean flag = true;
		if (email == null)
            flag = false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(email);
        if (m.matches())
        	flag = true;
        else
        	flag = false;

		if(flag) {
			return RESCODE.SUCCESS.getJSONRES();
		}else {
			return RESCODE.FAILURE.getJSONRES();
		}
	}
}

