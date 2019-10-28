package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.User;
import com.hydata.intelligence.platform.cell_link.model.MailBean;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.UserRepository;
import com.hydata.intelligence.platform.cell_link.utils.SendMailManager;
import com.hydata.intelligence.platform.cell_link.utils.SmsDemo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Autowired
    private SendMailManager sendMailManager;
    @Autowired
    private TemplateEngine templateEngine;


    private static Logger logger = LogManager.getLogger(CommunicationService.class);

    /**
     * 向用户手机号发送验证码
     * 参数phone与用户手机号一致--验证用户手机号
     * 参数phone与用户手机号不一致--更换的新手机号
     *
     * @param phone  手机号
     * @param userId 用户id
     * @return 发送结果
     */
    public JSONObject sendCode(Long userId, String phone) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            String phoneOld = userOptional.get().getPhone();
            if (phoneOld.equals(phone)) {
                //验证用户手机号
            } else {
                //验证更换的新手机号
            }
            Boolean result = smsDemo.sendCode(phone, getRandom());
//            Boolean result = smsDemo.sendCode(phone, 123456);
            if (result) return RESCODE.SUCCESS.getJSONRES();
        }
        return RESCODE.FAILURE.getJSONRES();
    }

    /**
     * 验证手机号
     *
     * @param userId 用户id
     * @param phone  手机号
     * @param code   验证码
     * @return 结果
     */
    public JSONObject vertifyPhone(Long userId, String phone, String code) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            JSONObject result = smsDemo.checkCode(phone, code);
            if (user.getPhone() != null && user.getPhone().equals(phone)) {
                //验证用户手机号
                logger.info("验证用户手机号：" + phone);
                if ((Integer) result.get("code") == 0) {
                    if (user.getIsVertifyPhone() == (byte) 0) {
                        //用户手机号未验证时
                        user.setIsVertifyPhone((byte) 1);
                        userRepository.saveAndFlush(user);
                    }
                    return RESCODE.SUCCESS.getJSONRES();
                }
            } else {
                //验证新手机号
                logger.info("验证新手机号：" + phone);
                if ((Integer) result.get("code") == 0) {
                    return RESCODE.SUCCESS.getJSONRES();
                }
            }
            return result;
        }
        return RESCODE.USER_NOT_EXIST.getJSONRES();
    }

    /**
     * 向用户邮箱发送验证码
     *
     * @param userId 用户id
     * @return 结果
     */
    @CacheEvict(cacheNames = {"user","log"},allEntries = true)
    public JSONObject sendEmail(Long userId, String email) {
        Pattern PHONE_PATTERN = Pattern.compile(
                "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"
        );
        Matcher m = PHONE_PATTERN.matcher(email);
        if (!m.matches()) return RESCODE.FAILURE.getJSONRES("邮箱格式错误");
        Context context = new Context();
        int randomNum = getRandom();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String emailInUse = null;
            if (user.getEmail() != null && user.getEmail().equals(email.trim())) {
                emailInUse = user.getEmail();
            } else {
                user.setEmail(email);
                emailInUse = email;
            }
            user.setEmailCode(randomNum);
            user.setEmailCodeStatus(0);
            user.setIsVertifyEmail((byte)0);
            HashMap<String, Object> codeMap = new HashMap();
            codeMap.put("code", randomNum);
            codeMap.put("email", emailInUse);
            String code = getCode(codeMap);
            context.setVariable("userId", userId);
            context.setVariable("code", code);
            //参数email为用户邮箱，向邮箱发送激活链接
            String emailContent = templateEngine.process("emailTemplate", context);
            MailBean mailBean = new MailBean();
            mailBean.setRecipient(user.getEmail());
            mailBean.setSubject("智能感知平台");
            mailBean.setContent(emailContent);
            return sendMailManager.sendMail(mailBean);
        }
        return RESCODE.USER_NOT_EXIST.getJSONRES();
    }

    /**
     * 生成随机验证码
     */
    private int getRandom() {
        double i = Math.random();
        int code = (int) Math.round(i * 1000000);
        if (code == 1000000 || code < 100000) {
            code = getRandom();
        }
        return code;
    }

    private String getCode(Map params) {
        JSONObject object = new JSONObject(params);
        byte[] s = object.toJSONString().getBytes();
        String code = base64Encode(s);
        System.out.println(code);
        return code;
    }

    public JSONObject getParams(String code) {
        byte[] s = base64Decode(code);
        String params = new String(s);
        JSONObject object = new JSONObject();
        try {
            object = JSONObject.parseObject(params);
        }catch (JSONException je){
            logger.info(je.getMessage());
        }
        return object;
    }

    private String base64Encode(byte[] s) {
        if (s == null) {
            return null;
        }
        BASE64Encoder b = new BASE64Encoder();
        return b.encode(s);
    }

    private byte[] base64Decode(String s) {
        if (s == null) {
            return null;
        }
        BASE64Decoder b = new BASE64Decoder();
        try {
            return b.decodeBuffer(s);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

/*    public static void main(String[] args) {
        Map<String, Object> codeMap = new HashMap();
        codeMap.put("code", getRandom());
        codeMap.put("email", "puyuting@hiynn.com");
        codeMap.put("time", new Date().getTime());
        String code = getCode(codeMap);
        JSONObject result =  getParams(code);
        System.out.println(result.get("code"));
    }*/
}
