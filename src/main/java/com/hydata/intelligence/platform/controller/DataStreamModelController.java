package com.hydata.intelligence.platform.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.DatastreamModel;
import com.hydata.intelligence.platform.dto.UnitType;
import com.hydata.intelligence.platform.model.DataStreamModel;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.UnitTypeRepository;
import com.hydata.intelligence.platform.service.DataStreamModelService;


/**
 * @author pyt
 * @createTime 2018年10月22日下午5:08:01
 */
@Transactional
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/dsm")
public class DataStreamModelController {
	private static Logger logger = LogManager.getLogger(DataStreamModelController.class);
	@Autowired
	private DataStreamModelService dsmService;
	@Autowired
	private UnitTypeRepository unitTypeRepository;
		
	@RequestMapping(value = "/add" ,method = RequestMethod.POST)
	    public JSONObject add(@RequestBody DataStreamModel model){			
	    	return dsmService.addData_stream_model(model);		
		}
	 
	 @RequestMapping(value = "/delete" ,method = RequestMethod.GET)
	    public JSONObject delete(int id){	    		
	    	return dsmService.deleteByDSM_id(id);
	    }
	 @RequestMapping(value = "/modify" ,method = RequestMethod.POST)
	    public JSONObject modify(@RequestBody DataStreamModel model){	    	
	    	return dsmService.modifyDSM(model);
	    }
	 @RequestMapping(value = "/query" ,method = RequestMethod.GET)
	    public String query(@RequestBody DatastreamModel dsmodel){
	    	logger.debug("test");
	    	System.out.println("hello!");	
	    	return "test";
	    }
	 @RequestMapping(value = "/SimPage",method = RequestMethod.GET)
	 	public JSONObject pageable(Integer productId,Integer page,Integer number) {
		 Page<DatastreamModel> DatastreamModelPage = dsmService.queryByProductId(productId,page-1,number);
         List<DatastreamModel> DatastreamModelList = DatastreamModelPage.getContent();
         List<DataStreamModel> Data_stream_modelList = new ArrayList<>();
         for(DatastreamModel model:DatastreamModelList) {
        	 DataStreamModel mod = new DataStreamModel();
        	 mod.setId(model.getId());
        	 mod.setProduct_id(model.getProductId());
        	 mod.setName(model.getName());
        	 mod.setCreateTime(model.getCreateTime());
        	 Optional<UnitType> unitOptional = unitTypeRepository.findById(model.getUnitTypeId());
        	 if(unitOptional.isPresent()) {
        		 UnitType unit  = unitOptional.get();
        		 mod.setUnit_name(unit.getName());
        		 mod.setUnit_symbol(unit.getSymbol());
        	 }
        	 Data_stream_modelList.add(mod);
         }
         return RESCODE.SUCCESS.getJSONRES(Data_stream_modelList, DatastreamModelPage.getTotalPages(), DatastreamModelPage.getTotalElements());
	 }

}

