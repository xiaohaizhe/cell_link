package com.hydata.intelligence.platform.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.model.MQTT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Jasmine
 * @createTime: 2018-12-27 14:49
 * @description: <MQTT实时接收消息处理，添加和删除订阅>
 * @modified:
 */
@Service
@Transactional
public class MqttHandler {
    @Autowired
    private MqttReceiveConfig mqttReceiveConfig;
    @Autowired
    private MQTT mqtt;

    private Logger logger = LogManager.getLogger(MqttHandler.class);


    /**
     * haizhe
     * 增加一个方法(供pyt调用）
     * 订阅topic
     */
    public void mqttAddDevice(String topic) throws MqttException {
        try {
            if (mqttReceiveConfig.clinkClient == null || !mqttReceiveConfig.clinkClient.isConnected()) {
                // 创建链接参数
                MqttConnectOptions connOpts = new MqttConnectOptions();
                // 在重新启动和重新连接时记住状态
                connOpts.setCleanSession(false);
                // 设置连接的用户名
                connOpts.setUserName(mqtt.getUserName());
                connOpts.setPassword(mqtt.getPassword().toCharArray());
                //设置遗嘱
                connOpts.setWill("message", "i`m gone".getBytes(), mqtt.getQos(), true);
                mqttReceiveConfig.clinkClient.connect(connOpts);
                logger.info("MQTT尝试重连");
            }
            Boolean hasTopic = (topic != null);
            Boolean hasClient = (mqttReceiveConfig.clinkClient!= null);
            //logger.info("尝试订阅"+topic+"，检查topic:"+hasTopic+"，检查client："+hasClient);
            mqttReceiveConfig.clinkClient.subscribe(topic);
            logger.info("成功订阅"+topic);

            } catch (MqttException me) {
                logger.debug(topic+"订阅失败");
                logger.debug("reason " + me.getReasonCode());
                logger.debug("msg " + me.getMessage());
                logger.debug("loc " + me.getLocalizedMessage());
                logger.debug("cause " + me.getCause());
                logger.debug("excep " + me);
                me.printStackTrace();
        }
    }

    /**
     * haizhe
     * 增加一个方法，（供pyt调用）
     * 取消订阅topic
     */
    public void mqttRemoveDevice(String topic) throws MqttException{
        try{
            if (mqttReceiveConfig.clinkClient == null || !mqttReceiveConfig.clinkClient.isConnected()) {
                // 创建链接参数
                MqttConnectOptions connOpts = new MqttConnectOptions();
                // 在重新启动和重新连接时记住状态
                connOpts.setCleanSession(false);
                // 设置连接的用户名
                connOpts.setUserName(mqtt.getUserName());
                connOpts.setPassword(mqtt.getPassword().toCharArray());
                //设置遗嘱
                connOpts.setWill("message", "i`m gone".getBytes(), mqtt.getQos(), true);

                mqttReceiveConfig.clinkClient.connect(connOpts);
                logger.info("MQTT尝试重连");
            }
            mqttReceiveConfig.clinkClient.unsubscribe(topic);
            logger.info("成功取消订阅" + topic);

        } catch (  MqttException me) {
            logger.debug(topic+"订阅失败");
            logger.debug("reason " + me.getReasonCode());
            logger.debug("msg " + me.getMessage());
            logger.debug("loc " + me.getLocalizedMessage());
            logger.debug("cause " + me.getCause());
            logger.debug("excep " + me);
            me.printStackTrace();
        }
    }

    /**
     * MQTT数据解析
     * 实时数据流格式String：name1, value1; name2, value2; ...
     * 返回格式JSONArray：[{"dm_name":"name1","value":"value1"},{"dm_name":"name2","value":"value2"}, ...]
     */
    public JSONArray mqttDataAnalysis(String data){
        //JSONArray result = JSONArray.parseArray(data);
        JSONArray result = new JSONArray();
        try {
        String[] datas = data.split(";") ;
        for (int i = 0; i < datas.length; i++) {
            JSONObject object = new JSONObject();
            String[] tmp = datas[i].split(",");
            String dm_name = tmp[0].trim();
            int value = Integer.parseInt(tmp[1].trim());
            object.put("dm_name", dm_name);
            object.put("value", value);
            result.add(object);
            }
        } catch (Exception e){
            logger.error("mqtt数据流解析失败");
            e.printStackTrace();
        }
        return result;
    }



}