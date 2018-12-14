package com.hydata.intelligence.platform.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.transaction.Transactional;

import com.hydata.intelligence.platform.controller.DeviceController;
import com.hydata.intelligence.platform.dto.*;
import com.hydata.intelligence.platform.repositories.*;
import com.hydata.intelligence.platform.utils.SendMailUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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


	/**
	 * 添加触发器
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
				if(deviceTriggerReturn!=null) {
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
		logger.debug("进入删除触发器");
		Optional<TriggerModel> triggerOptional = triggerRepository.findById(id);
		if(triggerOptional.isPresent()) {
			triggerRepository.deleteById(id);
			int result = deviceTriggerRepository.deleteByTriggerId(id);
			logger.debug("共删除触发器设备流关联数据："+result+"条");
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
			triggerModel.setModifyTime(new Date());
			triggerRepository.save(triggerModel);
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
	 * 获取触发器下已关联设备
	 * @param triggerId
	 * @return
	 */
	public JSONObject getAssociatedDevice(Integer triggerId) {
		return null;
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
					List<DdTrigger> triggers = ddTriggerRepository.findByDdId(dd_id);
					for(DdTrigger ddTrigger:triggers) {
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

