package com.hydata.intelligence.platform.utils;

import com.hydata.intelligence.platform.model.EmailHandlerModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.*;


public class EmailHandlerThread extends Thread{
	private static Logger logger = LogManager.getLogger(EmailHandlerThread.class);
	private static String  FROM=Config.getString("email.account");// 发件人电子邮箱
	private static String  VCode=Config.getString("email.password");

	@Override
	public void run() {
		while(true)
		{
			if(MqttClientUtil.getEmailQueue()!=null &&!MqttClientUtil.getEmailQueue().isEmpty()){
				try {
					logger.info("尝试发送邮件");
					EmailHandlerModel model = MqttClientUtil.getEmailQueue().poll(2, TimeUnit.SECONDS);
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

					// 1.获取默认session对象
					Session session = Session.getDefaultInstance(properties, new Authenticator() {
						public PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(FROM, VCode); // 发件人邮箱账号、授权码
						}
					});
					//session.setDebug(true);
					// 2.创建邮件对象
					Message message = new MimeMessage(session);
					// 2.1设置发件人
					message.setFrom(new InternetAddress(FROM));
					// 2.2设置接收人
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
					// 2.3设置邮件主题
					message.setSubject("智能感知平台|触发器警报");
					// 2.4设置邮件内容
					String content = "<html><p><b>触发器警报！</b></p><p>设备"+model.getDeviceId()+"的数据流"
							+model.getDmName()+"于"+model.getCreateTime()+"时传来了数据"+model.getDataValue()+
							"，符合触发条件:"+model.getTriggerSymbol()+model.getCriticalValue()+"触发了警报。</p></html>";
					message.setContent(content, "text/html;charset=UTF-8");
					// 3.发送邮件
					Transport.send(message);

				} catch (Exception e) {
					logger.error("触发器邮件发送失败");
					e.printStackTrace();
				}
			}
		}
	}

}
