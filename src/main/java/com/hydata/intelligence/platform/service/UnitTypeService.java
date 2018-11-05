package com.hydata.intelligence.platform.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import com.hydata.intelligence.platform.dto.UnitType;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.UnitTypeRepository;

/**
 * @author pyt
 * @createTime 2018年10月25日下午3:15:42
 */
@EnableAutoConfiguration
@Transactional
@Service
public class UnitTypeService {
	@Autowired
	private UnitTypeRepository unitTypeRepository;
	
	private static Logger logger = LogManager.getLogger(UnitTypeService.class);
	
	public Map<String, Object> add(UnitType ut){
		if(ut.getName().isEmpty()||ut.getSymbol().isEmpty()) {
			return RESCODE.PARAM_MISSING.getJSONRES();
		}
		logger.debug("添加单位");
		logger.debug("ut.getName()"+ut.getName());
		logger.debug("ut.getSymbol()"+ut.getSymbol());
		List<UnitType> unit_typeList = unitTypeRepository.findByNameAndSymbol(ut.getName(),ut.getSymbol());
		if(unit_typeList!=null&&unit_typeList.size()>0) {
			logger.debug("单位名称符号存在，直接返回unit_type_id");
			return RESCODE.SUCCESS.getJSONRES(unit_typeList.get(0));		
		}else {
			logger.debug("单位名称符号不存在，创建unit_type，返回unit_type_id");
			UnitType unitType = new UnitType();
			unitType.setName(ut.getName());
			unitType.setSymbol(ut.getSymbol());
			UnitType result1 = unitTypeRepository.save(unitType);
			return RESCODE.SUCCESS.getJSONRES(result1);	
		}
	}
 	

}

