package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.service.UserService;
import com.hydata.intelligence.platform.cell_link.utils.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/20 17:51
 * @Version
 */
@RestController
public class LoginController {
    @Autowired
    private UserService userService;
    @GetMapping("/")
    public String test() {
        return "Test.....";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public JSONObject login(String username, String password, Byte isRemember) {
        return userService.login(username,password,isRemember);
    }

    @RequestMapping(value = "api/user/logout", method = RequestMethod.GET)
    public JSONObject logout(Long user_id) {
        return userService.logout(user_id);
    }

    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    public JSONObject getToken(String username, String password) {
        return userService.getToken(username,password);
    }
}
