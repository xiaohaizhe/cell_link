package com.hydata.intelligence.platform.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse.SmsSendDetailDTO;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.hydata.intelligence.platform.dto.OperationLogs;
import com.hydata.intelligence.platform.dto.User;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.OperationLogsRepository;
import com.hydata.intelligence.platform.repositories.UserRepository;
import com.hydata.intelligence.platform.utils.SendMailUtils;
import com.hydata.intelligence.platform.utils.SmsDemo;
import com.hydata.intelligence.platform.utils.SmsDemo2;



/**
 * @author pyt
 * @createTime 2018年10月29日上午11:23:50
 */
@Transactional
@Service
public class VerificationService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OperationLogsRepository operationLogsRepository;
	
	@Autowired
	private SmsDemo SmsDemo;
	
	@Autowired
	private SmsDemo2 SmsDemo2;
	
	@Value("${vertify.time}")
	private Integer vertifytime;
	
	
		
	private Logger logger = LogManager.getLogger(VerificationService.class);
	
	/**
	 * 向手机发送验证码
	 * @param user_id
	 * @return
	 */
	public JSONObject sendSms(Long user_id) {
		Optional<User> optional = userRepository.findById(user_id);
		if(optional.isPresent()) {
			String phone = optional.get().getPhone();
			Integer code = getRandom();
			System.out.println("验证码："+code);
			SendSmsResponse sendsmsresponse = null;
			try {
				sendsmsresponse = SmsDemo.sendSms(phone,String.valueOf(code));
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			if(sendsmsresponse.getCode().equals("OK")){
				OperationLogs logs = new OperationLogs();
				logs.setUserId(user_id);
				logs.setOperationTypeId(3);
				logs.setMsg("发送验证码");
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
				return RESCODE.SUCCESS.getJSONRES();
			}else {
				return RESCODE.FAILURE.getJSONRES(sendsmsresponse);
			}
			/*JSONObject object = SmsDemo2.sendSms(phone);
			Integer code  = (Integer) object.get("code");
			if(code == 0) {
				OperationLogs logs = new OperationLogs();
				logs.setUserId(user_id);
				logs.setOperationTypeId(3);
				logs.setMsg("发送验证码");
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);				
			}
			return object;*/
		}else {
			return RESCODE.USER_ID_NOT_EXIST.getJSONRES();
		}
		
	}
	/**
	 * 向新手机发送验证码
	 * @param phone
	 * @return
	 */
	public JSONObject sendCode(Long user_id,String phone) {
		Optional<User> optional = userRepository.findById(user_id);
		if(optional.isPresent()) {
			Integer code = getRandom();
			System.out.println("验证码："+code);
			SendSmsResponse sendsmsresponse = null;
			try {
				sendsmsresponse = SmsDemo.sendSms(phone,String.valueOf(code));
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			if(sendsmsresponse.getCode().equals("OK")){
				OperationLogs logs = new OperationLogs();
				logs.setUserId(user_id);
				logs.setOperationTypeId(3);
				logs.setMsg("向新手机号："+phone+"发送验证码");
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
				return RESCODE.SUCCESS.getJSONRES();
			}else {
				return RESCODE.FAILURE.getJSONRES(sendsmsresponse);
			}
			//leancloud短信发送
			/*JSONObject object = SmsDemo2.sendSms(phone);
			Integer code  = (Integer) object.get("code");
			if(code == 0) {
				OperationLogs logs = new OperationLogs();
				logs.setUserId(user_id);
				logs.setOperationTypeId(3);
				logs.setMsg("向新手机号："+phone+"发送验证码");
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);				
			}
			return object;*/
		}else {
			return RESCODE.USER_ID_NOT_EXIST.getJSONRES();
		}		
	}
	
	/**
	 * 生成随机验证码
	 */
	public int getRandom() {
		double i = Math.random();		
		int code = (int) Math.round(i*1000000);
		if(code == 1000000 || code <100000) {
			code = getRandom();
		}
		return code;
	}
	
	/**
	 * 根据手机号获取该手机号下最新的验证码
	 * @param phone
	 * @return
	 */
	@SuppressWarnings("finally")
	public SmsSendDetailDTO getCode(String phone) {
		logger.debug("准备获取手机号"+phone+"下验证码");
		List<SmsSendDetailDTO> codelist = new ArrayList<>();
		QuerySendDetailsResponse response;
		try {
			response = SmsDemo.querySendDetails(phone);
			logger.debug(""+response.getMessage());
			codelist = response.getSmsSendDetailDTOs();
			logger.debug(codelist.get(0).getContent());				
			logger.debug("接收到的时间为："+codelist.get(0).getReceiveDate() +".");
			return codelist.get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("获取手机验证码异常,"+e.getMessage());	
			
		}
		return null;				
	}
	
	/**
	 * 验证手机号验证码
	 * 1.发送验证码
	 * 2.验证是否正确
	 * @param user_id
	 * @param smscode
	 * @return
	 */
	public JSONObject vertifySms1(Long user_id,String smscode){
		logger.debug("进入手机号验证码验证");
		Optional<User> optional = userRepository.findById(user_id);
		if(optional.isPresent()) {
			String phone = optional.get().getPhone();
			SmsSendDetailDTO smsDetail = getCode(phone);
			if(smsDetail!=null) {
				logger.debug(smsDetail.toString());
				logger.debug("手机号："+phone+"下有发送验证码");
				//最新短息消息
				String code = smsDetail.getOutId();
				logger.debug("最新验证码为："+code);
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
				if(min<vertifytime) {//短信有效时间
					logger.debug("短信在有效期内");
					if(smscode.equals(code)) {
						logger.debug("手机号:"+phone + ",验证码:" + smscode + " 验证成功。。。");
						OperationLogs logs1 = new OperationLogs();
						logs1.setUserId(user_id);
						logs1.setOperationTypeId(3);
						logs1.setMsg("手机验证码验证成功");
						logs1.setCreateTime(new Date());
						operationLogsRepository.save(logs1);
						return RESCODE.VERTIFY_SMS_SUCCESS.getJSONRES();
					}else {
						logger.debug("手机号:"+phone + ",验证码:" + smscode + " 验证失败。。。");
						OperationLogs logs = new OperationLogs();
						logs.setUserId(user_id);
						logs.setOperationTypeId(3);
						logs.setMsg("手机验证码验证失败");
						logs.setCreateTime(new Date());
						operationLogsRepository.save(logs);
						return RESCODE.VERTIFY_SMS_FAIL.getJSONRES();
					}
				}else {
					return RESCODE.VERTIFY_SMS_TIMEOUT.getJSONRES();
				}
			}else {
				return RESCODE.VERTIFY_SMS_NULL.getJSONRES();
			}
			//leancloud
			/*JSONObject object = SmsDemo2.verifySMSCode(phone, smscode);
			Integer code  = (Integer) object.get("code");
			if(code == 0) {
				logger.debug("手机号:"+phone + ",验证码:" + smscode + " 验证成功。。。");
				OperationLogs logs1 = new OperationLogs();
				logs1.setUserId(user_id);
				logs1.setOperationTypeId(3);
				logs1.setMsg("手机验证码验证成功");
				logs1.setCreateTime(new Date());
				operationLogsRepository.save(logs1);
				return RESCODE.VERTIFY_SMS_SUCCESS.getJSONRES();				
			}else {
				logger.debug("手机号:"+phone + ",验证码:" + smscode + " 验证失败。。。");
				logger.error(object);
				OperationLogs logs = new OperationLogs();
				logs.setUserId(user_id);
				logs.setOperationTypeId(3);
				logs.setMsg("手机验证码验证失败");
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
				return RESCODE.VERTIFY_SMS_FAIL.getJSONRES(object);
			}*/
		}else {
			return RESCODE.USER_ID_NOT_EXIST.getJSONRES();
		}		
	}
	/**
	 * 验证手机号验证码
	 * 1.发送验证码
	 * 2.验证是否正确
	 * @param phone
	 * @param smscode
	 * @return
	 */
	public JSONObject vertifyCode(Long user_id,String phone,String smscode){
		Optional<User> optional = userRepository.findById(user_id);
		if(optional.isPresent()) {
			SmsSendDetailDTO smsDetail = getCode(phone);
			if(smsDetail!=null) {
				logger.debug(smsDetail.toString());
				logger.debug("手机号："+phone+"下有发送验证码");
				//最新短息消息
				String code = smsDetail.getOutId();
				logger.debug("最新验证码为："+code);
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
				if(min<vertifytime) {//短信有效时间
					logger.debug("短信在有效期内");
					if(smscode.equals(code)) {
						logger.debug("手机号:"+phone + ",验证码:" + smscode + " 验证成功。。。");
						OperationLogs logs = new OperationLogs();
						logs.setUserId(user_id);
						logs.setOperationTypeId(3);
						logs.setMsg("新手机:"+phone+"验证码验证成功");
						logs.setCreateTime(new Date());
						operationLogsRepository.save(logs);
						return RESCODE.VERTIFY_SMS_SUCCESS.getJSONRES();
					}else {
						logger.debug("手机号:"+phone + ",验证码:" + smscode + " 验证失败。。。");
						OperationLogs logs = new OperationLogs();
						logs.setUserId(user_id);
						logs.setOperationTypeId(3);
						logs.setMsg("新手机:"+phone+"验证码验证失败");
						logs.setCreateTime(new Date());
						operationLogsRepository.save(logs);
						return RESCODE.VERTIFY_SMS_FAIL.getJSONRES();
					}
				}else {
					return RESCODE.VERTIFY_SMS_TIMEOUT.getJSONRES();
				}
			}else {
				return RESCODE.VERTIFY_SMS_NULL.getJSONRES();
			}
			//leancloud短信验证
			/*JSONObject object = SmsDemo2.verifySMSCode(phone, smscode);
			Integer code  = (Integer) object.get("code");
			if(code == 0) {
				logger.debug("手机号:"+phone + ",验证码:" + smscode + " 验证成功。。。");
				OperationLogs logs = new OperationLogs();
				logs.setUserId(user_id);
				logs.setOperationTypeId(3);
				logs.setMsg("新手机:"+phone+"验证码验证成功");
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
				return RESCODE.VERTIFY_SMS_SUCCESS.getJSONRES();
			}else {
				logger.debug("手机号:"+phone + ",验证码:" + smscode + " 验证失败。。。");
				OperationLogs logs = new OperationLogs();
				logs.setUserId(user_id);
				logs.setOperationTypeId(3);
				logs.setMsg("新手机:"+phone+"验证码验证失败");
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
				return RESCODE.VERTIFY_SMS_FAIL.getJSONRES(object);
			}*/
		}else {
			return RESCODE.USER_ID_NOT_EXIST.getJSONRES();
		}		
	}
	/**
	 * 用户验证手机号验证码,首次登陆
	 * @param user_id
	 * @param smscode
	 * @return
	 */
	public JSONObject vertifySms(Long user_id,String smscode) {
		logger.debug("进入用户首次登陆手机号验证");
		logger.info("输入验证码:"+smscode+".");
		Optional<User> optional = userRepository.findById(user_id);
		if(optional.isPresent()) {
			String phone = optional.get().getPhone().trim();
			SmsSendDetailDTO smsDetail = getCode(phone);
			if(smsDetail!=null) {
				logger.debug("手机号："+phone+"下有发送验证码");
				//最新短息消息
				String code = smsDetail.getOutId();
				logger.debug("最新验证码为："+code);
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
				if(min<vertifytime) {//短信有效时间
					logger.debug("短信在有效期内");
					if(smscode.equals(code)) {
						logger.debug("手机号:"+phone + ",验证码:" + smscode + " 验证成功。。。");
						User user = optional.get();
						user.setIsvertifyphone((byte)1);
						user.setIslogin((byte)1);
						user.setPhone(phone);
						OperationLogs logs = new OperationLogs();
						logs.setUserId(user_id);
						logs.setOperationTypeId(3);
						logs.setMsg("手机验证码验证成功");
						logs.setCreateTime(new Date());
						operationLogsRepository.save(logs);
						return RESCODE.VERTIFY_SMS_SUCCESS.getJSONRES(optional.get());
					}else {
						logger.debug("手机号:"+phone + ",验证码:" + smscode + " 验证失败。。。");
						OperationLogs logs = new OperationLogs();
						logs.setUserId(user_id);
						logs.setOperationTypeId(3);
						logs.setMsg("手机验证码验证失败");
						logs.setCreateTime(new Date());
						operationLogsRepository.save(logs);
						return RESCODE.VERTIFY_SMS_FAIL.getJSONRES();
					}
				}else {
					return RESCODE.VERTIFY_SMS_TIMEOUT.getJSONRES();
				}
			}else {
				return RESCODE.VERTIFY_SMS_NULL.getJSONRES();
			}
			/*JSONObject object = SmsDemo2.verifySMSCode(phone, smscode);
			logger.debug(object);
			Integer code  = (Integer) object.get("code");
			if(code == 0) {
				User user = optional.get();
				logger.debug("手机号:"+phone + ",验证码:" + smscode + " 验证成功。。。");
				user.setIsvertifyphone((byte)1);
				user.setIslogin((byte)1);
				user.setPhone(phone);
				logger.debug("修改用户是否登陆和手机是否验证状态");
				userRepository.saveAndFlush(user);
				logger.debug("用户状态修改完成");
				OperationLogs logs1 = new OperationLogs();
				logs1.setUserId(user_id);
				logs1.setOperationTypeId(1);
				logs1.setMsg("首次登陆成功");
				logs1.setCreateTime(new Date());
				operationLogsRepository.save(logs1);
				return RESCODE.SUCCESS.getJSONRES(optional.get());
			}else {
				OperationLogs logs = new OperationLogs();
				logs.setUserId(user_id);
				logs.setOperationTypeId(3);
				logs.setMsg("首次登陆，验证手机号失败");
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
				return RESCODE.VERTIFY_SMS_FAIL.getJSONRES();
			}*/
		}else {
			return RESCODE.USER_ID_NOT_EXIST.getJSONRES();
		}
	}
	/**
	 * 邮箱的简单正则判断
	 * @param email
	 * @return
	 */
	public boolean vertifyEmail1(String email){
		//1.正则判断
		if (email == null)
			return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(email);
		return m.matches();
	}
	/**
	 * 向邮箱发送验证码，用于验证用户邮箱
	 * @param id--用户id
	 * @param email
	 * @return
	 */
	public JSONObject sendEmail(Long id,String email){
		 boolean flag = vertifyEmail1(email);
		 String code = String.valueOf(getRandom());
		 if(flag) {
			 Optional<User> userOptional = userRepository.findById(id);
			 if(userOptional.isPresent()) {
				 userOptional.get().setEmail_code(code);
			 }
			 OperationLogs logs = new OperationLogs();
			 logs.setUserId(id);
			 logs.setOperationTypeId(4);
			 logs.setMsg("向邮箱"+email+"发送验证码");
			 logs.setCreateTime(new Date());
			 operationLogsRepository.save(logs);
			 return SendMailUtils.sendMail(email, code, "智能感知平台");
		 }else {
			return RESCODE.EMAIL_WRONG_FORMAT.getJSONRES(); 
		 }
	}
	/**
	 * 验证用户邮箱,判断验证码是否正确
	 * @param id
	 * @param email
	 * @param code
	 * @return
	 */
	public JSONObject vertifyEmail(Long id,String email,String code){
		Optional<User> userOptional = userRepository.findById(id);
		if(userOptional.isPresent()) {
			if(userOptional.get().getEmail_code().equals(code)) {
				userOptional.get().setIsvertifyemail((byte)1);
				userOptional.get().setEmail(email);
				OperationLogs logs = new OperationLogs();
				logs.setUserId(id);
				logs.setOperationTypeId(4);
				logs.setMsg("验证邮箱验证码成功");
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
				return RESCODE.EMAIL_VERTIFY_SUCCESS.getJSONRES();
			}else {
				userOptional.get().setIsvertifyemail((byte)0);
				OperationLogs logs = new OperationLogs();
				logs.setUserId(id);
				logs.setOperationTypeId(4);
				logs.setMsg("验证邮箱验证码失败");
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
				return RESCODE.EMAIL_VERTIFY_FAILURE.getJSONRES();
			}			
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 验证触发器邮箱是否正确，判断验证码是否正确
	 * @param id
	 * @param code
	 * @return
	 */
	public JSONObject vertifyEmailForUrl(Long id,String code){
		Optional<User> userOptional = userRepository.findById(id);
		if(userOptional.isPresent()) {
			if(userOptional.get().getEmail_code().equals(code)) {
				OperationLogs logs = new OperationLogs();
				logs.setUserId(id);
				logs.setOperationTypeId(4);
				logs.setMsg("验证触发器邮箱验证码成功");
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
				return RESCODE.EMAIL_VERTIFY_SUCCESS.getJSONRES();
			}else {
				OperationLogs logs = new OperationLogs();
				logs.setUserId(id);
				logs.setOperationTypeId(4);
				logs.setMsg("验证触发器邮箱验证码失败");
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
				return RESCODE.EMAIL_VERTIFY_FAILURE.getJSONRES();
			}			
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}

	public JSONObject getExpires(String newPhone) {
		JSONObject object = new JSONObject();
		SmsSendDetailDTO smsDetail = getCode(newPhone);
		if (smsDetail != null) {
			logger.debug("手机号：" + newPhone + "下有发送验证码");
			//最新短息消息
			String codeReturn = smsDetail.getOutId();
			object.put("codeReturn",codeReturn);
			logger.debug("最新验证码为：" + codeReturn);
			String receiveDate = smsDetail.getReceiveDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int min = 0;
			try {
				Date date = sdf.parse(receiveDate);
				Date now = new Date();
				long cost = now.getTime() - date.getTime();
				min = (int) (cost / 1000 / 60);
				object.put("min",min);
			} catch (ParseException e) {
				logger.error(e.getMessage());
			}
		}
		return object;
	}

	
}

