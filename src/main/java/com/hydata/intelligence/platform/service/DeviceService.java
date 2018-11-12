package com.hydata.intelligence.platform.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.DeviceDatastream;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.DeviceDatastreamRepository;
import com.hydata.intelligence.platform.repositories.DeviceRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.utils.ExcelUtils;

/**
 * @author pyt
 * @createTime 2018年10月31日上午11:39:02
 */
@Transactional
@Service
public class DeviceService {
	@Autowired
	private DeviceRepository deviceRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private DeviceDatastreamRepository deviceDatastreamRepository;
	
	@Autowired
	private ExcelUtils excelUtils;
	
	private static Logger logger = LogManager.getLogger(DeviceService.class);
	
	@SuppressWarnings("deprecation")
	public Page<Device> showAllByProductId(Integer product_id,Integer page,Integer number){
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"id");
		return deviceRepository.findByProductId(product_id, pageable);
	}
	/**
	 * 添加设备
	 * @param device
	 * @return
	 */
	public JSONObject addDevice(Device device){
		Optional<Product> productOptional = productRepository.findById(device.getProductId());
		logger.debug("检查添加设备的产品id是否存在");
		if(productOptional.isPresent()) {
			logger.debug("产品id存在");
			Optional<Device> deviceOptional = deviceRepository.findByProductIdAndDeviceSn(device.getProductId(), device.getDevice_sn());
			logger.debug("检查添加设备的鉴权信息是否重复");
			if(deviceOptional.isPresent()) {
				return RESCODE.AUTH_INFO_EXIST.getJSONRES();
			}
			logger.debug("产品:"+device.getProductId()+"下鉴权信息："+ device.getDevice_sn()+"不重复");
			device.setProtocolId(productOptional.get().getProtocolId());
			device.setCreateTime(new Date());
			Device deviceReturn= deviceRepository.save(device);
			return RESCODE.SUCCESS.getJSONRES(deviceReturn);
		}
		logger.debug("产品id不存在");
		return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 在产品中根据设备id或设备名称查询
	 * @param product_id
	 * @param page
	 * @param number
	 * @param device_snOrName
	 * @return
	 */
	public Page<Device> queryByDeviceSnOrName(Integer product_id,String deviceSnOrName,Integer page,Integer number){
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"id");
		Page<Device> result = deviceRepository.findAll(new Specification<Device>() {
			
			public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				 List<Predicate> predicateList = new ArrayList<>();

				 if (product_id != null && product_id >= 0) {
	                    predicateList.add(
	                            criteriaBuilder.equal(
	                                    root.get("productId").as(Integer.class),
	                                    product_id));
	             }
				 if(deviceSnOrName!=null && !deviceSnOrName.equals("")) {
					 if(isInteger(deviceSnOrName)) {
						 predicateList.add(
									//like：模糊匹配，跟SQL是一样的
			                            criteriaBuilder.like(
			                                    //user表里面有个String类型的name
			                                    root.get("device_sn").as(String.class),
			                                    //映射规则
			                                    "%" + deviceSnOrName + "%"));
					 }else {
						 predicateList.add(
		                            criteriaBuilder.like(
		                            		root.get("name").as(String.class),
		                            		"%" + deviceSnOrName + "%"));
					 }					 				
				 }
				 Predicate[] predicates = new Predicate[predicateList.size()];
	             return criteriaBuilder.and(predicateList.toArray(predicates));
			}
		}, pageable);
		
		return result;
	}
	/**
	 * 修改设备信息（设备名称、设备鉴权码、icon）
	 * @param device
	 * @return
	 */
	public JSONObject modifyDevice(Device device){
		Optional<Device> devOptional = deviceRepository.findById(device.getId());
		if(devOptional.isPresent()) {
			if(devOptional.get().getDevice_sn().equals(device.getDevice_sn())==false) {
				Optional<Device> deviceOptional = deviceRepository.findByProductIdAndDeviceSn(device.getProductId(), device.getDevice_sn());
				if(deviceOptional.isPresent()) {
					return RESCODE.AUTH_INFO_EXIST.getJSONRES();
				}else {
					devOptional.get().setDevice_sn(device.getDevice_sn());
				}
			}
			devOptional.get().setName(device.getName());
			devOptional.get().setModifyTime(new Date());
			Device deviceReturn= deviceRepository.save(devOptional.get());
			return RESCODE.SUCCESS.getJSONRES(deviceReturn);			
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 删除设备
	 * 1.数据流触发器（未完成）
	 * 2.设备（完成）
	 * @param id
	 * @return
	 */
	public JSONObject deleteDevice(Integer id){
		deviceRepository.deleteById(id);
		return RESCODE.SUCCESS.getJSONRES();
	}
	
	/**
	 * 获取产品下设备列表
	 * @param productId
	 * @return
	 */
	public JSONObject getByProductId(Integer productId) {
		List<Device> deviceList = deviceRepository.findByProductId(productId);
		JSONArray array = new JSONArray();
		for(Device device : deviceList) {
			array.add(device);
		}
		return RESCODE.SUCCESS.getJSONRES(array);
	}
	/**
	 * 获取设备下数据流列表
	 * @param deviceId
	 * @return
	 */
	public JSONObject getDDByDeviceId(Integer deviceId) {
		Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
		if(deviceOptional.isPresent()) {
			List<DeviceDatastream> ddList = deviceDatastreamRepository.findByDeviceId(deviceId);
			JSONArray array = new JSONArray();
			for(DeviceDatastream datastream : ddList) {
				array.add(datastream);
			}
			return RESCODE.SUCCESS.getJSONRES(array);
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}
	
	/**
	 * 解析设备上传的数据流
	 * 1.存储数据流
	 * 2.存储数据
	 * 3.触发
	 * @param jsonObject
	 */
	public void resolveDeviceData(JSONObject jsonObject) {
		
		logger.debug(jsonObject);
		//jsonObject.
		
	}
	
	/**
	 * 检查设备数据流，存储数据流
	 * @param deviceId
	 * @param dsName
	 */
	public void checkDsExistAndSave(Integer deviceId,String dsName) {
		logger.debug("检查设备："+deviceId+"下数据流："+dsName+"是否存在");
		Optional<DeviceDatastream> optional = deviceDatastreamRepository.findByDeviceIdAndDm_name(deviceId, dsName);
		if(optional.isPresent() == false) {
			logger.debug("设备："+deviceId+"下数据流："+dsName+"不存在，开始添加");
			DeviceDatastream datastream = new DeviceDatastream();
			datastream.setDeviceId(deviceId);
			datastream.setDm_name(dsName);
			deviceDatastreamRepository.save(datastream);
		}
	}
	
	public static boolean isInteger(String str) {    
	    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
	    return pattern.matcher(str).matches();    
	 }
	/**
	 * 获取
	 * @param deviceId
	 */
	public void checkTrigger(Integer deviceId) {
		
	}
	
	/**
	 * 解析表格中的设备信息并保存
	 * @param url
	 * @param productId
	 * @return
	 */
	public JSONObject importExcel(String url,Integer productId) {
		Optional<Product> productOptional = productRepository.findById(productId);
		if(productOptional.isPresent()) {
			Product product = productOptional.get();
			JSONObject object = ExcelUtils.importExcel(url);
			Set<String> keys = object.keySet();
			Iterator iterator = keys.iterator();
			while(iterator.hasNext()) {
				Device device = new Device();
				String key = (String) iterator.next();
				String value = (String) object.get(key);
				device.setName(key);
				device.setDevice_sn(value);
				device.setCreateTime(new Date());
				device.setProductId(productId);
				device.setProtocolId(product.getProtocolId());
				deviceRepository.save(device);
			}
			return RESCODE.SUCCESS.getJSONRES();
		}
		return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
	}
}

