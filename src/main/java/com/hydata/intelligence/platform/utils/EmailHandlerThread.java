package com.hydata.intelligence.platform.utils;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.hydata.intelligence.platform.model.RESCODE;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.python.jline.internal.Log;

import com.hydata.intelligence.platform.model.EmailHandlerModel;
import com.hydata.intelligence.platform.service.DeviceService;
import com.hydata.intelligence.platform.service.MqttReceiveConfig;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.model.RESCODE;
import com.sun.mail.util.MailSSLSocketFactory;

import static org.springframework.http.HttpHeaders.FROM;


public class EmailHandlerThread extends Thread{
private static Logger logger = LogManager.getLogger(EmailHandlerThread.class);
private static String  FROM=Config.getString("email.account");// 发件人电子邮箱
private static String  VCode=Config.getString("email.password");

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

					String host = "smtp.163.com"; // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)
					Properties properties = System.getProperties();// 获取系统属性
					properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
					properties.setProperty("mail.smtp.auth", "true");// 打开认证
					String email = model.getEmail();
					try {
						// 1.获取默认session对象
						Session session = Session.getDefaultInstance(properties, new Authenticator() {
							public PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(FROM, VCode); // 发件人邮箱账号、授权码
							}
						});
						Message message = new MimeMessage(session);
						// 2.1设置发件人
						message.setFrom(new InternetAddress(FROM));
						// 2.2设置接收人
						message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
						// 2.3设置邮件主题
						message.setSubject(model.getDeviceSn()+"|触发器警报");
						// 2.4设置邮件内容
						String content = "<html><head></head><body><h2>设备"+model.getDeviceSn()+"</h2><h3>的信息流"+model.getDmName()+"于："
								+ model.getCreateTime() + "时 </h3>触发警报，阈值为："+model.getCriticalValue()+"</body></html>";
						message.setContent(content, "text/html;charset=UTF-8");
						// 3.发送邮件
						Transport.send(message);
					} catch (Exception e) {
						e.printStackTrace();
					}



			} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.error("xxxxxxxxxxxxxxxx");
				}
			}
		}
	}

}
