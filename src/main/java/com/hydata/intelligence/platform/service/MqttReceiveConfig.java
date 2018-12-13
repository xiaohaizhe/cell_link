package com.hydata.intelligence.platform.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hydata.intelligence.platform.dto.*;
import com.hydata.intelligence.platform.model.DataStreamModel;
import com.hydata.intelligence.platform.model.TriggerModelModel;
import com.hydata.intelligence.platform.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.python.antlr.ast.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.hydata.intelligence.platform.model.EmailHandlerModel;
import com.hydata.intelligence.platform.utils.EmailHandlerThread;

import static java.lang.Short.valueOf;

/**
 * @author: Jasmine
 * @createTime:
 * @description: <MQTT接收消息处理>
 * @modified:
 */
@Configuration
@IntegrationComponentScan
public class MqttReceiveConfig {

	 @Autowired
	 private DatastreamModelRepository datastreamModelRepository;
	 @Autowired
	 private ProductRepository productRepository;
	 @Autowired
	 private TriggerRepository triggerRepository;
	 @Autowired
	 private DeviceService deviceService;
	 @Autowired
	 private DeviceDatastreamRepository	 deviceDatastreamRepository;
	 @Autowired
	 private DdTriggerRepository ddTriggerRepository;
	 @Autowired
	 private TriggerTypeRepository triggerTypeRepository;


	 @Value("${mqtt.serverURI}")
	 private String broker;

    @Value("${mqtt.username}")
	private String userName;

    @Value("${mqtt.password}")
	private String password;

    @Value("${mqtt.serverURI}")
	private String hostUrl;

    @Value("${mqtt.clientId}")
	private String clientId;

    @Value("${mqtt.defaultTopic}")
	private String defaultTopic;

    @Value("${mqtt.completionTimeout}")
	private int completionTimeout ;   //连接超时

	private static Logger logger = LogManager.getLogger(MqttReceiveConfig.class);



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
    @SuppressWarnings("rawtypes")
	public void init() throws MqttException{
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
					int qos = 1;
					// 内存存储
					MemoryPersistence persistence = new MemoryPersistence();
					try {
						sendClient = new MqttClient(broker, clientId, persistence);
						// 创建链接参数
						MqttConnectOptions connOpts = new MqttConnectOptions();
						// 在重新启动和重新连接时记住状态
						connOpts.setCleanSession(false);
						// 设置连接的用户名
						connOpts.setUserName(userName);
						connOpts.setPassword(password.toCharArray());
						connOpts.setWill("自定义", "i`m gone".getBytes(), qos, true);
						// 建立连接
						sendClient.connect(connOpts);
					} catch (MqttException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						/**
						 * log.error..........
						 */
					}
    			}
    		}
    	}

		/**
		 * haizhe
		 * 此处添加topic
		 * 初始化需要调用***********
		 * （1）找出所有通讯方式为mqtt的设备sn（pyt封装）
		 * （2）所有sn，添加到topic
		 */

		//for(int i = 0 ; i < ProductList.size() ; i++) {
		//	List<DatastreamModel> deviceList = datastreamModelRepository.findByProductId(1);
		//	for(int j = 0 ; j < ProductList.size() ; j++) {
		//		sendClient.subscribe(deviceList.get(j).toString());
		//	}
		//}

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
					MqttPahoMessageDrivenChannelAdapter adapter =
							new MqttPahoMessageDrivenChannelAdapter(clientId+"_inbound", mqttClientFactory());
					adapter.removeTopic(device.getDevice_sn());

				}
			}
		}
	}

    @Bean
    public MqttPahoMessageHandler getMqttPahoMessageHandler() {
    	MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientId, new DefaultMqttPahoClientFactory());
    	return handler;
    }

    @Bean
    public MqttConnectOptions getMqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setUserName(userName);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        mqttConnectOptions.setKeepAliveInterval(2);
        return mqttConnectOptions;
    }
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        //factory.setUserName(username);
        //factory.setPassword(password);

        return factory;
    }

    //接收通道
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    //配置client,监听默认主题message
    @Bean
    public MessageProducer inbound(){
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId+"_inbound", mqttClientFactory(),"message");


        adapter.setCompletionTimeout(completionTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    /**
     * haizhe
     * 增加一个方法(供pyt调用）
     * 添加topic，传入sn进来，将其加为topic
     * @return
     */
    public void mqttAddDevice(String deviceSn) throws MqttException{
		MqttPahoMessageDrivenChannelAdapter adapter =
				new MqttPahoMessageDrivenChannelAdapter(clientId+"_inbound", mqttClientFactory());

		adapter.addTopic(deviceSn,1);
		adapter.setCompletionTimeout(completionTimeout);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
	 }

    /**
     * haizhe
     * 增加一个方法，（供pyt调用）
     * 删除topic，传入sn进来，将其topic去除，不再订阅
     * TODO
     * @return
     */
	public void mqttRemoveDevice(String deviceSn) throws MqttException{
		MqttPahoMessageDrivenChannelAdapter adapter =
				new MqttPahoMessageDrivenChannelAdapter(clientId+"_inbound", mqttClientFactory());

		adapter.removeTopic(deviceSn);
		adapter.setOutputChannel(mqttInputChannel());
	}


    //通过通道获取数据
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get("deviceSn").toString();
            	String content = message.getPayload().toString();
            	cachedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						//解析收到的实时数据流
						JSONArray data= mqttDataAnalysis(content);
						//存储实时数据流到mongodb
						deviceService.dealWithData(topic, data);
						//根据DeviceSn+Dm_name找到对应的dd_id
						for(int i=0;i<data.size();i++) {
							try {
								JSONObject object = data.getJSONObject(i);
								String dm_name = object.getString("dm_name");
								int data_value = object.getIntValue("value");
								Optional<DeviceDatastream> ddId = deviceDatastreamRepository.findByDeviceSnAndDm_name(topic, dm_name);
								DeviceDatastream deviceDatastream = ddId.get();
								int dd_id = deviceDatastream.getId();
								//根据dd_id找到triggerId
								Optional<DdTrigger> triggerId = ddTriggerRepository.findByDdId(dd_id);
								DdTrigger ddTrigger = triggerId.get();
								int trigger_id = ddTrigger.getTriggerId();
								//根据triggerId找到对应的触发器信息
								//触发判断关系:">"或者"<"
								Optional<TriggerType> triggerinfo1 = triggerTypeRepository.findById(trigger_id);
								TriggerType triggerType = triggerinfo1.get();
								String symbol = triggerType.getSymbol();
								//触发阈值
								Optional<TriggerModel> triggerinfo2 = triggerRepository.findById(trigger_id);
								TriggerModel triggerModel = triggerinfo2.get();
								int criticalValue = valueOf(triggerModel.getCriticalValue());
								//触发方式：0：邮箱；1：url
								int triggerMode = triggerModel.getTriggerMode();
								//触发方式详细信息：url或邮箱地址
								String modeValue = triggerModel.getModeValue();

								//判断触发器是否触发
								if (((symbol.equals("<")) && (data_value < criticalValue)) || ((symbol.equals(">")) && (data_value > criticalValue))) {
									if (triggerMode == 0) {
										//加入发邮件的线程池
										//emailQueue.offer();
									} else if (triggerMode == 1) {
										//使用url发送警报
									}
								}
							} catch (Exception e) {
								logger.debug(e.getClass().getName() + ": " + e.getMessage());
							}
						}
						//	List<DeviceDatastream> deviceSn = deviceDatastreamRepository.findByDeviceSn(topic);
						//for (DeviceDatastream d:deviceSn) {
						//	Integer deviceId = d.getId();
						//	List<TriggerModel> triggerList = triggerRepository.findByDeviceId(deviceId);
						//}


						/**
						 * for (all 数据名称 in data）{
						 * if (curData.getString(0) in triggerList){
						 * 		for (all 数据 in 该数据名称){
						 * 			if ((curData < trigger_critical_value)&&(trigger_type == "<")) ||(curData > trigger_critical_value)&&(trigger_type == ">")) ) {
						 * 					if (trigger_Mode == 1) {
						 * 						//调用URL;
						 * 					}	else{
						 * 						emailqueue.offer(triggerList.get);
						 * 					}
						 * 				}
						 * 			}
						 * 		}
						 * 	}

						 */
		            	 //需要有线程专门从emailqueue中不断判断并发送email
		            	 //emailhandlerthread;
					}
            	
            	});
            	
//                String type = topic.substring(topic.lastIndexOf("/")+1);
//                if("hello".equalsIgnoreCase(topic)){
//                    System.out.println("hello,fuckXX,"+message.getPayload().toString());
//                }else if("hello1".equalsIgnoreCase(topic)){
//                    System.out.println("hello1,fuckXX,"+message.getPayload().toString());
//                }
            }
        };
    }



    /**
     * MQTT数据解析
	 * 实时信息流格式String：name1, value1; name2, value2;...
	 * 返回格式JSONArray：[{"dm_name":"name1","value":"value1"},{"dm_name":"name2","value":"value2"},...]
     */
    public JSONArray mqttDataAnalysis(String data){
		//JSONArray result = JSONArray.parseArray(data);
		JSONObject object = new JSONObject();
		JSONArray result = new JSONArray();
		String[] datas = data.split(";") ;
		for (int i = 0; i<datas.length; i++){
			String[] tmp = datas[i].split(",");
			String dm_name = tmp[0];
			int value = Integer.parseInt(tmp[1]);
			object.put("dm_name", dm_name);
			object.put("value", value);
			result.add(object);
		}
    	return result;
	}


    /**
	 * MQTT实时数据处理MQTTMessageHandler
	 * @param topic, message, deviceId
	 * 线程池
	 * 解析数据：设备id，数据流名称，实时数据流信息
	 * 线程池：触发器判断处理
	 * 存储数据流
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

