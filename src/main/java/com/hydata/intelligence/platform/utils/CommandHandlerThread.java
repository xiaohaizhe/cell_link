package com.hydata.intelligence.platform.utils;

import com.hydata.intelligence.platform.model.CommandHandlerModel;
import com.hydata.intelligence.platform.model.MQTT;
import com.hydata.intelligence.platform.repositories.CmdLogsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.*;


public class CommandHandlerThread extends Thread{
    private static Logger logger = LogManager.getLogger(CommandHandlerThread.class);
    @Autowired
    private MQTT mqtt;
    @Autowired
    private CmdLogsRepository cmdLogsRepository;

    @Override
    public void run() {
        while(true)
        {
            if(MqttClientUtil.getCommandQueue()!=null &&!MqttClientUtil.getCommandQueue().isEmpty()){
                try {
                    logger.info("尝试下发命令");
                    CommandHandlerModel model = MqttClientUtil.getCommandQueue().poll(2, TimeUnit.SECONDS);
                    // 创建命令消息
                    MqttMessage message = new MqttMessage(model.getCmd().getBytes());
                    // 设置消息的服务质量
                    logger.info("准备发送命令， MQTT连接情况：" + MqttClientUtil.getInstance().isConnected());
                    // 发布消息
                    MqttClientUtil.getInstance().publish(model.getDeviceId() + "/cmd", model.getCmd().getBytes(),
                            mqtt.getQos(), false);
                    //mqttHandler.publish(topic,content,true);
                    logger.info("向设备" + model.getDeviceId() + "发送了命令：" + model.getCmd());
                }catch (Exception e) {
                    logger.error("MQTT命令下发失败");
                    e.printStackTrace();
                }
            }
        }
    }

}
