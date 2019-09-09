package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.Datastream;
import com.hydata.intelligence.platform.cell_link.service.DatastreamService;
import com.hydata.intelligence.platform.cell_link.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private DatastreamService datastreamService;


    @GetMapping("/")
    public String test() {
        return "Test.....";
    }

    @PostMapping("/addDatastream")
    public JSONObject addDatastream(@RequestBody Datastream datastream) {
        return datastreamService.add(datastream);
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public JSONObject login(String username, String password, Byte isRemember) {
        return userService.login(username, password, isRemember);
    }

    @RequestMapping(value = "api/user/logout", method = RequestMethod.GET)
    public JSONObject logout(Long userId) {
        return userService.logout(userId);
    }

    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    public JSONObject getToken(String username, String password) {
        return userService.getToken(username, password);
    }
}
