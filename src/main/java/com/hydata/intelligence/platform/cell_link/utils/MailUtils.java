package com.hydata.intelligence.platform.cell_link.utils;

import com.hydata.intelligence.platform.cell_link.model.MailBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 * @ClassName MailUtils
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/7 15:09
 * @Version
 */
@Component
public class MailUtils {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${mail.sender}")
    private String sender;

    //发送简单邮件
    public void sendSimpleMail(MailBean mailBean) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            //邮件发送人
            simpleMailMessage.setFrom(sender);
            //邮件接收人
            simpleMailMessage.setTo(mailBean.getRecipient());
            //邮件主题
            simpleMailMessage.setSubject(mailBean.getSubject());
            //邮件内容
            simpleMailMessage.setText(mailBean.getContent());
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            System.out.println("邮件发送失败，失败原因" + e.getMessage());
        }
    }

    //发送HTML格式的邮件
    public void sendHTMLMail(MailBean mailBean) {
        MimeMessage mimeMailMessage = null;
        try {
            mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(mailBean.getRecipient());
            mimeMessageHelper.setSubject(mailBean.getSubject());
            StringBuilder sb = new StringBuilder();
            sb.append("<h1>SpirngBoot测试邮件HTML</h1>")
                    .append("<p style='color:#F00'>\"你是真的太棒了！\"</p>")
                    .append("<p style='text-align:right'>右对齐</p>");
            mimeMessageHelper.setText(sb.toString(), true);
            javaMailSender.send(mimeMailMessage);
        } catch (Exception e) {
            System.out.println("邮件发送失败，失败原因" + e.getMessage());
        }
    }
}
