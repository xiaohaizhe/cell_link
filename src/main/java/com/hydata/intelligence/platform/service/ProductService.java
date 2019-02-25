package com.hydata.intelligence.platform.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.python.jline.internal.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
	
	@Value("${geoKey}")
	private String geoKey;
	
	@Value("${geo.url}")
	private String geo_url;
	
	@Value("${regeo.url}")
	private String regeo_url;
	
	
	
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
		String msg = "添加产品："+product.getName();
		
		logs.setCreateTime(new Date());
		
		Optional<User> userOptional = userRepository.findById(product.getUserId());
		if(userOptional.isPresent()) {
			JSONObject result = checkProductName(product.getUserId(),product.getName());
			if((Integer)result.get("code")==2) {//产品名不存在				
				//产品类型字段弃用
				product.setProductTypeId(0);
				product.setRegistrationCode(getRegistrationCode());
				product.setCreateTime(new Date());
				Product productReturn = productRepository.save(product);
				if(productReturn!=null) {
					msg += "成功";
					logs.setMsg(msg);
					operationLogsRepository.save(logs);
					return RESCODE.SUCCESS.getJSONRES(productReturn);
				}
				msg += "失败";
				logs.setMsg(msg);
				operationLogsRepository.save(logs);
				return RESCODE.FAILURE.getJSONRES();
			}
			msg += "失败";
			logs.setMsg(msg);
			operationLogsRepository.save(logs);
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
		String msg = "修改产品："+product.getName();		
		logs.setCreateTime(new Date());
		
		//1.检查产品id是否存在
		Optional<Product> productOptional = productRepository.findById(product.getId());
		if(productOptional.isPresent()) {		
			Product productReturn = productOptional.get();
			if(productReturn.getName().equals(product.getName())==false) {
				JSONObject result = checkProductName(product.getUserId(),product.getName());
				if((Integer)result.get("code")==2) {//用户下产品名
					productReturn.setName(product.getName());
				}else {
					msg += "失败";
					logs.setMsg(msg);
					operationLogsRepository.save(logs);
					return RESCODE.PRODUCT_NAME_EXIST.getJSONRES();
				}				
			}		
			productReturn.setDescription(product.getDescription());
			productReturn.setLatitude(product.getLatitude());
			productReturn.setLontitude(product.getLontitude());
			productReturn.setCityCode(product.getCityCode());
			msg += "成功";
			logs.setMsg(msg);
			operationLogsRepository.save(logs);
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
	public JSONObject checkProductName(long user_id,String name){
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
	public JSONObject queryByUserId(long user_id,String name,Integer page,Integer number,Integer sort){
		logger.debug("获取用户下产品列表");
		Optional<User> optional = userRepository.findById(user_id);
		Pageable pageable;
		if(sort==-1) {
			//逆序
			pageable = new PageRequest(page, number, Sort.Direction.DESC,"id");
		}else {
			//顺序
			pageable = new PageRequest(page, number, Sort.Direction.ASC,"id");
		}
		if(optional.isPresent()) {		
			Page<Product> result= productRepository.queryByUserId(user_id,name==null?"":name, pageable);			
			logger.info(result.getContent());
			return RESCODE.SUCCESS.getJSONRES(result.getContent(),result.getTotalPages(),result.getTotalElements());
		}else if(user_id==0){
			Page<Product> result= productRepository.queryByName(name==null?"":name, pageable);
			logger.info(result.getContent());
			return RESCODE.SUCCESS.getJSONRES(result.getContent(),result.getTotalPages(),result.getTotalElements());
		}else {
			return RESCODE.USER_ID_NOT_EXIST.getJSONRES();
		}
		
	}

	public JSONObject deleteByUserId(Long user_id){
		List<Product> products= productRepository.findByUserId(user_id);
		JSONObject returnResult = new JSONObject();
		JSONArray failure = new JSONArray();
		int sum = products.size();
		int success = 0;
		for(Product product:products){
			JSONObject result = delete(product.getId());
			if ((Integer) result.get("code")==0){
				success++;
			}else{
				failure.add(product.getId());
			}
		}
		returnResult.put("Failed product ids",failure);
		returnResult.put("Total number of successfully deleted products",sum);
		return RESCODE.SUCCESS.getJSONRES(returnResult);
	}


	/**
	 * 删除产品
	 * @param product_ids
	 * @return
	 */
	public JSONObject deleteByProductIds(Long[] product_ids){
		logger.debug("进入产品删除");
		JSONObject returnResult = new JSONObject();
		JSONArray failure = new JSONArray();
		int success = 0;
		for(Long product_id:product_ids){
			JSONObject result = delete(product_id);
			if ((Integer) result.get("code")==0){
				success++;
			}else{
				failure.add(product_id);
			}
		}
		returnResult.put("Failed product ids",failure);
		returnResult.put("Total number of successfully deleted products",success);
		return RESCODE.SUCCESS.getJSONRES(returnResult);
	}
	/**
	 * 删除产品
	 * 包括：
	 * 设备、数据流模板、触发器、应用、、、、、
	 * @param product_id
	 * @return
	 */
	public JSONObject delete(Long product_id){
		logger.debug("进入产品删除");
		Optional<Product> optional = productRepository.findById(product_id);
		if(optional.isPresent()) {
			OperationLogs logs = new OperationLogs();
			logs.setUserId(optional.get().getUserId());
			logs.setOperationTypeId(5);
			logs.setMsg("删除产品:"+product_id);
			logs.setCreateTime(new Date());
			operationLogsRepository.save(logs);
			//1.查找产品下设备，删除设备
			JSONObject devices_object = deviceService.getByProductId(product_id);
			logger.debug(devices_object);
			List<Device> device_array =  (List<Device>) devices_object.get("data");
			for(int i = 0 ; i < device_array.size() ; i++) {
				Device device = device_array.get(i);
				deviceService.deleteDevice(device.getDevice_sn());
			}
			//2.查找产品下应用，删除应用
			applicationService.deleteByProductId(product_id);			
			//3.查找产品下触发器，删除触发器
			//2.删除产品
			productRepository.deleteById(product_id);

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
	public JSONObject getDetail(long product_id) {
		JSONObject jsonObject = new JSONObject();
		Optional<Product> productOptional =  productRepository.findById(product_id);
		if(productOptional.isPresent()) {
			jsonObject.put("product", productOptional.get());
			logger.debug(productOptional.get().toString());
			JSONObject object =  deviceService.getByProductId(product_id);
			List<Device> devices =  (List<Device>) object.get("data");
			jsonObject.put("device_sum", devices.size());
			long datastream_sum = 0;
			for(int i = 0;i<devices.size();i++) {
				Device device = devices.get(i);
				List<DeviceDatastream> datastreams = datastreamRepository.findByDeviceSn(device.getDevice_sn());
				datastream_sum += datastreams.size();
			}
			jsonObject.put("datastream_sum", datastream_sum);
			return RESCODE.SUCCESS.getJSONRES(jsonObject);	
		}else {
			return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
		}
			
	} 
	/**
	 * 获取首页热力图
	 * @return
	 */
	public JSONObject getHeatmap() {
		logger.info("进入获取首页热力图");
		List<Product> products =productRepository.findAll();
		List<Map<String, Double>> locations = new ArrayList<>();
		for(Product product :products) {
			Map<String, Double> location = new HashMap<>();
			if(product.getLontitude()==null||product.getLatitude()==null) {
				continue;
			}
			double lon = product.getLontitude();
			double lat = product.getLatitude();
			location.put("lon", lon);
			location.put("lat", lat);
			locations.add(location);
		}
		logger.info("地理位置list:"+locations);
		JSONObject result = getGeoName(locations);
		System.out.println(result);
		
		return RESCODE.SUCCESS.getJSONRES(result.get("data"));
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
	public JSONObject getProductOverview(Long productId) {
		logger.debug("进入获取产品概括数据信息");
		JSONObject jsonObject = new JSONObject();	
		JSONObject object = deviceService.getByProductId(productId);
		List<Device> devices =  (List<Device>) object.get("data");
		if(devices!=null&&devices.size()>0) {
			jsonObject.put("device_sum", devices.size());
			long ddsum = 0;
			for(int i = 0;i<devices.size();i++) {
				Device device = devices.get(i);
				if(device.getStatus()!=null&&device.getStatus()==1) {
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
	
	public JSONObject getGeoName(List<Map<String, Double>> locations) {
		if(locations.size()==0) {
			return RESCODE.PARAM_NULL.getJSONRES();
		}
		JSONObject returnResult = new JSONObject();
		JSONObject locWeight = new JSONObject();
		JSONObject lonAndLat = new JSONObject();
		RestTemplate restTemplate = new RestTemplate();
		
		
		//需要处理的位置信息量，即总产品数
		//geo每次只能批量处理20个一组的位置信息
		int sum = locations.size();
		//处理批次数
		int times = sum%20==0?sum/20:sum/20+1;
		
		for(int i=0;i<times;i++) {
			String url = regeo_url;
			url += "?key="+geoKey;
			//第一层,为批次数
			logger.info("第"+(i+1)+"批次位置信息处理");
			String loc = "";
			for(int j=i*20;j<sum&&j<20+i*20;j++) {
				//第二层,为位置信息index
				Map<String, Double> location = locations.get(j);
				double lon = location.get("lon");
				double lan = location.get("lat");
				loc += lon +","+lan+"|";
			}
			logger.info(loc);
			url +="&location="+(loc.length()>0?loc.substring(0, loc.length()-2):loc);
			url +="&batch=true";
			logger.info(url);
			JSONObject response = restTemplate.getForObject(url,JSONObject.class);
			logger.info("逆地理编码返回值"+response);
			if(Integer.parseInt((String) response.get("status"))==1) {
				logger.info("数据返回成功");
				ArrayList<Object> regeocodes =  (ArrayList<Object>) response.get("regeocodes");
				for(Object regeocode :regeocodes) {
					String locName = "";
					HashMap<String, Object> result = (HashMap<String, Object>) regeocode;
					HashMap<String, Object> addressComponent =  (HashMap<String, Object>) result.get("addressComponent");
					
					if(addressComponent.get("city").getClass()==String.class&&
							addressComponent.get("city").equals("")==false) {
						logger.info(addressComponent.get("city"));
						locName = (String) addressComponent.get("city");
						
					}else if(addressComponent.get("province").getClass()==String.class&&
							addressComponent.get("province").equals("")==false){
						logger.info(addressComponent.get("province"));
						locName = (String) addressComponent.get("province");
					}else {
						continue;
					}
					
					if(locWeight.get(locName)!=null) {
						int value = (int) locWeight.get(locName);
						locWeight.put(locName, value+1);
					}else {
						locWeight.put(locName, 1);
					}
					
					if(lonAndLat.get(locName)==null&&getLonAndLat(locName)!=null) {
						lonAndLat.put(locName, getLonAndLat(locName));
					}										
				}
			}
		}	
		/*Set locWeightSet = locWeight.entrySet();*/
		JSONArray locationWeight = new JSONArray();
		for (Entry<String, Object> entry : locWeight.entrySet()) {
			JSONObject weight = new JSONObject();
			weight.put("name", entry.getKey());
			weight.put("value", entry.getValue());
			locationWeight.add(weight);
		}
		returnResult.put("lonAndLat", lonAndLat);
		returnResult.put("locWeight", locationWeight);
		return RESCODE.SUCCESS.getJSONRES(returnResult);
	}
	
	public JSONArray getLonAndLat(String locName) {
		RestTemplate restTemplate = new RestTemplate();
		String url = geo_url;
		url += "?key="+geoKey;
		url += "&address="+locName;
		JSONObject response = restTemplate.getForObject(url,JSONObject.class);
		Log.info(response);
		if(Integer.parseInt((String) response.get("status"))==1) {
			JSONArray loca = new JSONArray();
			ArrayList<Object> geocodes =  (ArrayList<Object>) response.get("geocodes");
			for(Object geocode:geocodes) {
				HashMap<String, Object> result = (HashMap<String, Object>) geocode;
				String location = (String) result.get("location");
				String[] loc = location.split(",");				
				for(int i=0;i<loc.length;i++) {
					loca.add(Double.parseDouble(loc[i]));
				}				
			}
			return loca;
		}
		return null;
	}
	
	public String getRegistrationCode() {
		String registrationCode = String.valueOf(UUID.randomUUID()) ;
		if(productRepository.findByRegistrationCode(registrationCode).size()>0) {
			return getRegistrationCode();
		}
		return registrationCode;
	}

}

