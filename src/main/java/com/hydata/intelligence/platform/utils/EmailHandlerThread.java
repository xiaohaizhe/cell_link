package com.hydata.intelligence.platform.utils;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.python.jline.internal.Log;

import com.hydata.intelligence.platform.model.EmailHandlerModel;
import com.hydata.intelligence.platform.service.DeviceService;
import com.hydata.intelligence.platform.service.MqttReceiveConfig;

public class EmailHandlerThread extends Thread{
private static Logger logger = LogManager.getLogger(EmailHandlerThread.class);
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			if(MqttReceiveConfig.emailQueue != null &&  !MqttReceiveConfig.emailQueue.isEmpty())
			{
				try {
					EmailHandlerModel model = MqttReceiveConfig.emailQueue.poll(2, TimeUnit.SECONDS);
					/**
					 * haizhe
					 * 取出来后进行发邮件处理
					 * TODO
					 */
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.error("xxxxxxxxxxxxxxxx");
				}
			}
		}
	}

}
