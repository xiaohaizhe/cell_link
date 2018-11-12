package com.hydata.intelligence.platform.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.service.DeviceService;
import com.hydata.intelligence.platform.service.ProductService;
import com.hydata.intelligence.platform.utils.ExcelUtils;

/**
 * @author pyt
 * @createTime 2018年10月31日上午11:31:11
 */
@Transactional
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/device")
public class DeviceController {
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@RequestMapping(value="/show",method=RequestMethod.GET)
	public JSONObject showAll(Integer product_id,Integer page,Integer number){
		Page<Device> result = deviceService.showAllByProductId(product_id, page, number);
		return RESCODE.SUCCESS.getJSONRES(result, result.getTotalPages(), result.getTotalElements());
	}
	
	@RequestMapping(value ="/add",method=RequestMethod.POST)
	public JSONObject addDevice(@RequestBody Device device){
		return deviceService.addDevice(device);
	}
	
	@RequestMapping(value = "/query_by_sn_or_name",method = RequestMethod.GET)
	public JSONObject queryDeviceByDevice_snOrName(Integer product_id,Integer page,Integer number,String device_idOrName){
		Page<Device> result = deviceService.queryByDeviceSnOrName(product_id,device_idOrName, page, number);
		System.out.println("Controller:"+result);
		return RESCODE.SUCCESS.getJSONRES(result.getContent(), result.getTotalPages(), result.getTotalElements());
	}
	
	@RequestMapping(value="/modify",method=RequestMethod.POST)
	public JSONObject modifyDevice(@RequestBody Device device){		
		return deviceService.modifyDevice(device);
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.GET)
	public JSONObject delete(Integer id){
		return deviceService.deleteDevice(id);
	}
	
	@RequestMapping(value="/get_devicelist",method = RequestMethod.GET)
	public JSONObject getByProductId(Integer productId) {
		return deviceService.getByProductId(productId);
	}
	
	@RequestMapping(value="/get_ddlist",method = RequestMethod.GET)
	public JSONObject getDDByDeviceId(Integer deviceId) {
		return deviceService.getDDByDeviceId(deviceId);
	}
	
	@RequestMapping(value="/resolve_data",method = RequestMethod.POST)
	public void resolveDeviceData(JSONObject info) {
		deviceService.resolveDeviceData(info);
	}
	
	@RequestMapping(value = "/export_excel",method=RequestMethod.GET)
	public void exportExcel() {
		ExcelUtils.exportExcel();
	}
	
	@RequestMapping(value = "/import_excel",method=RequestMethod.GET)
	public JSONObject importExcel(String url,Integer productId) {
		return deviceService.importExcel(url, productId);	
	}
}

