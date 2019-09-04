package com.hydata.intelligence.platform.cell_link.service;

/**
 * @author: Jasmine
 * @createTime: 2019-09-03 15:19
 * @description:
 * @modified:
 */
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.Device;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.DeviceRepository;
import com.hydata.intelligence.platform.cell_link.utils.MqttUtils;
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
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author: Jasmine
 * @createTime: 2018-12-27 14:49
 * @description: <MQTT实时接收消息处理，添加和删除订阅>
 * @modified:
 */
@Service
@Transactional
public class MqttHandlerService {
    @Autowired
    private MqttInitService mqttInitService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceRepository deviceRepository;

    /* TODO:待添加
    @Autowired
    private TriggerService triggerService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CmdLogsRepository cmdLogsRepository;
    @Autowired
    private MQTT mqtt;
*/

    private Logger logger = LogManager.getLogger(MqttHandlerService.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");


    /**
     * haizhe
     * 增加一个方法(供pyt调用）
     * 订阅topic
     */
    public JSONObject mqttAddDevice(String topic)throws MqttException {
        MqttClient clinkClient= MqttUtils.getInstance();
        try {
            long isLong = 0;
            if (topic.equals("test")){
                isLong = 1;
            } else{
                isLong = Long.parseLong(topic);
            }
            Boolean hasClient = (clinkClient!= null);

            if (!hasClient || !clinkClient.isConnected()) {
                reconnect();
            }

            logger.info("尝试订阅"+topic+"，检查topic:"+isLong+"，检查client："+hasClient);
            if (hasClient) {
                IMqttToken token = MqttUtils.getInstance().subscribeWithResponse(topic);
                MqttUtils.getInstance().subscribe(topic +"/#");
                logger.info(topic+"订阅成功======="+token.isComplete());
                //测试！向所有订阅的topic里发送测试信息
                //clinkClient.publish(topic,(topic+" subscribed!").getBytes(),mqtt.getQos(),false);
                //publish(topic,(topic+" unsubscribed"),0,false);
                //发送粘性测试信息至broker
                //clinkClient.publish("test",(topic+"subscribed.").getBytes(),mqtt.getQos(),false);
                //logger.info("成功订阅" + topic);
                //publish("test",(topic+" unsubscribed"),0,false);
                return RESCODE.SUCCESS.getJSONRES();
            }
        } catch (MqttException me) {
            logger.error(topic+"订阅失败");
            logger.error(me);
                /*me.printStackTrace();
            }   catch (Exception e){
                logger.error(topic+"订阅回执发送失败");
                e.printStackTrace();*/
            return RESCODE.MQTT_DISCONNECTED.getJSONRES();
        } catch (NumberFormatException nfe) {
            logger.error(topic+"订阅失败：topic格式错误");
            logger.error(nfe);


        }
        return RESCODE.FAILURE.getJSONRES();

    }

    /**
     * haizhe
     * 增加一个方法，（供pyt调用）
     * 取消订阅topic
     */
    public JSONObject mqttRemoveDevice(String topic)throws  MqttException{
        MqttClient clinkClient= MqttUtils.getInstance();
        try{
            Boolean hasTopic = (topic != null);
            Boolean hasClient = (clinkClient!= null);

            if (!hasClient || !clinkClient.isConnected()) {
                reconnect();
                logger.info("MQTT尝试重连");
            }
            if (hasTopic && hasClient && clinkClient.isConnected()) {
                logger.info("成功取消订阅" + topic);
                //发送粘性测试信息至broker
                //clinkClient.publish("test",(topic+" unsubscribed.").getBytes(),mqtt.getQos(),true);
                //publish(topic,(topic+" unsubscribed"),0,false);
                //publish("test",(topic+" unsubscribed"),0,false);
                return RESCODE.SUCCESS.getJSONRES();
            }
        }catch (Exception e){
            logger.error(topic+"取消订阅回执发送失败");
        }
        return RESCODE.FAILURE.getJSONRES();
    }

    /**
     *  mqtt断线重连
     * @return broker连接状态
     */
    public JSONObject reconnect() {
        try {
            if (MqttUtils.getInstance().isConnected()){
                return RESCODE.NO_CHANGES.getJSONRES();
            }
            logger.info("MQTT尝试重连");
            MqttUtils.getInstance().reconnect();
        }catch (MqttException me){
            logger.error("MQTT重连失败");
            return RESCODE.MQTT_DISCONNECTED.getJSONRES();
        }
        return brokerStatus();
    }

    /**
     * 查询cell_link与broker的连接情况
     * @return broker连接状态
     */
    public JSONObject brokerStatus(){
        JSONObject object = new JSONObject();
        try {
            logger.info("查询broker连接状态===="+MqttUtils.getInstance().isConnected());
            object.put("当前连接状态", MqttUtils.getInstance().isConnected());
            object.put("用户名",MqttUtils.getOptions().getUserName());
            object.put("密码",MqttUtils.getOptions().getPassword());
            object.put("信息",MqttUtils.getOptions().getDebug());
        } catch (MqttException me){
            return RESCODE.MQTT_DISCONNECTED.getJSONRES(object);
        }
        return RESCODE.SUCCESS.getJSONRES(object);
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
            //logger.info("data.split: "+datas.length);
            for (int i = 0; i < datas.length; i++) {
                //logger.info("开始处理第"+i+"个数据"+datas[i]);
                JSONObject object = new JSONObject();
                String[] tmp = datas[i].split(",");
                if(!tmp[0].trim().isEmpty()) {
                    String dm_name = tmp[0].trim();
                    Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
                    if ((tmp.length > 1) ) {
                        if (dm_name.matches("time")) {
                            String sendtime = tmp[1].trim();
                            Date date = new Date();
                            String revtime = sdf.format(date);
                            long m = sdf.parse(revtime).getTime() - sdf.parse(sendtime).getTime();
                            logger.debug("相差毫秒数： "+m);
                        } else if (pattern.matcher(tmp[1].trim()).matches()) {
                            //int value = Integer.parseInt(tmp[1].trim());
                            double number = Double.valueOf(tmp[1].trim());
                            //Date time = new Date(System.currentTimeMillis());
                            //获取当前时间
                            Date date = new Date();
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String time = sdf2.format(date);
                            //logger.info("dm_name: " + dm_name + ", value: " + number);
                            object.put("dm_name", dm_name);
                            object.put("value", number);
                            object.put("time", time);
                            //logger.info(object.getString("time"));
                            //logger.info(sdf2.parse(object.getString("time")));
                            result.add(object);
                        }
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
     * 对设备传来的实时信息进行处理
     * @param topic：主题。如果是设备传来的信息，该主题应该为对应的设备id（regCode)
     * @param payload：消息内容。如果是设备传来的信息流，使用mqttDataAnalysis方法进行解析。
     * @throws Exception：见消息解析，触发器判断。
     */
    public void MessageHandler(String topic, String payload) throws Exception {
        //订阅主题为id传递的信息流: id存在于表中
        //boolean isExist = deviceService.checkDevicesn(topic);
        //boolean isNumber = StringUtils.isNumeric(topic);
        int isMqtt = 0;
        Date d1 = new Date();
        String t1 = sdf.format(d1);

        //if (isNumber) {
        try {
            if (topic.equals("test")) {
                isMqtt = 2;
            } else if (topic.indexOf('/') != -1) {
                isMqtt = 3;
            } else {
        /*TODO：判断是否协议为MQTT

                Optional<Device> device = deviceRepository.findById(Long.parseLong(topic));
                if (device.isPresent()){
                    Optional<Product>product = productRepository.findById(device.get().getProduct_id());
                    if ((product.isPresent())&&(product.get().getProtocolId()==1)){
                        isMqtt = 1;
                    }
                }
*/

                /*
                List<Product> products = productRepository.findByProtocolId(1);
                for (Product product : products) {
                    if (deviceRepository.findByProductIdandId(product.getId(),Long.parseLong(topic)).isPresent()) {
                        isMqtt = 1;
                        break;
                    }
                }*/

            }
        } catch(Exception e){
            logger.debug("MQTT实时数据流处理失败：topic格式错误，数据流未处理");
        }
        Date d2 = new Date();
        String t2 = sdf.format(d2);

        //}
        //logger.info("MQTT信息开始处理，设备已添加："+!isExist+", 设备鉴权码为数字："+isNumber);
        logger.info("MQTT新信息开始处理，设备注册码已找到, topic格式为：" + isMqtt);
        if (isMqtt == 1) {
            //开始解析数据流
            //判断离线信息
            Boolean isData = true;
            if (payload.equals("offline")){
                isData = false;
                Optional <Device> device = deviceRepository.findById(Long.parseLong(topic));
                if (device.isPresent()){
                    //device.get().setStatus(0);
                    deviceRepository.save(device.get());
                }
            }
            //判断在线信息
            if(isData) {
                Optional <Device> device = deviceRepository.findById(Long.parseLong(topic));
                if (device.isPresent()){
                    //device.get().setStatus(1);
                    deviceRepository.save(device.get());
                }
                MqttUtils.getMqttCachedThreadPool().execute(() -> {
                    logger.info("设备" + topic + "传来的信息： " + payload + "加入线程池，开始处理");
                    try {
                        //解析收到的实时数据流
                        JSONArray data = mqttDataAnalysis(payload);
                        if (!data.isEmpty()) {
                            Date d3 = new Date();
                            String t3 = sdf.format(d3);

                            //TODO:存储实时数据流到mongodb
                            //deviceService.dealWithData(Long.parseLong(topic), data);

                            Date d4 = new Date();
                            String t4 = sdf.format(d4);

                            //TODO:进行事件判断
                            //triggerService.TriggerAlarm(Long.parseLong(topic), data);

                            Date d5 = new Date();
                            String t5 = sdf.format(d5);
                            long m1 = sdf.parse(t2).getTime() - sdf.parse(t1).getTime();
                            long m2 = sdf.parse(t3).getTime() - sdf.parse(t2).getTime();
                            long m3 = sdf.parse(t4).getTime() - sdf.parse(t3).getTime();
                            long m4 = sdf.parse(t5).getTime() - sdf.parse(t4).getTime();

                            //logger.debug("时间节点：1. "+t1+", 2. "+t2+", 3. "+t3+", 4. "+t4+", 5. "+t5);

                            //logger.debug("查找设备相差毫秒数： "+m1+"处理信息相差毫秒数： "+m2+"，储存信息相差毫秒数： "+m3+", 触发信息相差毫秒数： "+m4);

                        }
                    } catch (Exception ie) {
                        logger.error(topic + "触发器触发失败");
                        ie.printStackTrace();
                    }
                });
            }
        } else if (isMqtt == 2) {
            logger.info("收到测试信息数据流:" + payload);
        } else if (isMqtt == 3){
            logger.info("收到命令数据"+payload);
            //TODO:命令回执
        }else {
            logger.debug(topic+"格式错误，数据流未处理");

        }


    }

    /**
     * 判断设备异常情况
     * @param device_id: 设备id
     * @return object: 返回设备状态"status": 1为在线（正常），0为离线（异常）
     */
    /*TODO:
    public JSONObject checkStatus(long device_id){
        //判断协议是否为MQTT
        List<Product> products = productRepository.findByProtocolId(1);
        for (Product product : products) {
            if (!deviceRepository.findById(device_id).isPresent()) {
                return RESCODE.DEVICE_ID_NOT_EXIST.getJSONRES();
            }
        }
        JSONObject object = new JSONObject();
        //检查设备在线情况
        Optional<Device> device = deviceRepository.findById(device_id);
        if (device.isPresent()) {
            if (device.get().getStatus() == 0) {
                return RESCODE.SUCCESS.getJSONRES(0);
            } else {
                return RESCODE.SUCCESS.getJSONRES(1);
            }
        } else {
            return RESCODE.DEVICE_NOT_EXIST.getJSONRES();

        }
    }
*/
}
