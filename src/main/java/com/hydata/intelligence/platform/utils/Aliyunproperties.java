package com.hydata.intelligence.platform.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author pyt
 * @createTime 2018年10月29日上午11:58:37
 */
@Component
public class Aliyunproperties {
	@Value("${aliyun.accessKeyId}")
	private  String accessKeyId;
	@Value("${aliyun.accessKeySecret}")
	private  String accessKeySecret;
	@Value("${aliyun.verifyCode}")
	private  String verifyCode;
	@Value("${aliyun.vertifytime}")
	private int min;
	public String getAccessKeyId() {
		return accessKeyId;
	}
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}	
}

