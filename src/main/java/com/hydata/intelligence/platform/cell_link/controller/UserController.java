package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.User;
import com.hydata.intelligence.platform.cell_link.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/21 14:52
 * @Version
 */
@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JSONObject addUser(@RequestBody @Validated User user, BindingResult br){
        return userService.addUser(user,br);
    }

    @RequestMapping(value = "/change_effectiveness", method = RequestMethod.GET)
    public JSONObject changeEffectiveness(Long userId){
        return userService.changeEffectiveness(userId);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JSONObject updateUser(@RequestBody @Validated User user, BindingResult br) {
        return userService.updateUser(user, br);
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public JSONObject resetUser(Long userId){
        return userService.resetUser(userId);
    }


    @RequestMapping(value = "/findByPage", method = RequestMethod.GET)
    public JSONObject findByPage(Integer page,Integer number,String sort){
        return userService.findByPage(page,number,sort);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public JSONObject modifyUser(@RequestBody @Validated User user, BindingResult br) {
        return userService.modifyUser(user, br);
    }
}
