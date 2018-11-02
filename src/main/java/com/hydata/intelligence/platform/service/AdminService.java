package com.hydata.intelligence.platform.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.websocket.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse.SmsSendDetailDTO;
import com.hydata.intelligence.platform.dto.Admin;
import com.hydata.intelligence.platform.dto.User;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.AdminRepository;
import com.hydata.intelligence.platform.repositories.UserRepository;
import com.hydata.intelligence.platform.utils.Aliyunproperties;
import com.hydata.intelligence.platform.utils.MD5;
import com.hydata.intelligence.platform.utils.WebServletUtil;

/**
 * @author pyt
 * @createTime 2018年10月31日下午5:49:20
 */
@Transactional
@Service
public class AdminService {
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private VerificationService webserviceService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private Aliyunproperties aliyunproperties;
	
	@Autowired
	private HttpServletRequest request;
	
	private static Logger logger = LogManager.getLogger(AdminService.class);
	
	public Map<String, Object> login(String name,String pwd){
		logger.debug("管理员开始登陆");
		logger.debug("getIpAddr:"+WebServletUtil.getIpAddr(request));
		logger.debug("getRemoteAddr:"+WebServletUtil.getRemoteAddr(request));
		logger.debug("getClientIpAddr:"+WebServletUtil.getClientIpAddr(request));
		logger.debug("getClientIpAddress:"+WebServletUtil.getClientIpAddress(request));	     

		pwd = MD5.compute(pwd);
		Optional<Admin> adminOptional = adminRepository.findByNameAndPwd(name, pwd);
		if(adminOptional.isPresent()) {
			if(adminOptional.get().getIslogin()==1) {
				return RESCODE.ADMIN_ALREADYIN.getJSONRES();
			}
			adminOptional.get().setIslogin((byte)1);
			return RESCODE.SUCCESS.getJSONRES();
		}
		return RESCODE.FAILURE.getJSONRES();
	}
	
	public Map<String, Object> logout(String name){
		logger.debug("管理员开始登出");
		logger.debug("getIpAddr:"+WebServletUtil.getIpAddr(request));
		logger.debug("getRemoteAddr:"+WebServletUtil.getRemoteAddr(request));
		logger.debug("getClientIpAddr:"+WebServletUtil.getClientIpAddr(request));
		logger.debug("getClientIpAddress:"+WebServletUtil.getClientIpAddress(request));	     
		Optional<Admin> adminOptional = adminRepository.findByName(name);
		if(adminOptional.isPresent()) {
			if(adminOptional.get().getIslogin()==1) {
				adminOptional.get().setIslogin((byte)0);
				return RESCODE.SUCCESS.getJSONRES();
			}
			return RESCODE.ADMIN_ALREADYOUT.getJSONRES();
		}
		return RESCODE.ADMIN_NAME_NOT_EXIST.getJSONRES();
	}
	
	public Map<String, Object> modifyAdminPwd(String name,String newPwd){
		Optional<Admin> adminOptional = adminRepository.findByName(name);
		if(adminOptional.isPresent()) {
			adminOptional.get().setPwd(MD5.compute(newPwd));
			adminOptional.get().setIslogin((byte)0);
			return RESCODE.MODIFY_PWD_SUCCESS.getJSONRES();
		}
		return RESCODE.NAME_NOT_EXIST.getJSONRES();
	}
	
	/**
	 * 修改管理员手机号
	 * 条件：已验证
	 * @param name
	 * @param newPhone
	 * @return
	 */
	public Map<String, Object> modifyAdminPhone(String name,String newPhone){
		Optional<Admin> adminOptional = adminRepository.findByName(name);
		if(adminOptional.isPresent()) {
			if(adminOptional.get().getPhone().equals(newPhone)) {
				return RESCODE.PHONE_NO_CHANGE.getJSONRES();
			}
			adminOptional.get().setPhone(newPhone);
			return RESCODE.MODIFY_PHONE_SUCCESS.getJSONRES();
		}
		return RESCODE.NAME_NOT_EXIST.getJSONRES();
	}
	
	/**
	 * 验证手机号验证码且修改
	 * @param name
	 * @param newPhone
	 * @param code
	 * @return
	 */
	public Map<String, Object> vertifyAndModifyAdminPhone(String name,String newPhone,String code){
		logger.debug("进入管理员修改验证手机");
		Optional<Admin> adminOptional = adminRepository.findByName(name);
		if(adminOptional.isPresent()) {
			if(adminOptional.get().getPhone().equals(newPhone)) {
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
						Admin admin = adminOptional.get();
						admin.setPhone(newPhone);
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
		}else {
			return RESCODE.NAME_NOT_EXIST.getJSONRES();
		}
	}
	
	public Map<String, Object> deleteUser(Integer user_id,String admin_name){
		logger.debug("管理员："+admin_name+"开始删除用户："+user_id);
		Optional<Admin> adminOptional = adminRepository.findByName(admin_name);
		if(adminOptional.isPresent()) {
			Optional<User> userOptional = userRepository.findById(user_id);
			if(userOptional.isPresent()) {
				logger.debug("getIpAddr:"+WebServletUtil.getIpAddr(request));
				logger.debug("getRemoteAddr:"+WebServletUtil.getRemoteAddr(request));
				logger.debug("getClientIpAddr:"+WebServletUtil.getClientIpAddr(request));
				logger.debug("getClientIpAddress:"+WebServletUtil.getClientIpAddress(request));	  
				userRepository.deleteById(user_id);
				return RESCODE.SUCCESS.getJSONRES();
			}
			return RESCODE.ID_NOT_EXIST.getJSONRES();
		}
		return RESCODE.ADMIN_NAME_NOT_EXIST.getJSONRES();
	}
	
	@SuppressWarnings("deprecation")
	public Page<User> queryUser(Integer page,Integer number){
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"id");
		return userRepository.findAll(pageable);
	}
	
	@SuppressWarnings("deprecation")
	public Page<User> queryUserByUser_name(String user_name,Integer page,Integer number){
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"id");
		return userRepository.findByName(user_name,pageable);
	}
	/**
	 * 改变用户的有效性
	 * @param user_id
	 * @param admin_name
	 * @return
	 */
	public Map<String, Object> changeUserEffectiveness(Integer user_id,String admin_name){
		logger.debug("管理员："+admin_name+"开始修改用户："+user_id+"的有效性");
		Optional<Admin> adminOptional = adminRepository.findByName(admin_name);
		if(adminOptional.isPresent()) {
			Optional<User> userOptional = userRepository.findById(user_id);
			if(userOptional.isPresent()) {
				logger.debug("getIpAddr:"+WebServletUtil.getIpAddr(request));
				logger.debug("getRemoteAddr:"+WebServletUtil.getRemoteAddr(request));
				logger.debug("getClientIpAddr:"+WebServletUtil.getClientIpAddr(request));
				logger.debug("getClientIpAddress:"+WebServletUtil.getClientIpAddress(request));	  
				User user = userOptional.get();
				user.setIsvalid((byte)user.getIsvalid()==1?(byte)0:(byte)1);
				return RESCODE.SUCCESS.getJSONRES();
			}
			return RESCODE.ID_NOT_EXIST.getJSONRES();
		}
		return RESCODE.ADMIN_NAME_NOT_EXIST.getJSONRES();
	}
}
