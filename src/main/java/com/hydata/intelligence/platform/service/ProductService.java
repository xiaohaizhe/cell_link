package com.hydata.intelligence.platform.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.dto.Protocol;
import com.hydata.intelligence.platform.dto.User;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.DeviceRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.repositories.ProtocolRepository;
import com.hydata.intelligence.platform.repositories.UserRepository;


/**
 * @author pyt
 * @createTime 2018年10月30日下午5:03:39
 */
@Transactional
@Service
public class ProductService {
	@Autowired
	private ProtocolRepository protocolRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DeviceRepository deviceRepository;
	
	private static Logger logger = LogManager.getLogger(DataStreamModelService.class);
	
	/**
	 * 获取全部协议
	 * @return
	 */
	public JSONObject getProtocol(){
		List<Protocol> protocolList = protocolRepository.findAll();
		if(protocolList!=null&&protocolList.size()>0) {
			return RESCODE.SUCCESS.getJSONRES(protocolList);
		}else {
			return RESCODE.FAILURE.getJSONRES();
		}
	}
	/**
	 * 添加产品
	 * @param product
	 * @return
	 */
	public JSONObject addProduct(Product product){		
		Optional<User> userOptional = userRepository.findById(product.getUserId());
		if(userOptional.isPresent()) {
			JSONObject result = checkProductName(product.getUserId(),product.getName());
			if((Integer)result.get("code")==2) {
				//用户名下产品名不存在
				//产品类型未定，均设为0
				product.setProductTypeId(0);
				product.setCreateTime(new Date());
				Product productReturn = productRepository.save(product);
				if(productReturn!=null) {
					return RESCODE.SUCCESS.getJSONRES(productReturn);
				}
				return RESCODE.FAILURE.getJSONRES();
			}
			return RESCODE.PRODUCT_NAME_EXIST.getJSONRES();
		}
		return RESCODE.USER_ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 修改产品
	 * @param product
	 * @return
	 */
	public JSONObject modifyProduct(Product product){
		//1.检查产品id是否存在
		Optional<Product> productOptional = productRepository.findById(product.getId());
		if(productOptional.isPresent()) {		
			Product productReturn = productOptional.get();
			if(productReturn.getName().equals(product.getName())==false) {
				JSONObject result = checkProductName(product.getUserId(),product.getName());
				if((Integer)result.get("code")==2) {
					productReturn.setName(product.getName());
				}else {
					return RESCODE.PRODUCT_NAME_EXIST.getJSONRES();
				}				
			}
			
			productReturn.setDescription(product.getDescription());
			productReturn.setLatitude(product.getLatitude());
			productReturn.setLontitude(product.getLontitude());
			return RESCODE.SUCCESS.getJSONRES();
		}		
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 检查用户名下产品名是否重复
	 * @param user_id
	 * @param name
	 * @return
	 */
	public JSONObject checkProductName(Integer user_id,String name){
		logger.debug("检查用户名下产品名是否重复");
		Optional<Product> productOptional = productRepository.findByUserIdAndName(user_id, name);
		if(productOptional.isPresent()) {
			return RESCODE.PRODUCT_NAME_EXIST.getJSONRES();
		}
		return RESCODE.PRODUCT_NAME_NOT_EXIST.getJSONRES();
	}
	/**
	 * 获取用户下产品列表
	 * @param user_id
	 * @param page
	 * @param number
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Page<Product> queryByUserId(Integer user_id,Integer page,Integer number){
		Pageable pageable = new PageRequest(page, number, Sort.Direction.DESC,"id");
		return productRepository.queryByUserId(user_id, pageable);
	}
	/**
	 * 删除产品
	 * 包括：
	 * 设备、数据流模板、触发器、应用、、、、、
	 * @param product_id
	 * @return
	 */
	public JSONObject delete(Integer product_id){
		
		
		return null;
	}
	/**
	 * 获取产品详情
	 * 1.产品具体信息
	 * 2.产品设备数量
	 * 3.产品数据点数量
	 * 4.产品触发信息数量
	 * @param product_id
	 * @return
	 */
	public JSONObject getDetail(Integer product_id) {
		JSONObject jsonObject = new JSONObject();
		Optional<Product> productOptional =  productRepository.findById(product_id);
		if(productOptional.isPresent()) {
			jsonObject.put("product", productOptional.get());
			List<Device> deviceList = deviceRepository.findByProductId(product_id);			
		}
		return RESCODE.SUCCESS.getJSONRES(jsonObject);
		
	} 
}

