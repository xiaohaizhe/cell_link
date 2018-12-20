package com.hydata.intelligence.platform.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.python.antlr.PythonParser.list_for_return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.DatastreamModel;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.DeviceDatastream;
import com.hydata.intelligence.platform.dto.OperationLogs;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.dto.UnitType;
import com.hydata.intelligence.platform.model.DataHistory;
import com.hydata.intelligence.platform.model.DataStreamModel;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.DatastreamModelRepository;
import com.hydata.intelligence.platform.repositories.DeviceDatastreamRepository;
import com.hydata.intelligence.platform.repositories.OperationLogsRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.repositories.UnitTypeRepository;
import com.hydata.intelligence.platform.utils.MongoDBUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

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
	
	private static MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();
	private static MongoClient meiyaClient = mongoDBUtil.getMongoConnect("127.0.0.1",27017);
	private static MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");
	
	
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
			logs.setMsg("添加数据流模板："+dsModel.getName());
			logs.setCreateTime(new Date());
			operationLogsRepository.save(logs);
		}
		
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
				return RESCODE.SUCCESS.getJSONRES();
			}
		}	
		return RESCODE.DSM_REPEAT.getJSONRES();
	}
	/**
	 * 通过id删除数据流模板
	 * @param id
	 * @return
	 */
	public JSONObject deleteByDSMId(Integer id){
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
	public Page<DatastreamModel> queryByProductId(Integer productId,Integer page,Integer number ) {
		Pageable pageable = new PageRequest(page, number, Sort.Direction.DESC,"id");
		return datastreamModelRepository.queryByProductId(productId, pageable);
	}
	
	public Page<DatastreamModel> findByName(Integer page,Integer number,String dsmName,Integer productId){
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
	
	public JSONObject getIncrement(Integer product_id, Date start, Date end) {
		JSONObject jsonObject = new JSONObject();	
		BasicDBObject query = new BasicDBObject(); 
		query.put("product_id", product_id);
		query.put("create_time",BasicDBObjectBuilder.start("$gte", start).add("$lte", end).get());//key为表字段名
		FindIterable<Document> documents1 = collection.find(query);
		List<Device> devices = new ArrayList<>();
		List<DataHistory> dataHistories = new ArrayList<>();
		for (Document d : documents1) {
			String device_sn = d.getString("device_sn");
			List<DeviceDatastream> datastreams = datastreamRepository.findByDeviceSn(device_sn);			
			for(DeviceDatastream dd:datastreams) {
				BasicDBObject query1 = new BasicDBObject(); 
				query1.put("dd_id", dd.getId());
				query1.put("create_time",BasicDBObjectBuilder.start("$gte", start).add("$lte", end).get());//key为表字段名
				FindIterable<Document> documents2 = collection.find(query1);
				for(Document dt : documents2) {
					DataHistory dataHistory = new DataHistory();
					dataHistory.setDd_id(dd.getId());
					dataHistory.setName(dt.getString("name"));
					dataHistory.setValue(dt.getDouble("value"));
					dataHistory.setDate(dt.getDate("date"));
					dataHistories.add(dataHistory);
				}
			}			
	    }
		return null;
	}
}

