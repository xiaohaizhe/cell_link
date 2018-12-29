package com.hydata.intelligence.platform.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.*;
import com.hydata.intelligence.platform.model.MQTT;
import com.hydata.intelligence.platform.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

import com.hydata.intelligence.platform.model.EmailHandlerModel;
import com.hydata.intelligence.platform.utils.EmailHandlerThread;


/**
 * @author: Jasmine
 * @createTime:
 * @description: <MQTT消费端初始化>
 * @modified:
 */
@Configuration
@IntegrationComponentScan
public class MqttReceiveConfig {

	 @Autowired
	 private static DatastreamModelRepository datastreamModelRepository;
	 @Autowired
	 private static ProductRepository productRepository;
	 @Autowired
	 private static TriggerRepository triggerRepository;
	 @Autowired
	 private static DeviceService deviceService;
	 @Autowired
	 private static TriggerService triggerService;
	 @Autowired
	 private static DeviceDatastreamRepository	 deviceDatastreamRepository;
	 @Autowired
	 private static DdTriggerRepository ddTriggerRepository;
	 @Autowired
	 private static TriggerTypeRepository triggerTypeRepository;
	 private static Logger logger = LogManager.getLogger(MqttReceiveConfig.class);
	 private static int qos = 2;

	/**
     * haizhe
     * init
     * mqtt初始化
     * @return
     */
    
    public static MqttClient sendClient;
    
    public static ExecutorService cachedThreadPool;
    
    public static BlockingQueue<EmailHandlerModel> emailQueue;
    
    public static EmailHandlerThread emailThread;

	public static MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(MQTT.getClientId()+"_inbound", mqttClientFactory());

	@SuppressWarnings("rawtypes")
	public static void init() throws MqttException{
    	/**
    	 * 注意单例!!!!!!!!!
    	 */
    	if(emailQueue== null)
    	{
    		synchronized(emailQueue)
    		{
    			if(emailQueue == null)
    			{
		    		cachedThreadPool = Executors.newCachedThreadPool();
		    	
		    		emailQueue = new ArrayBlockingQueue<EmailHandlerModel>(30);
		    		
		    		emailThread.start();

    			}
    		}
    	}
        /**
         * 此处应改为从配置文件读
         *String broker = "tcp://0.0.0.0:61613";
         *String userName = "admin";
         *String password = "admin";
         *String clientId = "cell_link_sendcmd";
         */

        /**
         * MQTT下发命令：设备SN+指令至Broker
         * @param deviceId, cmdMessage
         * 存储命令日志，获取回执信息
         */
        // 内存存储
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            sendClient = new MqttClient(MQTT.getBroker(), MQTT.getClientId(), persistence);
            // 创建链接参数
            MqttConnectOptions connOpts = new MqttConnectOptions();
            // 在重新启动和重新连接时记住状态
            connOpts.setCleanSession(false);
            // 设置连接的用户名
            connOpts.setUserName(MQTT.getUserName());
            connOpts.setPassword(MQTT.getPassword().toCharArray());
            connOpts.setWill("message", "断开连接".getBytes(), qos, true);
            // 建立连接
            sendClient.connect(connOpts);
            sendClient.subscribe("message");
        } catch (MqttException e) {
            e.printStackTrace();
        }

		/**
		 * haizhe
		 * 此处添加topic
		 * 初始化需要调用***********
		 * （1）找出所有通讯方式为mqtt的设备sn（pyt封装）
		 * （2）所有sn，添加到topic
		 */

        //或者换成DeviceService.getDeviceByProtocol也可以
		//找出所有MQTT协议的产品（protocolId=1)
		List<Product> productList =  productRepository.findByProtocolId(1);
		for(Product p:productList) {
			//找到该产品下所有的设备
			Integer product_id = p.getId();
			Optional<Product> productOptional =  productRepository.findById(product_id);
			if(productOptional.isPresent()) {
				JSONObject object =  deviceService.getByProductId(product_id);
				JSONArray devices = (JSONArray) object.get("data");
				for(int i = 0; i<devices.size();i++) {
					Device device = (Device) devices.get(i);
					//订阅该设备的鉴权信息Device_Sn
					//sendClient.subscribe(device.getDevice_sn());
					MqttHandler.mqttAddDevice(device.getDevice_sn());

				}
			}
		}
	}

    @Bean
    public MqttPahoMessageHandler getMqttPahoMessageHandler() {
    	MqttPahoMessageHandler handler = new MqttPahoMessageHandler(MQTT.getClientId(), new DefaultMqttPahoClientFactory());
    	return handler;
    }

    @Bean
    public static MqttConnectOptions getMqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setUserName(MQTT.getUserName());
        mqttConnectOptions.setPassword(MQTT.getPassword().toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{MQTT.getHostUrl()});
        mqttConnectOptions.setKeepAliveInterval(2);
        return mqttConnectOptions;
    }
    @Bean
    public static MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        //factory.setUserName(username);
        //factory.setPassword(password);
        return factory;
    }

	public void messageArrived(String topic, MqttMessage message) {
		cachedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				String payload = new String(message.getPayload());
				//解析收到的实时数据流
				JSONArray data= MqttHandler.mqttDataAnalysis(payload);
				//存储实时数据流到mongodb
				deviceService.dealWithData(topic, data);
				//进行触发器判断
				try {
					triggerService.TriggerAlarm(topic,data);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		});
	}
	/**
    //接收通道
    @Bean
    public static MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    //配置client,监听默认主题message
    @Bean
    public MessageProducer inbound(){
		//MqttPahoMessageDrivenChannelAdapter adapter =
		//		new MqttPahoMessageDrivenChannelAdapter(MQTT.getClientId()+"_inbound", mqttClientFactory());

		adapter.setCompletionTimeout(MQTT.getCompletionTimeout());
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(qos);
        adapter.setOutputChannel(mqttInputChannel());
        adapter.addTopic("message");
        return adapter;
    }
**/



    /**
	 * MQTT实时数据处理MQTTMessageHandler
	 * @param topic, message, deviceId
	 * 线程池
	 * 解析数据：设备id，数据流名称，实时数据流信息
	 * 线程池：触发器判断处理
	 * 存储数据流
	 * 弃
	 */

//	public void MQTTMessageHandler(String topic, MqttMessage message, String deviceSn) {
//		//线程池：一条数据流的解析：格式：数据名称1,value;数据名称2,value;...
//		String content = new String(message.getPayload());
//	
//		//SQL调取trigger信息
//		//MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");
//		//Map<String,Object> insert = new HashMap<>();
//		//List<String> deviceId = Lists.newArrayList();
//		//FindIterable<Document> documents = mongoDBUtil.queryDocumentIn(collection,"deviceId", deviceId);
//	    //mongoDBUtil.printDocuments(documents);
//
//	    //if (...){
//			TriggerService.TriggerAlarm(deviceSn, content);
//		//}
//		saveDataStream(deviceSn,content);
//
//	}


    /*通道2
    * 用于监听不同topic
    @Bean
    public MessageChannel mqttInputChannelTwo() {
        return new DirectChannel();
    }
    //配置client2，监听的topic:hell2,hello3
    @Bean
    public MessageProducer inbound1() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId+"_inboundTwo", mqttClientFactory(),
                        "hello2","hello3");
        adapter.setCompletionTimeout(completionTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannelTwo());
        return adapter;
    }

    //通过通道2获取数据
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannelTwo")
    public MessageHandler handlerTwo() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
                String type = topic.substring(topic.lastIndexOf("/")+1, topic.length());
                if("hello2".equalsIgnoreCase(topic)){
                    System.out.println("hello2 clientTwo,"+message.getPayload().toString());
                }else if("hello3".equalsIgnoreCase(topic)){
                    System.out.println("hello3 clientTwo,"+message.getPayload().toString());
                }
            }
        };
    }
  */
}

