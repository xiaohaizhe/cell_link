package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.User;
import com.hydata.intelligence.platform.cell_link.repository.UserRepository;
import com.hydata.intelligence.platform.cell_link.service.CommunicationService;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * @ClassName EmailController
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/26 13:39
 * @Version
 */
@Controller
@RequestMapping("/api")
public class EmailController {
    @Autowired
    private CommunicationService communicationService;
    @Autowired
    private UserRepository userRepository;

    /**
     * 邮箱激活接口
     * @param userId
     * @param code
     * @param model
     * @return
     */
    @RequestMapping("/{userId}/{code}")
    @Transactional
    public String activate(@PathVariable("userId") String userId, @PathVariable("code") String code, Model model) {
        JSONObject params = communicationService.getParams(code);
        String email = (String)params.get("email");
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        if (userOptional.isPresent()){
            User user = userOptional.get();
            String codeNum = (String)params.get("code");
            if (user.getEmail()!=null && user.getEmail().equals(email)
                    && user.getEmailCode()!=null && user.getEmailCode().equals(codeNum)){
                model.addAttribute("title",userId);
                return "index";
            }
        }
        return "error";
    }
}
