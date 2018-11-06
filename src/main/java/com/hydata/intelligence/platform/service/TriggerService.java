package com.hydata.intelligence.platform.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.hydata.intelligence.platform.dto.TriggerModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.DeviceTrigger;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.dto.TriggerType;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.model.TriggerModelModel;
import com.hydata.intelligence.platform.repositories.DeviceTriggerRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.repositories.TriggerRepository;
import com.hydata.intelligence.platform.repositories.TriggerTypeRepository;

/**
 * @author pyt
 * @createTime 2018年11月5日下午3:29:25
 */
@Transactional
@Service
public class TriggerService {
	@Autowired
	private TriggerRepository triggerRepository;
	
	@Autowired
	private TriggerTypeRepository triggerTypeRepository;
	
	@Autowired
	private DeviceTriggerRepository deviceTriggerRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
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
				deviceTrigger.setDeviceId(trigger.getDatastreamId());
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
}

