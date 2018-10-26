package com.hydata.intelligence.platform;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author pyt
 * @createTime 2018年10月26日下午1:59:50
 */
public class SpringBootStartApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(Application.class);
	}

}

