package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.model.MailBean;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.utils.SendMailManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * @ClassName MailService
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/7 16:13
 * @Version
 */
@Component
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${mail.sender}")
    private String sender;

    private static Logger logger = LogManager.getLogger(MailService.class);

    public void sendHtmlMail(MailBean mailBean) {
        MimeMessage mimeMailMessage = null;
        try {
            mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(mailBean.getRecipient());
            mimeMessageHelper.setSubject(mailBean.getSubject());
            mimeMessageHelper.setText(mailBean.getContent(), true);
            javaMailSender.send(mimeMailMessage);
            logger.info("邮件发送成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
