package com.hydata.intelligence.platform.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import com.hydata.intelligence.platform.dto.*;
import com.hydata.intelligence.platform.model.EmailHandlerModel;
import com.hydata.intelligence.platform.repositories.*;
import com.hydata.intelligence.platform.utils.MqttClientUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@Configuration
public class TriggerService {
	@Autowired
	private TriggerRepository triggerRepository;
	
	@Autowired
	private TriggerTypeRepository triggerTypeRepository;
	
	@Autowired
	private DeviceTriggerRepository deviceTriggerRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private DeviceDatastreamRepository	 deviceDatastreamRepository;
	
	@Autowired
	private DdTriggerRepository ddTriggerRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DeviceRepository deviceRepository;

	private static Logger logger = LogManager.getLogger(TriggerService.class);

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
	 * 添加触发器
	 * 外部接口
	 * device_sn由接口api/device/get_devicelist获取
	 * datastreamId由接口api/device/get_devicedslist获取
	 * @param trigger
	 * @return
	 */
	public JSONObject addTrigger(TriggerModel trigger,String api_key) {
		Optional<Product> productoptional = productRepository.findById(trigger.getProductId());
		Optional<TriggerType> triggerTypeOptional = triggerTypeRepository.findById(trigger.getTriggerTypeId());
		if(productoptional.isPresent()&&triggerTypeOptional.isPresent()) {
			Optional<User> optional = userRepository.findById(productoptional.get().getUserId());
			if(optional.isPresent()&&optional.get().getDefaultKey().equals(api_key)) {
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
						return RESCODE.SUCCESS.getJSONRES(deviceTriggerReturn);
					}
					return RESCODE.TRIGGER_DEVICE_ADD_FAILURE.getJSONRES();
				}
				return RESCODE.TRIGGER_ADD_FAILURE.getJSONRES();
			}else {
				return RESCODE.API_KEY_ERROR.getJSONRES();
			}			
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 根据触发器id删除触发器，
	 * 包括删除触发器设备流关联
	 * @param id
	 * @return
	 */
	public JSONObject delTrigger(long id) {
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
		logger.debug("修改触发器");
		logger.debug(triggerModel.toString());
		Optional<Product> productoptional = productRepository.findById(triggerModel.getProductId());
		Optional<TriggerModel> triggerOptional = triggerRepository.findById(triggerModel.getId());
		if(productoptional.isPresent()&&triggerOptional.isPresent()) {
			logger.debug("产品id与触发器id均存在");
			TriggerModel triggerModelOld = triggerOptional.get();
			String device_sn = triggerModelOld.getDevice_sn();
			Long datastream_id = triggerModelOld.getDatastreamId();
						
			int a=0;
			if(device_sn!=null&&device_sn.equals(triggerModel.getDevice_sn())&&datastream_id==triggerModel.getDatastreamId()) {
				a = 3;
			}else if(device_sn!=null&&device_sn.equals(triggerModel.getDevice_sn())&&datastream_id!=triggerModel.getDatastreamId()) {
				a = 2;
			}else if(device_sn!=null&&device_sn.equals(triggerModel.getDevice_sn())==false&&datastream_id==triggerModel.getDatastreamId()) {
				//该情况不存在
				//关联设备改变，则关联数据流必变
				a = 1;
			}
			
			//修改trigger_model
			triggerModel.setCreateTime(triggerModelOld.getCreateTime());
			triggerModel.setModifyTime(new Date());
			triggerRepository.save(triggerModel);
			
			switch (a) {
			case 3:
				//设备编号与设备数据流id不变
				break;
			case 2:
				//设备编号不变，设备数据流变化
				//关联设备的数据流变化
				//1.trigger_model数据变化
				//2.dd_trigger变化，查找与trigger关联设备，修改关联数据流
				Long trigger_id = triggerModel.getId();
				List<DdTrigger> ddTriggers = ddTriggerRepository.findByTriggerId(trigger_id);
				for(DdTrigger ddTrigger : ddTriggers) {
					ddTriggerRepository.deleteById(ddTrigger.getId());
				}
				
				Long dsId = triggerModel.getDatastreamId();
				Optional<DeviceDatastream> optional = deviceDatastreamRepository.findById(dsId);
				if(optional.isPresent()) {
					DeviceDatastream datastream = optional.get();
					String dm_name = datastream.getDm_name();
					
					List<DeviceTrigger> deviceTriggers = deviceTriggerRepository.findByTriggerId(trigger_id);
					for(DeviceTrigger deviceTrigger:deviceTriggers) {
						String device_sn1 = deviceTrigger.getDevice_sn();
						Optional<DeviceDatastream> optional1 = deviceDatastreamRepository.findByDeviceSnAndDm_name(device_sn1, dm_name);
						if(optional1.isPresent()) {
							Long dd_id = optional1.get().getId();
							DdTrigger ddTrigger = new DdTrigger();
							ddTrigger.setDdId(dd_id);
							ddTrigger.setDmName(dm_name);
							ddTrigger.setMode(triggerModel.getTriggerMode());
							ddTrigger.setModeMsg(triggerModel.getModeValue());
							ddTrigger.setProductId(triggerModel.getProductId());
							ddTrigger.setTriggerId(triggerModel.getId());
							ddTriggerRepository.save(ddTrigger);
						}
					}
				}	
				break;	
				
			case 1:
				
				break;
			default:
				//设备编号变化，设备数据流变化
				//关联设备与数据流均变化
				//1.trigger_model数据变化
				//2.device_trigger变化，查找与trigger关联设备，修改关联数据流
				Optional<DeviceTrigger> optional2 = deviceTriggerRepository.findByDeviceSnAndTriggerId(triggerModelOld.getDevice_sn(), triggerModelOld.getId());
				if(optional2.isPresent()) {
					deviceTriggerRepository.deleteById(optional2.get().getId());
				}
				List<DdTrigger> ddTriggers2 = ddTriggerRepository.findByTriggerId(triggerModelOld.getId());
				for(DdTrigger ddTrigger : ddTriggers2) {
					ddTriggerRepository.deleteById(ddTrigger.getId());
				}
				Optional<DeviceDatastream> optional3 = deviceDatastreamRepository.findById(triggerModel.getDatastreamId());
				if(optional3.isPresent()) {
					String dm_name = optional3.get().getDm_name();
					List<DeviceTrigger> deviceTriggers = deviceTriggerRepository.findByTriggerId(triggerModel.getId());
					for(DeviceTrigger deviceTrigger:deviceTriggers) {
						String device_sn1 = deviceTrigger.getDevice_sn();
						Optional<DeviceDatastream> optional1 = deviceDatastreamRepository.findByDeviceSnAndDm_name(device_sn1, dm_name);
						if(optional1.isPresent()) {
							Long dd_id = optional1.get().getId();
							DdTrigger ddTrigger = new DdTrigger();
							ddTrigger.setDdId(dd_id);
							ddTrigger.setDmName(dm_name);
							ddTrigger.setMode(triggerModel.getTriggerMode());
							ddTrigger.setModeMsg(triggerModel.getModeValue());
							ddTrigger.setProductId(triggerModel.getProductId());
							ddTrigger.setTriggerId(triggerModel.getId());
							ddTriggerRepository.save(ddTrigger);
						}
					}
				}
				break;
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
			Long triggerId = trigger.getId();
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
			Long trigger_id = deviceTrigger.getTriggerId();
		    Optional<TriggerModel> optional = triggerRepository.findById(trigger_id);
		    if(optional.isPresent()) {
		    	triggers.add(optional.get());
		    }
		}		
		return RESCODE.SUCCESS.getJSONRES(triggers,result.getTotalPages(),result.getTotalElements());
	}	
	/**
	 * 获取触发器下已关联设备
	 * @param trigger_id
	 * @return
	 */
	public JSONObject getAssociatedDevices(Integer trigger_id,Integer page,Integer number) {
		/*MongoClient meiyaClient = mongoDBUtil.getMongoConnect(mongoDB.getHost(),mongoDB.getPort());
		MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");*/
		Page<DeviceTrigger>  result = getAssociatedDeviceSn(trigger_id, page, number);
		List<DeviceTrigger> deviceTriggers = result.getContent();
		JSONArray devices = new JSONArray();
		for(DeviceTrigger deviceTrigger:deviceTriggers) {
			String device_sn = deviceTrigger.getDevice_sn();
			/*Map<String,Object> conditions = Maps.newHashMap();
	        conditions.put("device_sn",device_sn);
	        FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,null,null,null);*/
			Optional<Device> deviceOptional = deviceRepository.findByDevice_sn(device_sn);
			/*for (Document d : documents) {
	       	Device device = deviceService.returnDevice(d);
	       	devices.add(device);
	        }*/
			if(deviceOptional.isPresent()) {
				devices.add(deviceOptional.get());
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
	public JSONObject getNotAssociatedDevices(Long product_id,Integer trigger_id,Integer page,Integer number) {
		/*MongoClient meiyaClient = mongoDBUtil.getMongoConnect(mongoDB.getHost(),mongoDB.getPort());
		MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");*/
		
		List<DeviceTrigger> deviceTriggers = deviceTriggerRepository.findByTriggerId(trigger_id);
		List<String> deviceSns = new ArrayList<>();
		for(DeviceTrigger deviceTrigger:deviceTriggers) {
			deviceSns.add(deviceTrigger.getDevice_sn());
		}
		/*Map<String,Object> conditions = Maps.newHashMap();
        conditions.put("product_id",product_id);
        Map<String,Object> sortParams = Maps.newHashMap();
        sortParams.put("create_time",-1);
        FindIterable<Document> documents = mongoDBUtil.queryDocumentNin(collection,conditions,"device_sn",deviceSns,sortParams,(page-1)*number,number);
        JSONArray array = new JSONArray();*/
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"create_time");
		Page<Device> devicePage = deviceRepository.findByNameNotIn(deviceSns,product_id, pageable);
		
        
		
		return RESCODE.SUCCESS.getJSONRES(devicePage.getContent(),devicePage.getTotalPages(),devicePage.getTotalElements());
	}
	/**
	 * 根据触发器名称查询
	 * @param product_id
	 * @param name
	 * @param page
	 * @param number
	 * @return
	 */
	public JSONObject getByName(Long product_id,String name,Integer page,Integer number) {
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
	public JSONObject triggerAssociatedDevice(Long trigger_id,String device_sn) {
		Optional<TriggerModel> optional = triggerRepository.findById(trigger_id);
		if(optional.isPresent()) {
			DeviceTrigger deviceTrigger = new DeviceTrigger();
			deviceTrigger.setDevice_sn(device_sn);
			deviceTrigger.setTriggerId(trigger_id);
			deviceTriggerRepository.save(deviceTrigger);
			
			TriggerModel triggerModel = optional.get();
			Long ds_id = triggerModel.getDatastreamId();
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
			for(int i=0;i<data.size();i++) {
				try {
					//根据DeviceSn+Dm_name找到对应的dd_id
					JSONObject object = data.getJSONObject(i);
					String dm_name = object.getString("dm_name");
					int data_value = object.getIntValue("value");
					Date time = object.getDate("time");
					Optional<DeviceDatastream> ddId = deviceDatastreamRepository.findByDeviceSnAndDm_name(deviceSn, dm_name);
					if (ddId.isPresent()) {
						DeviceDatastream deviceDatastream = ddId.get();
						Long dd_id = deviceDatastream.getId();
						//根据dd_id找到triggerId
						List<DdTrigger> triggerList = ddTriggerRepository.findByDdId(dd_id);
						if (!triggerList.isEmpty()){
							for (DdTrigger ddTrigger : triggerList) {
								Long trigger_id = ddTrigger.getTriggerId();
								//触发阈值
								Optional<TriggerModel> triggerinfo2 = triggerRepository.findById(trigger_id);
								if (triggerinfo2.isPresent()) {
									TriggerModel triggerModel = triggerinfo2.get();
									int criticalValue = valueOf(triggerModel.getCriticalValue());
									//触发方式：0：邮箱；1：url
									int triggerMode = triggerModel.getTriggerMode();
									//触发方式详细信息：url或邮箱地址
									String modeValue = triggerModel.getModeValue();
									//根据triggerId找到对应的触发器信息
									//触发判断关系:">"或者"<"
									Optional<TriggerType> triggerinfo1 = triggerTypeRepository.findById(triggerModel.getTriggerTypeId());
									if (triggerinfo1.isPresent()) {
										TriggerType triggerType = triggerinfo1.get();
										String symbol = triggerType.getSymbol();
										//判断触发器是否触发
										if (((symbol.equals("<")) && (data_value < criticalValue)) || ((symbol.equals(">")) && (data_value > criticalValue))) {
											logger.info("警报触发：设备" + deviceSn + "的数据流" + dm_name + "值为" + data_value + ";" + data_value + symbol + criticalValue);
											if (triggerMode == 0) {
												//加入发邮件的线程池
												EmailHandlerModel model = new EmailHandlerModel();
												model.setCreateTime(time);
												model.setCriticalValue(criticalValue);
												model.setEmail(modeValue);
												model.setDmName(dm_name);
												model.setDeviceSn(deviceSn);
												model.setTriggerSymbol(symbol);
												model.setDataValue(String.valueOf(data_value));
												MqttClientUtil.getEmailQueue().offer(model);
											} else if (triggerMode == 1) {
												//使用url发送警报
											}
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					logger.debug(e.getClass().getName() + ": " + e.getMessage());
				}
			}
		}





}

