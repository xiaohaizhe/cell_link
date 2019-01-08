package com.hydata.intelligence.platform.service;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.EmailHandlerModel;
import com.hydata.intelligence.platform.model.MQTT;
import com.hydata.intelligence.platform.model.MongoDB;
import com.hydata.intelligence.platform.repositories.*;
import com.hydata.intelligence.platform.utils.MongoDBUtils;
import com.hydata.intelligence.platform.utils.MqttClientUtil;
import com.hydata.intelligence.platform.utils.StringUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: Jasmine
 * @createTime:
 * @description: <MQTT消费端初始化> ：建立长连接，订阅已添加设备
 * @modified:
 */
@Transactional
@Service
public class MqttReceiveConfig {
	private static MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private TriggerService triggerService;
	@Autowired
	private MQTT mqtt;
	@Autowired
	private MqttHandler mqttHandler;
	@Autowired
	private MongoDB mongoDB;

	private Logger logger = LogManager.getLogger(MqttReceiveConfig.class);
	public ExecutorService cachedThreadPool;
	public MqttClient clinkClient;
	public static BlockingQueue<EmailHandlerModel> emailQueue;

	/**
	 * @author: Jasmine
	 * @createTime:
	 * @description: <建立MQTT连接，并订阅已添加设备>
	 * @modified:
	 */
	public void init() throws MqttException {
		//初始化线程池：信息处理线程池以及触发器发送邮件线程池
		logger.info("MQTT线程池初始化");
		emailQueue = MqttClientUtil.getEmailQueue();
		clinkClient= MqttClientUtil.getInstance();
		cachedThreadPool = MqttClientUtil.getCachedThreadPool();
		clinkClient.connect(MqttClientUtil.getOptions());
		// 设置回调函数
		clinkClient.setCallback(new MqttCallback() {
			public void connectionLost(Throwable cause) {
				//System.out.println("connectionLost");
				logger.info("MQTT断开连接");
				try {
					mqttHandler.reconnect(clinkClient);
				} catch(MqttException me){
					logger.error("MQTT重连失败");
					me.printStackTrace();
				}
			}

			public void messageArrived(String topic, MqttMessage message) {
				//System.out.println("topic:"+topic);
				//System.out.println("Qos:"+message.getQos());
				//System.out.println("message content:"+new String(message.getPayload()));
				String payload = new String(message.getPayload());
				logger.info("接收到信息");
				logger.info("主题：" + topic);
				logger.info("Qos:" + message.getQos());
				logger.info("内容:" + payload);
				//处理实时信息
				//订阅主题为device_Sn传递的信息流: device_Sn重复且为数字
				boolean isExist = deviceService.checkDevicesn(topic);
                boolean isNumber = StringUtils.isNumeric(topic);
                if (!isExist && isNumber) {
					cachedThreadPool.execute(() -> {
						//解析收到的实时数据流
						JSONArray data = mqttHandler.mqttDataAnalysis(payload);
						//存储实时数据流到mongodb
						deviceService.dealWithData(topic, data);
						//进行触发器判断
						try {
							triggerService.TriggerAlarm(topic, data);
						} catch (InterruptedException e) {
							logger.error(topic + "触发器触发失败");
							e.printStackTrace();
						}
					});
				}
			}

			public void deliveryComplete(IMqttDeliveryToken token) {
				//System.out.println("deliveryComplete---------"+ token.isComplete());
				logger.info("传输完成---------" + token.isComplete());
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

		/**
		 * haizhe
		 * 此处添加topic
		 * 初始化需要调用***********
		 * （1）找出所有通讯方式为mqtt的设备sn（pyt封装）
		 * （2）所有sn，添加到topic
		 */

        try {
            String test = "test";
			IMqttToken token = MqttClientUtil.getInstance().subscribeWithResponse(test);
			logger.info(test+"订阅成功======="+token.isComplete());
            //clinkClient.subscribe(test);
        } catch (Exception e){
            logger.debug("测试订阅test失败");
        }
		//找出所有MQTT协议的产品（protocolId=1)
		logger.info("------------------------------");
		logger.info("初始化订阅开始：");
		MongoClient meiyaClient = mongoDBUtil.getMongoConnect(mongoDB.getHost(),mongoDB.getPort());
		MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");
		List<Product> products = productRepository.findByProtocolId(1);
		for(Product product : products) {
			Map<String,Object> conditions = Maps.newHashMap();
			conditions.put("product_id",product.getId());
			FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,null,null,null);
			for (Document d : documents) {
				String device_sn = d.getString("device_sn");
				mqttHandler.mqttAddDevice(device_sn);
			}
		}
		logger.info("初始化订阅结束");
		logger.info("------------------------------");

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