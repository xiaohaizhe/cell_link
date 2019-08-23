package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.User;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.UserRepository;
import com.hydata.intelligence.platform.cell_link.utils.Constants;
import com.hydata.intelligence.platform.cell_link.utils.JWTHelper;
import com.hydata.intelligence.platform.cell_link.utils.MD5;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Optional;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/21 11:12
 * @Version
 */
@Transactional
@Service
public class UserService {
    @Value("${token.rem}")
    private Long TOKEN_EXPIRED_TIME_REM;
    @Value("${token.dont.rem}")
    private Long TOKEN_EXPIRED_TIME_DONT_REM;
    @Autowired
    private UserRepository userRepository;

    private static Logger logger = LogManager.getLogger(UserService.class);

    /**
     * 管理员或用户登陆
     *
     * @param username 用户名
     * @param password 密码
     * @param isRem    是否记住密码:0-不记密码，1-记住密码
     * @return 成功返回用户数据与token
     */
    public JSONObject login(String username, String password, Byte isRem) {
        Optional<User> userOptional = userRepository.findByName(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            password = MD5.compute(password);
            if (password.equals(user.getPwd())) {
                if (user.getType() == 1 && user.getIsVertifyPhone() == 0) {
                    return RESCODE.PHONE_NOT_VERTIFY.getJSONRES();
                }
                user.setIsRemember(isRem);
                Long time;
                if (isRem == 0) {//不记密码
                    time = TOKEN_EXPIRED_TIME_DONT_REM;
                } else {
                    time = TOKEN_EXPIRED_TIME_REM;
                }
                String jwtToken = JWTHelper.generateToken(user, time);
                JSONObject object = new JSONObject();
                object.put("user", user);
                object.put("token", jwtToken);
                return RESCODE.SUCCESS.getJSONRES(object);
            }
            return RESCODE.NAME_OR_PASSWORD_WRONG.getJSONRES();
        }
        return RESCODE.USER_NOT_EXIST.getJSONRES();
    }

    /**
     * 管理员添加普通用户
     *
     * @param user 用户
     * @param br   验证结果
     * @return
     */
    public JSONObject addUser(User user, BindingResult br) {
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            if (isUserNameExist(user.getName())) {
                logger.debug("账号名已存在");
                return RESCODE.NAME_EXIST.getJSONRES();
            }
            user.setPwd(MD5.compute(MD5.compute("000000")));    //普通用户初始密码为：000000
            user.setType(1);    //普通用户
            user.setIsPwdModified((byte) 0);    //初始密码未修改
            user.setIsVertifyPhone((byte) 0);   //手机未验证
            user.setIsVertifyEmail((byte) 0);   //邮箱未验证
            user.setStatus(1);  //正常状态
            User userReutrn = userRepository.save(user);
            return RESCODE.SUCCESS.getJSONRES(userReutrn);
        }
        return object;
    }

    /**
     * 用户名是否已重复
     *
     * @param username 用户名
     * @return
     */
    public Boolean isUserNameExist(String username) {
        Optional<User> userOptional = userRepository.findByName(username);
        return userOptional.isPresent();
    }
}
