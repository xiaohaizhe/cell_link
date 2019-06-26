package com.hydata.intelligence.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.service.ProductService;
import com.hydata.intelligence.platform.utils.CheckParams;

import java.util.List;

/**
 * @author pyt
 * @createTime 2018年10月30日下午4:56:04
 */
@RestController
@RequestMapping(value="/api/product")
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@RequestMapping(value = "/get_protocols",method = RequestMethod.GET)
	public JSONObject getProtocol(){
		return productService.getProtocol();
	}
	
	@RequestMapping(value = "/add" ,method = RequestMethod.POST)
	public JSONObject addProduct(@RequestBody @Validated Product product, BindingResult br){
		if(br.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			sb.append(br.getObjectName()+":");
			List<FieldError> errors  = br.getFieldErrors();
			for (FieldError error : errors){
				sb.append("["+error.getField() + ":"+error.getDefaultMessage()+"].");
			}
			return RESCODE.PARAM_ERROR.getJSONRES(sb.toString());
		}
		return productService.addProduct(product);
	}
	
	@RequestMapping(value = "/modify",method = RequestMethod.POST)
	public JSONObject modifyProduct(@RequestBody Product product){
		return productService.modifyProduct(product);
	}
	
	@RequestMapping(value = "/query",method=RequestMethod.GET)
	public JSONObject query(Long user_id,String name,Integer page,Integer number,Integer sort){
		JSONObject params = new JSONObject();
		params.put("user_id", user_id);
		params.put("page", page);
		params.put("number", number);
		params.put("sort", sort);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return productService.queryByUserId(user_id,name, page-1, number,sort);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}	
	}
	
	@RequestMapping(value = "/delete",method=RequestMethod.GET)
	public JSONObject delete(Long[] product_ids){

		JSONObject params = new JSONObject();
		params.put("product_ids", product_ids);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return productService.deleteByProductIds(product_ids);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
	}

	@RequestMapping(value = "/deleteByUserId",method=RequestMethod.GET)
	public JSONObject deleteByUserId(Long user_id){
		JSONObject params = new JSONObject();
		params.put("user_id", user_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return productService.deleteByUserId(user_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
	}
	
	@RequestMapping(value = "/get_detail",method=RequestMethod.GET)
	public JSONObject getDetail(Long product_id) {
		JSONObject params = new JSONObject();
		params.put("product_id", product_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return productService.getDetail(product_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value = "/get_heatmap",method=RequestMethod.GET)
	public JSONObject getHeatmap() {
		return productService.getHeatmap();
	}
	
	@RequestMapping(value = "/get_product_overview",method=RequestMethod.GET)
	public JSONObject getProductOverview(Long product_id) {	
		JSONObject params = new JSONObject();
		params.put("product_id", product_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return productService.getProductOverview(product_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
	}

	@RequestMapping(value = "/get_device_and_ds",method=RequestMethod.GET)
	public JSONObject getDeviceAndDatastream(Long product_id) {
		JSONObject params = new JSONObject();
		params.put("product_id", product_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {
			return productService.getDeviceAndDatastream(product_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
	}
	
}

