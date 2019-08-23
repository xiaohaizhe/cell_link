package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.service.CommunicationService;
import com.hydata.intelligence.platform.cell_link.utils.SmsDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CommunicationController
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/23 17:59
 * @Version
 */
@RestController
@RequestMapping("api/com")
public class CommunicationController {
    @Autowired
    private CommunicationService communicationService;

    @GetMapping("/getPhoneCode")
    public JSONObject sendCode(Long userId){
        return communicationService.sendCode(userId);
    }
}
