package com.hydata.intelligence.platform.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;
import javax.transaction.Transactional;

import com.alibaba.fastjson.JSONException;
import com.hydata.intelligence.platform.dto.*;
import com.hydata.intelligence.platform.model.EmailHandlerModel;
import com.hydata.intelligence.platform.repositories.*;
import com.hydata.intelligence.platform.utils.MqttClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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

import java.text.SimpleDateFormat;

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

	@Autowired
	private TriggerLogsRepository triggerLogsRepository;



	private static Logger logger = LogManager.getLogger(TriggerService.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 添加触发器
	 * device_sn由接口api/device/get_devicelist获取
	 * datastreamId由接口api/device/get_devicedslist获取
	 * @param trigger
	 * @return
	 */
	public JSONObject addTrigger(TriggerModel trigger) {
		logger.info("进入触发器添加");
		logger.debug(trigger.toString());
		if(trigger.getProductId()!=0&&trigger.getTriggerTypeId()!=null
				&&trigger.getName()!=null&&trigger.getCriticalValue()!=null
				&&trigger.getTriggerMode()!=null&&trigger.getModeValue()!=null
				&&trigger.getDeviceId()!=null&&trigger.getDatastreamId()!=0) {
			Optional<Product> productoptional = productRepository.findById(trigger.getProductId());
			if(productoptional.isPresent()) {
				Optional<TriggerType> triggerTypeOptional = triggerTypeRepository.findById(trigger.getTriggerTypeId());
				if(triggerTypeOptional.isPresent()) {
					Optional<Device> deviceOptional = deviceRepository.findById(trigger.getDeviceId());
					if(deviceOptional.isPresent()) {
						Optional<DeviceDatastream> deviceDatastreamOptional = deviceDatastreamRepository.findById(trigger.getDatastreamId());
						if(deviceDatastreamOptional.isPresent()) {
							if(deviceDatastreamOptional.get().getDevice_id()==trigger.getDeviceId()) {
								trigger.setCreateTime(new Date());
								TriggerModel triggerReturn = triggerRepository.save(trigger);
								logger.debug("触发器保存结束");
								if(triggerReturn!=null) {
									//保存DeviceTrigger，设备触发器关联关系表
									DeviceTrigger deviceTrigger = new DeviceTrigger();
									deviceTrigger.setTriggerId(triggerReturn.getId());
									deviceTrigger.setDeviceId(trigger.getDeviceId());
									deviceTrigger.setDeviceName(deviceOptional.get().getName());
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
							return RESCODE.DEVICE_DATASTREAM_NOT_EXIST.getJSONRES();
						}
						return RESCODE.DEVICE_DATASTREAM_NOT_EXIST.getJSONRES();
					}
					return RESCODE.DEVICE_SN_NOT_EXIST.getJSONRES();
				}
				return RESCODE.TRIGGER_TYPE_ID_NOT_EXIST.getJSONRES();
			}
			return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES();
		}
	}

	/**
	 * 添加触发器
	 * 外部接口
	 * 未使用到
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
					deviceTrigger.setDeviceId(trigger.getDeviceId());
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
	 * 包括删除触发器设备、设备数据流关联
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
			logger.debug("共删除触发器-设备关联数据："+result1+"条");
			int result2 = ddTriggerRepository.deleteByTriggerId(id);
			logger.debug("共删除触发器-设备数据流关联数据："+result2+"条");
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
//		Optional<Product> productoptional = productRepository.findById(triggerModel.getProductId());
		Optional<TriggerModel> triggerOptional = triggerRepository.findById(triggerModel.getId());
		if(triggerOptional.isPresent()) {
			logger.debug("触发器id存在");
			TriggerModel triggerModelOld = triggerOptional.get();
			Long product_id = triggerModelOld.getProductId();
			if(product_id==triggerModel.getProductId()) {
				Long device_id = triggerModelOld.getDeviceId();
				Long datastream_id = triggerModelOld.getDatastreamId();

				int a=0;
				if(device_id==triggerModel.getDeviceId()&&
						datastream_id==triggerModel.getDatastreamId()) {
					a = 3;
				}else if(device_id==triggerModel.getDeviceId()&&
						datastream_id!=triggerModel.getDatastreamId()) {
					a = 2;
				}else if(device_id!=triggerModel.getDeviceId()&&
						datastream_id==triggerModel.getDatastreamId()) {
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
								Long device_id1 = deviceTrigger.getDeviceId();
								Optional<DeviceDatastream> optional1 = deviceDatastreamRepository.findByDeviceIdAndDm_name(device_id1, dm_name);
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
					/*Optional<DeviceTrigger> optional2 = deviceTriggerRepository.findByDeviceSnAndTriggerId(triggerModelOld.getDevice_sn(), triggerModelOld.getId());
					if(optional2.isPresent()) {
						deviceTriggerRepository.deleteById(optional2.get().getId());
					}*/

						//第一步，删除后设备触发器关联关系
						List<DeviceTrigger> deviceTriggerList = deviceTriggerRepository.findByTriggerId(triggerModelOld.getId());
						for(DeviceTrigger dt:deviceTriggerList) {
							deviceTriggerRepository.deleteById(dt.getId());
						}
						Optional<Device> deviceOptional = deviceRepository.findById(triggerModel.getDeviceId());
						if (deviceOptional.isPresent()){
							DeviceTrigger dt = new DeviceTrigger();
							dt.setTriggerId(triggerModel.getId());
							dt.setDeviceId(triggerModel.getDeviceId());
							dt.setDeviceName(deviceOptional.get().getName());
							deviceTriggerRepository.save(dt);

							List<DdTrigger> ddTriggers2 = ddTriggerRepository.findByTriggerId(triggerModelOld.getId());
							for(DdTrigger ddTrigger : ddTriggers2) {
								ddTriggerRepository.deleteById(ddTrigger.getId());
							}
							Optional<DeviceDatastream> optional3 = deviceDatastreamRepository.findById(triggerModel.getDatastreamId());
							if(optional3.isPresent()) {
								String dm_name = optional3.get().getDm_name();
								List<DeviceTrigger> deviceTriggers = deviceTriggerRepository.findByTriggerId(triggerModel.getId());
								for(DeviceTrigger deviceTrigger:deviceTriggers) {
									Long device_id1 = deviceTrigger.getDeviceId();
									Optional<DeviceDatastream> optional1 = deviceDatastreamRepository.findByDeviceIdAndDm_name(device_id1, dm_name);
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
						}
						break;
				}
				return RESCODE.SUCCESS.getJSONRES();
			}
			return RESCODE.PRODUCT_ID_CAN_NOT_CHANGE.getJSONRES();
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}

	/**
	 * 查询产品下全部触发器
	 * @param productId
	 * @return
	 */
	public JSONObject getTriggersByProductId(Long productId) {
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
	 * @param device_id
	 * @return
	 */
	public JSONObject getAssociatedTriggers(Long device_id ,String name, Integer page,Integer number) {
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"id");
		Page<DeviceTrigger> result = deviceTriggerRepository.queryByDeviceId(device_id,name==null?"":name, pageable);
		JSONArray triggers = new JSONArray();
		for(DeviceTrigger deviceTrigger:result.getContent()) {
			Long trigger_id = deviceTrigger.getTriggerId();
			Optional<TriggerModel> optional = triggerRepository.findById(trigger_id);
			if(optional.isPresent()) {
				TriggerModel trigger = optional.get();
				JSONObject object = new JSONObject();
				object.put("id", trigger.getId());
				object.put("name", trigger.getName());
				object.put("createTime", sdf.format(trigger.getCreateTime()));
				String datastream_name = "";
				if(deviceDatastreamRepository.findById(trigger.getDatastreamId()).isPresent()) {
					datastream_name = deviceDatastreamRepository.findById(trigger.getDatastreamId()).get().getDm_name();
				}
				object.put("datastreamName", datastream_name);
				object.put("criticalValue", trigger.getCriticalValue());
				object.put("triggerType", getTriggerType(trigger.getTriggerTypeId()));
				Integer associated_device_sum ;
				associated_device_sum = deviceTriggerRepository.findByTriggerId(trigger.getId()).size();
				object.put("associatedDeviceSum", associated_device_sum);
				object.put("triggerMode", trigger.getTriggerMode());
				object.put("modeValue", trigger.getModeValue());
				triggers.add(object);
			}
		}
		return RESCODE.SUCCESS.getJSONRES(triggers,result.getTotalPages(),result.getTotalElements());
	}

	/**
	 * 获取设备关联触发器overview
	 * 总数、昨日新增，7日新增
	 * @param device_id
	 * @return
	 */
	public JSONObject getAssociatedTriggers(Long device_id) {
		@SuppressWarnings("deprecation")
		List<DeviceTrigger> deviceTriggers = deviceTriggerRepository.findByDeviceId(device_id);
		JSONObject result = new JSONObject();
		result.put("associatedTriggerSum", deviceTriggers.size());
		int yesterday_sum =0;
		int SevenDays_sum =0;

		Date SevendaysAgo = new Date();
		SevendaysAgo.setDate(SevendaysAgo.getDate()-7);
		SevendaysAgo.setHours(0);
		SevendaysAgo.setMinutes(0);
		SevendaysAgo.setSeconds(0);

		Date end = new Date();
		end.setHours(0);
		end.setMinutes(0);
		end.setSeconds(0);
		Date start = new Date();
		start.setDate(start.getDate()-1);
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);

		for(DeviceTrigger devicetrigger:deviceTriggers) {
			Optional<TriggerModel> triggerModelOptional=triggerRepository.findById(devicetrigger.getTriggerId());
			if(triggerModelOptional.isPresent()) {
				Long create_time = triggerModelOptional.get().getCreateTime().getTime();
				if(create_time>=start.getTime()&&create_time<=end.getTime()) {
					yesterday_sum++;
				}
				if(create_time<=new Date().getTime()&&create_time>=SevendaysAgo.getTime()) {
					SevenDays_sum++;
				}
			}
		}
		result.put("yesterdaySum", yesterday_sum);
		result.put("SevenDaysSum", SevenDays_sum);

		return RESCODE.SUCCESS.getJSONRES(result);
	}
	/**
	 * 获取触发器下已关联设备
	 * @param trigger_id
	 * @return
	 */
	public JSONObject getAssociatedDevices(Long trigger_id,String name,Integer page,Integer number,String start,String end) {
		List<DeviceTrigger> deviceTriggers =deviceTriggerRepository.findByTriggerIdAndDeviceName(trigger_id,name==null?"":name);
		logger.info("触发器下关联设备数量为："+deviceTriggers.size());
		List<Long> deviceIds = new ArrayList<>();
		for(DeviceTrigger deviceTrigger:deviceTriggers) {
			deviceIds.add(deviceTrigger.getDeviceId());
		}
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"create_time");
		try{
			Page<Device> devicePage = deviceRepository.findByIdInAndAndCreate_timeIsBetween(deviceIds,sdf.parse(start),sdf.parse(end),pageable);
			return RESCODE.SUCCESS.getJSONRES(devicePage.getContent(),devicePage.getTotalPages(),devicePage.getTotalElements());
		}catch (ParseException parseException) {
			return RESCODE.TIME_PARSE_ERROR.getJSONRES(parseException.getMessage());
		}
	}

	public List<Device> getAssociatedDevices(Long trigger_id){
		List<DeviceTrigger> deviceTriggers =deviceTriggerRepository.findByTriggerId(trigger_id);
		logger.info("触发器下关联设备数量为："+deviceTriggers.size());
		List<Long> deviceIds = new ArrayList<>();
		for(DeviceTrigger deviceTrigger:deviceTriggers) {
			deviceIds.add(deviceTrigger.getDeviceId());
		}
		List<Device> devices = deviceRepository.findByIdIn(deviceIds);
		return devices;
	}

	public Page<DeviceTrigger> getAssociatedDeviceSn(Long trigger_id,String name,Integer page,Integer number) {
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"id");
		Page<DeviceTrigger> result = deviceTriggerRepository.queryByTriggerId(trigger_id,name, pageable);
		return result;
	}

	/**
	 * 获取触发器下未关联设备
	 * @param trigger_id
	 * @param page
	 * @param number
	 * @return
	 */
	public JSONObject getNotAssociatedDevices(Long product_id,Long trigger_id,String name,Integer page,Integer number,
											  String start,String end) {
		logger.info("进入获取触发器下未关联设备");
		List<DeviceTrigger> deviceTriggers = deviceTriggerRepository.findByTriggerId(trigger_id);
		logger.info("触发器下关联设备数量为："+deviceTriggers.size());
		List<Long> deviceIds = new ArrayList<>();
		for(DeviceTrigger deviceTrigger:deviceTriggers) {
			deviceIds.add(deviceTrigger.getDeviceId());
		}
		logger.info(deviceIds);
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"create_time");
		try{
			Page<Device> devicePage = deviceRepository.findByNameNotIn(deviceIds,name==null?"":name,product_id ,
					sdf.parse(start),sdf.parse(end),pageable);
			logger.info(devicePage);
			return RESCODE.SUCCESS.getJSONRES(devicePage.getContent(),devicePage.getTotalPages(),devicePage.getTotalElements());
		}catch (ParseException pe){
			return RESCODE.TIME_PARSE_ERROR.getJSONRES(pe.getMessage());
		}
	}

	public List<Device> getNotAssociatedDevices(Long product_id,Long trigger_id) {
		logger.info("进入获取触发器下未关联设备");
		List<DeviceTrigger> deviceTriggers = deviceTriggerRepository.findByTriggerId(trigger_id);
		logger.info("触发器下关联设备数量为："+deviceTriggers.size());
		List<Long> deviceIds = new ArrayList<>();
		for(DeviceTrigger deviceTrigger:deviceTriggers) {
			deviceIds.add(deviceTrigger.getDeviceId());
		}
		logger.info(deviceIds);
		List<Device> devices = deviceRepository.findByIdNotInAndProduct_id(deviceIds,product_id);
		return devices;
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
		Page<TriggerModel> result = triggerRepository.queryByProductIdAndName(product_id, name==null?"":name, pageable);
		JSONArray triggers = new JSONArray();
		for(TriggerModel trigger:result.getContent()) {

			JSONObject object = new JSONObject();
			object.put("id", trigger.getId());
			object.put("name", trigger.getName());
			object.put("createTime",  sdf.format(trigger.getCreateTime()));
			String datastream_name = "";
			if(deviceDatastreamRepository.findById(trigger.getDatastreamId()).isPresent()) {
				datastream_name = deviceDatastreamRepository.findById(trigger.getDatastreamId()).get().getDm_name();
			}
			object.put("datastreamName", datastream_name);
			object.put("criticalValue", trigger.getCriticalValue());
			object.put("triggerType", getTriggerType(trigger.getTriggerTypeId()));
			Integer associated_device_sum ;
			associated_device_sum = deviceTriggerRepository.findByTriggerId(trigger.getId()).size();
			object.put("associatedDeviceSum", associated_device_sum);
			object.put("triggerMode", trigger.getTriggerMode());
			object.put("modeValue", trigger.getModeValue());
			object.put("deviceId",trigger.getDeviceId());
			object.put("datastreamId",trigger.getDatastreamId());
			triggers.add(object);

		}


		return RESCODE.SUCCESS.getJSONRES(triggers,result.getTotalPages(),result.getTotalElements());
	}
	/**
	 * 触发器与设备关联（或设备与触发器关联）
	 * @param trigger_id
	 * @param device_id
	 * @return
	 */
	public JSONObject triggerAssociatedDevice(Long trigger_id,Long device_id) {
		Optional<TriggerModel> optional = triggerRepository.findById(trigger_id);
		if(optional.isPresent()) {
			Optional<Device> deviceOptional = deviceRepository.findById(device_id);
			if(deviceOptional.isPresent()) {
				Optional<DeviceTrigger> deviceTriggerOptional = deviceTriggerRepository.findByDeviceIdAndTriggerId(device_id, trigger_id);
				if(deviceTriggerOptional.isPresent()==false) {
					//1.设备与触发器关联
					DeviceTrigger deviceTrigger = new DeviceTrigger();
					deviceTrigger.setDeviceId(device_id);
					deviceTrigger.setTriggerId(trigger_id);
					deviceTrigger.setDeviceName(deviceOptional.get().getName());
					deviceTriggerRepository.save(deviceTrigger);

					TriggerModel triggerModel = optional.get();
					Long ds_id = triggerModel.getDatastreamId();
					Optional<DeviceDatastream> datastreamOptional = deviceDatastreamRepository.findById(ds_id);
					if(datastreamOptional.isPresent()) {
						logger.info("触发器中数据流id存在");
						String dm_name = datastreamOptional.get().getDm_name();
						//2.设备下是否存在同名数据流
						Optional<DeviceDatastream>  datastream2Optional = deviceDatastreamRepository.findByDeviceIdAndDm_name(device_id, dm_name);
						if(datastream2Optional.isPresent()) {
							//待查询重复
							//3.存在即关联设备数据流与触发器
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
					return RESCODE.DEVICE_DATASTREAM_NOT_EXIST.getJSONRES();
				}
				return RESCODE.TRIGGER_DEVICE_ALREADY_IN_RELATION.getJSONRES();
			}
			return RESCODE.DEVICE_SN_NOT_EXIST.getJSONRES();
		}
		return RESCODE.TRIGGER_ID_NOT_EXIST.getJSONRES();
	}

	public JSONObject triggerDisconnectedDevice(Long trigger_id,Long device_id){
		Optional<DeviceTrigger> deviceTriggerOptional =
				deviceTriggerRepository.findByDeviceIdAndTriggerId(device_id,trigger_id);
		if (deviceTriggerOptional.isPresent()) {
			List<DeviceDatastream> deviceDatastreams = deviceDatastreamRepository.findByDeviceId(device_id);
			List<Long> ddid = new ArrayList<>();
			for (DeviceDatastream dd :
					deviceDatastreams) {
				ddid.add(dd.getId());
			}
			List<DdTrigger> ddTriggers = ddTriggerRepository.findByTriggerId(trigger_id);
			logger.info("设备数据流触发器数量："+ddTriggers.size());
			for (DdTrigger dt :
					ddTriggers) {
				if (ddid.contains(dt.getDdId())) {
					ddTriggerRepository.deleteById(dt.getId());
				}
			}
			deviceTriggerRepository.deleteById(deviceTriggerOptional.get().getId());
			return RESCODE.SUCCESS.getJSONRES();
		}else{
			return RESCODE.ID_NOT_EXIST.getJSONRES();
		}
	}

	public JSONObject triggerAssociatedAllDevices(Long trigger_id){
		Optional<TriggerModel> triggerModelOptional = triggerRepository.findById(trigger_id);
		if (triggerModelOptional.isPresent()){
			Long product_id  = triggerModelOptional.get().getProductId();
			List<Device> devices = getNotAssociatedDevices(product_id,trigger_id);
			for (Device device:
				 devices) {
				triggerAssociatedDevice(trigger_id,device.getId());
			}
		}
		return RESCODE.SUCCESS.getJSONRES();
	}
	public JSONObject triggerDisconnectedAllDevices(Long trigger_id){
		List<Device> devices=getAssociatedDevices(trigger_id);
		for (Device device:
			 devices) {
			triggerDisconnectedDevice(trigger_id,device.getId());
		}
		return RESCODE.SUCCESS.getJSONRES();
	}

	/**
	 * 获取触发信息数量趋势
	 * @param triggerId
	 * @param start
	 * @param end
	 * @return
	 */
	public JSONObject getIncrement(Long triggerId,Date start,Date end) throws ParseException{
		List<TriggerLogs> triggerLogs = new ArrayList<>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		logger.info(sdf.format(start));
		logger.info(sdf1.parse(sdf1.format(start)).getTime());
		logger.info(sdf.format(new Date()));
		logger.info(new Date().getTime());
		logger.info(sdf.format(end));
		logger.info(end.getTime());
		JSONObject statistics = new JSONObject();
		int len = 0;
		if (end.getTime() > new Date().getTime()) {
			logger.debug("结束时间比当前时间晚");
			len = new Long((new Date().getTime() - sdf1.parse(sdf1.format(start)).getTime()) / 1000 / 60 / 60 / 24).intValue();
			logger.debug(len);
		} else {
			logger.debug("结束时间早于当前时间");
			len = new Long((sdf1.parse(sdf1.format(end)).getTime() - sdf1.parse(sdf1.format(start)).getTime()) / 1000 / 60 / 60 / 24).intValue();
		}
		logger.debug("共需循环" + (len + 1) + "次");
		try {
			Date temp = sdf.parse(sdf.format(start));
			for (int i = 0; i <= len; i++) {
				logger.debug("第" + (i + 1) + "次");
				statistics.put(sdf1.format(temp), 0);
				temp.setDate(temp.getDate() + 1);
			}
		} catch (ParseException e) {
			logger.error("触发信息时间格式错误");
			e.printStackTrace();
		}

		Optional<TriggerModel> trigger = triggerRepository.findById(triggerId);
		//logger.info("触发器id："+triggerId+"已找到"+trigger.isPresent());
		//Optional<TriggerLogs> tl = triggerLogsRepository.findByTriggerId(triggerId);
		if (trigger.isPresent()) {
			logger.info(statistics);
			List <TriggerLogs> tmp = triggerLogsRepository.findByTriggerId(triggerId);
			if (!tmp.isEmpty()){
				logger.info(triggerId+"触发日志已找到:"+tmp);
			}
			List<TriggerLogs> logs = triggerLogsRepository.findByTriggerIdAndSendTimeBetween(triggerId,start,end);
			logger.info("已找到触发日志："+logs);
			for (TriggerLogs log : logs) {
				triggerLogs.add(log);
			}


			for (TriggerLogs triggerLog : triggerLogs) {
				String d = sdf1.format(triggerLog.getSendTime());
				if (statistics.get(d) != null) {
					statistics.put(d, (Integer) statistics.get(d) + 1);
				} else {
					statistics.put(d, 1);
				}
			}
			JSONArray array = new JSONArray();
			for (Map.Entry<String, Object> entry : statistics.entrySet()) {
				JSONObject sum = new JSONObject();
				sum.put("time", entry.getKey());
				sum.put("value", entry.getValue());
				array.add(sum);
			}

			//根据时间排序
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < array.size(); i++) {
				jsonValues.add(array.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private static final String KEY_NAME = "time";

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String valA = "";
					String valB = "";
					try {
						String aStr = a.getString(KEY_NAME);
						valA = aStr.replaceAll("-", "");
						String bStr = b.getString(KEY_NAME);
						valB = bStr.replaceAll("-", "");
					} catch (JSONException e) {
						logger.debug("时间格式错误，无法排序");
					}
					return valA.compareTo(valB);
				}
			});
			JSONArray result = new JSONArray();
			for (int i = 0; i < array.size(); i++) {
				result.add(jsonValues.get(i));
			}

			return RESCODE.SUCCESS.getJSONRES(result);
		}
		return null;
	}



	/**
	 * 获取总触发信息数量趋势
	 * @param start
	 * @param end
	 * @return
	 */
	public JSONObject getTotalIncrement(long product_id, Date start,Date end) throws ParseException{
		List<TriggerLogs> triggerLogs = new ArrayList<>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		logger.info(sdf.format(start));
		logger.info(sdf1.parse(sdf1.format(start)).getTime());
		logger.info(sdf.format(new Date()));
		logger.info(new Date().getTime());
		logger.info(sdf.format(end));
		logger.info(end.getTime());
		JSONObject statistics = new JSONObject();
		int len = 0;
		if (end.getTime() > new Date().getTime()) {
			logger.debug("结束时间比当前时间晚");
			len = new Long((new Date().getTime() - sdf1.parse(sdf1.format(start)).getTime()) / 1000 / 60 / 60 / 24).intValue();
			logger.debug(len);
		} else {
			logger.debug("结束时间早于当前时间");
			len = new Long((sdf1.parse(sdf1.format(end)).getTime() - sdf1.parse(sdf1.format(start)).getTime()) / 1000 / 60 / 60 / 24).intValue();
		}
		logger.debug("共需循环" + (len + 1) + "次");
		try {
			Date temp = sdf.parse(sdf.format(start));
			for (int i = 0; i <= len; i++) {
				logger.debug("第" + (i + 1) + "次");
				statistics.put(sdf1.format(temp), 0);
				temp.setDate(temp.getDate() + 1);
			}
		} catch (ParseException e) {
			logger.error("触发信息时间格式错误");
			e.printStackTrace();
		}
		Optional <Product> products = productRepository.findById(product_id);
		if (products.isPresent()){
			List<TriggerModel> triggers = triggerRepository.findByProductId(product_id);
			for (TriggerModel trigger:triggers){
				Optional<TriggerModel> tg = triggerRepository.findById(trigger.getId());
				if (tg.isPresent()) {
					logger.info(statistics);
					List<TriggerLogs> logs = triggerLogsRepository.findByTriggerIdAndSendTimeBetween(tg.get().getId(), start, end);
					for (TriggerLogs log : logs) {
						triggerLogs.add(log);
					}

					for (TriggerLogs triggerLog : triggerLogs) {
						String d = sdf1.format(triggerLog.getSendTime());
						if (statistics.get(d) != null) {
							statistics.put(d, (Integer) statistics.get(d) + 1);
						} else {
							statistics.put(d, 1);
						}
					}
				}
			}
			JSONArray array = new JSONArray();
			for (Map.Entry<String, Object> entry : statistics.entrySet()) {
				JSONObject sum = new JSONObject();
				sum.put("time", entry.getKey());
				sum.put("value", entry.getValue());
				array.add(sum);
			}

			//根据时间排序
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < array.size(); i++) {
				jsonValues.add(array.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private static final String KEY_NAME = "time";

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String valA = "";
					String valB = "";
					try {
						String aStr = a.getString(KEY_NAME);
						valA = aStr.replaceAll("-", "");
						String bStr = b.getString(KEY_NAME);
						valB = bStr.replaceAll("-", "");
					} catch (JSONException e) {
						logger.debug("时间格式错误，无法排序");
					}
					return valA.compareTo(valB);
				}
			});
			JSONArray result = new JSONArray();
			for (int i = 0; i < array.size(); i++) {
				result.add(jsonValues.get(i));
			}

			return RESCODE.SUCCESS.getJSONRES(result);
		}
		return null;
	}
	/**
	 * @author: Jasmine
	 * @createTime: 2018年11月20日上午11:31:11
	 * @description: <触发器触发模块>
	 */

	public void TriggerAlarm(Long device_id, JSONArray data) throws InterruptedException{
		for(int i=0;i<data.size();i++) {
			try {
				//根据DeviceSn+Dm_name找到对应的dd_id
				JSONObject object = data.getJSONObject(i);
				String dm_name = object.getString("dm_name");
				int data_value = object.getIntValue("value");
				Date time = object.getDate("time");
				//logger.info("触发器判断：时间: "+time);
				Optional<DeviceDatastream> ddId = deviceDatastreamRepository.findByDeviceIdAndDm_name(device_id, dm_name);
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
										String msg = "设备" + device_id + "的数据流" + dm_name + "值为" + data_value + "; " + data_value + symbol + criticalValue;
										logger.info("警报触发: "+msg);
										TriggerLogs triggerLogs = new TriggerLogs();
										triggerLogs.setId(System.currentTimeMillis());
										triggerLogs.setMsg(msg);
										triggerLogs.setSendTime(time);
										triggerLogs.setTriggerId(trigger_id);
										triggerLogsRepository.save(triggerLogs);
/*										logger.info("触发日志已保存:"+triggerLogs);
										List <TriggerLogs> tmp = triggerLogsRepository.findByTriggerId(trigger_id);
										logger.info(trigger_id+"触发日志已找到:"+tmp);
*/
										if (triggerMode == 0) {
											//加入发邮件的线程池
											EmailHandlerModel model = new EmailHandlerModel();
											model.setCreateTime(time);
											model.setCriticalValue(criticalValue);
											model.setEmail(modeValue);
											model.setDmName(dm_name);
											model.setDeviceId(device_id);
											model.setTriggerSymbol(symbol);
											model.setDataValue(String.valueOf(data_value));
											MqttClientUtil.getEmailQueue().offer(model);
										} else if (triggerMode == 1) {
											//使用url发送警报
											JSONObject param = new JSONObject();
											param.put("triggerId",trigger_id);
											param.put("criticalValue",criticalValue);
											param.put("deviceId",device_id);
											param.put("triggerSymbol",symbol);
											param.put("dataValue",data_value);
											param.put("time",time);
											logger.info("url: "+modeValue+", 触发："+param);
											logger.info(urlTrigger(modeValue, param));
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

	/**
	 * post请求
	 * @param url
	 * @param json
	 * @return
	 */
	public static JSONObject urlTrigger(String url,JSONObject json){
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		JSONObject response = null;
		try {
			StringEntity s = new StringEntity(json.toString());
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");
			post.setEntity(s);
			HttpResponse res = client.execute(post);
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = res.getEntity();
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
				response.put("result",result);
			}
		} catch (Exception e) {
			//logger.debug(e);
			throw new RuntimeException(e);
		}
		return response;
	}

	public String getTriggerType(Integer triggerTypeId) {
		Optional<TriggerType> tyOptional = triggerTypeRepository.findById(triggerTypeId);
		if(tyOptional.isPresent()) {
			return tyOptional.get().getSymbol();
		}
		return "";
	}
}

