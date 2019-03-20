package com.hydata.intelligence.platform.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse.SmsSendDetailDTO;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.OperationLogs;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.dto.User;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.DeviceDatastreamRepository;
import com.hydata.intelligence.platform.repositories.DeviceRepository;
import com.hydata.intelligence.platform.repositories.OperationLogsRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.repositories.UserRepository;
import com.hydata.intelligence.platform.utils.Config;
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
	private DeviceDatastreamRepository deviceDatastreamRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OperationLogsRepository operationLogsRepository;
	
	@Autowired
	private DeviceRepository deviceRepository;
	
	@Value("${spring.data.mongodb.uri}")
	private String mongouri;
	
	@Value("${spring.datasource.url}")
	private String mysqlurl;
	
	private Logger logger = LogManager.getLogger(UserService.class);
	/**
	 * 用户登陆
	 * @param name
	 * @param pwd
	 * @return
	 */
	public JSONObject login(String name, String pwd){
		logger.info("MongoDB数据库地址：");
		logger.info(mongouri);
		logger.info("mysql数据库地址：");
		logger.info(mysqlurl);
		Optional<User> userOptional = userRepository.findByName(name);
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			if(user.getIsvalid()==0) {
				return RESCODE.USER_IS_NOT_VALID.getJSONRES();
			}
			if(user.getIslogin()==1) {
				return RESCODE.USER_ALREADY_IN.getJSONRES();
			}
			if(MD5.compute(pwd.trim()).equals(user.getPwd())) {
				if(user.getIsvertifyphone()==1) {
					user.setIslogin((byte)1);
					OperationLogs logs = new OperationLogs();
					logs.setUserId(user.getId());
					logs.setOperationTypeId(1);
					logs.setMsg("登陆成功");
					logs.setCreateTime(new Date());
					operationLogsRepository.save(logs);
					return RESCODE.SUCCESS.getJSONRES(user);
				}else {
					OperationLogs logs = new OperationLogs();
					logs.setUserId(user.getId());
					logs.setOperationTypeId(1);
					logs.setMsg("首次登陆，进入验证手机号");
					logs.setCreateTime(new Date());
					operationLogsRepository.save(logs);
					return RESCODE.PHONE_NOT_VERTIFY.getJSONRES(user);
				}				
			}else {
				OperationLogs logs = new OperationLogs();
				logs.setUserId(user.getId());
				logs.setOperationTypeId(1);
				logs.setMsg("登陆失败，密码错误");
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
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
	public JSONObject logout(Long id){
		Optional<User> userOptional = userRepository.findById(id);
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			
			if(user.getIslogin()==0) {
				return RESCODE.USER_ALREADY_OUT.getJSONRES();
			}
			user.setIslogin((byte)0);
			OperationLogs logs = new OperationLogs();
			logs.setUserId(user.getId());
			logs.setOperationTypeId(2);
			logs.setMsg("注销登陆");
			logs.setCreateTime(new Date());
			operationLogsRepository.save(logs);
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
	@Transactional
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
	
	public JSONObject vertifyAndModifyUserPhone(Long user_id,String newPhone, String code){
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
				if(min<Config.getInt("aliyun.vertifytime")) {//短信有效时间
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
	
	public JSONObject modifyUser(User user) {
		Optional<User> userOptional = userRepository.findById(user.getId());
		if(userOptional.isPresent()) {
			if(user.getPwd()!=null) {
				userOptional.get().setPwd(MD5.compute(user.getPwd()));
			}
			try{
				
				if(user.getPhone()!=null) {
					userOptional.get().setPhone(user.getPhone());
					userOptional.get().setIsvertifyphone((byte)1);
				}
				if(user.getEmail()!=null) {
					userOptional.get().setEmail(user.getEmail());
					userOptional.get().setIsvertifyphone((byte)1);
				}
			}catch (Exception e) {

			    BindException ex = (BindException)e;

			    List<ObjectError> errors = ex.getAllErrors();

			    ObjectError error = errors.get(0);

			    String msg = error.getDefaultMessage();
			    
			    return RESCODE.FAILURE.getJSONRES(msg) ;
			}
			
			return RESCODE.SUCCESS.getJSONRES();
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}
	
	public JSONObject adminModifyUser(User user) {
		System.out.println(user.toString());
		Optional<User> userOptional = userRepository.findById(user.getId());
		if(userOptional.isPresent()) {
			if(user.getName()!=null && userOptional.get().getName().equals(user.getName()) == false){
				JSONObject result = vertifyName(user.getName());
				if((Integer)result.get("code")==2) {//无重复用户名
					userOptional.get().setName(user.getName());
				}else {
					return result;
				}
			}
			if(userOptional.get().getPhone()==null||userOptional.get().getPhone().equals(user.getPhone()==null?"":user.getPhone())==false) {
				userOptional.get().setPhone(user.getPhone());
				userOptional.get().setIsvertifyphone((byte)0);
			}
			if(userOptional.get().getEmail()==null||userOptional.get().getEmail().equals(user.getEmail()==null?"":user.getEmail())==false) {
				userOptional.get().setEmail(user.getEmail());
				userOptional.get().setIsvertifyemail((byte)0);
			}
			if(userOptional.get().getPwd()==null||userOptional.get().getPwd().equals(user.getPwd()==null?"":MD5.compute(user.getPwd()))==false){
				userOptional.get().setPwd(MD5.compute(user.getPwd()));
			}			
			userOptional.get().setIslogin((byte)0);
			return RESCODE.SUCCESS.getJSONRES();
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 首页
	 * 获取全站总览数据量：
	 * 用户总量
	 * 设备总量
	 * 设备数据流总量
	 * @return
	 */
	public  JSONObject getGlobalStatistics() {
		long uSum = userRepository.count();
		long dSum = deviceRepository.count();
		long ddSum = deviceDatastreamRepository.count();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("user_sum", uSum);
		jsonObject.put("device_sum", dSum);
		jsonObject.put("device_datastream_sum", ddSum);
		return RESCODE.SUCCESS.getJSONRES(jsonObject);
	}
	/**
	 * 个人页面
	 * 获取个人产品量
	 * @return
	 */
	public JSONObject getProductQuantity(Long userId) {
		JSONObject jsonObject = new JSONObject();
		if(userId==0) {//管理员用户
			long product_sum =  productRepository.count();
			jsonObject.put("product_sum", product_sum);	
		}else {
			List<Product> products = productRepository.findByUserId(userId);
			if(products!=null && products.size()>0) {
				jsonObject.put("product_sum", products.size());			
			}else {
				jsonObject.put("product_sum", 0);
			}						
		}
		return RESCODE.SUCCESS.getJSONRES(jsonObject);
	}
	
	public JSONObject getOperationLogs(Integer userId,String keyWord) {
		List<OperationLogs> ols = operationLogsRepository.findByUserIdAndKeyWord(userId,keyWord);
		return RESCODE.SUCCESS.getJSONRES(ols);
	}

}

