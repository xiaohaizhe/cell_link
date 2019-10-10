package com.hydata.intelligence.platform.cell_link.service;

/**
 * @author: Jasmine
 * @createTime: 2019-09-03 15:17
 * @description:
 * @modified:
 */
import com.hydata.intelligence.platform.cell_link.entity.Device;
import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.DeviceGroupRepository;
import com.hydata.intelligence.platform.cell_link.repository.DeviceRepository;
import com.hydata.intelligence.platform.cell_link.repository.ScenarioRepository;
import com.hydata.intelligence.platform.cell_link.utils.MqttUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

/**
 * @author: Jasmine
 * @createTime:
 * @description: <MQTT消费端初始化> ：建立长连接，订阅已添加设备
 * @modified:
 */
@Transactional
@Service
public class MqttInitService {
    /*private static MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();*/

    @Autowired
    private MqttHandlerService mqttHandler;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private ScenarioRepository scenarioRepository;
    @Autowired
    private DeviceGroupRepository deviceGroupRepository;

    private Logger logger = LogManager.getLogger(MqttInitService.class);
    private MqttClient clinkClient;

    /**
     * @author: Jasmine
     * @createTime:
     * @description: <建立MQTT连接，并订阅已添加设备>
     * @modified:
     */
    public void init() throws MqttException{
        try {
            logger.debug("进入初始化");
            //TODO:初始化线程池：信息处理线程池以及触发器发送邮件线程池
            //logger.info("MQTT线程池初始化");
            clinkClient = MqttUtils.getInstance();
            IMqttToken token = clinkClient.connectWithResult(MqttUtils.getOptions());
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
                        mqttHandler.reconnect();
                    } catch (Exception e) {
                        logger.error("MQTT重连失败");
                        logger.error(e);					}
                }

                public void messageArrived(String topic, MqttMessage message){
                    //System.out.println("topic:"+topic);
                    //System.out.println("Qos:"+message.getQos());
                    //System.out.println("message content:"+new String(message.getPayload()));
                    String payload = new String(message.getPayload());
                    //payload = payload.substring(1);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                    logger.info("==========接收到实时信息==========");
                    logger.info("主题：" + topic);
                    logger.info("Qos:" + message.getQos());
                    logger.info("内容:" + payload);
                    logger.info("时间："+sdf.format(System.currentTimeMillis()));
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
                clinkClient.publish("test","cell-link initialized".getBytes(), 0,false);
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
            //判断设备所在产品是否为mqtt格式
            List<DeviceGroup> dgs = deviceGroupRepository.findByProtocol(2);
            logger.debug("MQTT协议下的设备组:");
            for (DeviceGroup dg:dgs){
                logger.info("设备组id:"+dg.getDgId());
                List<Device> devices = deviceRepository.findByDeviceGroup(dg.getDgId());
                logger.info("设备组下的设备");
                for (Device device:devices) {
                    logger.info("设备编码：" + device.getDeviceId());
                    mqttHandler.mqttAddDevice(String.valueOf(device.getDeviceId()));
                }
            }
            logger.info("初始化订阅结束");
            logger.info("------------------------------");
        } catch(MqttException me){
            logger.error("mqtt连接失败");
            me.printStackTrace();
        }
/**
            List<Product> products = productRepository.findByProtocolId(1);
            for (Product product : products) {
                List<Device> deviceList = deviceRepository.findByProductId(product.getId());
                for(Device device:deviceList) {
                    logger.info("设备编码："+device.getId());
                    mqttHandler.mqttAddDevice(String.valueOf(device.getId()));
                }

            }
            logger.info("初始化订阅结束");
            logger.info("------------------------------");
**/



        //发送粘性测试信息至broker
        try {
            clinkClient.publish("test", "Traversed MongoDB to add topics".getBytes(),0,false);
            //mqttHandler.publish("test", "Traversed MongoDB to add topics",0,true);
        } catch (Exception e){
            logger.error("MQTT 测试信息发送失败");
            e.printStackTrace();
        }

        // 断开连接
        //clinkClient.disconnect();
        // 关闭客户端
        //clinkClient.close();
        //System.exit(0);

    }

}
