package com.hydata.intelligence.platform.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.transaction.Transactional;

import com.hydata.intelligence.platform.controller.DeviceController;
import com.hydata.intelligence.platform.dto.*;
import com.hydata.intelligence.platform.model.EmailHandlerModel;
import com.hydata.intelligence.platform.repositories.*;
import com.hydata.intelligence.platform.utils.MongoDBUtils;
import com.hydata.intelligence.platform.utils.SendMailUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mysql.fabric.xmlrpc.base.Array;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.model.TriggerModelModel;

import static java.lang.Short.valueOf;

/**
 * @author pyt
 * @createTime 2018年11月5日下午3:29:25
 */
@Transactional
@Service
@EnableAsync
@Configuration
public class TriggerService {
	@Autowired
	private TriggerRepository triggerRepository;
	
	@Autowired
	private TriggerTypeRepository triggerTypeRepository;
	
	@Autowired
	private DeviceTriggerRepository deviceTriggerRepository;

	@Autowired
	private DatastreamModelRepository datastreamModelRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private DeviceDatastreamRepository	 deviceDatastreamRepository;
	
	@Autowired
	private DdTriggerRepository ddTriggerRepository;

	private static Logger logger = LogManager.getLogger(TriggerService.class);
	
	private static MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();
	private static MongoClient meiyaClient = mongoDBUtil.getMongoConnect("127.0.0.1",27017);
	private static MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");


	/**
	 * 添加触发器
	 * device_sn由接口api/device/get_devicelist获取
	 * datastreamId由接口api/device/get_devicedslist获取
	 * @param trigger
	 * @return
	 */
	public JSONObject addTrigger(TriggerModel trigger) {
		Optional<Product> productoptional = productRepository.findById(trigger.getProductId());
		Optional<TriggerType> triggerTypeOptional = triggerTypeRepository.findById(trigger.getTriggerTypeId());
		if(productoptional.isPresent()&&triggerTypeOptional.isPresent()) {		
			trigger.setCreateTime(new Date());
			TriggerModel triggerReturn = triggerRepository.save(trigger);
			logger.debug("触发器保存结束");
			if(triggerReturn!=null) {
				DeviceTrigger deviceTrigger = new DeviceTrigger();
				deviceTrigger.setTriggerId(triggerReturn.getId());
				deviceTrigger.setDevice_sn(trigger.getDevice_sn());
				DeviceTrigger deviceTriggerReturn = deviceTriggerRepository.save(deviceTrigger);
				
				DdTrigger ddTrigger = new DdTrigger();
				ddTrigger.setDdId(trigger.getDatastreamId());
				Optional<DeviceDatastream> dmNameOptional = deviceDatastreamRepository.findById(trigger.getDatastreamId());
				ddTrigger.setDmName(dmNameOptional.isPresent()?dmNameOptional.get().getDm_name():"");
				ddTrigger.setMode(trigger.getTriggerMode());
				ddTrigger.setModeMsg(trigger.getModeValue());
				ddTrigger.setProductId(trigger.getProductId());
				ddTrigger.setTriggerId(triggerReturn.getId());
				DdTrigger ddTriggerReturn = ddTriggerRepository.save(ddTrigger);
				
				if(deviceTriggerReturn!=null && ddTriggerReturn != null) {
					return RESCODE.SUCCESS.getJSONRES();
				}
				return RESCODE.TRIGGER_DEVICE_ADD_FAILURE.getJSONRES();
			}
			return RESCODE.TRIGGER_ADD_FAILURE.getJSONRES();
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 根据触发器id删除触发器，
	 * 包括删除触发器设备流关联
	 * @param id
	 * @return
	 */
	public JSONObject delTrigger(Integer id) {
		logger.debug("进入删除触发器："+id);
		Optional<TriggerModel> triggerOptional = triggerRepository.findById(id);
		if(triggerOptional.isPresent()) {
			triggerRepository.deleteById(id);
			logger.debug("成功删除触发器");
			int result1 = deviceTriggerRepository.deleteByTriggerId(id);
			logger.debug("共删除触发器设备流关联数据："+result1+"条");
			int result2 = ddTriggerRepository.deleteByTriggerId(id);
			logger.debug("共删除触发器设备流关联数据："+result2+"条");
			return RESCODE.SUCCESS.getJSONRES();
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}
	
	/**
	 * 修改触发器
	 * @param triggerModel
	 * @return
	 */
	public JSONObject modifyTrigger(TriggerModel triggerModel) {
		Optional<Product> productoptional = productRepository.findById(triggerModel.getProductId());
		Optional<TriggerModel> triggerOptional = triggerRepository.findById(triggerModel.getId());
		if(productoptional.isPresent()&&triggerOptional.isPresent()) {
			TriggerModel triggerModelOld = triggerOptional.get();
			triggerModel.setCreateTime(triggerModelOld.getCreateTime());
			triggerModel.setModifyTime(new Date());
			triggerRepository.save(triggerModel);
			//修改关联设备
			//不删除，仅添加?
			List<DeviceTrigger> deviceTriggers = deviceTriggerRepository.findByDeviceSn(triggerModel.getDevice_sn());
			boolean flag = true;
			for(DeviceTrigger deviceTrigger : deviceTriggers) {
				if(deviceTrigger.getTriggerId()==triggerModel.getId()) {
					flag = false;
					break;
				}
			}
			if(flag) {
				DeviceTrigger deviceTrigger = new DeviceTrigger();
				deviceTrigger.setTriggerId(triggerModel.getId());
				deviceTrigger.setDevice_sn(triggerModel.getDevice_sn());
				DeviceTrigger deviceTriggerReturn = deviceTriggerRepository.save(deviceTrigger);
			}
			//修改关联数据流
			//不删除，仅添加?
			List<DdTrigger> ddTriggers = ddTriggerRepository.findByDdId(triggerModel.getDatastreamId());
			if(ddTriggers==null||ddTriggers.size()<1) {
				DdTrigger ddTrigger = new DdTrigger();
				ddTrigger.setDdId(triggerModel.getDatastreamId());
				Optional<DeviceDatastream> dmNameOptional = deviceDatastreamRepository.findById(triggerModel.getDatastreamId());
				ddTrigger.setDmName(dmNameOptional.isPresent()?dmNameOptional.get().getDm_name():"");
				ddTrigger.setMode(triggerModel.getTriggerMode());
				ddTrigger.setModeMsg(triggerModel.getModeValue());
				ddTrigger.setProductId(triggerModel.getProductId());
				ddTrigger.setTriggerId(triggerModel.getId());
				DdTrigger ddTriggerReturn = ddTriggerRepository.save(ddTrigger);
			}
			
			return RESCODE.SUCCESS.getJSONRES();
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}
	
	/**
	 * 查询产品下全部触发器
	 * @param productId
	 * @return
	 */
	public JSONObject getTriggersByProductId(Integer productId) {
		List<TriggerModel> triggerList = triggerRepository.findByProductId(productId);
		JSONArray triggerModelList = new JSONArray();
		for(TriggerModel trigger :triggerList) {
			TriggerModelModel model = new TriggerModelModel();
			Integer triggerId = trigger.getId();
			List<DeviceTrigger> deviceTriggerList = deviceTriggerRepository.findByTriggerId(triggerId);
			model.setTrigger(trigger);
			model.setDeviceTriggerList(deviceTriggerList);
			triggerModelList.add(model);
		}
		return RESCODE.SUCCESS.getJSONRES(triggerModelList);
	}
	/**
	 * 获取设备关联触发器
	 * @param device_sn
	 * @return
	 */
	public JSONObject getAssociatedTriggers(String device_sn,Integer page,Integer number) {
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"id");
		Page<DeviceTrigger> result = deviceTriggerRepository.queryByDeviceSn(device_sn, pageable);
		JSONArray triggers = new JSONArray();
		for(DeviceTrigger deviceTrigger:result.getContent()) {
			Integer trigger_id = deviceTrigger.getTriggerId();
		    Optional<TriggerModel> optional = triggerRepository.findById(trigger_id);
		    if(optional.isPresent()) {
		    	triggers.add(optional.get());
		    }
		}		
		return RESCODE.SUCCESS.getJSONRES(triggers,result.getTotalPages(),result.getTotalElements());
	}	
	/**
	 * 获取触发器下已关联设备
	 * @param triggerId
	 * @return
	 */
	public JSONObject getAssociatedDevices(Integer trigger_id,Integer page,Integer number) {
		Page<DeviceTrigger>  result = getAssociatedDeviceSn(trigger_id, page, number);
		List<DeviceTrigger> deviceTriggers = result.getContent();
		JSONArray devices = new JSONArray();
		for(DeviceTrigger deviceTrigger:deviceTriggers) {
			String device_sn = deviceTrigger.getDevice_sn();
			Map<String,Object> conditions = Maps.newHashMap();
	        conditions.put("device_sn",device_sn);
	        FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,null,null,null);
	        for (Document d : documents) {
	       	Device device = deviceService.returnDevice(d);
	       	devices.add(device);
	        }
		}
		return RESCODE.SUCCESS.getJSONRES(devices,result.getTotalPages(),result.getTotalElements());
	}
	
	public Page<DeviceTrigger> getAssociatedDeviceSn(Integer trigger_id,Integer page,Integer number) {
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"id");			
		Page<DeviceTrigger> result = deviceTriggerRepository.queryByTriggerId(trigger_id, pageable);
		return result;
	}
	
	/**
	 * 获取触发器下未关联设备
	 * @param trigger_id
	 * @param page
	 * @param number
	 * @return
	 */
	public JSONObject getNotAssociatedDevices(Integer product_id,Integer trigger_id,Integer page,Integer number) {
		List<DeviceTrigger> deviceTriggers = deviceTriggerRepository.findByTriggerId(trigger_id);
		List<String> deviceSns = new ArrayList<>();
		for(DeviceTrigger deviceTrigger:deviceTriggers) {
			deviceSns.add(deviceTrigger.getDevice_sn());
		}
		Map<String,Object> conditions = Maps.newHashMap();
        conditions.put("product_id",product_id);
        Map<String,Object> sortParams = Maps.newHashMap();
        sortParams.put("create_time",-1);
        FindIterable<Document> documents = mongoDBUtil.queryDocumentNin(collection,conditions,"device_sn",deviceSns,sortParams,(page-1)*number,number);
        JSONArray array = new JSONArray();
        for (Document d : documents) {
	       	Device device = deviceService.returnDevice(d);
	       	array.add(device);
        }
		
		return RESCODE.SUCCESS.getJSONRES(array);
	}
	/**
	 * 根据触发器名称查询
	 * @param productId
	 * @param name
	 * @param page
	 * @param number
	 * @return
	 */
	public JSONObject getByName(Integer product_id,String name,Integer page,Integer number) {
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"id");
		Page<TriggerModel> result = triggerRepository.queryByProductIdAndName(product_id, name, pageable);
		return RESCODE.SUCCESS.getJSONRES(result.getContent(),result.getTotalPages(),result.getTotalElements());
	}
	/**
	 * 触发器与设备关联（或设备与触发器关联）
	 * @param trigger_id
	 * @param device_sn
	 * @return
	 */
	public JSONObject triggerAssociatedDevice(Integer trigger_id,String device_sn) {
		Optional<TriggerModel> optional = triggerRepository.findById(trigger_id);
		if(optional.isPresent()) {
			DeviceTrigger deviceTrigger = new DeviceTrigger();
			deviceTrigger.setDevice_sn(device_sn);
			deviceTrigger.setTriggerId(trigger_id);
			deviceTriggerRepository.save(deviceTrigger);
			
			TriggerModel triggerModel = optional.get();
			Integer ds_id = triggerModel.getDatastreamId();
			Optional<DeviceDatastream> datastreamOptional = deviceDatastreamRepository.findById(ds_id);
			if(datastreamOptional.isPresent()) {
				String dm_name = datastreamOptional.get().getDm_name();
				Optional<DeviceDatastream>  datastream2Optional = deviceDatastreamRepository.findByDeviceSnAndDm_name(device_sn, dm_name);
				if(datastream2Optional.isPresent()) {
					DdTrigger ddTrigger = new DdTrigger();
					ddTrigger.setDdId(datastream2Optional.get().getId());
					ddTrigger.setDmName(dm_name);
					ddTrigger.setMode(triggerModel.getTriggerMode());
					ddTrigger.setModeMsg(triggerModel.getModeValue());
					ddTrigger.setProductId(triggerModel.getProductId());
					ddTrigger.setTriggerId(trigger_id);
					ddTriggerRepository.save(ddTrigger);
					return RESCODE.SUCCESS.getJSONRES();
				}
				return RESCODE.DEVICE_DATASTREAM_NOT_EXIST.getJSONRES();
			}
			return RESCODE.ID_NOT_EXIST.getJSONRES("datastream");
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES("trigger");
	}

	/**
	 * @author: Jasmine
	 * @createTime: 2018年11月20日上午11:31:11
	 * @description: <触发器触发模块>
	 */

	public void TriggerAlarm(String deviceSn, JSONArray data) throws InterruptedException{

			//根据DeviceSn+Dm_name找到对应的dd_id
			for(int i=0;i<data.size();i++) {
				try {
					JSONObject object = data.getJSONObject(i);
					String dm_name = object.getString("dm_name");
					int data_value = object.getIntValue("value");
					Optional<DeviceDatastream> ddId = deviceDatastreamRepository.findByDeviceSnAndDm_name(deviceSn, dm_name);
					DeviceDatastream deviceDatastream = ddId.get();
					int dd_id = deviceDatastream.getId();
					//根据dd_id找到triggerId
					List<DdTrigger> triggerList = ddTriggerRepository.findByDdId(dd_id);
					for(DdTrigger ddTrigger:triggerList) {
						int trigger_id = ddTrigger.getTriggerId();
						//根据triggerId找到对应的触发器信息
						//触发判断关系:">"或者"<"
						Optional<TriggerType> triggerinfo1 = triggerTypeRepository.findById(trigger_id);
						TriggerType triggerType = triggerinfo1.get();
						String symbol = triggerType.getSymbol();
						//触发阈值
						Optional<TriggerModel> triggerinfo2 = triggerRepository.findById(trigger_id);
						TriggerModel triggerModel = triggerinfo2.get();
						int criticalValue = valueOf(triggerModel.getCriticalValue());
						//触发方式：0：邮箱；1：url
						int triggerMode = triggerModel.getTriggerMode();
						//触发方式详细信息：url或邮箱地址
						String modeValue = triggerModel.getModeValue();

						//判断触发器是否触发
						if (((symbol.equals("<")) && (data_value < criticalValue)) || ((symbol.equals(">")) && (data_value > criticalValue))) {
							if (triggerMode == 0) {
								//加入发邮件的线程池
								//emailQueue.offer();
								EmailHandlerModel model = new EmailHandlerModel();
								Date time = new Date(System.currentTimeMillis());
								model.setCreateTime(time);
								model.setCriticalValue(criticalValue);
								model.setEmail(modeValue);
								model.setDmName(dm_name);
								model.setDeviceSn(deviceSn);
								model.setTriggerSymbol(symbol);
								MqttReceiveConfig.emailQueue.offer(model);
							} else if (triggerMode == 1) {
								//使用url发送警报
							}
						}
					}
				} catch (Exception e) {
					logger.debug(e.getClass().getName() + ": " + e.getMessage());
				}
			}
		}





}

