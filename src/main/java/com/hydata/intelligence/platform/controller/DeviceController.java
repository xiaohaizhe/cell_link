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
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.service.DeviceService;

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
	
	@RequestMapping(value="/showAll",method=RequestMethod.GET)
	public JSONObject showAll(Integer product_id,Integer page,Integer number){
		Page<Device> result = deviceService.showAllByProductId(product_id, page, number);
		return RESCODE.SUCCESS.getJSONRES(result.getContent(), result.getTotalPages(), result.getTotalElements());
	}
	
	@RequestMapping(value ="/add",method=RequestMethod.POST)
	public JSONObject addDevice(@RequestBody Device device){
		return deviceService.addDevice(device);
	}
	
	@RequestMapping(value = "/queryByDevice_snOrName",method = RequestMethod.GET)
	public JSONObject queryDeviceByDevice_snOrName(Integer product_id,Integer page,Integer number,String device_idOrName){
		Page<Device> result = deviceService.queryByDeviceSnOrName(product_id, page, number, device_idOrName);
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
}

