package com.hydata.intelligence.platform.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
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

 /**
 * @author: Jasmine
 * @createTime:
 * @description: <MQTT接收消息处理>
 * @modified:
 */
@Configuration
@IntegrationComponentScan
public class MqttReceiveConfig {

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.serverURI1}")
    private String hostUrl;

    @Value("${mqtt.clientId}")
    private String clientId;

    @Value("${mqtt.defaultTopic}")
    private String defaultTopic;

    @Value("${mqtt.completionTimeout}")
    private int completionTimeout ;   //连接超时
    
    
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
	static void init()
    {
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
					 */
					String broker = "tcp://0.0.0.0:61613";
					String userName = "admin";
					String password = "admin";
					String clientId = "cell_link_sendcmd";
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
		
    }
    
    
    @Bean
    public MqttPahoMessageHandler getMqttPahoMessageHandler() {
    	MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientId, new DefaultMqttPahoClientFactory());
    	return handler;
    }

    @Bean
    public MqttConnectOptions getMqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
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

    //配置client,监听的topic
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId+"_inbound", mqttClientFactory(),
                        "hello","hello1");
        /**
         * haizhe
         * 此处添加topic
         * 初始化需要调用***********
         * （1）找出所有通讯方式为mqtt的设备sn（pyt封装）
         * （2）所有sn，添加到topic
         * 
         * TODO
         */
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
     * TODO
     * @return
     */
    
    
    /**
     * haizhe
     * 增加一个方法，（供pyt调用）
     * 删除topic，传入sn进来，将其topic去除，不再订阅
     * TODO
     * @return
     */

    

    //通过通道获取数据
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
            	String content = message.getPayload().toString();
            	cachedThreadPool.execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						/**
		            	 * 解析content;
		            	 * 假设返回一个map
		            	 * 调用解析方法，解析到数据流名称和对应数据值
		            	 * 
		            	 * TODO
		            	 * 
		            	 * 
		            	 * 
		            	 * 
		            	 * 
		            	 * 
		            	 * 解析完成后，进行存储
		            	 * 调用存储历史数据流的信息 
		            	 * 
		            	 * TODO
		            	 *
		            	 *
		            	 *
		            	 *
		            	 * 从数据库里调用triggermodel，
		            	 * 将对应数据流和数据值进行对比判断
		            	 * 判断是否符合触发条件
		            	 * 
		            	 * TODO
		            	 * 
		            	 * 
		            	 * 如果符合触发条件
		            	 * 
		            	 * 判断其触发模式triggermode
		            	 * 
		            	 * 如果是url，则直接调用url
		            	 * TODO
		            	 * 
		            	 * 如果是email，则调用email方法
		            	 * 
		            	 * TODO
		            	 * 加入queue方法
		            	 * emailqueue.offer()
		            	 * 
		            	 * 需要有线程专门从emailqueue中不断判断并发送email
		            	 * emailhandlerthread;
		            	 * 
		            	 */
						
						
						
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
     * 
     */
    
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


	/**
	 * HTTP实时数据处理HTTPMessageHandler
	 */
	public void HTTPMessageHandler(){
	}
	
	
	
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

