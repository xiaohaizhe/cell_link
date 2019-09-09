package com.hydata.intelligence.platform.cell_link.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @ClassName BeanRegisterConfig
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/21 10:48
 * @Version
 */
@Configuration
public class BeanRegisterConfig {
    @Bean
    public FilterRegistrationBean createFilterBean() {
        //过滤器注册类
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new JwtFilter());
        registration.addUrlPatterns("/api/*"); //需要过滤的接口
        return registration;
    }

    @Bean
    public KeyGenerator myKeyGenerator(){
        return (o, method, params) -> o.getClass().getSimpleName()+"."+method.getName()+ Arrays.asList(params);
    }


}
