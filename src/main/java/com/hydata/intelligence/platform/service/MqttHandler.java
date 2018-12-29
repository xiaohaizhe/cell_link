package com.hydata.intelligence.platform.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;

import static com.hydata.intelligence.platform.service.MqttReceiveConfig.*;

/**
 * @author: Jasmine
 * @createTime: 2018-12-27 14:49
 * @description: <MQTT接收消息处理，添加和删除订阅>
 * @modified:
 */
public class MqttHandler {

    @Autowired
    private DatastreamModelRepository datastreamModelRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TriggerRepository triggerRepository;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private TriggerService triggerService;
    @Autowired
    private DeviceDatastreamRepository deviceDatastreamRepository;
    @Autowired
    private DdTriggerRepository ddTriggerRepository;
    @Autowired
    private TriggerTypeRepository triggerTypeRepository;

    private static int qos = 2;

    private static Logger logger = LogManager.getLogger(MqttReceiveConfig.class);

    /**
     * haizhe
     * 增加一个方法(供pyt调用）
     * 添加topic，传入sn进来，将其加为topic
     * @return
     */
    //@Bean
    //public static MessageChannel mqttDataChannel() {
    //    return new DirectChannel();
    //}

    public static void mqttAddDevice(String topic) throws MqttException {
        receiveClient.subscribe(topic);
        //adapter.addTopic(topic);
    }


    /**
     * haizhe
     * 增加一个方法，（供pyt调用）
     * 删除topic，传入sn进来，将其topic去除，不再订阅
     * TODO
     * @return
     */

    public static void mqttRemoveDevice(String topic) throws MqttException{
        receiveClient.unsubscribe(topic);
        //adapter.removeTopic(topic);
    }

/**
    //通过通道获取数据
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get("deviceSn").toString();
                String content = message.getPayload().toString();
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        //解析收到的实时数据流
                        JSONArray data= mqttDataAnalysis(content);
                        //存储实时数据流到mongodb
                        deviceService.dealWithData(topic, data);
                        //进行触发器判断
                        try {
                            triggerService.TriggerAlarm(topic,data);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        };
    }

**/

    /**
     * MQTT实时数据流接收
     * 解析+储存+触发器
     */
    public void messageArrived(String topic, MqttMessage message) {
    cachedThreadPool.execute(() -> {
        String payload = new String(message.getPayload());
        //解析收到的实时数据流
        JSONArray data= MqttHandler.mqttDataAnalysis(payload);
        //存储实时数据流到mongodb
        deviceService.dealWithData(topic, data);
        //进行触发器判断
        try {
            triggerService.TriggerAlarm(topic,data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    });
}
    /**
     * MQTT数据解析
     * 实时信息流格式String：name1, value1; name2, value2;...
     * 返回格式JSONArray：[{"dm_name":"name1","value":"value1"},{"dm_name":"name2","value":"value2"},...]
     */
    public static JSONArray mqttDataAnalysis(String data){
        //JSONArray result = JSONArray.parseArray(data);
        JSONObject object = new JSONObject();
        JSONArray result = new JSONArray();
        String[] datas = data.split(";") ;
        for (int i = 0; i<datas.length; i++){
            String[] tmp = datas[i].split(",");
            String dm_name = tmp[0];
            int value = Integer.parseInt(tmp[1]);
            object.put("dm_name", dm_name);
            object.put("value", value);
            result.add(object);
        }
        return result;
    }



}
