package com.hydata.intelligence.platform.cell_link.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName MailBean
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/7 15:07
 * @Version
 */
@Data
public class MailBean implements Serializable {
    private String recipient;   //邮件接收人
    private String subject;     //邮件主题
    private String content;     //邮件内容

}
