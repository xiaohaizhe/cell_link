package com.hydata.intelligence.platform.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.MQTT;
import com.hydata.intelligence.platform.repositories.CmdLogsRepository;
import com.hydata.intelligence.platform.repositories.DeviceRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;
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
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    private Logger logger = LogManager.getLogger(MqttHandler.class);


    /**
     * haizhe
     * 增加一个方法(供pyt调用）
     * 订阅topic
     */
    public void mqttAddDevice(String topic)throws MqttException {
        MqttClient clinkClient= MqttClientUtil.getInstance();
        try {
            long isLong = 0;
            if (topic.equals("test")){
                isLong = 1;
            } else{
                isLong = Long.parseLong(topic);
            }
            Boolean hasClient = (clinkClient!= null);

            if (!hasClient || !clinkClient.isConnected()) {
                reconnect(clinkClient);
            }

            logger.info("尝试订阅"+topic+"，检查topic:"+isLong+"，检查client："+hasClient);
            if (hasClient) {
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
                me.printStackTrace();/*
            }   catch (Exception e){
                logger.error(topic+"订阅回执发送失败");
                e.printStackTrace();*/
            } catch (NumberFormatException nfe) {
                logger.error(topic+"订阅失败：topic格式错误");
                nfe.printStackTrace();


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
            data.trim();
            data = data.substring(1);
            String[] datas = data.split(";") ;
            logger.info("data.split: "+datas.length);
            for (int i = 0; i < datas.length; i++) {
                logger.info("开始处理第"+i+"个数据"+datas[i]);
                JSONObject object = new JSONObject();
                String[] tmp = datas[i].split(",");
                if(!tmp[0].trim().isEmpty()) {
                    String dm_name = tmp[0].trim();
                    Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
                    if ((tmp.length > 1) && (pattern.matcher(tmp[1].trim()).matches())) {
                        //int value = Integer.parseInt(tmp[1].trim());
                        double number = Double.valueOf(tmp[1].trim());
                        logger.info("dm_name: "+dm_name+", value: "+number);
                        //Date time = new Date(System.currentTimeMillis());
                        //获取当前时间
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = sdf.format(date);
                        object.put("dm_name", dm_name);
                        object.put("value", number);
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
        logger.info(topic);
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
     * @param topic：主题。如果是设备传来的信息，该主题应该为对应的设备id（regCode)
     * @param payload：消息内容。如果是设备传来的信息流，使用mqttDataAnalysis方法进行解析。
     * @throws Exception：见消息解析，触发器判断。
     */
    public void MessageHandler(String topic, String payload) throws Exception{
        //订阅主题为id传递的信息流: id存在于表中
        //boolean isExist = deviceService.checkDevicesn(topic);
        //boolean isNumber = StringUtils.isNumeric(topic);
        int isMqtt = 0;
        //if (isNumber) {
        List<Product> products = productRepository.findByProtocolId(1);
        for (Product product : products) {
            try {
                if (topic.equals("test")){
                    isMqtt = 2;
                } else if (topic.indexOf('/')!=-1){
                    isMqtt = 3;
                    //在此可以添加CmdLog对于res_msg的更新
                } else if (deviceRepository.findById(Long.parseLong(topic)).isPresent()) {
                    isMqtt = 1;
                }
            } catch (Exception e){
                logger.debug("MQTT实时数据流处理失败：topic格式错误，数据流未处理");
            }
        }
        //}
        //logger.info("MQTT信息开始处理，设备已添加："+!isExist+", 设备鉴权码为数字："+isNumber);
        logger.info("MQTT新信息开始处理，设备注册码已找到, topic格式为："+isMqtt);
        if (isMqtt==1) {
            MqttClientUtil.getCachedThreadPool().execute(() -> {
                logger.info("设备"+topic+"传来的信息： "+payload+"加入线程池，开始处理");
                try {
                    //解析收到的实时数据流
                    JSONArray data = mqttDataAnalysis(payload);
                    if (!data.isEmpty()) {
                        //存储实时数据流到mongodb
                        deviceService.dealWithData(Long.parseLong(topic), data);
                        //进行触发器判断
                        triggerService.TriggerAlarm(Long.parseLong(topic), data);
                    }
                } catch (InterruptedException ie) {
                    logger.error(topic + "触发器触发失败");
                    ie.printStackTrace();
                }
            });
        } else if(isMqtt==2) {
            logger.info("收到测试信息数据流:" + payload);
        } else {
            logger.debug(topic+"格式错误，数据流未处理");
/*

            if (topic.equals("test")) {
                logger.info("测试添加订阅成功！");
            }
*/

        }


    }

}