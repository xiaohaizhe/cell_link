package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.User;
import com.hydata.intelligence.platform.cell_link.repository.UserRepository;
import com.hydata.intelligence.platform.cell_link.service.CommunicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private CommunicationService communicationService;
    @Autowired
    private UserRepository userRepository;

    private static Logger logger = LogManager.getLogger(EmailController.class);

    /**
     * 邮箱激活接口
     *
     * @param userId
     * @param code
     * @param model
     * @return
     */
    @RequestMapping("/{userId}/{code}")
    @Transactional
    public String activate(@PathVariable("userId") Long userId, @PathVariable("code") String code, Model model) {
        logger.info("开始邮箱激活");
        logger.info("激活用户id：" + userId + "的邮箱");
        logger.info("激活验证码为：" + code);
        JSONObject params = communicationService.getParams(code);
        if (params.get("email") != null && params.get("code") != null) {
            String email = (String) params.get("email");
            logger.info("根据激活验证码获得email:" + email);
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                logger.info(params);
                Integer codeNum = (Integer) params.get("code");
                logger.info("根据激活验证码获得codeNum:" + codeNum);
                logger.info("email:"+user.getEmail());
                logger.info("emailCode:"+user.getEmailCode());
                if (user.getEmail() != null
                        && user.getEmail().equals(email)
                        && user.getEmailCode() != null
                        && user.getEmailCode().equals(codeNum)) {
                    if (user.getEmailCodeStatus()!=null && user.getEmailCodeStatus() == 1) {
                        model.addAttribute("resCode", 2);
                        logger.info("邮箱验证码已验证");
                        return "index";
                    }
                    user.setEmailCodeStatus(1);
                    user.setIsVertifyEmail((byte)1);
                    userRepository.saveAndFlush(user);
                    model.addAttribute("resCode", 0);
                    logger.info("验证成功");
                    return "index";
                }
            }
        }
        logger.info("验证失败");
        model.addAttribute("resCode", 1);
        return "index";
    }
}
