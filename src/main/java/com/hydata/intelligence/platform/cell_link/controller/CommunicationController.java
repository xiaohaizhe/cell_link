package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.service.CommunicationService;
import com.hydata.intelligence.platform.cell_link.utils.SmsDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @ClassName CommunicationController
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/23 17:59
 * @Version
 */
@RestController
@RequestMapping("api/user")
public class CommunicationController {
    @Autowired
    private CommunicationService communicationService;

    @GetMapping("/sms_phone")
    public JSONObject sendCode(Long user_id, String phone){
        return communicationService.sendCode(user_id,phone);
    }

    @GetMapping("/vertify_phone")
    public JSONObject vertifyPhone(Long user_id, String phone,String code){
        return communicationService.vertifyPhone(user_id,phone,code);
    }

    @GetMapping("/sms_email")
    public JSONObject sendEmail(Long userId){
         return communicationService.sendEmail(userId);
    }
}
