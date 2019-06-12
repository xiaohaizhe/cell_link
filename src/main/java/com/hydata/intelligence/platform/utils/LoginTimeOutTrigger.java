package com.hydata.intelligence.platform.utils;

import com.hydata.intelligence.platform.dto.Admin;
import com.hydata.intelligence.platform.dto.User;
import com.hydata.intelligence.platform.repositories.AdminRepository;
import com.hydata.intelligence.platform.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName LoginTimeOutTrigger
 * @Description TODO
 * @Author pyt
 * @Date 2019/6/10 11:02
 * @Version
 */
@Component
public class LoginTimeOutTrigger {
    private static Logger logger = LogManager.getLogger(LoginTimeOutTrigger.class);
    private static Integer hour = 5;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;

    public void LoginTimeOut(String userName,Boolean isAdmin) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                logger.debug("定时结束，用户登出");
                if (isAdmin){
                    Optional<Admin> adminOptional =adminRepository.findByName(userName);
                    if (adminOptional.isPresent()) adminOptional.get().setIslogin((byte)0);
                }else{
                    Optional<User> userOptional = userRepository.findByName(userName);
                    if(userOptional.isPresent()) userOptional.get().setIslogin((byte)0);
                }
                timer.cancel();
            }
        }, hour * 60 * 60 * 1000);
    }
}
