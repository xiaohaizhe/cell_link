package com.hydata.intelligence.platform.service;

import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.MQTT;
import com.hydata.intelligence.platform.repositories.*;
import com.hydata.intelligence.platform.utils.MqttClientUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Jasmine
 * @createTime:
 * @description: <MQTT消费端初始化> ：建立长连接，订阅已添加设备
 * @modified:
 */
@Transactional
@Service
public class MqttReceiveConfig {
	/*private static MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();*/

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private MQTT mqtt;
	@Autowired
	private MqttHandler mqttHandler;
	@Autowired
	private DeviceRepository deviceRepository;

	private Logger logger = LogManager.getLogger(MqttReceiveConfig.class);
	//private ExecutorService cachedThreadPool;
	private MqttClient clinkClient;
	//public BlockingQueue<EmailHandlerModel> emailQueue;

	/**
	 * @author: Jasmine
	 * @createTime:
	 * @description: <建立MQTT连接，并订阅已添加设备>
	 * @modified:
	 */
	public void init() throws MqttException{
		try {
			logger.debug("进入初始化");
			//初始化线程池：信息处理线程池以及触发器发送邮件线程池
			//logger.info("MQTT线程池初始化");
			clinkClient = MqttClientUtil.getInstance();
			MqttClientUtil.getEmailQueue();
			MqttClientUtil.getCachedThreadPool();
			IMqttToken token = clinkClient.connectWithResult(MqttClientUtil.getOptions());
            logger.info("客户端连接完成======"+token.isComplete());
            logger.info("客户端连接状态："+clinkClient.isConnected());

			//连接成功后，测试订阅test主题
			try {
				String test = "test";
				mqttHandler.mqttAddDevice("test");
//				token = MqttClientUtil.getInstance().subscribeWithResponse(test);
//				logger.info(test + "订阅成功=======" + token.isComplete());
				//clinkClient.subscribe(test);
			} catch (Exception e) {
				logger.debug("测试订阅test失败");
			}

			// 设置回调函数
					clinkClient.setCallback(new MqttCallback() {
				public void connectionLost(Throwable cause) {
					//System.out.println("connectionLost");
					logger.info("MQTT断开连接");
					try {
						mqttHandler.reconnect(clinkClient);
					} catch (MqttException me) {
						logger.error("MQTT重连失败");
						me.printStackTrace();
					}
				}

				public void messageArrived(String topic, MqttMessage message){
					//System.out.println("topic:"+topic);
					//System.out.println("Qos:"+message.getQos());
					//System.out.println("message content:"+new String(message.getPayload()));
					String payload = new String(message.getPayload());
					payload = payload.substring(1);
					logger.info("==========接收到实时信息==========");
					logger.info("主题：" + topic);
					logger.info("Qos:" + message.getQos());
					logger.info("内容:" + payload);
					logger.info("=================================");
					try {
						mqttHandler.MessageHandler(topic, payload);
					} catch (Exception e){
						logger.error("信息处理失败");
						logger.error("原因： "+e);
						//logger.error(e.getMessage());
						//logger.error(e.getClass());

					}
				}

				public void deliveryComplete(IMqttDeliveryToken token) {
					//System.out.println("deliveryComplete---------"+ token.isComplete());
					logger.info("传输完成---------" + token.isComplete());
					//MqttClientUtil.getSemaphore().release();
				}
			});
//
//		Boolean isConnect = clinkClient.isConnected();
//		Boolean isNull = (clinkClient==null);
//        if (isNull || !isConnect) {
//            logger.debug("MQTT连接失败");
//        } else {
//            logger.info("MQTT连接建立");
//        }

			//发送粘性测试信息至broker
			try {
				clinkClient.publish("test","cell-link initialized".getBytes(),mqtt.getQos(),false);
				//mqttHandler.publish("test", "cell-link initialized",0,true);
			} catch (Exception e){
				logger.error("MQTT 测试信息发送失败");
				e.printStackTrace();
			}

/*            //初始化成功后，测试addDevice方法
            try {
                //token = MqttClientUtil.getInstance().subscribeWithResponse("123456");
                //logger.info("设备123456订阅成功=======" + token.isComplete());
                mqttHandler.mqttAddDevice("test/addDevice");
                clinkClient.publish("test/addDevice","testing add device".getBytes(),0,true);
            } catch (Exception e) {
                logger.debug("测试添加设备失败");
            }*/


			/**
			 * haizhe
			 * 此处添加topic
			 * 初始化需要调用***********
			 * （1）找出所有通讯方式为mqtt的设备id（pyt封装）
			 * （2）所有id，添加到topic
			 */

			//找出所有MQTT协议的产品（protocolId=1)
			logger.info("------------------------------");
			logger.info("初始化订阅开始：");
			List<Product> products = productRepository.findByProtocolId(1);
			logger.debug("MQTT协议下的产品");
			for (Product product : products) {
				logger.info("产品id:"+product.getId());
				List<Device> deviceList = deviceRepository.findByProductId(product.getId());
				logger.info("产品下的设备");
				for(Device device:deviceList) {
					logger.info("设备编码："+device.getId());
					mqttHandler.mqttAddDevice(String.valueOf(device.getId()));
				}
				
			}
			logger.info("初始化订阅结束");
			logger.info("------------------------------");

		} catch(MqttException me){
			logger.error("mqtt连接失败");
			me.printStackTrace();
		}


		//发送粘性测试信息至broker
		try {
			clinkClient.publish("test", "Traversed MongoDB to add topics".getBytes(),0,false);
			//mqttHandler.publish("test", "Traversed MongoDB to add topics",0,true);
		} catch (Exception e){
			logger.error("MQTT 测试信息发送失败");
			e.printStackTrace();
		}
		/**弃用
		 *
		List<Product> productList = productRepository.findByProtocolId(1);
		for (Product p : productList) {
			//找到该产品下所有的设备
			Integer product_id = p.getId();
			Optional<Product> productOptional = productRepository.findById(product_id);
			if (productOptional.isPresent()) {
				JSONObject object = deviceService.getByProductId(product_id);
				JSONArray devices = (JSONArray) object.get("data");
				for (int i = 0; i < devices.size(); i++) {
					Device device = (Device) devices.get(i);
					logger.info("查询到已存储设备信息："+device.getDevice_sn()+"---准备订阅");
					//订阅该设备的鉴权信息Device_Sn
					//receiveClient.subscribe(device.getDevice_sn());
					mqttHandler.mqttAddDevice(device.getDevice_sn());

				}
			}
		}**/

		// 断开连接
		//clinkClient.disconnect();
		// 关闭客户端
		//clinkClient.close();
		//System.exit(0);

	}

}