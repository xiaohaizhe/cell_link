package com.hydata.intelligence.platform.utils;

import com.hydata.intelligence.platform.model.CommandHandlerModel;
import com.hydata.intelligence.platform.model.EmailHandlerModel;
import com.hydata.intelligence.platform.model.MQTT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

//@Component
public class MqttClientUtil {
    private static MqttClient instance = null;
    private static MqttConnectOptions connOpts;
    private static Logger logger = LogManager.getLogger(MqttClientUtil.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private MqttClientUtil() {
    }

    /**
     * 返回MqttClient的单例
     *
     * @return
     */
/*
    @Autowired
    private MQTT mqtt;

    private static MQTT smqtt;

    @PostConstruct
    public void beforeInit() {
        smqtt = mqtt;
    }
*/

    private static String broker = Config.getString("mqtt.serverURI");
    private static String clientId = Config.getString("mqtt.clientId");
    private static String userName = Config.getString("mqtt.username");
    private static String password = Config.getString("mqtt.password");
    private static String cleanSession = Config.getString("mqtt.cleanSession");
    //private static final int MAX_IN_FLIGHT = Config.getInt("mqtt.maxinFlight");

    private static ExecutorService cachedThreadPool = null;
    private static BlockingQueue<EmailHandlerModel> emailQueue = null;
    private static EmailHandlerThread emailThread = new EmailHandlerThread();
    private static BlockingQueue<CommandHandlerModel> commandQueue = null;
    private static CommandHandlerThread commandThread = new CommandHandlerThread();

    public static MqttClient getInstance() throws MqttException {
        if (instance == null) {
            synchronized (MqttClientUtil.class) {
                try {
                    if (instance == null) {
                        logger.info("==========MQTT连接初始化==========");
                        // 新建client
                        connOpts = new MqttConnectOptions();
                        // 生成clientID
                        Date date = new Date();
                        clientId = clientId+ sdf.format(date);
                        // 内存存储
                        MemoryPersistence persistence = new MemoryPersistence();
                        instance = new MqttClient(broker, clientId, persistence);
                        logger.info("读取broker地址： "+broker);
                        logger.info("读取client id: "+clientId);
                        logger.info("读取用户名: "+userName);
                        logger.info("读取密码: "+password);
                    }

                    //载入MQTT连接设置
                    //TODO: 正式版cleanSession为false
                    connOpts.setCleanSession(!cleanSession.equals("true"));
                    connOpts.setUserName(userName);
                    connOpts.setPassword(password.toCharArray());
                    //connOpts.setMaxInflight(MAX_IN_FLIGHT);
                    connOpts.setWill("message", "cell-link lost connection".getBytes(), 1, true);
                    logger.info("=========MQTT完成连接设置==========");
                }catch (MqttException e) {
                    logger.error("MQTT连接初始化失败");
                    e.printStackTrace();
                }
            }
        }
        return instance;
    }

    public static MqttConnectOptions getOptions() {
        return connOpts;
    }

    public static BlockingQueue<EmailHandlerModel> getEmailQueue()    {
        if (emailQueue == null) {
            synchronized (MqttClientUtil.class) {
                if (emailQueue == null) {
                    logger.info("触发器邮件发送线程池初始化");
                    //cachedThreadPool = Executors.newCachedThreadPool();
                    emailQueue = new ArrayBlockingQueue<EmailHandlerModel>(50);
                    //semaphore = new Semaphore(MAX_IN_FLIGHT);
                    emailThread.start();
                }
            }
        }
        return emailQueue;
    }

    public static BlockingQueue<CommandHandlerModel> getCommandQueue()    {
        if (commandQueue == null) {
            synchronized (MqttClientUtil.class) {
                if (commandQueue == null) {
                    logger.info("MQTT命令下发线程池初始化");
                    //cachedThreadPool = Executors.newCachedThreadPool();
                    commandQueue = new ArrayBlockingQueue<CommandHandlerModel>(50);
                    //semaphore = new Semaphore(MAX_IN_FLIGHT);
                    commandThread.start();
                }
            }
        }
        return commandQueue;
    }


    public static ExecutorService getCachedThreadPool(){
        if (cachedThreadPool == null) {
            synchronized (MqttClientUtil.class) {
                if (cachedThreadPool == null) {
                    logger.info("实时信息解析线程池初始化");
                    cachedThreadPool = Executors.newCachedThreadPool();
                    //emailQueue = new ArrayBlockingQueue<EmailHandlerModel>(50);
                    //semaphore = new Semaphore(MAX_IN_FLIGHT);
                    //emailThread.start();
                }
            }
        }
        return cachedThreadPool;
    }

}