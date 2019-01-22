package com.hydata.intelligence.platform.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.service.DeviceService;
import com.hydata.intelligence.platform.utils.ExcelUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author pyt
 * @createTime 2018年10月31日上午11:31:11
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/device")
public class DeviceController {
	@Autowired
	private DeviceService deviceService;
	
	@Value("${spring.data.mongodb.uri}")
	private String mongouri;
	
	@Value("${spring.datasource.url}")
	private String mysqlurl;
	
	@RequestMapping(value="/show",method=RequestMethod.GET)
	public JSONObject showAll(Long product_id,Integer page,Integer number,int sort){
		return deviceService.showAllByProductIdM(product_id, page, number,sort);
	}
	
	@RequestMapping(value ="/add",method=RequestMethod.POST)
	public JSONObject addDevice(@RequestBody Device device){
		return deviceService.addDeviceM(device);
	}
	
	@RequestMapping(value = "/query_by_sn_or_name",method = RequestMethod.GET)
	public JSONObject queryDeviceByDevice_snOrName(Long product_id,Integer page,Integer number,String device_snOrName){
		return deviceService.queryByDeviceSnOrName_m(product_id,device_snOrName, page, number);
	}
	
	@RequestMapping(value="/modify",method=RequestMethod.POST)
	public JSONObject modifyDevice(@RequestBody Device device){		
		return deviceService.modifyDevice_m(device);
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.GET)
	public JSONObject delete(String device_sn){
		return deviceService.deleteDevice(device_sn);
	}
	
	@RequestMapping(value="/get_devicelist",method = RequestMethod.GET)
	public JSONObject getByProductId(Integer productId) {
		return deviceService.getByProductId(productId);
	}
	
	@RequestMapping(value="/get_devicedslist",method = RequestMethod.GET)
	public JSONObject getDeviceDsByDeviceSn(String device_sn) {
		return deviceService.getDeviceDsByDeviceSn(device_sn);
	}
	
	@RequestMapping(value="/{device_sn}/resolve_data",method = RequestMethod.POST)
	public JSONObject resolveDeviceData(@PathVariable String device_sn,@RequestBody JSONObject jsonObject) {
		return deviceService.resolveDeviceData(device_sn,jsonObject);
	}
	
	@RequestMapping(value = "/export_excel",method=RequestMethod.GET)
	public void exportExcel() {
		ExcelUtils.exportExcel();
	}
	
	@RequestMapping(value = "/import_excel",method=RequestMethod.GET)
	public JSONObject importExcel(String url,Long productId) {
		return deviceService.importExcel(url, productId);	
	}
	
	@RequestMapping(value= "/get_increment",method = RequestMethod.GET)
	public JSONObject getIncrement(Long product_id,String start ,String end) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return deviceService.getIncrement(product_id, sdf.parse(start), sdf.parse(end));
	}
	
	@RequestMapping(value= "/get_cmd_logs",method = RequestMethod.GET)
	public JSONObject getCmdLogs(String device_sn) {
		return deviceService.getCmdLogs(device_sn);
	}
	
	@RequestMapping(value= "/get_device_ds_data",method = RequestMethod.GET)
	public JSONObject getDeviceDsData(Integer dd_id,Date start,Date end) {
		return deviceService.getDeviceDsData(dd_id, start, end);
	}
	
	@RequestMapping(value= "/get_data")
	public void recieveData(JSONObject jsonObject) {
		deviceService.recieveData(jsonObject);
	}
	
	
	@RequestMapping(value= "/test_repo_add_device")
	public void test_repo_add_device(Long product_id) {
		deviceService.test_repo_add_device(product_id);
	}
	
	@RequestMapping(value= "/test_add_data_history")	
	public void test_add_data_history() {
		deviceService.test_data_history();
	}
	
	@RequestMapping(value= "/test_find_by_devicesn")
	public void testFindByDeviceSn(String device_sn) {
		deviceService.test_find_by_devicesn(device_sn);
	}
	

}

