package com.hydata.intelligence.platform.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse.SmsSendDetailDTO;
import com.hydata.intelligence.platform.dto.Admin;
import com.hydata.intelligence.platform.dto.User;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.UserRepository;
import com.hydata.intelligence.platform.utils.Aliyunproperties;
import com.hydata.intelligence.platform.utils.MD5;

/**
 * @author pyt
 * @createTime 2018年10月29日上午9:27:26
 */
@Transactional
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VerificationService webserviceService;
	
	@Autowired 
	private Aliyunproperties aliyunproperties;
	
	private Logger logger = LogManager.getLogger(UserService.class);
	/**
	 * 用户登陆
	 * @param name
	 * @param pwd
	 * @return
	 */
	public JSONObject login(String name, String pwd){
		Optional<User> userOptional = userRepository.findByName(name);
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			if(user.getIsvalid()==0) {
				return RESCODE.USER_IS_NOT_VALID.getJSONRES();
			}
			if(MD5.compute(pwd.trim()).equals(user.getPwd())) {
				if(user.getIsvertifyphone()==1) {
					user.setIslogin((byte)1);
					return RESCODE.SUCCESS.getJSONRES(user);
				}else {
					return RESCODE.PHONE_NOT_VERTIFY.getJSONRES();
				}				
			}else {
				return RESCODE.WRONG_PWD.getJSONRES();
			}			
		}else {			
			return RESCODE.NAME_NOT_EXIST.getJSONRES();
		}
	}
	/**
	 * 用户注销登陆
	 * @param id
	 * @return
	 */
	public JSONObject logout(Integer id){
		Optional<User> userOptional = userRepository.findById(id);
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			user.setIslogin((byte)0);
			return RESCODE.SUCCESS.getJSONRES();
		}else {
			return RESCODE.ID_NOT_EXIST.getJSONRES();
		}		
	}
	/**
	 * 验证用户名是否重复
	 * @param name
	 * @return
	 */
	public JSONObject vertifyName(String name){
		Optional<User> userOptional = userRepository.findByName(name);
		if(userOptional.isPresent()) {
			return RESCODE.NAME_EXIST.getJSONRES();
		}else {
			return RESCODE.NAME_NOT_EXIST.getJSONRES();
		}
	}
	
	public JSONObject addAccount(User user){
		JSONObject result = vertifyName(user.getName());
		if((Integer)result.get("code")==1) {
			logger.debug("账号名已存在");
			return RESCODE.NAME_EXIST.getJSONRES();
		}
		user.setPwd(MD5.compute(user.getPwd()));
		user.setType(0);
		user.setIsvertifyphone((byte)0);
		user.setIsvertifyemail((byte)0);
		user.setDefaultKey(MD5.compute(MD5.compute(user.getName())));
		user.setIslogin((byte)0);
		//新建用户均为有效
		user.setIsvalid((byte)1);
		user.setCreateTime(new Date());
		User userReutrn = userRepository.save(user);
		return RESCODE.SUCCESS.getJSONRES(userReutrn);
	}
	
	public JSONObject vertifyAndModifyUserPhone(Integer user_id,String newPhone, String code){
		logger.debug("进入用户:"+user_id+"开始修改自己的手机号为："+newPhone);
		
		Optional<User> userOptional = userRepository.findById(user_id);
		if(userOptional.isPresent()) {
			if(userOptional.get().getPhone().equals(newPhone)) {
				return RESCODE.PHONE_NO_CHANGE.getJSONRES();
			}
			SmsSendDetailDTO smsDetail = webserviceService.getCode(newPhone);
			if(smsDetail!=null) {
				logger.debug("手机号："+newPhone+"下有发送验证码");
				//最新短息消息
				String codeReturn = smsDetail.getOutId();
				logger.debug("最新验证码为："+codeReturn);
				String receiveDate = smsDetail.getReceiveDate();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				int min =0;
				try {
					Date date = sdf.parse(receiveDate);
					Date now = new Date();
					long cost = now.getTime()-date.getTime();
					min = (int) (cost/1000/60);				
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(min<aliyunproperties.getMin()) {//短信有效时间
					logger.debug("短信在有效期内");
					if(code.equals(codeReturn)) {
						logger.debug("手机号:"+newPhone + ",验证码:" + code + " 验证成功。。。");						
						User user = userOptional.get();
						user.setPhone(newPhone);
						return RESCODE.VERTIFY_SMS_AND_MODIFY_PHONE_SUCCESS.getJSONRES();									
					}else {
						logger.debug("手机号:"+newPhone + ",验证码:" + code + " 验证失败。。。");
						return RESCODE.VERTIFY_SMS_FAIL.getJSONRES();
					}				
				}else {
					return RESCODE.VERTIFY_SMS_TIMEOUT.getJSONRES();
				}
			}else {
				return RESCODE.VERTIFY_SMS_NULL.getJSONRES();
			}			
		}
		return RESCODE.USER_ID_NOT_EXIST.getJSONRES();
	}

}

