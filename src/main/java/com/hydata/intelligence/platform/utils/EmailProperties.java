package com.hydata.intelligence.platform.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author pyt
 * @createTime 2018年10月31日上午9:28:29
 */
@Component
public class EmailProperties {
	@Value("${email.account}")
	private String account;
	@Value("${email.password}")
	private String password;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}

