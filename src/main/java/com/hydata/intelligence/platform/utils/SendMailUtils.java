package com.hydata.intelligence.platform.utils;

import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.model.RESCODE;

/**
 * @author pyt
 * @createTime 2018年10月30日下午3:01:21
 */
public class SendMailUtils {
	/*private static EmailProperties emailProperties = new EmailProperties();*/
	/*private static String  FROM="m18206295380@163.com";// 发件人电子邮箱
    private static String  VCode="puyuting2018";//授权码或者账号密码
*/  private static String  FROM=Config.getString("email.account");// 发件人电子邮箱
	private static String  VCode=Config.getString("email.password");
	
	public static JSONObject sendMail(String email,String code,String title){
        String host = "smtp.163.com"; // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)
        Properties properties = System.getProperties();// 获取系统属性
        properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
        properties.setProperty("mail.smtp.auth", "true");// 打开认证
        try {
            // 1.获取默认session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {                
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM, VCode); // 发件人邮箱账号、授权码
                }
            });
            // 2.创建邮件对象
            Message message = new MimeMessage(session);
            // 2.1设置发件人
            message.setFrom(new InternetAddress(FROM));
            // 2.2设置接收人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // 2.3设置邮件主题
            message.setSubject(title+"|验证码");
            // 2.4设置邮件内容
            String content = "<html><head></head><body><h2>"+title+"</h2><h3>动态验证码："
                    + code + " </h3>请在5分钟内完成验证。如非本人操作请忽略。</body></html>";
            message.setContent(content, "text/html;charset=UTF-8");
            // 3.发送邮件
            Transport.send(message);
           return RESCODE.EMAIL_SEND_SUCCESS.getJSONRES();
        } catch (Exception e) {
            e.printStackTrace();
            return RESCODE.EMAIL_EXCEPTION.getJSONRES(e);
        }
    }

}

