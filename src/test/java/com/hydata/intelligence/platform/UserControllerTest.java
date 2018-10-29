package com.hydata.intelligence.platform;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hydata.intelligence.platform.utils.Aliyunproperties;
import com.hydata.intelligence.platform.utils.MD5;

/**
 * @author pyt
 * @createTime 2018年10月22日下午2:19:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

	@Autowired
	private Aliyunproperties aliyunproperties;
	
	@Test
	public void test() {
		String str = "test";
		System.out.println(MD5.compute(str));
		System.out.println(aliyunproperties.getAccessKeyId());
		System.out.println(aliyunproperties.getAccessKeySecret());
	}

}

