package com.hydata.intelligence.platform.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.hydata.intelligence.platform.dto.OperationLogs;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.dto.Protocol;
import com.hydata.intelligence.platform.dto.TriggerModel;
import com.hydata.intelligence.platform.dto.User;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.ApplicationRepository;
import com.hydata.intelligence.platform.repositories.DeviceDatastreamRepository;
import com.hydata.intelligence.platform.repositories.OperationLogsRepository;
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
	private DeviceDatastreamRepository datastreamRepository;
	
	@Autowired
	private ApplicationRepository  applicationRepository;
	
	@Autowired
	private TriggerRepository triggerRepository;
	
	@Autowired
	private OperationLogsRepository operationLogsRepository;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private ApplicationService applicationService;
	
	
	
	private static Logger logger = LogManager.getLogger(DataStreamModelService.class);
	
	public void setProtocol() {
		
		List<Protocol> list = protocolRepository.findAll();
		List<String> ps = new ArrayList<>();
		for(Protocol prot:list) {
			ps.add(prot.getName());
		}
		if(ps.contains("MQTT")==false) {
			Protocol protocol = new Protocol();
			protocol.setName("MQTT");
			protocolRepository.save(protocol);
		}
		if(ps.contains("HTTP")==false) {
			Protocol protocol = new Protocol();
			protocol.setName("HTTP");
			protocolRepository.save(protocol);
		}		
	}
	/**
	 * 获取全部协议
	 * @return
	 */
	public JSONObject getProtocol(){
		logger.debug("进入获取全部协议");
		List<Protocol> protocolList = protocolRepository.findAll();
		if(protocolList!=null&&protocolList.size()>0) {
			logger.debug("成功获取协议列表："+protocolList.toString());
			return RESCODE.SUCCESS.getJSONRES(protocolList);
		}else {
			logger.debug("获取协议列表失败");
			return RESCODE.FAILURE.getJSONRES();
		}
	}
	/**
	 * 添加产品
	 * @param product
	 * @return
	 */
	public JSONObject addProduct(Product product){
		OperationLogs logs = new OperationLogs();
		logs.setUserId(product.getUserId());
		logs.setOperationTypeId(5);
		logs.setMsg("添加产品:"+product.toString());
		logs.setCreateTime(new Date());
		operationLogsRepository.save(logs);
		Optional<User> userOptional = userRepository.findById(product.getUserId());
		if(userOptional.isPresent()) {
			JSONObject result = checkProductName(product.getUserId(),product.getName());
			if((Integer)result.get("code")==2) {//产品名不存在				
				//产品类型字段弃用
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
		OperationLogs logs = new OperationLogs();
		logs.setUserId(product.getUserId());
		logs.setOperationTypeId(5);
		logs.setMsg("修改产品，产品修改为："+product.toString());
		logs.setCreateTime(new Date());
		operationLogsRepository.save(logs);
		//1.检查产品id是否存在
		Optional<Product> productOptional = productRepository.findById(product.getId());
		if(productOptional.isPresent()) {		
			Product productReturn = productOptional.get();
			if(productReturn.getName().equals(product.getName())==false) {
				JSONObject result = checkProductName(product.getUserId(),product.getName());
				if((Integer)result.get("code")==2) {//用户下产品名
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
		logger.debug("检查用户:"+user_id+"名下产品名:"+name+"是否重复");
		Optional<Product> productOptional = productRepository.findByUserIdAndName(user_id, name);
		if(productOptional.isPresent()) {
			logger.debug("用户:"+user_id+"名下产品名:"+name+"重复");
			return RESCODE.PRODUCT_NAME_EXIST.getJSONRES();
		}
		logger.debug("用户:"+user_id+"名下产品名:"+name+"不重复");
		return RESCODE.PRODUCT_NAME_NOT_EXIST.getJSONRES();
	}
	/**
	 * 获取用户下产品列表
	 * @param user_id
	 * @param page
	 * @param number
	 * @return
	 */
	public JSONObject queryByUserId(Integer user_id,Integer page,Integer number,Integer sort){
		Optional<User> optional = userRepository.findById(user_id);
		if(optional.isPresent()) {
			Pageable pageable;
			if(sort==0) {
				//逆序
				pageable = new PageRequest(page, number, Sort.Direction.DESC,"id");
			}else {
				//顺序
				pageable = new PageRequest(page, number, Sort.Direction.ASC,"id");
			}			
			Page<Product> result = productRepository.queryByUserId(user_id, pageable);
			return RESCODE.SUCCESS.getJSONRES(result.getContent(),result.getTotalPages(),result.getTotalElements());
		}else {
			return RESCODE.USER_ID_NOT_EXIST.getJSONRES();
		}
		
	}
	/**
	 * 删除产品
	 * 包括：
	 * 设备、数据流模板、触发器、应用、、、、、
	 * @param product_id
	 * @return
	 */
	public JSONObject delete(Integer product_id){		
		Optional<Product> optional = productRepository.findById(product_id);
		if(optional.isPresent()) {
			//1.查找产品下设备，删除设备
			JSONObject devices_object = deviceService.getByProductId(product_id);
			JSONArray device_array = (JSONArray) devices_object.get("data");
			for(int i = 0 ; i < device_array.size() ; i++) {
				Device device = (Device) device_array.get(i);
				deviceService.deleteDevice(device.getDevice_sn());
			}
			//2.查找产品下应用，删除应用
			applicationService.deleteByProductId(product_id);			
			//3.查找产品下触发器，删除触发器
			//2.删除产品
			productRepository.deleteById(product_id);
			OperationLogs logs = new OperationLogs();
			logs.setUserId(product_id);
			logs.setOperationTypeId(5);
			logs.setMsg("删除产品");
			logs.setCreateTime(new Date());
			operationLogsRepository.save(logs);
			return RESCODE.SUCCESS.getJSONRES();
		}
		return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 获取产品详情
	 * 1.产品具体信息
	 * 2.产品设备数量
	 * 3.产品数据点数量
	 * 4.产品触发信息数量(未完成)
	 * @param product_id
	 * @return
	 */
	public JSONObject getDetail(Integer product_id) {
		JSONObject jsonObject = new JSONObject();
		Optional<Product> productOptional =  productRepository.findById(product_id);
		if(productOptional.isPresent()) {
			jsonObject.put("product", productOptional.get());
			logger.debug(productOptional.get().toString());
			JSONObject object =  deviceService.getByProductId(product_id);
			JSONArray devices = (JSONArray) object.get("data");
			jsonObject.put("device_sum", devices.size());
			long datastream_sum = 0;
			for(int i = 0;i<devices.size();i++) {
				Device device = (Device) devices.get(i);
				List<DeviceDatastream> datastreams = datastreamRepository.findByDeviceSn(device.getDevice_sn());
				datastream_sum += datastreams.size();
			}
			jsonObject.put("datastream_sum", datastream_sum);
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
		JSONObject result = deviceService.getByProductId(productId);
		JSONArray devices = (JSONArray) result.get("data");
		if(devices!=null&&devices.size()>0) {
			jsonObject.put("device_sum", devices.size());
			long ddsum = 0;
			for(int i = 0;i<devices.size();i++) {
				Device device = (Device) devices.get(i);
				if(device.getStatus()==1) {
					List<DeviceDatastream> deviceDatastreams = datastreamRepository.findByDeviceSn(device.getDevice_sn());
					if(deviceDatastreams!=null&&deviceDatastreams.size()>0) {
						ddsum+=deviceDatastreams.size();
					}
				}							
			}
			jsonObject.put("device_datastream_sum", ddsum);
		}else {
			jsonObject.put("device_sum", 0);
		}
		List<Application> applications = applicationRepository.findByProductIdAndType(productId,0);
		if(applications!=null&&applications.size()>0) {
			jsonObject.put("application_sum", applications.size());
			int c_sum =0;
			for(Application application :applications) {
				if(application.getApplicationType() == 0) {
					c_sum++;
				}
			}
			jsonObject.put("correlation_analyse_sum", c_sum);
			jsonObject.put("linear_analyse_sum", applications.size()-c_sum);
		}else {
			jsonObject.put("application_sum", 0);
			jsonObject.put("correlation_analyse_sum", 0);
			jsonObject.put("linear_analyse_sum", 0);
		}
		List<TriggerModel> triggerModels = triggerRepository.findByProductId(productId);
		if(triggerModels!=null && triggerModels.size()>0) {
			jsonObject.put("trigger_sum", applications.size());
		}else {
			jsonObject.put("trigger_sum", 0);
		}				
		return RESCODE.SUCCESS.getJSONRES(jsonObject);
	}
	
	/*@SuppressWarnings("deprecation")
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
	}*/
}

