package com.hydata.intelligence.platform.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.model.MQTT;
import com.hydata.intelligence.platform.utils.MqttClientUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
    private MQTT mqtt;


    private Logger logger = LogManager.getLogger(MqttHandler.class);


    /**
     * haizhe
     * 增加一个方法(供pyt调用）
     * 订阅topic
     */
    public void mqttAddDevice(String topic)throws MqttException {
        MqttClient clinkClient= MqttClientUtil.getInstance();
        try {
            Boolean hasTopic = (topic != null);
            Boolean hasClient = (clinkClient!= null);

            if (!hasClient || !clinkClient.isConnected()) {
                reconnect(clinkClient);
            }

            logger.info("尝试订阅"+topic+"，检查topic:"+hasTopic+"，检查client："+hasClient);
            if (hasTopic && hasClient) {
                IMqttToken token = MqttClientUtil.getInstance().subscribeWithResponse(topic);
                logger.info(topic+"订阅成功======="+token.isComplete());
                //logger.info("成功订阅" + topic);
            }
            } catch (MqttException me) {
                logger.debug(topic+"订阅失败");
                me.printStackTrace();
        }
    }

    /**
     * haizhe
     * 增加一个方法，（供pyt调用）
     * 取消订阅topic
     */
    public void mqttRemoveDevice(String topic)throws  MqttException{
        MqttClient clinkClient= MqttClientUtil.getInstance();
        try{
            Boolean hasTopic = (topic != null);
            Boolean hasClient = (clinkClient!= null);

            if (!hasClient || !clinkClient.isConnected()) {
                reconnect(clinkClient);
                logger.info("MQTT尝试重连");
            }
            if (hasTopic && hasClient && clinkClient.isConnected()) {
                logger.info("成功取消订阅" + topic);
            }
        } catch (  MqttException me) {
            logger.debug(topic+"订阅失败");
            me.printStackTrace();
        }
    }


    public void reconnect(MqttClient mqttClient) throws MqttException {
        try {
            logger.info("MQTT尝试重连");
            MqttClientUtil.getInstance().reconnect();
        }catch (MqttException me){
            logger.debug("mqtt重连失败");
            me.printStackTrace();
        }
    }


    /**
     * MQTT数据解析
     * 实时数据流格式String：name1, value1; name2, value2; ...
     * 返回格式JSONArray：[{"dm_name":"name1","value":"value1","time":"time"},{"dm_name":"name2","value":"value2","time":"time"}, ...]
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
                Date time = new Date(System.currentTimeMillis());
                object.put("dm_name", dm_name);
                object.put("value", value);
                object.put("time",time);
                result.add(object);
            }
        } catch (Exception e){
            logger.error("mqtt数据流解析失败");
            e.printStackTrace();
        }
        logger.info("MQTT实时数据已解析："+result);
        return result;
    }

}