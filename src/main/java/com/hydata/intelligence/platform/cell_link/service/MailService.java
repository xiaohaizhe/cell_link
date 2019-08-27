package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.model.MailBean;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * @ClassName MailService
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/7 16:13
 * @Version
 */
@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${mail.sender}")
    private String sender;

    public JSONObject sendHtmlMail(MailBean mailBean) {
        MimeMessage mimeMailMessage = null;
        try {
            mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(mailBean.getRecipient());
            mimeMessageHelper.setSubject(mailBean.getSubject());
            mimeMessageHelper.setText(mailBean.getContent(), true);
            javaMailSender.send(mimeMailMessage);
            return RESCODE.SUCCESS.getJSONRES();
        } catch (Exception e) {
            System.out.println("邮件发送失败，失败原因" + e.getMessage());
            return RESCODE.FAILURE.getJSONRES(e.getMessage());
        }
    }
}
