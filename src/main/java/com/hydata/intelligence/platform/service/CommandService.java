package com.hydata.intelligence.platform.service;

import javax.transaction.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.CmdLogs;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.MQTT;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.CmdLogsRepository;
import com.hydata.intelligence.platform.repositories.DeviceRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.utils.MongoDBUtils;
import com.hydata.intelligence.platform.utils.MqttClientUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class CommandService {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
    private MqttReceiveConfig mqttReceiveConfig;
	@Autowired
    private MQTT mqtt;
	@Autowired
    private MqttHandler mqttHandler;
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
    private CmdLogsRepository cmdLogsRepository;
	
    private Logger logger = LogManager.getLogger(MqttHandler.class);

	private static MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();
	/*private static MongoClient meiyaClient = (MongoClient) mongoDBUtil.getMongoConnect();
	private static MongoCollection<Document> collection = mongoDBUtil.getMongoCollection((com.mongodb.client.MongoClient) meiyaClient,"cell_link","device");
*/

    //@Resource
    //private MqttPahoMessageHandler mqttHandler;

    //待修改

    /**
     * MQTT的下发命令
     * @param topic： 设备id
     * @param content：命令信息
     * @param type：命令类型：0为字符串，1为十六进制
     * @return
     */
    public JSONObject send(long topic, String content, int type, long userid) {

    	/*MongoClient meiyaClient = mongoDBUtil.getMongoConnect(mongoDB.getHost(),mongoDB.getPort());
		MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");*/
        /**
         * haizhe
         * (1）根据topic判断其通讯方式
         * (2）根据通讯方式，调用不同的send方法，
         * 若为mqtt，调用mqtt的方法
         */
        //查找device对应的productID
        /*Map<String, Object> conditions = Maps.newHashMap();
        conditions.put("device_sn", topic);
        JSONArray array = new JSONArray();
        FindIterable<Document> documents = mongoDBUtil.queryDocument(collection, conditions, null, null, null, null, null, null);*/

        if (content == null || content.equals("")) {
            return RESCODE.FAILURE.getJSONRES();
        }

        //将16进制的content转换为字符串格式
        if (type ==1){
            content = content.replace(" ", "");
            byte[] baKeyword = new byte[content.length() / 2];
            for (int i = 0; i < baKeyword.length; i++) {
                try {
                    baKeyword[i] = (byte) (0xff & Integer.parseInt(content.substring(i * 2, i * 2 + 2), 16));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                content = new String(baKeyword, StandardCharsets.UTF_8);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        Optional<Device> deviceOptional = deviceRepository.findById(topic);
        if(deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            Optional<Product> productOptional = productRepository.findById(device.getProduct_id());
             if (productOptional.isPresent()) {
                 boolean isMqtt = productOptional.get().getProtocolId()==1;
                    if (isMqtt) {
                        try {
                            // 创建命令消息
                            MqttMessage message = new MqttMessage(content.getBytes());
                            // 设置消息的服务质量
                            message.setQos(mqtt.getQos());
                            // 发布消息
                            MqttClientUtil.getInstance().publish(String.valueOf(topic), message);
                            //mqttHandler.publish(topic,content,true);
                            /**
                             * haizhe
                             * (1) 存入指令log，
                             * 此处不需要disconnect
                             */
                            logger.info("向设备" + topic + "发送了命令：" + content);
                            //储存命令下发日志
                            CmdLogs cmdLog = new CmdLogs();
                            cmdLog.setId(System.currentTimeMillis());
                            cmdLog.setDevice_id(topic);
                            cmdLog.setMsg(content);
                            cmdLog.setProductId(device.getProduct_id());
                            Date date = new Date();
                            cmdLog.setSendTime(date);
                            cmdLog.setUserId(userid);
                            cmdLog.setRes_code(0);
                            cmdLog.setRes_msg("命令已发往设备");
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
                            logger.error("向设备：" + topic + "下发命令失败");
                            //logger.debug("reason " + me.getReasonCode());
                            logger.debug("msg " + me.getMessage());
                            logger.debug("loc " + me.getLocalizedMessage());
                            logger.debug("cause " + me.getCause());
                            logger.debug("excep " + me);
                            me.printStackTrace();
                            return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
                        }
                    } else {
                        logger.info("产品协议不支持命令下发");
                        return RESCODE.NO_CHANGES.getJSONRES();
                    }
             } else {
                 logger.error("产品id未找到,向设备："+topic+"下发命令失败");
                 return RESCODE.DEVICE_SN_NOT_EXIST.getJSONRES();
             }
             logger.info("向设备"+topic+"成功发送了命令："+content);
             return RESCODE.SUCCESS.getJSONRES();
    	}else {
            logger.error("设备信息未找到"+topic+"，命令发送失败");
    		return RESCODE.DEVICE_ID_NOT_EXIST.getJSONRES();
    	}
    	
           
       

        //存储命令日志

        //接受回执信息
//      // 构建消息
//      Message<String> messages = MessageBuilder.withPayload(content).setHeader(MqttHeaders.TOPIC, topic).build();
//      // 发送消息
//      mqttHandler.handleMessage(messages);

        
    }
}
