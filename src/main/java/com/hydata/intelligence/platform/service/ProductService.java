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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.Application;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.DeviceDatastream;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.dto.Protocol;
import com.hydata.intelligence.platform.dto.TriggerModel;
import com.hydata.intelligence.platform.dto.User;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.ApplicationRepository;
import com.hydata.intelligence.platform.repositories.DeviceDatastreamRepository;
import com.hydata.intelligence.platform.repositories.DeviceRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.repositories.ProtocolRepository;
import com.hydata.intelligence.platform.repositories.TriggerRepository;
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
	
	@Autowired
	private DeviceDatastreamRepository datastreamRepository;
	
	@Autowired
	private ApplicationRepository  applicationRepository;
	
	@Autowired
	private TriggerRepository triggerRepository;
	
	@Autowired
	private DeviceService deviceService;
	
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
	public Page<Product> queryByUserId(Integer user_id,Integer page,Integer number,Integer sort){
		Pageable pageable;
		if(sort==0) {
			//逆序
			pageable = new PageRequest(page, number, Sort.Direction.DESC,"id");
		}else {
			//顺序
			pageable = new PageRequest(page, number, Sort.Direction.ASC,"id");
		}
		
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
	/**
	 * 获取首页热力图
	 * @return
	 */
	public JSONObject getHeatmap() {
		List<Product> products =productRepository.findAll();
		JSONArray array = new JSONArray();
		for(Product product :products) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("latitude", product.getLatitude());
			jsonObject.put("lontitude", product.getLontitude());
			array.add(jsonObject);
		}
		return RESCODE.SUCCESS.getJSONRES(array);
	}
	/**
	 * 获取产品概括数据信息
	 * 已连接设备
	 * 在线数据流
	 * 应用
	 * 触发器
	 * 相关性热力图（未加入）
	 * 线性回归数（未加入）
	 * @param productId
	 * @return
	 */
	public JSONObject getProductOverview(Integer productId) {
		JSONObject jsonObject = new JSONObject();		
		List<Device> devices = deviceRepository.findByProductId(productId);
		if(devices!=null&&devices.size()>0) {
			jsonObject.put("device_sum", devices.size());
			long ddsum = 0;
			for(Device device : devices) {
				List<DeviceDatastream> deviceDatastreams = datastreamRepository.findByDeviceId(device.getId());
				if(deviceDatastreams!=null&&deviceDatastreams.size()>0) {
					ddsum+=deviceDatastreams.size();
				}			
			}
			jsonObject.put("device_datastream_sum", ddsum);
		}else {
			jsonObject.put("device_sum", 0);
		}
		List<Application> applications = applicationRepository.findByProductIdAndType(productId,0);
		if(applications!=null&&applications.size()>0) {
			jsonObject.put("application_sum", applications.size());
		}else {
			jsonObject.put("application_sum", 0);
		}
		List<TriggerModel> triggerModels = triggerRepository.findByProductId(productId);
		if(triggerModels!=null && triggerModels.size()>0) {
			jsonObject.put("application_sum", applications.size());
		}else {
			jsonObject.put("application_sum", 0);
		}				
		return RESCODE.SUCCESS.getJSONRES(jsonObject);
	}
	
	public JSONObject getIncrement(Integer productId ,Integer type) {
		Date end = new Date();
		Date start = new Date();
		if(type==0) {			
			start.setDate(start.getDate()-start.getDay()+1);
			start.setHours(0);
			start.setMinutes(0);
			start.setSeconds(0);
		}else {
			start.setDate(1);
			start.setHours(0);
			start.setMinutes(0);
			start.setSeconds(0);
		}
//		获取设备信息
		JSONObject jsonObject = deviceService.getIncrement(productId, start, end);		
		return jsonObject;
	}
}

