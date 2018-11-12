package com.hydata.intelligence.platform.controller;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.service.ProductService;

/**
 * @author pyt
 * @createTime 2018年10月30日下午4:56:04
 */
@Transactional
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/product")
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@RequestMapping(value = "/get_protocols",method = RequestMethod.GET)
	public JSONObject getProtocol(){
		return productService.getProtocol();
	}
	
	@RequestMapping(value = "/add" ,method = RequestMethod.POST)
	public JSONObject addProduct(@RequestBody Product product){		
		return productService.addProduct(product);
	}
	
	@RequestMapping(value = "/modify",method = RequestMethod.POST)
	public JSONObject modifyProduct(@RequestBody Product product){
		return productService.modifyProduct(product);
	}
	
	@RequestMapping(value = "/query",method=RequestMethod.GET)
	public JSONObject query(Integer user_id,Integer page,Integer number){
		Page<Product> result = productService.queryByUserId(user_id, page-1, number);
		return RESCODE.SUCCESS.getJSONRES(result.getContent(),result.getTotalPages(),result.getTotalElements());
	}
	
	@RequestMapping(value = "/delete",method=RequestMethod.GET)
	public JSONObject delete(Integer product_id){
		return null;
	}
	
	@RequestMapping(value = "/get_detail",method=RequestMethod.GET)
	public JSONObject getDetail(Integer product_id) {
		return null;
	}
	

}

