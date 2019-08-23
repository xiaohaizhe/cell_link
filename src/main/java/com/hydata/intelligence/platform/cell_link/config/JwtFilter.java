package com.hydata.intelligence.platform.cell_link.config;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.utils.JWTHelper;
import io.jsonwebtoken.Claims;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName JwtFilter
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/21 10:29
 * @Version
 */
public class JwtFilter implements Filter {
    private static Logger logger = LogManager.getLogger(JwtFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = request.getHeader("authorization"); //获取请求传来的token
        Claims claims = JWTHelper.verifyJwt(token); //验证token
        if (claims == null) {
            JSONObject object = new JSONObject();
            object.put("expired",true);
            response.getWriter().write(object.toString());
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }


}
