package com.hydata.intelligence.platform;

import static org.junit.Assert.*;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.junit.Test;

import com.hydata.intelligence.platform.utils.SendMailUtils;

/**
 * @author pyt
 * @createTime 2018年10月30日下午2:13:39
 */
public class WebserviceControllerTest {
	@Test
	public void test2() {
		//fail("Not yet implemented");
		SendMailUtils.sendMail("puyuting@hiynn.com", "1234", "智能感知平台");		
	}


}

 