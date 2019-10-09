package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.User;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.AppRepository;
import com.hydata.intelligence.platform.cell_link.repository.DatastreamRepository;
import com.hydata.intelligence.platform.cell_link.repository.DeviceGroupRepository;
import com.hydata.intelligence.platform.cell_link.repository.UserRepository;
import com.hydata.intelligence.platform.cell_link.utils.Constants;
import com.hydata.intelligence.platform.cell_link.utils.JWTHelper;
import com.hydata.intelligence.platform.cell_link.utils.MD5;
import com.hydata.intelligence.platform.cell_link.utils.PageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    private DeviceGroupRepository deviceGroupRepository;
    @Autowired
    private DatastreamRepository datastreamRepository;
    @Autowired
    private AppRepository appRepository;
    @Autowired
    private OplogService oplogService;

    private static Logger logger = LogManager.getLogger(UserService.class);

    public JSONObject getUser(User user) {
        JSONObject object = new JSONObject();
        object.put("userId", user.getUserId());
        object.put("name", user.getName());
        object.put("type", user.getType());
        object.put("phone", user.getPhone());
        object.put("isPwdModified", user.getIsPwdModified());
        object.put("isVertifyPhone", user.getIsVertifyPhone());
        object.put("isVertifyEmail", user.getIsVertifyEmail());
        object.put("email", user.getEmail());
        object.put("created",user.getCreated());
        object.put("modified",user.getModified());
        object.put("status",user.getStatus());
        return object;
    }

    /**
     * 管理员或用户登陆
     *
     * @param username 用户名
     * @param password 密码
     * @param isRem    是否记住密码:0-不记密码，1-记住密码
     * @return 成功返回用户数据与token
     */
    @CacheEvict(cacheNames = "log",allEntries = true)
    public JSONObject login(String username, String password, Byte isRem) {
        Optional<User> userOptional = userRepository.findByName(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            password = MD5.compute(password);
            if (password.equals(user.getPwd())) {
                if (user.getType() == 1 && user.getStatus() == 0) {
                    oplogService.login(user.getUserId(),"登陆失败");
                    return RESCODE.USER_NOT_EXIST.getJSONRES();
                }
                user.setIsRemember(isRem);
                Long time;
                if (isRem == null || isRem == 0) {//不记密码
                    time = TOKEN_EXPIRED_TIME_DONT_REM;
                } else {
                    time = TOKEN_EXPIRED_TIME_REM;
                }
                String jwtToken = JWTHelper.generateToken(user, time);
                JSONObject object = new JSONObject();
                object.put("user", getUser(user));
                object.put("token", jwtToken);
                oplogService.login(user.getUserId(),"登陆成功");
                return RESCODE.SUCCESS.getJSONRES(object);
            }
            oplogService.login(user.getUserId(),"用户名或密码错误，登陆失败");
            return RESCODE.NAME_OR_PASSWORD_WRONG.getJSONRES();
        }
        return RESCODE.USER_NOT_EXIST.getJSONRES();
    }

    /**
     * 管理员或用户登出
     *
     * @param user_id 用户id
     * @return 结果
     */
    @CacheEvict(cacheNames = "log",allEntries = true)
    public JSONObject logout(Long user_id) {
        oplogService.logout(user_id,"登出成功");
        return RESCODE.SUCCESS.getJSONRES();
    }

    /**
     * 外部接口
     * 获取token
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    public JSONObject getToken(String username, String password) {
        JSONObject result = login(username, password, (byte) 0);
        if ((Integer) result.get(Constants.RESPONSE_CODE_KEY) == 0) {
            JSONObject data = (JSONObject) result.get(Constants.RESPONSE_DATA_KEY);
            String token = (String) data.get("token");
            User user = (User) data.get("user");
            JSONObject object = new JSONObject();
            object.put("token", token);
            object.put("userId", user.getUserId());
            oplogService.user(user.getUserId(),"获取token，以调用外部接口");
            return RESCODE.SUCCESS.getJSONRES(object);
        }
        return result;
    }

    /**
     * 管理员添加普通用户
     *
     * @param user 用户
     * @param br   验证结果
     * @return
     */
    @CacheEvict(value = "user",allEntries = true)
    public JSONObject addUser(User user, BindingResult br) {
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            if (isUserNameExist(user.getName())) {
                logger.debug("账号名已存在");
                return RESCODE.NAME_EXIST.getJSONRES();
            }
            user.setPwd(MD5.compute("000000"));    //普通用户初始密码为：000000
            user.setType(1);    //普通用户
            user.setIsPwdModified((byte) 0);    //初始密码未修改
            user.setIsVertifyPhone((byte) 0);   //手机未验证
            user.setIsVertifyEmail((byte) 0);   //邮箱未验证
            user.setStatus(1);  //正常状态
            User userReutrn = userRepository.save(user);
            return RESCODE.SUCCESS.getJSONRES(getUser(userReutrn));
        }
        return object;
    }

    /**
     * 管理员:改变用户有效性
     *
     * @param user_id 用户id
     * @return 结果
     */
    @CacheEvict(cacheNames = "user", allEntries = true)
    public JSONObject changeEffectiveness(Long user_id) {
        Optional<User> userOptional = userRepository.findById(user_id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getStatus() == 0) user.setStatus(1);
            else user.setStatus(0);
            userRepository.saveAndFlush(user);
            return RESCODE.SUCCESS.getJSONRES();
        }
        return RESCODE.USER_NOT_EXIST.getJSONRES();
    }

    /**
     * 管理员编辑用户：用户名、手机号、邮箱
     *
     * @param user 用户
     * @param br   验证
     * @return 结果
     */
    @CacheEvict(cacheNames = "user", allEntries = true)
    public JSONObject updateUser(User user, BindingResult br) {
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            if (user.getUserId() != null) {
                Optional<User> userOptional = userRepository.findById(user.getUserId());
                if (userOptional.isPresent()) {
                    User userOld = userOptional.get();
                    //1.是否修改用户名
                    if (user.getName() != null && !user.getName().equals(userOld.getName())) {
                        if (isUserNameExist(user.getName())) {
                            logger.debug("账号名已存在");
                            return RESCODE.NAME_EXIST.getJSONRES();
                        }
                        userOld.setName(user.getName());
                    }
                    //2.是否修改手机号
                    if (user.getPhone() != null && !user.getPhone().equals(userOld.getName())) {
                        userOld.setPhone(user.getPhone());
                        userOld.setIsVertifyPhone((byte) 0);    //手机未验证
                    }
                    //3.是否修改邮箱
                    if (user.getEmail() != null && !user.getEmail().equals(userOld.getEmail())) {
                        userOld.setEmail(user.getEmail());
                        userOld.setIsVertifyEmail((byte) 0);     //邮箱未验证
                    }
                    User userNew = userRepository.saveAndFlush(userOld);
                    return RESCODE.SUCCESS.getJSONRES(getUser(userNew));
                }
            }
            return RESCODE.USER_NOT_EXIST.getJSONRES();
        }
        return object;
    }

    /**
     * 管理员重置用户账号：
     * 密码重置为000000，手机号、邮箱需重新验证
     *
     * @param userId
     * @return
     */
    @CacheEvict(cacheNames = {"user","log"},allEntries = true)
    public JSONObject resetUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPwd(MD5.compute("000000"));    //重置初始密码：000000
            user.setIsPwdModified((byte) 0);    //初始密码未修改
            user.setIsVertifyPhone((byte) 0);   //手机未验证
            user.setIsVertifyEmail((byte) 0);   //邮箱未验证
            userRepository.saveAndFlush(user);
            oplogService.user(userId,"重置账号");
            return RESCODE.SUCCESS.getJSONRES();
        }
        return RESCODE.USER_NOT_EXIST.getJSONRES();
    }

    /**
     * 管理员下：用户分页
     *
     * @param page
     * @param number
     * @param sort
     * @return
     */
    @Cacheable(cacheNames = "user",keyGenerator = "myKeyGenerator")
    public JSONObject findByPage(Integer page, Integer number, String sort) {
        Pageable pageable = PageUtils.getPage(page, number, sort);
        Page<User> userPage = userRepository.findByType(1, pageable);
        List<JSONObject> userList = new ArrayList<>();
        for (User user : userPage.getContent()) {
            userList.add(getUser(user));
        }
        return RESCODE.SUCCESS.getJSONRES(userList, userPage.getTotalPages(), userPage.getTotalElements());
    }

    /**
     * 用户名是否已重复
     *
     * @param username 用户名
     * @return
     */
    public synchronized Boolean isUserNameExist(String username) {
        Optional<User> userOptional = userRepository.findByName(username);
        return userOptional.isPresent();
    }

    /**
     * 用户个人中心修改用户：密码、手机号、邮箱
     *
     * @param user
     * @param br
     * @return
     */
    @CacheEvict(cacheNames = {"user","log"},allEntries = true)
    public JSONObject modifyUser(User user, BindingResult br) {
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            Optional<User> userOptional = userRepository.findById(user.getUserId());
            if (userOptional.isPresent()) {
                User userOld = userOptional.get();
                if (user.getPwd() != null && !userOld.getPwd().equals(MD5.compute(user.getPwd()))) {
                    //修改密码
                    userOld.setPwd(MD5.compute(user.getPwd()));
                    if (userOld.getIsPwdModified() == (byte) 0) userOld.setIsPwdModified((byte) 1);
                }
                if (user.getPhone() != null && !user.getPhone().equals(userOld.getPhone())) {
                    userOld.setPhone(user.getPhone());
                }
                if (user.getEmail() != null && !user.getEmail().equals(userOld.getEmail())) {
                    userOld.setEmail(user.getEmail());
                }
                User userNew = userRepository.saveAndFlush(userOld);
                oplogService.user(userNew.getUserId(),"修改用户信息");
                return RESCODE.SUCCESS.getJSONRES(getUser(userNew));
            }
            return RESCODE.USER_NOT_EXIST.getJSONRES();
        }
        return object;
    }

    @Cacheable(cacheNames = "user",keyGenerator = "myKeyGenerator")
    public JSONObject getOverview() {
        Long userSum = userRepository.count() - 1;
        Long dgSum = deviceGroupRepository.count();
        Long datastreamSum = datastreamRepository.count();
        Long appSum = appRepository.count();
        JSONObject object = new JSONObject();
        object.put("userSum", userSum);
        object.put("dgSum", dgSum);
        object.put("datastreamSum", datastreamSum);
        object.put("appSum", appSum);
        return RESCODE.SUCCESS.getJSONRES(object);
    }
}
