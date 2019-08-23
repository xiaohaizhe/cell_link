package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.User;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.UserRepository;
import com.hydata.intelligence.platform.cell_link.utils.SmsDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.OpenOption;
import java.util.Optional;

/**
 * @ClassName CommunicationService
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/23 18:03
 * @Version
 */
@Service
@Transactional
public class CommunicationService {
    @Autowired
    private SmsDemo smsDemo;
    @Autowired
    private UserRepository userRepository;

    /**
     * 向用户手机号发送验证码
     * @param userId 用户id
     * @return 发送结果
     */
    public JSONObject sendCode(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()){
            String phone = userOptional.get().getPhone();
            Boolean result = smsDemo.sendCode(phone);
            if (result) return RESCODE.SUCCESS.getJSONRES();
        }return RESCODE.FAILURE.getJSONRES();
    }
}
