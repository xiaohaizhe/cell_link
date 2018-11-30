package com.hydata.intelligence.platform.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
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
import com.hydata.intelligence.platform.utils.ExcelUtils;

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
	
	
	@RequestMapping(value="/show",method=RequestMethod.GET)
	public JSONObject showAll(Integer product_id,Integer page,Integer number,int sort){
		return deviceService.showAllByProductIdM(product_id, page, number,sort);
	}
	
	@RequestMapping(value ="/add",method=RequestMethod.POST)
	public JSONObject addDevice(@RequestBody Device device){
		return deviceService.addDeviceM(device);
	}
	
	@RequestMapping(value = "/query_by_sn_or_name",method = RequestMethod.GET)
	public JSONObject queryDeviceByDevice_snOrName(Integer product_id,Integer page,Integer number,String device_idOrName){
		return deviceService.queryByDeviceSnOrName_m(product_id,device_idOrName, page, number);
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
	
	@RequestMapping(value="/get_ddlist",method = RequestMethod.GET)
	public JSONObject getDDByDeviceSn(String device_sn) {
		return deviceService.getDeviceDsByDeviceSn(device_sn);
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
	
	@RequestMapping(value= "/get_increment",method = RequestMethod.GET)
	public JSONObject getIncrement(Integer product_id,String start ,String end) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return deviceService.getIncrement(product_id, sdf.parse(start), sdf.parse(end));
	}
	
	@RequestMapping(value= "/find",method = RequestMethod.GET)
	public void finddevice(String device_sn) {
		deviceService.findDevice(device_sn);
	}
	
	@RequestMapping(value= "/get_cmd_logs",method = RequestMethod.GET)
	public JSONObject getCmdLogs(String device_sn) {
		return deviceService.getCmdLogs(device_sn);
	}
	
	@RequestMapping(value= "/get_DDD",method = RequestMethod.GET)
	public JSONObject getDDD(Integer dd_id,Date start,Date end) {
		return deviceService.getDeviceDsData(dd_id, start, end);
	}


	/**
	 * @author: Jasmine
	 * @createTime: 2018年11月20日上午11:31:11
	 * @description: <容器数据类型-实时载入数据流>
	 * @modified:
	 */
	public static class LiveDataStream {
		private int intData;
		String input;
		String client;
		public LiveDataStream(int d){
			intData = d;
		}
		public LiveDataStream(String d){
			String HOST = "tcp://0.0.0.0:61613";
			String TOPIC = "mqtt/test";
			int qos = 1;
			String clientid = "subClient1";
			String userName = "admin";
			String passWord = "admin";
			try {
				MqttClient client = new MqttClient(HOST, clientid, new MemoryPersistence());
				MqttConnectOptions options = new MqttConnectOptions();
				options.setCleanSession(true);
				options.setUserName(userName);
				options.setPassword(passWord.toCharArray());
				options.setConnectionTimeout(10);
				options.setKeepAliveInterval(20);
				client.setCallback(new MqttCallback() {
					public void connectionLost(Throwable cause) {
						System.out.println("connectionLost");
					}
					public void messageArrived(String topic, MqttMessage message) {
						//System.out.println("topic:"+topic);
						//System.out.println("Qos:"+message.getQos());
						//System.out.println("message content:"+new String(message.getPayload()));
						input = new String(message.getPayload());
					}

					public void deliveryComplete(IMqttDeliveryToken token) {
						System.out.println("deliveryComplete---------"+ token.isComplete());
					}
				});
				client.connect(options);
				client.subscribe(TOPIC, qos);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public int getData(){
			return intData;
		}
		@Override
		public String toString(){
			return input;
		}
		public String getClient(){
			return null;
		}
	}

	/**
	 * @author: Jasmine
	 * @createTime:
	 * @description: <数据流存储>
	 * @modified:
	 */
	public class SaveDataStream {

	}
}

