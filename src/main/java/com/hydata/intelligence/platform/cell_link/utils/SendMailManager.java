package com.hydata.intelligence.platform.cell_link.utils;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.model.MailBean;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.service.DeviceGroupService;
import com.hydata.intelligence.platform.cell_link.service.MailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SendMailManager
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/3 10:58
 * @Version
 */
@Component
public class SendMailManager {
    @Autowired
    private MailService mailService;
    private static Logger logger = LogManager.getLogger(SendMailManager.class);
    //缓存发送邮件的队列
    private static BlockingQueue<MailBean> queue = new ArrayBlockingQueue<MailBean>(2000);
    //发送邮件线程状态
    private static boolean isRunning = false;
    //超时时间间隔
    private static long TIMEOUT_INTERVAL = 3*60*1000;
    public JSONObject sendMail(MailBean mail){
        boolean rsp = false;
        try {
            //阻塞3秒
            rsp = queue.offer(mail, 3, TimeUnit.SECONDS);
            if(!rsp){
                throw new Exception("服务器发送邮件繁忙，请稍后再发");
            }
            if(!isRunning){
                startup();
                isRunning = true;
            }
        } catch (Exception e) {
            logger.error("添加发送邮件队列出错", e);
        }
        if (rsp) return RESCODE.SUCCESS.getJSONRES();
        else return RESCODE.FAILURE.getJSONRES();
    }
    private void startup(){
        if(isRunning) {
            return;
        }
        Thread th = new  Thread(){
            @Override
            public void run(){
                logger.debug("邮件发送消息线程启动");
                long timestamp = System.currentTimeMillis();
                while(isRunning)
                {
                    long timeout = timestamp + TIMEOUT_INTERVAL;
                    if(System.currentTimeMillis()>timeout)
                    {
                        //空跑一段时间(3分钟)后线程退出
                        break;
                    }
                    try
                    {
                        if(queue.size()==0)
                        {
                            Thread.sleep(1000);
                            continue;
                        }
                        timestamp = System.currentTimeMillis();
                        MailBean mail = queue.poll();
                        mailService.sendHtmlMail(mail);
                        //发送一个邮件休息2秒，防止发送过快，导致主油箱被锁定
                        Thread.sleep(2000);
                    } catch (Exception e)
                    {
                        logger.error("邮件推送线程出错",e);
                    }
                }
                isRunning = false;
                logger.debug("邮件发送线程停止。");
            }
        };
        th.start();
    }
}
