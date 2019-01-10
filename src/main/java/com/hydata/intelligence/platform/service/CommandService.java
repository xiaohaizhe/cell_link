package com.hydata.intelligence.platform.service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.MQTT;
import com.hydata.intelligence.platform.model.MongoDB;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.utils.MongoDBUtils;
import com.hydata.intelligence.platform.utils.MqttClientUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Transactional
@Service
public class CommandService {
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private MongoDB mongoDB;
	@Autowired
    private MqttReceiveConfig mqttReceiveConfig;
	@Autowired
    private MQTT mqtt;
	@Autowired
    private MqttHandler mqttHandler;
    private Logger logger = LogManager.getLogger(MqttHandler.class);

	private static MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();
	/*private static MongoClient meiyaClient = (MongoClient) mongoDBUtil.getMongoConnect();
	private static MongoCollection<Document> collection = mongoDBUtil.getMongoCollection((com.mongodb.client.MongoClient) meiyaClient,"cell_link","device");
*/

    //@Resource
    //private MqttPahoMessageHandler mqttHandler;

    //待修改
    public JSONObject send(String topic, String content) {

    	MongoClient meiyaClient = mongoDBUtil.getMongoConnect(mongoDB.getHost(),mongoDB.getPort());
		MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");
        /**
         * haizhe
         * (1）根据topic判断其通讯方式
         * (2）根据通讯方式，调用不同的send方法，
         * 若为mqtt，调用mqtt的方法
         */
        //查找device对应的productID
        Map<String, Object> conditions = Maps.newHashMap();
        conditions.put("device_sn", topic);
        JSONArray array = new JSONArray();
        FindIterable<Document> documents = mongoDBUtil.queryDocument(collection, conditions, null, null, null, null, null, null);
        for (Document d : documents) {
            long product_id = d.getInteger("product_id");
            Optional<Product> productOptional = productRepository.findById(product_id);
            if (productOptional.isPresent()) {
                try {
                    // 创建命令消息
                    //MqttMessage message = new MqttMessage(content.getBytes());
                    // 设置消息的服务质量
                    //message.setQos(mqtt.getQos());
                    // 发布消息
                    //MqttClientUtil.getInstance().publish(topic, message);
                    mqttHandler.publish(topic,content,false);
                    /**
                     * haizhe
                     * (1) 存入指令log，
                     * 此处不需要disconnect
                     */
                    logger.info("向设备"+topic+"发送了命令："+content);

                    // 断开连接
                    //MqttReceiveConfig.sendClient.disconnect();
                    // 关闭客户端
                    //sampleClient.close();
                    //System.exit(0);
                } catch (Exception me) {
                    //System.err.println("reason " + me.getReasonCode());
                    //System.err.println("msg " + me.getMessage());
                    //System.err.println("loc " + me.getLocalizedMessage());
                    //System.err.println("cause " + me.getCause());
                    //System.err.println("excep " + me);
                    //me.printStackTrace();
                    logger.debug("向设备："+topic+"下发命令失败");
                    //logger.debug("reason " + me.getReasonCode());
                    logger.debug("msg " + me.getMessage());
                    logger.debug("loc " + me.getLocalizedMessage());
                    logger.debug("cause " + me.getCause());
                    logger.debug("excep " + me);
                    me.printStackTrace();
                    return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
                }


            } else {
                logger.error("未找到设备:"+topic);
                logger.error("命令无法发送");

            }
        }

        //存储命令日志

        //接受回执信息
//      // 构建消息
//      Message<String> messages = MessageBuilder.withPayload(content).setHeader(MqttHeaders.TOPIC, topic).build();
//      // 发送消息
//      mqttHandler.handleMessage(messages);

        return RESCODE.SUCCESS.getJSONRES();
    }
}
