package com.hydata.intelligence.platform.service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.hydata.intelligence.platform.repositories.*;
import com.hydata.intelligence.platform.utils.MqttClientUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.ApplicationAnalysis;
import com.hydata.intelligence.platform.dto.ApplicationAnalysisDatastream;
import com.hydata.intelligence.platform.dto.ApplicationChart;
import com.hydata.intelligence.platform.dto.ApplicationChartDatastream;
import com.hydata.intelligence.platform.dto.CmdLogs;
import com.hydata.intelligence.platform.dto.Data_history;
import com.hydata.intelligence.platform.dto.DdTrigger;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.DeviceDatastream;
import com.hydata.intelligence.platform.dto.DeviceTrigger;
import com.hydata.intelligence.platform.dto.OperationLogs;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.dto.TriggerModel;
import com.hydata.intelligence.platform.dto.User;
import com.hydata.intelligence.platform.model.DataHistory;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.utils.ExcelUtils;
import com.hydata.intelligence.platform.utils.StringUtils;

/**
 * @author pyt
 * @date 2018/10/31 11:39:02
 */
@Transactional
@Service
public class DeviceService {
	@Autowired
	private  ProductRepository productRepository;
	@Autowired
	private  DeviceDatastreamRepository deviceDatastreamRepository;
	@Autowired
	private  OperationLogsRepository operationLogsRepository;
	@Autowired
	private  CmdLogsRepository cmdLogsRepository;
	@Autowired
	private  MqttHandler mqttHandler;
	@Autowired
	private  TriggerService triggerService;
	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private  DeviceRepository deviceRepository;
	@Autowired
	private  DataHistoryRepository dataHistoryRepository;
	@Autowired
	private  DeviceTriggerRepository deviceTriggerRepository;
	@Autowired
	private  DdTriggerRepository ddTriggerRepository;
	@Autowired
	private  ApplicationChartRepository applicationChartRepository;

	@Autowired
	private  ApplicationChartDatastreamRepository applicationChartDatastreamRepository;
	@Autowired
	private  TriggerRepository triggerRepository;
	@Autowired
	private ProductService productService;

	@Value("${spring.data.mongodb.uri}")
	private String mongouri;
	
	@Value("${spring.datasource.url}")
	private String mysqlurl;

	private static Logger logger = LogManager.getLogger(DeviceService.class);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public JSONObject showAllByProductIdM(Long product_id,Integer page,Integer number,int sort) {
		logger.debug("进入产品下的设备分页查询");
		logger.info("MongoDB数据库地址：");
		logger.info(mongouri);
		logger.info("mysql数据库地址：");
		logger.info(mysqlurl);
		Pageable pageable;
		if(sort==-1) {
			//逆序
			pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"create_time");
		}else {
			//顺序
			pageable = new PageRequest(page-1, number, Sort.Direction.ASC,"create_time");
		}
		Page<Device> devicePage = deviceRepository.findDeviceByProductid(product_id, pageable);
		return RESCODE.SUCCESS.getJSONRES(devicePage.getContent(),devicePage.getTotalPages(),devicePage.getTotalElements());
	}

	/**
	 * 向MongoDB中存储设备表
	 * @param device
	 * @return
	 */
	public JSONObject addDeviceM(Device device) {
		logger.debug("进入addDeviceM");
		/*MongoClient meiyaClient = mongoDBUtil.getMongoConnect(mongoDB.getHost(),mongoDB.getPort());
		MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");*/
		Optional<Product> productOptional = productRepository.findById(device.getProduct_id());
		logger.debug("检查添加设备的产品id是否存在");
		if(productOptional.isPresent()) {
			logger.debug("产品id存在");
			boolean isNumber = StringUtils.isNumeric(device.getDevice_sn());
			boolean flag = checkDevicesn(device.getDevice_sn(),device.getProduct_id());
			logger.info("设备编码是否符合规范："+isNumber);
			logger.info("设备编码是否已存在："+flag);
			if(flag && isNumber) {
				logger.debug("设备编码符合规范且数据库中不存在");
	            /*Map<String,Object> insert = new HashMap<>();
	               //1、测试增加
	            insert.put("name",device.getName());
	            insert.put("device_sn", device.getDevice_sn());
	            insert.put("product_id",device.getProductId());
	            insert.put("create_time",new Date());
	            insert.put("status", null);
	            mongoDBUtil.insertDoucument(collection,insert);*/
				device.setId(System.currentTimeMillis());
				device.setCreate_time(new Date());				
				logger.debug("开始存日志");			
	            OperationLogs logs = new OperationLogs();
	            logs.setId(System.currentTimeMillis());
				logs.setUserId(productOptional.get().getUserId());
				logs.setOperationTypeId(6);
				logs.setMsg("添加设备:"+device.getDevice_sn());
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
				logger.debug("结束存日志");

//				haizhe
//				若为mqtt通讯方式，调用Jasmine方法，添加topic
				if(productOptional.get().getProtocolId()!=null&&productOptional.get().getProtocolId()==1) {
					logger.debug("设备协议id为1，即MQTT");
					try {
						mqttHandler.mqttAddDevice(device.getDevice_sn());
						device.setProtocolId(1);
					} catch (MqttException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return RESCODE.DEVICE_ADD_MQTT_ERROR.getJSONRES();
					}finally {
						deviceRepository.save(device);
						return RESCODE.SUCCESS.getJSONRES();
					}
				}else{
					device.setProtocolId(2);
					deviceRepository.save(device);
					return RESCODE.SUCCESS.getJSONRES();
				}
			}else {
				return RESCODE.AUTH_INFO_EXIST.getJSONRES();
			}
		}
		logger.debug("产品id不存在");
		return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 获取使用某协议的全部device_sn
	 * @param protocol_id 协议id
	 * @return 返回值
	 */
	public JSONObject getDeviceByProtocol(Integer protocol_id) {
		/*MongoClient meiyaClient = mongoDBUtil.getMongoConnect(mongoDB.getHost(),mongoDB.getPort());
		MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");*/
		List<Product> products = productRepository.findByProtocolId(protocol_id);
		JSONArray array = new JSONArray();
		for(Product product : products) {
			List<Device> deviceList = deviceRepository.findByProductId(product.getId());
			for(Device device : deviceList) {
				array.add(device.getDevice_sn());
			}
		}		
		return RESCODE.SUCCESS.getJSONRES(array);
	}
	
	/**
	 * 检查设备鉴权信息是否重复
	 * @param device_sn 设备编码
	 * @return 可使用/不可使用
	 */
	public Boolean checkDevicesn(String device_sn,Long product_id) {
		Optional<Device> deviceOptional = deviceRepository.findByDevice_sn(device_sn,product_id);
        return !deviceOptional.isPresent();
    }

	/**
	 * 产品下设备分页设备名模糊查询
	 * @param product_id 产品id
	 * @param deviceSnOrName 设备编码或设备名
	 * @param page 页码
	 * @param number	每页显示数量
	 * @param start	设备创建开始时间
	 * @param end	设备创建结束时间
	 * @return 返回分页。
	 */
	public JSONObject queryByDeviceSnOrName_m(Long product_id,String deviceSnOrName,Integer page,Integer number,Date start,Date end) {
		logger.info("进入queryByDeviceSnOrName_m");
		logger.info("开始时间："+sdf.format(start));
		logger.info("结束时间："+sdf.format(end));
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"create_time");
		Page<Device> devicePage = null;
		Optional<Device> deviceOptional = null;
		if(deviceSnOrName!=null&&!deviceSnOrName.equals("")&&StringUtils.isNumeric(deviceSnOrName)) {
			logger.info(deviceSnOrName+":是数字串");
			deviceOptional = deviceRepository.findByDevice_sn(deviceSnOrName,product_id);		
		}else if(deviceSnOrName!=null&&!deviceSnOrName.equals("")){
			logger.info(deviceSnOrName+":不是数字");
			devicePage = deviceRepository.findDeviceByNameAndTime(product_id, deviceSnOrName,start,end,pageable);
		}else {
			devicePage = deviceRepository.findDeviceByTime(product_id,start,end,pageable);
		}		
		
		if(devicePage!=null) {
			logger.info("根据设备名模糊查询");
			List<Device> devices = devicePage.getContent();
			List devicesAndRelatedApp = new ArrayList<>();
			for(Device device : devices) {
				Set<Long> apps = getRelatedApp(device.getId());
				
				JSONObject deviceDetail = new JSONObject();
				deviceDetail.put("id", device.getId());
				deviceDetail.put("device_sn", device.getDevice_sn());
				deviceDetail.put("name", device.getName());
				deviceDetail.put("iconUrl", device.getIconUrl());
				deviceDetail.put("create_time", sdf.format(device.getCreate_time()));
				deviceDetail.put("app_sum", apps.size());
				deviceDetail.put("apps", apps);
				devicesAndRelatedApp.add(deviceDetail);
			}
			return RESCODE.SUCCESS.getJSONRES(devicesAndRelatedApp,devicePage.getTotalPages(),devicePage.getTotalElements());
		}else if(deviceOptional.isPresent()) {
			logger.info("根据设备编码查询");
			List devicesAndRelatedApp = new ArrayList<>();
			Set<Long> apps = getRelatedApp(deviceOptional.get().getId());
			JSONObject deviceDetail = new JSONObject();
			deviceDetail.put("device_sn", deviceOptional.get().getDevice_sn());
			deviceDetail.put("name", deviceOptional.get().getName());
			deviceDetail.put("create_time", sdf.format(deviceOptional.get().getCreate_time()));
			deviceDetail.put("iconUrl", deviceOptional.get().getIconUrl());
			deviceDetail.put("app_sum", apps.size());
			deviceDetail.put("apps", apps);
			devicesAndRelatedApp.add(deviceDetail);
			return RESCODE.SUCCESS.getJSONRES(devicesAndRelatedApp,1,1);
		}else {
			logger.info("未查询到");
			return RESCODE.SUCCESS.getJSONRES(null,0 ,0);
		}
		
	}

	/**
	 * 修改设备
	 * @param device 设备
	 * @return 修改结果
	 */
	public JSONObject modifyDevice_m(Device device) {
		logger.debug("进入设备修改");
		Optional<Device> deviceOptional = deviceRepository.findById(device.getId());
		if(deviceOptional.isPresent()) {
			Device device_old = deviceOptional.get();
			if(device_old.getName().equals(device.getName())) {
				return RESCODE.NO_CHANGES.getJSONRES();
			}else {
				device_old.setModify_time(new Date());
				device_old.setName(device.getName());
				deviceRepository.save(device_old);
				//修改设备触发器关联关系表
				List<DeviceTrigger> deviceTriggers = deviceTriggerRepository.findByDeviceId(device.getId());
				for (DeviceTrigger dt:
					 deviceTriggers) {
					dt.setDeviceName(device.getName());
					deviceTriggerRepository.save(dt);
				}
				return RESCODE.SUCCESS.getJSONRES();
			}
		}
		return RESCODE.DEVICE_SN_NOT_EXIST.getJSONRES();
	}
	
	/**
	 * 删除设备
	 * 1删除设备数据流触发器（完成）
	 * 2.设备（完成）
	 * @param device_id 设备id
	 * @return 删除结果
	 */
	public JSONObject deleteDevice(Long device_id){
		Optional<Device> deviceOptional = deviceRepository.findById(device_id);
		if(deviceOptional.isPresent()) {
			Device device = deviceOptional.get();
			//删除基于设备的触发器
			List<TriggerModel> triggers = triggerRepository.findByDeviceId(device_id);
			for(TriggerModel trigger : triggers) {
				triggerService.delTrigger(trigger.getId());
			}
			//删除与该设备关联的触发器关系
			List<DeviceTrigger> dts = deviceTriggerRepository.findByDeviceId(device_id);
			for (DeviceTrigger deviceTrigger:dts){
				deviceTriggerRepository.deleteById(deviceTrigger.getId());
			}
			//删除该设备下数据流与触发器的关系
			List<DeviceDatastream> dds = deviceDatastreamRepository.findByDeviceId(device_id);
			for (DeviceDatastream dd :
					dds) {
				List<DdTrigger> ddts = ddTriggerRepository.findByDdId(dd.getId());
				for (DdTrigger ddt :
					 ddts) {
					ddTriggerRepository.deleteById(ddt.getId());
				}
			}
			Optional< Product> optional = productRepository.findById(device.getProduct_id());
			if(optional.isPresent()&&optional.get().getProtocolId()!=null&&optional.get().getProtocolId()==1) {
//		         haizhe
//				 若为mqtt通讯方式，调用Jasmine方法，删除其topic
				try {
					mqttHandler.mqttRemoveDevice(String.valueOf(device_id));
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					logger.debug("删除设备topic："+device_id+"发生异常");
					logger.debug(e.getMessage());
				}
			}
			//删除设备下数据流
			logger.info("删除设备下数据流");
			List<DeviceDatastream> deviceDatastreamList = deviceDatastreamRepository.findByDeviceId(device_id);
			for (DeviceDatastream dd:
					deviceDatastreamList
				 ) {
				deviceDatastreamRepository.delete(dd);
			}
			deviceRepository.delete(device);	        
			return RESCODE.SUCCESS.getJSONRES();
		}else {
			return RESCODE.DEVICE_SN_NOT_EXIST.getJSONRES();
		}
		
	}
	
	/**
	 * 获取产品下设备简单列表
	 * @param productId 产品id
	 * @return 返回值
	 */
	public JSONObject getByProductId(long productId) {
		List<Device> deviceList = deviceRepository.findByProductId(productId);
		//设备关联应用
		return RESCODE.SUCCESS.getJSONRES(deviceList);
	}
	/**
	 * 获取设备下数据流分页列表
	 * @param id
	 * @return
	 */
	public JSONObject getDeviceDsByDeviceId(Long id,Integer page,Integer number) {
		logger.debug("开始获取设备"+id+"下数据流列表");
		Optional<Device> deviceOptional = deviceRepository.findById(id);
		if(deviceOptional.isPresent()) {
			logger.info("根据id查询到设备");
			Pageable pagea = new PageRequest(page-1, number, Sort.Direction.DESC,"id");
			Page<DeviceDatastream> pageResult =  deviceDatastreamRepository.findAll(new Specification<DeviceDatastream>() {

				@Override
				public Predicate toPredicate(Root<DeviceDatastream> root, CriteriaQuery<?> query,
						CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicateList = new ArrayList<>();

					 if (id != 0) {
		                    predicateList.add(
		                            criteriaBuilder.equal(
		                                    root.get("device_id").as(Long.class),
		                                    id));
		             }
					 Predicate[] predicates = new Predicate[predicateList.size()];
		             return criteriaBuilder.and(predicateList.toArray(predicates));
				}
				
			}, pagea);
			
			List<DeviceDatastream> ddList = pageResult.getContent();
			JSONObject result = new JSONObject();
			JSONArray a = new JSONArray();
			int dd_sum = 0 ;
			int dd_sum_y = 0;
			int dd_sum_7 = 0;
			for(DeviceDatastream datastream : ddList) {
				
				List<Data_history> dhs = dataHistoryRepository.findByDd_id(datastream.getId());
				dd_sum += dhs.size();
				
				Date date_y = new Date();
				date_y.setDate(new Date().getDate()-1);	
				try {
					List<Data_history> dhs_y = dataHistoryRepository.findByDd_idAndCreate_timeBetween(
							datastream.getId(),
							sdf1.parse(sdf1.format(date_y)),
							sdf1.parse(sdf1.format(new Date())));
					dd_sum_y += dhs_y.size();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					
					logger.error(e.getMessage());
				}
				
				
				Date date_7 = new Date();
				date_7.setDate(date_7.getDate()-7);	
				List<Data_history> dhs_7 = dataHistoryRepository.findByDd_idAndCreate_timeBetween(
						datastream.getId(),date_7,new Date());
				dd_sum_7 += dhs_7.size();
				
				Pageable pageable = new PageRequest(0, 1, Sort.Direction.DESC,"create_time");
				Page<Data_history> latestdh=dataHistoryRepository.findByDd_id(datastream.getId(), pageable);
				
				JSONObject ds =  new JSONObject();
				ds.put("id", datastream.getId());
				ds.put("dm_name", datastream.getDm_name());
				if(latestdh.getContent().size()>0)
					ds.put("update_time", sdf2.format(latestdh.getContent().get(0).getCreate_time()));
				else
					ds.put("update_time", null);					
				a.add(ds);
			}
			result.put("DeviceDatastreams", a);
			result.put("dd_sum", dd_sum);
			result.put("dd_sum_y", dd_sum_y);
			result.put("dd_sum_7", dd_sum_7);
			return RESCODE.SUCCESS.getJSONRES(result,pageResult.getTotalPages(),pageResult.getTotalElements());
		}else {
			logger.debug("设备"+id+"不存在");
			return RESCODE.ID_NOT_EXIST.getJSONRES();
		}
	}
	
	public JSONObject getDeviceDsByDeviceId(Long id) {
		logger.debug("开始获取设备"+id+"下数据流列表");
		Optional<Device> deviceOptional = deviceRepository.findById(id);
		if(deviceOptional.isPresent()) {
			List<DeviceDatastream> ddList = deviceDatastreamRepository.findByDeviceId(id);
			
			return RESCODE.SUCCESS.getJSONRES(ddList);
		}else {
			logger.debug("设备"+id+"不存在");
			return RESCODE.ID_NOT_EXIST.getJSONRES();
		}
	}

	/**
	 * 解析设备上传的数据流
	 * regCode：设备注册码
	 * 1.存储数据流
	 * 2.存储数据
	 * 3.触发
	 * @param jsonObject 上传的数据流信息
	 */
	public JSONObject resolveDeviceData(Long id, JSONObject jsonObject) {
		logger.info("设备"+id+"发来了http实时信息："+jsonObject);
		//jsonObject
		//检查设备鉴权码
		boolean isHttp = false;
		List<Product> products = productRepository.findByProtocolId(2);
		for (Product product : products) {
			if (deviceRepository.findById(id).isPresent()){
				isHttp = true;
			}
		}
		logger.info("HTTP新信息开始处理，设备注册码已找到："+isHttp);
		if (isHttp) {
			try {
				httpDataHandler(id, jsonObject);
			} catch (Exception e){
				return RESCODE.FAILURE.getJSONRES("HTTP数据解析失败"+e);
			}
		}else {
			return RESCODE.DEVICE_SN_NOT_EXIST.getJSONRES();
		}
		return RESCODE.SUCCESS.getJSONRES();

	}

	/**
	 * 解析实时数据流
	 *
	 * @param data
	 * @return
	 *
	 *	http实时数据流格式
	 *	{
	 *  "device_sn":"123456",
	 *	"datastreams": [
	 *	{
	 *	"dm_name": "temperature", //数据流名称或数据流模板名称
	 *	"value": 42 //上传数据点值
	 *	},
	 *	{
	 *	"dm_name": "humid", //数据流名称或数据流模板名称
	 *	"value": 35 //上传数据点值
	 *	},
	 *	{…}
	 *	]
	 *	}
	 */
	public void httpDataHandler(Long id, JSONObject data){
		JSONArray result = new JSONArray();
		MqttClientUtil.getCachedThreadPool().execute(() -> {
			//解析数据
			try {
				//String topic = data.getString("device_id");
				//检查device_id
				JSONArray array = data.getJSONArray("datastreams");
				if ((array != null) && (id != null)) {
					for (int i = 0; i < array.size(); i++) {
						JSONObject data_point = array.getJSONObject(i);
						String dm_name = data_point.getString("dm_name");
						//String time = data_point.getString("at");
						//获取当前时间
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String time = sdf.format(date);
						String value = data_point.getString("value");
						JSONObject object = new JSONObject();
						if ((dm_name != null) && (value != null)) {
							object.put("dm_name", dm_name);
							object.put("time", time);
							object.put("value", value);
							result.add(object);
						} else {
							logger.debug("数据格式错误，解析失败");
						}
					}
				} else {
					logger.debug("数据格式错误，解析失败：datastreams不存在");
				}
			//存储数据
			if (!result.isEmpty()) {
				logger.info("http数据解析结果为：" + data + "---开始保存数据");
				dealWithData(id, result);
				//触发判断
				try {
					triggerService.TriggerAlarm(id, result);
				} catch (InterruptedException e) {
					logger.error("http实时数据触发失败");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
				logger.error("HTTP解析失败");
			}
		});
	}

	public static boolean isInteger(String str) {    
	    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
	    return pattern.matcher(str).matches();    
	 }
	
	/**
	 * 解析表格中的设备信息并保存
	 * @param file 文件
	 * @param productId 产品id
	 * @return 文件处理结果
	 */
	public JSONObject importExcel(MultipartFile file,long productId,HttpServletRequest request) {
		logger.debug("解析表格中的设备信息并保存");
		JSONObject result = new JSONObject();
		JSONObject failMsg = new JSONObject();
		Optional<Product> productOptional = productRepository.findById(productId);
		if(productOptional.isPresent()) {
			Product product = productOptional.get();
			JSONObject objectReturn = ExcelUtils.importExcel(file);
			if((Integer)objectReturn.get("code")==0) {
				JSONArray array = objectReturn.getJSONArray("data");
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
							boolean isExist = checkDevicesn(devicesn,productId);
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
							Device device = new Device();
							device.setId(System.currentTimeMillis());
							device.setCreate_time(new Date());
							device.setDevice_sn(devicesn);
							device.setName(name);
							device.setProduct_id(productId);
							device.setProtocolId(product.getProtocolId());
							deviceRepository.save(device);
							if(product.getProtocolId()==1) {
								logger.debug("设备协议id为1，即MQTT");
								try {
									mqttHandler.mqttAddDevice(device.getDevice_sn());
								} catch (MqttException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();							
								}
							}
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
			}else {
				return objectReturn;
			}
		}
		return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
	}
	
	/**
	 * 获取设备数量趋势
	 * @param productId 产品id
	 * @param start 创建开始时间
	 * @param end 创建结束时间
	 * @return	分页
	 */
	public JSONObject getIncrement(Long productId,Date start,Date end) {
		if(end.getTime()> new Date().getTime()) {
			end = new Date();
		}
		int length;
		try {
			length =  (int) ((sdf1.parse(sdf1.format(end)).getTime() - sdf1.parse(sdf1.format(start)).getTime())/1000/60/60/24);
		}catch (ParseException e){
			return RESCODE.TIME_PARSE_ERROR.getJSONRES();
		}

		logger.debug("共需循环"+(length+1)+"次");
		List<Device> devices = deviceRepository.findByCreate_timeBetween(productId, start, end);
//		趋势分析图表数据
		JSONArray array = new JSONArray();
		
		Date sdate = new Date();
		Date edate = new Date();
		try {
			sdate = sdf.parse(sdf.format(start));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0;i<length;i++) {
			logger.debug("第"+(i+1)+"次");
			try {				
				edate = sdf.parse(sdf.format(sdate));
				edate.setDate(edate.getDate()+1);
				edate.setHours(0);
				edate.setMinutes(0);
				edate.setSeconds(0);
				logger.debug("开始："+sdf.format(sdate));
				logger.debug("结束："+sdf.format(edate));
				JSONObject object = new JSONObject();
				int s=0;
				for(Device device:devices) {
					if(device.getCreate_time().getTime()>=sdate.getTime()&&device.getCreate_time().getTime()<edate.getTime()) {
						s++;
					}
				}
				object.put("time",sdf1.format(sdate));
				object.put("value", s);
				array.add(object);
				sdate = sdf.parse(sdf.format(edate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
				e.printStackTrace();
			}			
		}
		logger.debug("最后一次");
		try {
			edate = sdf.parse(sdf.format(end));
			logger.debug("开始："+sdf.format(sdate));
			logger.debug("结束："+sdf.format(edate));
			JSONObject object = new JSONObject();
			int s=0;
			for(Device device:devices) {
				if(device.getCreate_time().getTime()>=sdate.getTime()&&device.getCreate_time().getTime()<edate.getTime()) {
					s++;
				}
			}
			object.put("time",sdf1.format(sdate));
			object.put("value", s);
			array.add(object);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}	
		return RESCODE.SUCCESS.getJSONRES(array);
	}
	/**
	 * 获取设备下发命令日志
	 * @param device_id
	 * @return
	 */
	public JSONObject getCmdLogs(Long device_id) {
		List<CmdLogs> cmdLogs = cmdLogsRepository.findByDeviceId(device_id);
		return RESCODE.SUCCESS.getJSONRES(cmdLogs);
	}
	
	public JSONObject getDeviceDsData(long dd_id,Date start,Date end) {
		List<Data_history> data_histories = dataHistoryRepository.findByDd_idAndCreate_timeBetween(dd_id, start, end);
		return RESCODE.SUCCESS.getJSONRES(data_histories);
	}

	public List<Data_history> getDeviceDsDataForChart(long dd_id){
		Pageable pageable = new PageRequest(0, 10, Sort.Direction.DESC,"create_time");
		Page<Data_history> data_historyPage = dataHistoryRepository.findByDd_id(dd_id,pageable);
		int size = data_historyPage.getContent().size();
		List<Data_history> data_histories = new ArrayList<>();
		for (int i = 0 ; i <size ;i++){
			data_histories.add(data_historyPage.getContent().get(size-1-i));
		}
		return data_histories;
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
	/*public void saveDataStream(String deviceSn, String LivaDataStream){
		MongoClient meiyaClient = mongoDBUtil.getMongoConnect(mongoDB.getHost(),mongoDB.getPort());
		MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");
		
		Map<String,Object> insert = new HashMap<>();
		insert.put("name","saveDataStream");
		//insert.put("device_sn", deviceSn); 用device_id，找dd_id, 数据流名称匹配
		insert.put("product_id",1);
		insert.put("create_time",new Date());
		//insert.put("data_history",LiveDataStream);
		mongoDBUtil.insertDoucument(collection,insert);
	}*/
	
	/**
	 * 对获取数据进行处理
	 */
	public void  dealWithData(Long device_id,JSONArray data) {
		/*MongoClient meiyaClient = mongoDBUtil.getMongoConnect(mongoDB.getHost(),mongoDB.getPort());
		MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","data_history");
		*/
		logger.debug("进入dealWithData处理数据");
		logger.debug(data.toJSONString());
		logger.info("设备编号为："+device_id);
		List<DeviceDatastream> deviceDsList = deviceDatastreamRepository.findByDeviceId(device_id);	
		//1.获取设备编号：deviceSn下全部数据流名称deviceDatastreamName
		logger.info(deviceDsList.size());
		JSONArray names = new JSONArray();
		for(DeviceDatastream dd:deviceDsList) {			
			String dm_name = dd.getDm_name();
			names.add(dm_name);			
		}
		//2.上传数据中未存入的数据流存入
		logger.info(data.size());
		for(int i = 0; i<data.size(); i++) {

			JSONObject object = (JSONObject)data.get(i);
			if(names.contains(object.getString("dm_name"))==false){
				DeviceDatastream datastream = new DeviceDatastream();
				datastream.setDevice_id(device_id);
				datastream.setDm_name(object.getString("dm_name"));
				DeviceDatastream ddReturn =deviceDatastreamRepository.save(datastream);
				names.add(object.getString("dm_name"));
				//（1）检查设备是否与触发器关联
				List<DeviceTrigger> devicetriggers = deviceTriggerRepository.findByDeviceId(device_id);
				for(DeviceTrigger dt:devicetriggers) {					
					Optional<TriggerModel> optional  = triggerRepository.findById(dt.getTriggerId());
					if(optional.isPresent()) {
						Optional<DeviceDatastream> ddOptional = deviceDatastreamRepository.findById(optional.get().getDatastreamId());
						if(ddOptional.isPresent()) {
							if(ddOptional.get().getDm_name().equals(object.getString("dm_name"))) {
								DdTrigger ddTrigger = new DdTrigger();
								ddTrigger.setDdId(ddReturn.getId());
								ddTrigger.setDmName(object.getString("dm_name"));
								ddTrigger.setModeMsg(optional.get().getModeValue());
								ddTrigger.setProductId(optional.get().getProductId());
								ddTrigger.setMode(optional.get().getTriggerMode());
								ddTrigger.setTriggerId(optional.get().getId());
								ddTriggerRepository.save(ddTrigger);
							}
						}
					}
					
				}				
				
			}

			Optional<DeviceDatastream> ddOptional = deviceDatastreamRepository.findByDeviceIdAndDm_name(device_id, object.getString("dm_name"));
			if(ddOptional.isPresent()){
				DeviceDatastream dd= ddOptional.get();
				Data_history data_history = new Data_history();
				data_history.setId(System.currentTimeMillis());
				data_history.setDd_id(dd.getId());
				try{
					data_history.setCreate_time(sdf2.parse(object.getString("time")));
				}catch (Exception e){
					logger.error(e.getMessage());
					data_history.setCreate_time(new Date());
				}
				data_history.setName(object.getString("dm_name"));
				data_history.setValue(Double.parseDouble(object.getString("value")));
				dataHistoryRepository.save(data_history);
			}
		}
	}
	/**
	 * 处理http协议发送的数据
	 * @param jsonObject
	 * 弃
	 */
	public void recieveData(JSONObject jsonObject){
		
	}
	
	/**
	 * 获取设备详情
	 * 对外接口
	 * @param device_id
	 * @return
	 */
	public JSONObject getDeviceDetail(Long device_id,String api_key) {
		/*MongoClient meiyaClient = mongoDBUtil.getMongoConnect(mongoDB.getHost(),mongoDB.getPort());
		MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");
		MongoCollection<Document> data_history = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","data_history");
		*/
		logger.debug("开始获取设备"+device_id+"详情");
		/*JSONObject object = new JSONObject();
		Map<String,Object> conditions = Maps.newHashMap();
        conditions.put("device_sn",device_sn);       
        FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,null,null,null);
        JSONArray array = new JSONArray();
        Device device = null;
        for (Document d : documents) {
        	device = returnDevice(d);
        	array.add(device);
        	object.put("device_sn", device_sn);
        	object.put("create_time", device.getCreateTime());
        	object.put("modify_time", device.getModifyTime());
        	object.put("name", device.getName());
        	object.put("status", device.getStatus());
        }*/
		JSONObject object = new JSONObject();
		Optional<Device> deviceOptional = deviceRepository.findById(device_id);
		if(deviceOptional.isPresent()) {
			Device device = deviceOptional.get();
			object.put("id", device_id);
			object.put("device_sn", device.getDevice_sn());
        	object.put("create_time", device.getCreate_time());
        	object.put("modify_time", device.getModify_time());
        	object.put("name", device.getName());
        	object.put("status", device.getStatus());
			Optional<Product> productOptional = productRepository.findById(device.getProduct_id());
	        if(productOptional.isPresent()) {
	        	Optional<User> userOptional = userRepository.findById(productOptional.get().getUserId());
	        	if(userOptional.isPresent()) {
	        		String key = userOptional.get().getDefaultKey();
	        		if(key.equals(api_key)) {
	        			
        				List<DeviceDatastream> ddList = deviceDatastreamRepository.findByDeviceId(device_id);
        				JSONArray a = new JSONArray();
        				Pageable pageable = new PageRequest(0, 1, Sort.Direction.DESC,"create_time");
        				for(DeviceDatastream datastream : ddList) {
        					/*BasicDBObject query = new BasicDBObject(); 
        					query.put("dd_id", datastream.getId()+"");
        					Document sortDocument = new Document();
        					sortDocument.append("date", -1);
        					FindIterable<Document> documents1 = data_history.find(query).limit(1).sort(sortDocument);
        					for (Document d : documents1) {
        						DataHistory dataHistory = returnData(d);
        						a.add(dataHistory);
        				    }*/
        					Page<Data_history> data_historyPage = dataHistoryRepository.findByDd_id(datastream.getId(),pageable);
        					if(data_historyPage.getContent().size()>0) {
        						a.add(data_historyPage.getContent().get(0));
        					}       					
        				}
        				object.put("datastream", a);
						logger.info("获取设备详情接口成功调用");
        				return RESCODE.SUCCESS.getJSONRES(object);
	        			
	        		}else {
						logger.info("鉴权key错误");
	        			return RESCODE.API_KEY_ERROR.getJSONRES();
	        		}
	        	}else {
					logger.info("用户id不存在");
	        		return RESCODE.USER_ID_NOT_EXIST.getJSONRES();
	        	}
	        }else {
				logger.info("产品id不存在");
	        	return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
	        }
		}else {
			logger.info("device_sn不存在");
			return RESCODE.DEVICE_SN_NOT_EXIST.getJSONRES();
		}
        
	}
	
	public JSONObject getDeviceDatastream(Long device_id ,String api_key) {
		/*MongoClient meiyaClient = mongoDBUtil.getMongoConnect(mongoDB.getHost(),mongoDB.getPort());
		MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");
		Map<String,Object> conditions = Maps.newHashMap();
        conditions.put("device_sn",device_sn);       
        FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,null,null,null);
        JSONArray array = new JSONArray();
        Device device = null;
        for (Document d : documents) {
        	device = returnDevice(d);
        	array.add(device);
        }	*/
		Optional<Device> deviceOptional = deviceRepository.findById(device_id);
		if(deviceOptional.isPresent()) {
			Device device = deviceOptional.get();
			 Optional<Product> productOptional = productRepository.findById(device.getProduct_id());
		        if(productOptional.isPresent()) {
		        	Optional<User> userOptional = userRepository.findById(productOptional.get().getUserId());
		        	if(userOptional.isPresent()) {
		        		String key = userOptional.get().getDefaultKey();
		        		if(key.equals(api_key)) {
		        			List<DeviceDatastream> ddList = deviceDatastreamRepository.findByDeviceId(device_id);
	        				return RESCODE.SUCCESS.getJSONRES(ddList);
		        		}else {
		        			return RESCODE.API_KEY_ERROR.getJSONRES();
		        		}
		        	}else {
		        		return RESCODE.USER_ID_NOT_EXIST.getJSONRES();
		        	}
		        }else {
		        	return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
		        }
		}else {
			return RESCODE.DEVICE_SN_NOT_EXIST.getJSONRES();
		}
       
	}
	
	/**
	 * 对外接口
	 * 获取设备数据流数据点
	 * @param id
	 * @param name
	 * @param start
	 * @param end
	 * @param api_key
	 * @return
	 */
	public JSONObject getDeviceDatastreamData(Long id,String name,Date start, Date end ,String api_key) {
		Optional<Device> deviceOptional = deviceRepository.findById(id);
		if(deviceOptional.isPresent()) {
			Device device = deviceOptional.get();
			Optional<Product> productOptional = productRepository.findById(device.getProduct_id());
	        if(productOptional.isPresent()) {
	        	Optional<User> userOptional = userRepository.findById(productOptional.get().getUserId());
	        	if(userOptional.isPresent()) {
	        		String key = userOptional.get().getDefaultKey();
	        		if(key.equals(api_key)) {
	        			Optional<DeviceDatastream> optional = deviceDatastreamRepository.findByDeviceIdAndDm_name(id, name);
        				if(optional.isPresent()) {
        					long dd_id = optional.get().getId();
        					return getDeviceDsData(dd_id, start, end);        					
        				}else {
        					return RESCODE.DEVICE_DATASTREAM_NOT_EXIST.getJSONRES();
        				}
	        		}else {
	        			return RESCODE.API_KEY_ERROR.getJSONRES();
	        		}
	        	}else {
	        		return RESCODE.USER_ID_NOT_EXIST.getJSONRES();
	        	}
	        }else {
	        	return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
	        }
		}else {
			return RESCODE.DEVICE_SN_NOT_EXIST.getJSONRES();
		}
        
	}
	
	public void test_repo_add_device(Long product_id) {
		Device device = new Device();
		device.setProduct_id(product_id);
		device.setDevice_sn("84643681");
		device.setName("test_repo_add_device");
		device.setCreate_time(new Date());
		deviceRepository.save(device);
	}
	
	public void test_data_history() {
		Data_history dataHistory = new Data_history();
		dataHistory.setId(System.currentTimeMillis());
		dataHistory.setDd_id(1L);
		dataHistory.setName("test");
		dataHistory.setValue((double)1);
		dataHistory.setCreate_time(new Date());
		dataHistoryRepository.save(dataHistory);
	}
	
	public void exportDevice(Long product_id,HttpServletRequest request, HttpServletResponse response){
		List<Device> devices = deviceRepository.findByProductId(product_id);
		JSONArray array = new JSONArray();
		for (Device device:
			 devices) {
			JSONObject object = new JSONObject();
			object.put("id",device.getId());
			object.put("device_sn",device.getDevice_sn());
			object.put("name",device.getName());
			Optional<Product> productOptional = productRepository.findById(device.getProduct_id());
			if (productOptional.isPresent()){
				object.put("protocol",productService.getProtocol(productOptional.get().getProtocolId()));
			}else{
				object.put("protocol","无");
			}
			object.put("create_time",device.getCreate_time());
			array.add(object);
		}
		ExcelUtils.exportDevice(array,request,response);
	}
	
	public Set<Long> getRelatedApp(Long device_id) {
		List<DeviceDatastream> dds = deviceDatastreamRepository.findByDeviceId(device_id);
		Set<Long> appIds = new HashSet<>();
		for(DeviceDatastream dd : dds) {
			List<ApplicationChartDatastream> acds = applicationChartDatastreamRepository.findByDd_id(dd.getId());
			for(ApplicationChartDatastream acd:acds) {
				Optional<ApplicationChart> acOptional = applicationChartRepository.findById(acd.getAcId());
				if(acOptional.isPresent()) {
					appIds.add(acOptional.get().getApplicationId());
				}
			}
			
			/*List<ApplicationAnalysisDatastream> aads = applicationAnalysisDatastreamRepository.findByDd_id(dd.getId());
			for(ApplicationAnalysisDatastream aad:aads) {
				Optional<ApplicationAnalysis> aaOptional = applicationAnalysisRepository.findById(aad.getAaId());
				if(aaOptional.isPresent()) {
					appIds.add(aaOptional.get().getApplicationId());
				}
			}*/
		}
		return appIds;
	}

	public JSONObject autoAdd(String registration_code,String device_sn,String api_key){
		List<Product> products = productRepository.findByRegistrationCode(registration_code);
		if (products.size()==1){
			Product p = products.get(0);
			Optional<User> userOptional = userRepository.findById(p.getUserId());
			if (userOptional.isPresent()&&userOptional.get().getDefaultKey().equals(api_key)){

				Boolean canBeUsed = checkDevicesn(device_sn,p.getId());
				if (canBeUsed){
					Device device = new Device();
					device.setId(System.currentTimeMillis());
					device.setName("设备"+device_sn);
					device.setDevice_sn(device_sn);
					device.setCreate_time(new Date());
					device.setProtocolId(p.getProtocolId());
					device.setProduct_id(p.getId());
					Device result = deviceRepository.save(device);
					JSONObject device_id = new JSONObject();
					device_id.put("device_id",result.getId());
					return RESCODE.SUCCESS.getJSONRES(device_id);
				}else{
					String msg = "设备鉴权信息重复";
					return RESCODE.FAILURE.getJSONRES(msg);
				}
			}else{
				String msg = "用户鉴权失败";
				return RESCODE.FAILURE.getJSONRES(msg);
			}
		}else{
			return RESCODE.FAILURE.getJSONRES();
		}
	}
}

