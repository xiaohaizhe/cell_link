package com.hydata.intelligence.platform.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.DeviceDatastream;
import com.hydata.intelligence.platform.dto.OperationLogs;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.DeviceDatastreamRepository;
import com.hydata.intelligence.platform.repositories.DeviceRepository;
import com.hydata.intelligence.platform.repositories.OperationLogsRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.utils.ExcelUtils;
import com.hydata.intelligence.platform.utils.MongoDBUtils;
import com.hydata.intelligence.platform.utils.StringUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

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
	private OperationLogsRepository operationLogsRepository;
	
	private static Logger logger = LogManager.getLogger(DeviceService.class);
	
	@SuppressWarnings("deprecation")
	public Page<Device> showAllByProductId(Integer product_id,Integer page,Integer number,int sort){
		Pageable pageable;
		if(sort==0) {
			//逆序
			pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"id");
		}else {
			//顺序
			pageable = new PageRequest(page-1, number, Sort.Direction.ASC,"id");
		}
		
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
			OperationLogs logs = new OperationLogs();
			logs.setUserId(productOptional.get().getUserId());
			logs.setOperationTypeId(6);
			logs.setMsg("添加设备:"+device.getDevice_sn());
			logs.setCreateTime(new Date());
			operationLogsRepository.save(logs);
			logger.debug("产品id存在");
			Optional<Device> deviceOptional = deviceRepository.findByProductIdAndDeviceSn(device.getProductId(), device.getDevice_sn());
			logger.debug("检查添加设备的鉴权信息是否重复");
			if(deviceOptional.isPresent()) {
				return RESCODE.AUTH_INFO_EXIST.getJSONRES();
			}
			logger.debug("产品:"+device.getProductId()+"下鉴权信息："+ device.getDevice_sn()+"不重复");
			device.setProtocolId(productOptional.get().getProtocolId());
			device.setCreateTime(new Date());
			
			MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();
	        MongoClient meiyaClient = mongoDBUtil.getMongoConnect("127.0.0.1",27017);
	 
	        try {
	            MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");
	            Map<String,Object> insert = new HashMap<>();
	               //1、测试增加
	            insert.put("dd_id",2);
	            mongoDBUtil.insertDoucument(collection,insert);
	           
	         
	        } catch (Exception e) {
	            System.err.println(e.getClass().getName() + ": " + e.getMessage());
	        }
			
			
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
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"id");
		Page<Device> result = deviceRepository.findAll(new Specification<Device>() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
			
			Optional<Product> productOptional = productRepository.findById(devOptional.get().getProductId()) ;
			if(productOptional.isPresent()) {
				OperationLogs logs = new OperationLogs();
				logs.setUserId(productOptional.get().getUserId());
				logs.setOperationTypeId(6);
				logs.setMsg("修改设备:"+device.getDevice_sn());
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
			}			
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
		Optional<Device> deviceOptional = deviceRepository.findById(id);
		if(deviceOptional.isPresent()) {
			Optional<Product> productOptional = productRepository.findById(deviceOptional.get().getProductId());
			if(productOptional.isPresent()) {
				OperationLogs logs = new OperationLogs();
				logs.setUserId(productOptional.get().getUserId());
				logs.setOperationTypeId(6);
				logs.setMsg("删除设备:"+deviceOptional.get().getDevice_sn());
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
			}
		}
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
		JSONObject result = new JSONObject();
		JSONObject failMsg = new JSONObject();
		Optional<Product> productOptional = productRepository.findById(productId);
		if(productOptional.isPresent()) {
			Product product = productOptional.get();
			JSONObject objectReturn = ExcelUtils.importExcel(url);
			JSONArray array = objectReturn.getJSONArray("result");
			int count = 0;
			for(int i=0;i<array.size();i++) {
				JSONObject object  = array.getJSONObject(i);
				Set<String> keys = object.keySet();
				Iterator<String> iterator = keys.iterator();				
				while(iterator.hasNext()) {
					String key = (String) iterator.next();
					JSONObject value = (JSONObject) object.get(key);
					Set<String> names = value.keySet();
					Iterator<String> it = names.iterator();
					while(it.hasNext()) {
						String name = (String) it.next();
						String devicesn = (String) value.get(name);
						logger.debug(name+":"+devicesn);
						Optional<Device> deviceOptional = deviceRepository.findByProductIdAndDeviceSn(productId, devicesn);
						logger.debug("检查添加设备的鉴权信息是否重复");
						if(deviceOptional.isPresent()) {
							count++;
							failMsg.put(key, "鉴权信息已存在");
							logger.debug("编号为："+key+"的设备数据鉴权信息重复");
							continue;
						}
						if(!StringUtils.isNumeric(devicesn)) {
							count++;
							failMsg.put(key, "鉴权信息包含数字以外字符");
							logger.debug("编号为："+key+"的设备数据鉴权信息包含数字以外字符");
							continue;
						}
						logger.debug("编号为："+key+"的设备数据鉴权信息有效");
						Device device = new Device();
						device.setName(name);
						device.setDevice_sn(devicesn);
						device.setCreateTime(new Date());
						device.setProductId(productId);
						device.setProtocolId(product.getProtocolId());
						deviceRepository.save(device);
						
					}
					failMsg.put("sum", count);
				}
				
			}
			result.put("sum", array.size());
			result.put("success", array.size()-count);
			result.put("fail", failMsg);
			
			OperationLogs logs = new OperationLogs();
			logs.setUserId(productOptional.get().getUserId());
			logs.setOperationTypeId(6);
			logs.setMsg("批量添加设备，添加结果："+result.toString());
			logs.setCreateTime(new Date());
			operationLogsRepository.save(logs);
			return RESCODE.SUCCESS.getJSONRES(result);
		}
		return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
	}
	
	/**
	 * 获取设备数量趋势
	 * @param productId
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public JSONObject getIncrement(Integer productId,Date start,Date end) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		List<Device> devices = deviceRepository.findAll(new Specification<Device>() {			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				 List<Predicate> predicateList = new ArrayList<>();

				 if (productId != null && productId >= 0) {
                    predicateList.add(
                            criteriaBuilder.equal(
                                    root.get("productId").as(Integer.class),
                                    productId));
	             }
				 if(start!=null) {
//					 大于等于创建时间
					 predicateList.add(
							 criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(Date.class),
									 start)
							 );
				 }
				 if(start!=null) {
//					 小于等于创建时间
					 predicateList.add(
							 criteriaBuilder.lessThanOrEqualTo(root.get("createTime").as(Date.class),
									 end)
							 );
				 }
				 Predicate[] predicates = new Predicate[predicateList.size()];
	             return criteriaBuilder.and(predicateList.toArray(predicates));
			}
		});
		JSONObject jsonObject = new JSONObject();	
//		累计新增设备
		jsonObject.put("CumulativeResult", (devices!=null&&devices.size()>0)?devices.size():0);
//		在线设备
		List<Device> deviceTotal = deviceRepository.findByProductId(productId);
		int onlineResult = 0;
		for(Device device : deviceTotal) {
			if(device.getStatus()!=null&&device.getStatus()==1) {
				onlineResult++;
			}
		}
		jsonObject.put("OnlineResult", onlineResult);
//		获取今日新增数据
		Date today = new Date();
		Date sToday = new Date();
		try {
			sToday = sdf.parse(sdf.format(today));
			sToday.setHours(0);
			sToday.setMinutes(0);
			sToday.setSeconds(0);
			int s=0;
			for(Device device:devices) {
				if(device.getCreateTime().getTime()>=sToday.getTime()&&device.getCreateTime().getTime()<today.getTime()) {
					s++;
				}
			}
			jsonObject.put("increment_today", s);
			
//			获取昨日新增数据			
			Date syesterday = sdf.parse(sdf.format(sToday));
			s=0;
			for(Device device:devices) {
				if(device.getCreateTime().getTime()>=syesterday.getTime()&&device.getCreateTime().getTime()<sToday.getTime()) {
					s++;
				}
			}
			jsonObject.put("increment_yesterday", s);			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
//		趋势分析图表数据
		JSONArray array = new JSONArray();
		int len = (int) (end.getTime()-start.getTime())/1000/60/60/24;
		logger.debug("共需循环"+len+"次");
		Date sdate;
		Date edate;
		for(int i=0;i<len;i++) {
			logger.debug("第"+(i+1)+"次");
			try {
				sdate = sdf.parse(sdf.format(start));
				sdate.setDate(sdate.getDate()+i);
				edate = sdf.parse(sdf.format(sdate));
				edate.setDate(edate.getDate()+1);
				logger.debug("开始："+sdf.format(sdate));
				logger.debug("结束："+sdf.format(edate));
				JSONObject object = new JSONObject();
				int s=0;
				for(Device device:devices) {
					if(device.getCreateTime().getTime()>=sdate.getTime()&&device.getCreateTime().getTime()<edate.getTime()) {
						s++;
					}
				}
				object.put(sdf1.format(sdate), s);
				array.add(object);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		logger.debug("最后一次");
		try {
			sdate = sdf.parse(sdf.format(start));
			sdate.setDate(sdate.getDate()+len);
			edate = sdf.parse(sdf.format(end));
			logger.debug("开始："+sdf.format(sdate));
			logger.debug("结束："+sdf.format(edate));
			JSONObject object = new JSONObject();
			int s=0;
			for(Device device:devices) {
				if(device.getCreateTime().getTime()>=sdate.getTime()&&device.getCreateTime().getTime()<edate.getTime()) {
					s++;
				}
			}
			object.put(sdf1.format(sdate), s);
			array.add(object);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jsonObject.put("chart", array);		
		return RESCODE.SUCCESS.getJSONRES(jsonObject);
	}
}

