package com.hydata.intelligence.platform.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.hydata.intelligence.platform.utils.CheckParams;


/**
 * @author pyt
 * @createTime 2018年10月22日下午5:08:01
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/dsm")
public class DataStreamModelController {
	private static Logger logger = LogManager.getLogger(DataStreamModelController.class);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private DataStreamModelService dsmService;
	@Autowired
	private UnitTypeRepository unitTypeRepository;
		
	@RequestMapping(value = "/add" ,method = RequestMethod.POST)
	    public JSONObject add(@RequestBody DataStreamModel model){			
	    	return dsmService.addDataStreamModel(model);		
		}
	 
	 @RequestMapping(value = "/delete" ,method = RequestMethod.GET)
	    public JSONObject delete(Long id){
		 JSONObject params = new JSONObject();
			params.put("id", id);
			JSONObject result = CheckParams.checkParams(params);
			if((Integer)result.get("code")==0) {			
				return dsmService.deleteByDSMId(id);
			}else {
				return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
			}
	    	
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
	 	public JSONObject pageable(Long productId,Integer page,Integer number) {
		 JSONObject params = new JSONObject();
			params.put("productId", productId);
			params.put("page", page);
			params.put("number", number);
			JSONObject result = CheckParams.checkParams(params);
			if((Integer)result.get("code")==0) {			
				
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
				
			}else {
				return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
			}
	 }
	 @RequestMapping(value ="/find_by_name",method = RequestMethod.GET)
	 public JSONObject findByName(Integer page,Integer number,String dsmName,Long productId) {
		JSONObject params = new JSONObject();
		params.put("page", page);
		params.put("number", number);
		params.put("productId", productId);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			 return dsmService.findByName(page-1,number,dsmName==null?"":dsmName,productId);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}		
	 }
	 
	 @RequestMapping(value ="/get_increment",method = RequestMethod.GET)
	 public JSONObject getIncrement(Long product_id,String start,String end) throws ParseException {		 
		 	JSONObject params = new JSONObject();
			params.put("product_id", product_id);
			params.put("start", start);
			params.put("end", end);
			JSONObject result = CheckParams.checkParams(params);
			if((Integer)result.get("code")==0) {			
				return dsmService.getIncrement(product_id, sdf.parse(start), sdf.parse(end));
			}else {
				return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
			}	     
	 }

}

