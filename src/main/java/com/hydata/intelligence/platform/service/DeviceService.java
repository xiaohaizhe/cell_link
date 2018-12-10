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
import javax.transaction.Transactional;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hydata.intelligence.platform.dto.CmdLogs;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.DeviceDatastream;
import com.hydata.intelligence.platform.dto.OperationLogs;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.DataHistory;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.CmdLogsRepository;
import com.hydata.intelligence.platform.repositories.DeviceDatastreamRepository;
import com.hydata.intelligence.platform.repositories.OperationLogsRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.utils.ExcelUtils;
import com.hydata.intelligence.platform.utils.MongoDBUtils;
import com.hydata.intelligence.platform.utils.StringUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

/**
 * @author pyt
 * @createTime 2018年10月31日上午11:39:02
 */
@Transactional
@Service
@EnableAsync
public class DeviceService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private DeviceDatastreamRepository deviceDatastreamRepository;
	
	@Autowired
	private OperationLogsRepository operationLogsRepository;
	
	@Autowired
	private CmdLogsRepository cmdLogsRepository;
	
	private static Logger logger = LogManager.getLogger(DeviceService.class);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	
	private static MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();
	private static MongoClient meiyaClient = mongoDBUtil.getMongoConnect("127.0.0.1",27017);
	private static MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");
	private static MongoCollection<Document> data_history = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","data_history");



	/**产品下设备列表展示
	 * @param product_id
	 * @param page
	 * @param number
	 * @param sort
	 * @return
	 * 弃用
	 */
	/*@SuppressWarnings("deprecation")
	public Page<Device> showAllByProductId(Integer product_id,Integer page,Integer number,int sort){
		Pageable pageable;
		if(sort==0) {
			//逆序
			pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"id");
		}else {
			//顺序
			pageable = new PageRequest(page-1, number, Sort.Direction.ASC,"id");
		}
		
		return null;
	}*/
	
	public JSONObject showAllByProductIdM(Integer product_id,Integer page,Integer number,int sort) {
		 Map<String,Object> conditions = Maps.newHashMap();
         conditions.put("product_id",product_id);
         Map<String,Object> sortParams = Maps.newHashMap();
         sortParams.put("create_time",sort);
         FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,sortParams,(page-1)*number,number);
         JSONArray array = new JSONArray();
         for (Document d : documents) {
        	Device device = returnDevice(d);
        	array.add(device);
         }		
		return RESCODE.SUCCESS.getJSONRES(array);
	}
	
	public Device returnDevice(Document d) {
		Device device = new Device();
		device.setDevice_sn(d.getString("device_sn"));
		device.setName(d.getString("name"));
		device.setProductId(d.getInteger("product_id"));
		device.setCreateTime(d.getDate("create_time"));
		device.setStatus(d.getInteger("status"));
		return device;		
	}
	/**
	 * 添加设备
	 * @param device
	 * @return
	 * 弃用
	 */
	/*public JSONObject addDevice(Device device){
		Optional<Product> productOptional = productRepository.findById(device.getProductId());
		logger.debug("检查添加设备的产品id是否存在");
		if(productOptional.isPresent()) {
			//MYsql存储
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
			Device deviceReturn= deviceRepository.save(device);
	     					
			return RESCODE.SUCCESS.getJSONRES(deviceReturn);
		}
		logger.debug("产品id不存在");
		return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
	}*/
	/**
	 * 向MongoDB中存储设备表
	 * @param device
	 * @return
	 */
	public JSONObject addDeviceM(Device device) {
		Optional<Product> productOptional = productRepository.findById(device.getProductId());
		logger.debug("检查添加设备的产品id是否存在");
		if(productOptional.isPresent()) {
			boolean isNumber = StringUtils.isNumeric(device.getDevice_sn());
			boolean flag = checkDevicesn(device.getDevice_sn());
			if(flag && isNumber) {
	            Map<String,Object> insert = new HashMap<>();
	               //1、测试增加
	            insert.put("name",device.getName());
	            insert.put("device_sn", device.getDevice_sn());
	            insert.put("product_id",device.getProductId());
	            insert.put("create_time",new Date());
	            insert.put("status", null);
	            mongoDBUtil.insertDoucument(collection,insert);
	            OperationLogs logs = new OperationLogs();
				logs.setUserId(productOptional.get().getUserId());
				logs.setOperationTypeId(6);
				logs.setMsg("添加设备:"+device.getDevice_sn());
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
				/**
				 * haizhe 
				 * 若为mqtt通讯方式，调用Jasmine方法，添加topic 
				 */
	            return RESCODE.SUCCESS.getJSONRES();
			}else {
				return RESCODE.AUTH_INFO_EXIST.getJSONRES();
			}
		}
		logger.debug("产品id不存在");
		return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 获取使用某协议的全部device_sn
	 * @param protocol_id
	 * @return
	 */
	public JSONObject getDeviceByProtocol(Integer protocol_id) {
		List<Product> products = productRepository.findByProtocolId(protocol_id);
		JSONArray array = new JSONArray();
		for(Product product : products) {
			Map<String,Object> conditions = Maps.newHashMap();
            conditions.put("product_id",product.getId());
			FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,null,null,null);
			for (Document d : documents) {
				String device_sn = d.getString("device_sn");
				array.add(device_sn);	       
		    }		
		}		
		return RESCODE.SUCCESS.getJSONRES(array);
	}
	
	/**
	 * 检查设备鉴权信息是否重复
	 * @param device_sn
	 * @return
	 */
	public Boolean checkDevicesn(String device_sn) {
		logger.debug("device_sn:" + device_sn);
		try {          
            Map<String,Object> conditions = Maps.newHashMap();
            conditions.put("device_sn",device_sn);
            FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,null,null,1);            	
            System.out.println("documents");
            int num = 0;
            for (Document d : documents) {
                System.out.println("第" + (++num) + "条数据： " + d.toString());
            }
			return num == 0;
        } catch (Exception e) {
            logger.debug(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }		
	}
	/**
	 * 在产品中根据设备id或设备名称查询
	 * @param product_id
	 * @param page
	 * @param number
	 * @param device_snOrName
	 * @return
	 * 弃用
	 */
	/*public Page<Device> queryByDeviceSnOrName(Integer product_id,String deviceSnOrName,Integer page,Integer number){
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"id");
		Page<Device> result = deviceRepository.findAll(new Specification<Device>() {
			
			*//**
			 * 
			 *//*
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
	}*/
	
	public JSONObject queryByDeviceSnOrName_m(Integer product_id,String deviceSnOrName,Integer page,Integer number) {
		BasicDBObject query = new BasicDBObject();
		query.put("product_id",product_id);
		if(StringUtils.isNumeric(deviceSnOrName)) {
			query.put("device_sn",deviceSnOrName);
		}else {
			Pattern pattern = Pattern.compile("^.*" + deviceSnOrName +".*$", Pattern.CASE_INSENSITIVE);
			query.put("name",pattern);//key为表字段名
		}		
		FindIterable<Document> documents = collection.find(query).limit(number).skip((page-1)*number).sort(new BasicDBObject("sort",1));
		JSONArray array = new JSONArray();
		for (Document d : documents) {
			Device device = returnDevice(d);
			array.add(device);	       
	    }
		return RESCODE.SUCCESS.getJSONRES(array);
	}
	
	/**
	 * 修改设备信息（设备名称、设备鉴权码、icon）
	 * @param device
	 * @return
	 * 弃用
	 */
	/*public JSONObject modifyDevice(Device device){
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
	}*/
	/**
	 * 修改设备
	 * @param device
	 * @return
	 */
	public JSONObject modifyDevice_m(Device device) {
		BasicDBObject query = new BasicDBObject();
		query.put("device_sn",device.getDevice_sn());
		BasicDBObject update = new BasicDBObject();
		update.put("name",device.getName());	
		update.put("status", device.getStatus());
		update.put("modify_time", new Date());
		
		Document conditonDocument = new Document();
		query.keySet().stream().filter(p -> null != p).forEach(o -> {
            conditonDocument.append(o,query.get(o));
        });
        
        Document updateDocument = new Document();
        update.keySet().stream().filter(p -> null != p).forEach(o -> {
            updateDocument.append(o,update.get(o));
        });
        
		//UpdateResult result = collection.updateOne(query, update);
		//System.out.println(result);
		collection.findOneAndUpdate(conditonDocument, new Document("$set",updateDocument));
		return RESCODE.SUCCESS.getJSONRES();
	}
	
	/**
	 * 删除设备
	 * 1.数据流触发器（未完成）
	 * 2.设备（完成）
	 * @param device_sn
	 * @return
	 */
	public JSONObject deleteDevice(String device_sn){
		/*Optional<Device> deviceOptional = deviceRepository.findById(id);
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
		}*/
		
		long count =0;
		Map<String,Object> conditionParams = Maps.newHashMap();
        conditionParams.put("device_sn",device_sn);
        count = mongoDBUtil.deleteDocument(collection,true,conditionParams);
        System.out.println(count);
        /**
         * haizhe
         * 若为mqtt通讯方式，调用Jasmine方法，删除其topic
         */
        if(count==0 ) {
        	return RESCODE.AUTH_INFO_NOT_EXIST.getJSONRES();
        }
		return RESCODE.SUCCESS.getJSONRES();
	}
	
	/**
	 * 获取产品下设备列表
	 * @param productId
	 * @return
	 */
	public JSONObject getByProductId(Integer productId) {
		 Map<String,Object> conditions = Maps.newHashMap();
         conditions.put("product_id",productId);       
         FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,null,null,null);
         JSONArray array = new JSONArray();
         for (Document d : documents) {
        	Device device = returnDevice(d);
        	array.add(device);
         }		
		return RESCODE.SUCCESS.getJSONRES(array);
	}
	/**
	 * 获取设备下数据流列表
	 * @param deviceSn
	 * @return
	 */
	public JSONObject getDeviceDsByDeviceSn(String deviceSn) {
		logger.debug("开始获取设备"+deviceSn+"下数据流列表");
		Map<String,Object> conditions = Maps.newHashMap();
        conditions.put("device_sn",deviceSn);       
        FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,null,null,null);
        JSONArray array = new JSONArray();
        for (Document d : documents) {
        	Device device = returnDevice(d);
        	array.add(device);
        }	
		if(array.size()==1) {
			List<DeviceDatastream> ddList = deviceDatastreamRepository.findByDeviceSn(deviceSn);
			JSONArray a = new JSONArray();
			for(DeviceDatastream datastream : ddList) {
				a.add(datastream);
			}
			return RESCODE.SUCCESS.getJSONRES(a);
		}else {
			logger.debug("设备"+deviceSn+"不存在");
			return RESCODE.ID_NOT_EXIST.getJSONRES();
		}
	}
	
	/**
	 * 解析设备上传的数据流
	 * 1.存储数据流
	 * 2.存储数据
	 * 3.触发
	 * @param jsonObject
	 * 弃
	 */
	public void resolveDeviceData(JSONObject jsonObject) {
		
		logger.debug(jsonObject);
		//jsonObject.
		
	}
	
	/**
	 * 检查设备数据流，存储数据流
	 * @param deviceSn， dsName
	 * @param dsName
	 * 弃
	 */
	public void checkDsExistAndSave(String deviceSn,String dsName) {
		logger.debug("检查设备："+deviceSn+"下数据流："+dsName+"是否存在");
		Optional<DeviceDatastream> optional = deviceDatastreamRepository.findByDeviceSnAndDm_name(deviceSn, dsName);
		if(optional.isPresent() == false) {
			logger.debug("设备："+deviceSn+"下数据流："+dsName+"不存在，开始添加");
			DeviceDatastream datastream = new DeviceDatastream();
			datastream.setDevice_sn(deviceSn);
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
					String key = iterator.next();
					JSONObject value = (JSONObject) object.get(key);
					Set<String> names = value.keySet();
					Iterator<String> it = names.iterator();
					while(it.hasNext()) {
						String name = it.next();
						String devicesn = (String) value.get(name);
						logger.debug(name+":"+devicesn);
						//Optional<Device> deviceOptional = deviceRepository.findByProductIdAndDeviceSn(productId, devicesn);
						boolean isExist = checkDevicesn(devicesn);
						logger.debug("检查添加设备的鉴权信息是否重复");
						if(isExist == false) {
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
						/*Device device = new Device();
						device.setName(name);
						device.setDevice_sn(devicesn);
						device.setCreateTime(new Date());
						device.setProductId(productId);
						device.setProtocolId(product.getProtocolId());
						deviceRepository.save(device);*/
						Map<String,Object> insert = new HashMap<>();
			               //1、测试增加
			            insert.put("name",name);
			            insert.put("device_sn", devicesn);
			            insert.put("product_id",productId);
			            insert.put("create_time",new Date());
			            insert.put("protocol_id", product.getProtocolId());
			            insert.put("status", null);
			            mongoDBUtil.insertDoucument(collection,insert);						
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
		/*
//		累计新增设备
		jsonObject.put("CumulativeResult", (devices!=null&&devices.size()>0)?devices.size():0);
//		在线设备
		Map<String,Object> conditions = Maps.newHashMap();
        conditions.put("product_id",productId);
        conditions.put("status", 1);
        Map<String,Object> sortParams = Maps.newHashMap();
        sortParams.put("create_time",1);
        FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,sortParams,null,null);
        List<Device> deviceTotal = new ArrayList<>();
        for (Document d : documents1) {
			Device device = returnDevice(d);
			devices.add(device);			
	    }
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
		}*/	
		JSONObject jsonObject = new JSONObject();	
		BasicDBObject query = new BasicDBObject(); 
		query.put("product_id", productId);
		query.put("create_time",BasicDBObjectBuilder.start("$gte", start).add("$lte", end).get());//key为表字段名
		FindIterable<Document> documents1 = collection.find(query);
		List<Device> devices = new ArrayList<>();
		for (Document d : documents1) {
			Device device = returnDevice(d);
			devices.add(device);			
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
	
	public JSONObject finddevice(String name) {
		Pattern pattern = Pattern.compile("^.*" + name +".*$", Pattern.CASE_INSENSITIVE);
		BasicDBObject query = new BasicDBObject(); 
		query.put("name",pattern);//key为表字段名
		FindIterable<Document> documents = collection.find(query);
		for (Document d : documents) {
	        System.out.println(d.toJson());
	    }
		return null;
	}
	
	public void findDevice(String name) {
		Pattern pattern = Pattern.compile("^.*" + name +".*$", Pattern.CASE_INSENSITIVE);
		BasicDBObject query = new BasicDBObject(); 
		query.put("name",pattern);//key为表字段名
		FindIterable<Document> documents = collection.find(query).limit(5).skip(0*10).sort(new BasicDBObject("sort",1));
		for (Document d : documents) {
	        System.out.println(d.toJson());
	    }
	}
	/**
	 * 获取设备下发命令日志
	 * @param device_sn
	 * @return
	 */
	public JSONObject getCmdLogs(String device_sn) {
		List<CmdLogs> cmdLogs = cmdLogsRepository.findByDeviceSn(device_sn);
		return RESCODE.SUCCESS.getJSONRES(cmdLogs);
	}
	
	public JSONObject getDeviceDsData(Integer dd_id,Date start,Date end) {
		MongoCollection<Document> col = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","data_history");
		BasicDBObject query = new BasicDBObject(); 
		query.put("dd_id", dd_id);
		query.put("date",BasicDBObjectBuilder.start("$gte", start).add("$lte", end).get());//key为表字段名
		FindIterable<Document> documents1 = col.find(query);
		List<DataHistory> datas = new ArrayList<>();
		for (Document d : documents1) {
			DataHistory dataHistory = returnData(d);
			datas.add(dataHistory);			
	    }
		return RESCODE.SUCCESS.getJSONRES(datas);
	}
	
	public DataHistory returnData(Document d) {
		DataHistory dataHistory = new DataHistory();
		dataHistory.setDd_id(d.getInteger("dd_id"));
		dataHistory.setName(d.getString("name"));
		dataHistory.setValue(d.getDouble("value"));
		dataHistory.setDate(d.getDate("date"));
		return dataHistory;
	}

	
	/**
	 * 存储数据流：设备id+实时数据流信息至Mongodb
	 * 等个解析
	 * @param deviceSn, LiveDataStream
	 * 弃
	 */
	public static void saveDataStream(String deviceSn, String LivaDataStream){
		MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");
		Map<String,Object> insert = new HashMap<>();
		insert.put("name","saveDataStream");
		//insert.put("device_sn", deviceSn); 用device_id，找dd_id, 数据流名称匹配
		insert.put("product_id",1);
		insert.put("create_time",new Date());
		//insert.put("data_history",LiveDataStream);
		mongoDBUtil.insertDoucument(collection,insert);
	}
	
	/**
	 * 对获取数据进行处理
	 */
	
	public void  dealWithData(String deviceSn,JSONArray data) {
		logger.debug("进入dealWithData处理数据");
		List<DeviceDatastream> deviceDsList = deviceDatastreamRepository.findByDeviceSn(deviceSn);	
		//1.获取设备编号：deviceSn下全部数据流名称deviceDatastreamName
		JSONArray names = new JSONArray();
		for(DeviceDatastream dd:deviceDsList) {			
			String dm_name = dd.getDm_name();
			names.add(dm_name);			
		}
		//2.上传数据中未存入的数据流存入
		for(int i = 0; i<data.size(); i++) {
			JSONObject object = (JSONObject) data.get(i);
			Set<String> set = object.keySet();
			Iterator<String> iterator = set.iterator();
			while(iterator.hasNext()) {
				String name = iterator.next();
				if(names.contains(name)==false) {
					DeviceDatastream datastream = new DeviceDatastream();
					datastream.setDevice_sn(deviceSn);
					datastream.setDm_name(name);
					deviceDatastreamRepository.save(datastream);
					names.add(name);
				}
			}
		}
		List<DeviceDatastream> deviceDsList1 = deviceDatastreamRepository.findByDeviceSn(deviceSn);
		//3.将数据存入历史数据
		for(DeviceDatastream datastream : deviceDsList1) {
			Integer dd_id = datastream.getId();
			String name = datastream.getDm_name();
			double value;
			for(int i = 0; i<data.size(); i++) {
				JSONObject object = (JSONObject) data.get(i);
				Set<String> set = object.keySet();
				if(set.contains(name)) {
					value = object.getDoubleValue(name);
					Map<String,Object> insert = new HashMap<>();
		               //1、测试增加
		            insert.put("name",name);
		            insert.put("dd_id",dd_id);
		            insert.put("value",value);
		            insert.put("time",new Date());
		            mongoDBUtil.insertDoucument(collection,insert);
					break;
				}				
			}
		}		
	}

	

}

