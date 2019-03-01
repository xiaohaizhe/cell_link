package com.hydata.intelligence.platform.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;

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
import com.hydata.intelligence.platform.dto.Data_history;
import com.hydata.intelligence.platform.dto.DatastreamModel;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.DeviceDatastream;
import com.hydata.intelligence.platform.dto.OperationLogs;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.dto.UnitType;
import com.hydata.intelligence.platform.model.DataStreamModel;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.DataHistoryRepository;
import com.hydata.intelligence.platform.repositories.DatastreamModelRepository;
import com.hydata.intelligence.platform.repositories.DeviceDatastreamRepository;
import com.hydata.intelligence.platform.repositories.DeviceRepository;
import com.hydata.intelligence.platform.repositories.OperationLogsRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.repositories.UnitTypeRepository;
import com.hydata.intelligence.platform.utils.MongoDBUtils;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:50:28
 */
@Transactional
@Service
public class DataStreamModelService {
	@Autowired
	private DatastreamModelRepository datastreamModelRepository;
	
	@Autowired
	private UnitTypeRepository unitTypeRepository;
	
	@Autowired
	private UnitTypeService unit_type_Service;
	
	@Autowired
	private OperationLogsRepository operationLogsRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private DeviceDatastreamRepository datastreamRepository;
	
	@Autowired
	private DeviceRepository deviceRepository;
	
	@Autowired
	private DataHistoryRepository dataHistoryRepository;
	
	private static MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();
	/*private static MongoClient meiyaClient = mongoDBUtil.getMongoConnect();
	private static MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");
	*/
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static Logger logger = LogManager.getLogger(DataStreamModelService.class);
	
	/**
	 * 添加数据流模板
	 * @param product_id
	 * @param dsm_name
	 * @param unit_name
	 * @param unit_symbol
	 * @return
	 */
	public JSONObject addDataStreamModel(DataStreamModel dsModel){
		Optional<Product> productOptional =  productRepository.findById(dsModel.getProduct_id());
		if(productOptional.isPresent()) {
			OperationLogs logs = new OperationLogs();
			logs.setUserId(productOptional.get().getUserId());
			logs.setOperationTypeId(7);
			String msg = "添加数据流模板："+dsModel.getName();			
			logs.setCreateTime(new Date());
			
			
			logger.debug("进入添加设备数据流模板");	
			JSONObject checkResult = checkModel(dsModel);
			logger.debug(checkResult.toString());
			if((Integer)checkResult.get("code") == 0) {
				UnitType unitTypeNew = new UnitType();
				UnitType unitTypeReturn = new UnitType();
				unitTypeReturn.setId(0);
				unitTypeNew.setName(dsModel.getUnit_name());
				unitTypeNew.setSymbol(dsModel.getUnit_symbol());			
				JSONObject result = unit_type_Service.add(unitTypeNew);
				if((Integer)result.get("code")==0) {
					unitTypeReturn = (UnitType)result.get("data");
				}		
				DatastreamModel datastreamModel = new DatastreamModel();
				datastreamModel.setName(dsModel.getName());
				datastreamModel.setProductId(dsModel.getProduct_id());
				datastreamModel.setUnitTypeId(unitTypeReturn.getId());
				datastreamModel.setCreateTime(new Date());
				DatastreamModel result2 = datastreamModelRepository.save(datastreamModel);		
				logger.debug("添加设备数据流模板结束");
				if(result2!=null) {
					logger.debug("添加设备数据流模板结果："+result2.toString());
					logs.setMsg(msg+"成功");
					operationLogsRepository.save(logs);
					return RESCODE.SUCCESS.getJSONRES();
				}
			}
			logs.setMsg(msg+"失败");
			operationLogsRepository.save(logs);
			return RESCODE.DSM_REPEAT.getJSONRES();
		}
		return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();		
	}
	/**
	 * 通过id删除数据流模板
	 * @param id
	 * @return
	 */
	public JSONObject deleteByDSMId(long id){
		/**
		 *  1.数据流触发器删除（未加）
		 *  2.设备数据流删除（未加）
		 *  3.数据流模板删除
		 */
		Optional<DatastreamModel> datastreamModel = datastreamModelRepository.findById(id);
		if(datastreamModel.isPresent()) {
			datastreamModelRepository.deleteById(id);
			Optional<Product> productOptional =  productRepository.findById(datastreamModel.get().getProductId());
			if(productOptional.isPresent()) {
				OperationLogs logs = new OperationLogs();
				logs.setUserId(productOptional.get().getUserId());
				logs.setOperationTypeId(7);
				logs.setMsg("删除数据流模板："+datastreamModel.get().getName());
				logs.setCreateTime(new Date());
				operationLogsRepository.save(logs);
			}			
			return RESCODE.SUCCESS.getJSONRES();
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();		
	}
	
	public JSONObject modifyDSM(DataStreamModel dsModel){
		logger.debug("进入修改数据流模板，模板id:"+dsModel.getId());
		JSONObject checkResult = checkModel(dsModel);
		if((Integer)checkResult.get("code") == 0) {
			UnitType unitTypeNew = new UnitType();
			UnitType unitTypeReturn = new UnitType();
			unitTypeReturn.setId(0);
			unitTypeNew.setName(dsModel.getUnit_name());
			unitTypeNew.setSymbol(dsModel.getUnit_symbol());
			JSONObject result = unit_type_Service.add(unitTypeNew);
			if((Integer)result.get("code")==0) {
				unitTypeReturn = (UnitType)result.get("data");
			}
			Optional<DatastreamModel> dsm_optional = datastreamModelRepository.findById(dsModel.getId());
			if(dsm_optional.isPresent()) {
				DatastreamModel dsm = dsm_optional.get();
				dsm.setName(dsModel.getName());
				dsm.setUnitTypeId(unitTypeReturn.getId());
				DatastreamModel dsmResult = datastreamModelRepository.save(dsm);
				
				Optional<Product> productOptional =  productRepository.findById(dsm.getProductId());
				if(productOptional.isPresent()) {
					OperationLogs logs = new OperationLogs();
					logs.setUserId(productOptional.get().getUserId());
					logs.setOperationTypeId(7);
					logs.setMsg("修改数据流模板："+dsModel.getName());
					logs.setCreateTime(new Date());
					operationLogsRepository.save(logs);
				}
				
				return RESCODE.SUCCESS.getJSONRES(dsmResult);
			}
			return RESCODE.ID_NOT_EXIST.getJSONRES();
		}
		return RESCODE.DSM_REPEAT.getJSONRES();
		
	}
	/**
	 * 检查数据流模板
	 * @param dsModel
	 * @return
	 */
	public JSONObject checkModel(DataStreamModel dsModel){
		logger.debug("检查数据流模板："+dsModel.getName()+dsModel.getUnit_name()+dsModel.getUnit_symbol()+"是否重复");
		List<DatastreamModel> dsm = datastreamModelRepository.findByProductIdAndName(dsModel.getProduct_id(),dsModel.getName());
		boolean isRepeat = false;
		if(dsm!=null) {
			logger.debug("产品："+dsModel.getProduct_id()+"下数据流模板名称:"+dsModel.getName()+"存在");
			logger.debug("开始检查单位名称符号是否一致");
			logger.debug("进入循环检查");			
			for(DatastreamModel datastreamModel:dsm) {				
				logger.debug(datastreamModel.getUnitTypeId());
				Optional<UnitType> ut = unitTypeRepository.findById(datastreamModel.getUnitTypeId());
				logger.debug(ut.isPresent());
				if(ut.isPresent()) {
					UnitType unitType = ut.get();
					logger.debug(unitType.toString());
					logger.debug("unitType.getName()"+unitType.getName());
					logger.debug("dsModel.getName()"+dsModel.getUnit_name());
					logger.debug(unitType.getName().equals(dsModel.getUnit_name()));
					logger.debug("unitType.getSymbol()"+unitType.getSymbol());
					logger.debug("dsModel.getUnit_symbol()"+dsModel.getUnit_symbol());
					logger.debug(unitType.getSymbol().equals(dsModel.getUnit_symbol()));
					if(unitType.getName().equals(dsModel.getUnit_name()) && 
							unitType.getSymbol().equals(dsModel.getUnit_symbol())) {
						logger.debug("产品："+dsModel.getProduct_id()+"下数据流模板名称:"+dsModel.getName()+"存在，且单位名称符号一致");
						isRepeat = true;
						break;
					}
				}
			}			
		}	
		logger.debug("数据流是否重复："+isRepeat);
		if(isRepeat) {
			return RESCODE.DSM_REPEAT.getJSONRES();
		}
		return RESCODE.SUCCESS.getJSONRES();		
	}
	
	@SuppressWarnings("deprecation")
	public Page<DatastreamModel> queryByProductId(Long productId,Integer page,Integer number ) {
		Pageable pageable = new PageRequest(page, number, Sort.Direction.DESC,"id");
		return datastreamModelRepository.queryByProductId(productId, pageable);
	}
	
	public Page<DatastreamModel> findByName(Integer page,Integer number,String dsmName,Long productId){
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page, number, Sort.Direction.DESC,"id");
		Page<DatastreamModel> result = datastreamModelRepository.findAll(new Specification<DatastreamModel>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<DatastreamModel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				 List<Predicate> predicateList = new ArrayList<>();

				 if (productId != null && productId >= 0) {
	                    predicateList.add(
	                            criteriaBuilder.equal(
	                                    root.get("productId").as(Integer.class),
	                                    productId));
	             }
				 if(dsmName!=null && !dsmName.equals("")) {
					
						 predicateList.add(
									//like：模糊匹配，跟SQL是一样的
			                            criteriaBuilder.like(
			                                    //user表里面有个String类型的name
			                                    root.get("name").as(String.class),
			                                    //映射规则
			                                    "%" + dsmName + "%"));				 				
				 }
				 Predicate[] predicates = new Predicate[predicateList.size()];
	             return criteriaBuilder.and(predicateList.toArray(predicates));
			}
		}, pageable);
		return result;
	}
	
	public JSONObject getIncrement(Long product_id, Date start, Date end) throws ParseException {
		/*MongoClient meiyaClient = mongoDBUtil.getMongoConnect(mongoDB.getHost(),mongoDB.getPort());
		MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");
		JSONObject jsonObject = new JSONObject();	
		BasicDBObject query = new BasicDBObject(); 
		query.put("product_id", product_id);
		query.put("create_time",BasicDBObjectBuilder.start("$gte", start).add("$lte", end).get());//key为表字段名
		FindIterable<Document> documents1 = collection.find(query);*/
		List<Device> devices = deviceRepository.findByProductId(product_id);
		List<Data_history> dataHistories = new ArrayList<>();
		
		logger.info(sdf.format(start));
		logger.info(sdf1.parse(sdf1.format(start)).getTime());
		logger.info(sdf.format(new Date()));
		logger.info(new Date().getTime());
		logger.info(sdf.format(end));
		logger.info(end.getTime());
		JSONObject statistics = new JSONObject();
		int len = 0;
		if(end.getTime()>new Date().getTime()) {
			logger.debug("结束时间比当前时间晚");
			len =  new Long((new Date().getTime()-sdf1.parse(sdf1.format(start)).getTime())/1000/60/60/24).intValue();
			logger.debug(len);
		}else {
			logger.debug("结束时间早于当前时间");
			len = new Long((sdf1.parse(sdf1.format(end)).getTime()-sdf1.parse(sdf1.format(start)).getTime())/1000/60/60/24).intValue();
		}		
		logger.debug("共需循环"+(len+1)+"次");
		try {
			Date temp = sdf.parse(sdf.format(start));
			for(int i=0;i<=len;i++) {
				logger.debug("第"+(i+1)+"次");
				statistics.put(sdf1.format(temp), 0);
				temp.setDate(temp.getDate()+1);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info(statistics);
		for (Device device: devices) {
			String device_sn = device.getDevice_sn();
			List<DeviceDatastream> datastreams = datastreamRepository.findByDeviceSn(device_sn);			
			for(DeviceDatastream dd:datastreams) {
				/*BasicDBObject query1 = new BasicDBObject(); 
				query1.put("dd_id", dd.getId());
				query1.put("create_time",BasicDBObjectBuilder.start("$gte", start).add("$lte", end).get());//key为表字段名
				FindIterable<Document> documents2 = collection.find(query1);*/
				List<Data_history> dataHistortList = dataHistoryRepository.findByDd_idAndCreate_timeBetween(dd.getId(), start, end);
				for(Data_history dataHistory : dataHistortList) {					
					dataHistories.add(dataHistory);
				}
			}			
	    }
		for(Data_history dh:dataHistories){
			String d = sdf1.format(dh.getCreate_time());
			if(statistics.get(d) != null) {
				statistics.put(d, (Integer)statistics.get(d)+1);
			}else {
				statistics.put(d, 1);
			}
		}
		JSONArray array = new JSONArray();
		for (Entry<String, Object> entry : statistics.entrySet()) {
			JSONObject sum = new JSONObject();
			sum.put("time", entry.getKey());
			sum.put("value", entry.getValue());
			array.add(sum);
		}
		
		return RESCODE.SUCCESS.getJSONRES(array);
	}
}

