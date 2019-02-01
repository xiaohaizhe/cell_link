package com.hydata.intelligence.platform.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.model.MQTT;
import com.hydata.intelligence.platform.utils.Config;
import com.hydata.intelligence.platform.utils.MqttClientUtil;
import com.hydata.intelligence.platform.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
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
    @Autowired
    private MqttReceiveConfig mqttReceiveConfig;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private TriggerService triggerService;

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
                //测试！向所有订阅的topic里发送测试信息
                //clinkClient.publish(topic,(topic+" subscribed!").getBytes(),mqtt.getQos(),false);
                //publish(topic,(topic+" unsubscribed"),0,false);
                //发送粘性测试信息至broker
                //clinkClient.publish("test",(topic+"subscribed.").getBytes(),mqtt.getQos(),false);
                //logger.info("成功订阅" + topic);
                //publish("test",(topic+" unsubscribed"),0,false);
            }
            } catch (MqttException me) {
                logger.error(topic+"订阅失败");
                me.printStackTrace();
            }   catch (Exception e){
                logger.error(topic+"订阅回执发送失败");
                e.printStackTrace();
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
                //发送粘性测试信息至broker
                //clinkClient.publish("test",(topic+" unsubscribed.").getBytes(),mqtt.getQos(),true);
                //publish(topic,(topic+" unsubscribed"),0,false);
                //publish("test",(topic+" unsubscribed"),0,false);
            }
        } catch (  MqttException me) {
            logger.error(topic+"订阅失败");
            me.printStackTrace();
        }catch (Exception e){
            logger.error(topic+"取消订阅回执发送失败");
            e.printStackTrace();
        }
    }


    public void reconnect(MqttClient mqttClient) throws MqttException {
        try {
            logger.info("MQTT尝试重连");
            MqttClientUtil.getInstance().reconnect();
        }catch (MqttException me){
            logger.error("mqtt重连失败");
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
                if(!tmp[0].trim().isEmpty()) {
                    String dm_name = tmp[0].trim();
                    if ((tmp.length > 1) && (tmp[1].trim().matches("^[0-9]\\d*$"))) {
                        int value = Integer.parseInt(tmp[1].trim());
                        //Date time = new Date(System.currentTimeMillis());
                        //获取当前时间
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = sdf.format(date);
                        object.put("dm_name", dm_name);
                        object.put("value", value);
                        object.put("time", time);
                        result.add(object);
                    }
                } else {
                    logger.debug("MQTT上传信息流格式错误");
                }
            }
            if(!result.isEmpty()) {
                logger.info("MQTT实时数据已解析：" + result);
            }
        } catch (Exception e){
            logger.error("MQTT数据流解析失败");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * MQTT信息发送：向默认主题发送信息
     * @param message：发送信息内容
     * @throws Exception
     */
    public void publish(String message) throws Exception{
        String topic = Config.getString("mqtt.defaultTopic");
        try {
            //TODO: 发布信息堵塞问题待解决
            MqttClientUtil.getSemaphore().acquire();
            MqttClientUtil.getInstance().publish(topic, message.getBytes(), mqtt.getQos(), false);
            logger.info("向主题"+topic+"发送了信息："+message);
        } catch (InterruptedException ie){
            logger.error("信息发送失败：信息堵塞");
            ie.printStackTrace();
        } catch (MqttException me){
            logger.error("信息发送失败：");
            logger.error("原因："+me.getCause());
        }
    }

    /**
     * MQTT信息发送：发送信息，使用默认QOS
     * @param topic: 主题
     * @param message: 消息内容
     * @throws Exception
     */
    public void publish(String topic, String message) throws Exception{
        try {
            //TODO: semaphore null
            MqttClientUtil.getSemaphore().acquire();
            MqttClientUtil.getInstance().publish(topic, message.getBytes(), mqtt.getQos(), false);
            logger.info("向主题"+topic+"发送了信息："+message);
        } catch (InterruptedException ie){
            logger.error("信息发送失败：信息堵塞");
            ie.printStackTrace();
        } catch (MqttException me){
            logger.error("信息发送失败：");
            logger.error("原因："+me.getCause());
        }
    }

    /**
     * MQTT信息发送：发送信息
     * @param topic： 主题
     * @param message： 消息内容
     * @param retained: 消息粘性
     * @throws Exception
     */

    public void publish(String topic, String message, Boolean retained) throws Exception{
        try {
            MqttClientUtil.getSemaphore().acquire();
            MqttClientUtil.getInstance().publish(topic, message.getBytes(), mqtt.getQos(), retained);
            logger.info("向主题"+topic+"发送了信息："+message);
        } catch (InterruptedException ie){
            logger.error("信息发送失败：信息堵塞");
            ie.printStackTrace();
        } catch (MqttException me){
            logger.error("信息发送失败：");
            logger.error("原因："+me.getCause());
        }
    }

    /**
     * MQTT信息发送
     * @param topic：主题
     * @param message：消息内容
     * @param qos：消息质量
     * @param retained：消息粘性
     * @throws Exception
     */
    public void publish(String topic, String message, int qos, Boolean retained) throws Exception{
        try {
            MqttClientUtil.getSemaphore().acquire();
            MqttClientUtil.getInstance().publish(topic, message.getBytes(), qos, retained);
            logger.info("向主题"+topic+"发送了信息："+message);
        } catch (InterruptedException ie){
            logger.error("信息发送失败：信息堵塞");
            ie.printStackTrace();
        } catch (MqttException me){
            logger.error("信息发送失败：");
            logger.error("原因："+me.getCause());
        }
    }

    /**
     * 对设备传来的实时信息进行处理
     * @param topic：主题。如果是设备传来的信息，该主题应该为对应的设备鉴权码(Device_sn)
     * @param payload：消息内容。如果是设备传来的信息流，使用mqttDataAnalysis方法进行解析。
     * @throws Exception：见消息解析，触发器判断。
     */
    public void MessageHandler(String topic, String payload) throws Exception{
        //订阅主题为device_Sn传递的信息流: device_Sn重复且为数字
        boolean isExist = deviceService.checkDevicesn(topic);
        boolean isNumber = StringUtils.isNumeric(topic);
        logger.info("MQTT信息开始处理，设备已添加："+!isExist+", 设备鉴权码为数字："+isNumber);
        if (!isExist && isNumber) {
            MqttClientUtil.getCachedThreadPool().execute(() -> {
                logger.info("设备"+topic+"传来的信息： "+payload+"加入线程池，开始处理");
                try {
                    //解析收到的实时数据流
                    JSONArray data = mqttDataAnalysis(payload);
                    if (!data.isEmpty()) {
                        //存储实时数据流到mongodb
                        deviceService.dealWithData(topic, data);
                        //进行触发器判断
                        triggerService.TriggerAlarm(topic, data);
                    }
                } catch (InterruptedException ie) {
                    logger.error(topic + "触发器触发失败");
                    ie.printStackTrace();
                }
            });
        } else {
            logger.debug(topic+"不是数字，数据流未处理");
/*

            if (topic.equals("test")) {
                logger.info("测试添加订阅成功！");
            }
*/

        }

    }

}