package com.hydata.intelligence.platform.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import com.hydata.intelligence.platform.dto.DatastreamModel;
import com.hydata.intelligence.platform.dto.UnitType;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.repositories.DatastreamModelRepository;
import com.hydata.intelligence.repositories.UnitTypeRepository;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:50:28
 */
@EnableAutoConfiguration
@Transactional
public class Data_stream_model_Service {
	@Autowired
	private DatastreamModelRepository datastreamModelRepository;
	
	@Autowired
	private UnitTypeRepository unitTypeRepository;
	
	public Map<String, Object> addData_stream_model(int product_id,String dsm_name,String unit_name,String unit_symbol){
		List<UnitType> unit_typeList = unitTypeRepository.findByNameAndSymbol(unit_name, unit_symbol);
		int unit_type_id=0;
		//单位类型存在,直接返回单位类型id
		//单位类型不存在,创建并返回单位类型id
		if(unit_typeList!=null&&unit_typeList.size()>0) {
			unit_type_id = unit_typeList.get(0).getId();			
		}else {
			UnitType unitType = new UnitType();
			unitType.setName(unit_name);
			unitType.setSymbol(unit_symbol);
			UnitType result1 = unitTypeRepository.save(unitType);
			unit_type_id = result1.getId();
		}
		DatastreamModel datastreamModel = new DatastreamModel();
		datastreamModel.setName(dsm_name);
		datastreamModel.setProductId(product_id);
		datastreamModel.setUnitTypeId(unit_type_id);
		datastreamModel.setCreateTime(new Date());
		DatastreamModel result2 = datastreamModelRepository.save(datastreamModel);
		
		if(result2!=null) {
			return RESCODE.SUCCESS.getJSONRES();
		}
		return RESCODE.FAILURE.getJSONRES();
	}

}

