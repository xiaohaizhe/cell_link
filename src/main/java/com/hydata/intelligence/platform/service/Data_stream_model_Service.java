package com.hydata.intelligence.platform.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hydata.intelligence.platform.dto.DatastreamModel;
import com.hydata.intelligence.platform.dto.UnitType;
import com.hydata.intelligence.platform.model.Data_stream_model;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.DatastreamModelRepository;
import com.hydata.intelligence.platform.repositories.UnitTypeRepository;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:50:28
 */
@Transactional
@Service
public class Data_stream_model_Service {
	@Autowired
	private DatastreamModelRepository datastreamModelRepository;
	
	@Autowired
	private UnitTypeRepository unitTypeRepository;
	
	@Autowired
	private Unit_type_Service unit_type_Service;
	
	private static Logger logger = LogManager.getLogger(Data_stream_model_Service.class);
	
	/**
	 * 添加数据流模板
	 * @param product_id
	 * @param dsm_name
	 * @param unit_name
	 * @param unit_symbol
	 * @return
	 */
	public Map<String, Object> addData_stream_model(Data_stream_model dsModel){
		logger.debug("进入添加设备数据流模板");	
		Map<String, Object> checkResult = checkModel(dsModel);
		logger.debug(checkResult.toString());
		if((Integer)checkResult.get("code") == 0) {
			UnitType unitTypeNew = new UnitType();
			UnitType unitTypeReturn = new UnitType();
			unitTypeReturn.setId(0);
			unitTypeNew.setName(dsModel.getUnit_name());
			unitTypeNew.setSymbol(dsModel.getUnit_symbol());			
			Map<String, Object> result = unit_type_Service.add(unitTypeNew);
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
	public Map<String, Object> deleteByDSM_id(Integer id){
		/**
		 *  1.数据流触发器删除（未加）
		 *  2.设备数据流删除（未加）
		 *  3.数据流模板删除
		 */
		Optional<DatastreamModel> datastreamModel = datastreamModelRepository.findById(id);
		if(datastreamModel.isPresent()) {
			datastreamModelRepository.deleteById(id);
			return RESCODE.SUCCESS.getJSONRES();
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();		
	}
	
	public Map<String, Object> modifyDSM(Data_stream_model dsModel){
		logger.debug("进入修改数据流模板，模板id:"+dsModel.getId());
		Map<String, Object> checkResult = checkModel(dsModel);
		if((Integer)checkResult.get("code") == 0) {
			UnitType unitTypeNew = new UnitType();
			UnitType unitTypeReturn = new UnitType();
			unitTypeReturn.setId(0);
			unitTypeNew.setName(dsModel.getUnit_name());
			unitTypeNew.setSymbol(dsModel.getUnit_symbol());
			Map<String, Object> result = unit_type_Service.add(unitTypeNew);
			if((Integer)result.get("code")==0) {
				unitTypeReturn = (UnitType)result.get("data");
			}
			Optional<DatastreamModel> dsm_optional = datastreamModelRepository.findById(dsModel.getId());
			if(dsm_optional.isPresent()) {
				DatastreamModel dsm = dsm_optional.get();
				dsm.setName(dsModel.getName());
				dsm.setUnitTypeId(unitTypeReturn.getId());
				DatastreamModel dsmResult = datastreamModelRepository.save(dsm);
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
	public Map<String, Object> checkModel(Data_stream_model dsModel){
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
	
	public Map<String, Object> queryDSM(Integer productId,Integer page,Integer number){
		
		return null;
	}
	@SuppressWarnings("deprecation")
	public Page<DatastreamModel> queryByProductId(Integer productId,Integer page,Integer number ) {
		Pageable pageable = new PageRequest(page, number, Sort.Direction.DESC,"id");
		return datastreamModelRepository.queryByProductId(productId, pageable);
	}
}

