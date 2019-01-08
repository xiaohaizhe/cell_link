package com.hydata.intelligence.platform.utils;

import com.hydata.intelligence.platform.model.EmailHandlerModel;
import com.hydata.intelligence.platform.model.MQTT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MqttClientUtil {
    private static MqttClient instance = null;
    private static MqttConnectOptions connOpts;
    private static Logger logger = LogManager.getLogger(MqttClientUtil.class);

    private MqttClientUtil() {
    }

    /**
     * 返回MqttClient的单例
     *
     * @return
     */
    @Autowired
    private MQTT mqtt;

    private static MQTT smqtt;

    @PostConstruct
    public void beforeInit() {
        smqtt = mqtt;
    }


    public static MqttClient getInstance() {
        if (instance == null) {
            synchronized (MqttClientUtil.class) {
                try {
                    if (instance == null) {
                        logger.info("MQTT连接初始化");
                        // 新建client
                        connOpts = new MqttConnectOptions();
                        // 内存存储
                        MemoryPersistence persistence = new MemoryPersistence();

                        instance = new MqttClient(smqtt.getBroker(), smqtt.getClientId(), persistence);
                    }

                    //断开连接时
                    connOpts.setCleanSession(smqtt.getCleanSession());
                    connOpts.setUserName(smqtt.getUserName());
                    connOpts.setPassword(smqtt.getPassword().toCharArray());
                    connOpts.setWill("message", "cell-link断开连接".getBytes(), 1, true);
                    logger.info("MQTT完成连接设置");
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

    public static BlockingQueue<EmailHandlerModel> emailQueue = null;
    public static ExecutorService cachedThreadPool;
    public static EmailHandlerThread emailThread = new EmailHandlerThread();

    public static BlockingQueue<EmailHandlerModel> getEmailQueue()    {
        if (emailQueue == null) {
            synchronized (MqttClientUtil.class) {
                if (emailQueue == null) {
                    logger.info("线程池初始化");
                    cachedThreadPool = Executors.newCachedThreadPool();
                    emailQueue = new ArrayBlockingQueue<EmailHandlerModel>(30);
                    emailThread.start();
                }
            }
        }
        return emailQueue;
    }

    public static ExecutorService getCachedThreadPool(){
        return cachedThreadPool;
    }


}