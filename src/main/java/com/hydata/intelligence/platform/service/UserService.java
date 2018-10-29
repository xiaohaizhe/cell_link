package com.hydata.intelligence.platform.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import com.hydata.intelligence.platform.dto.User;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.UserRepository;
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
	
	public Map<String, Object> login(String name, String pwd){
		Optional<User> userOptional = userRepository.findByName(name);
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			if(MD5.compute(pwd.trim()).equals(user.getPwd())) {
				user.setPwd(null);
				return RESCODE.SUCCESS.getJSONRES(user);
			}else {
				return RESCODE.WRONG_PWD.getJSONRES();
			}			
		}else {			
			return RESCODE.NAME_NOT_EXIST.getJSONRES();
		}
	}
	
	public Map<String, Object> logout(Integer id){
		Optional<User> userOptional = userRepository.findById(id);
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			user.setIslogin((byte)0);
			userRepository.save(user);
			return RESCODE.SUCCESS.getJSONRES();
		}else {
			return RESCODE.ID_NOT_EXIST.getJSONRES();
		}		
	}
	
	public Map<String , Object> vertifyName(String name){
		Optional<User> userOptional = userRepository.findByName(name);
		if(userOptional.isPresent()) {
			return RESCODE.NAME_EXIST.getJSONRES();
		}else {
			return RESCODE.NAME_NOT_EXIST.getJSONRES();
		}
	}

}

