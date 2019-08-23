package com.hydata.intelligence.platform.cell_link;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.hydata.intelligence.platform.cell_link.utils.SmsDemo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CellLinkApplicationTests {
    @Autowired
    private SmsDemo smsDemo;

    @Test
    public void contextLoads() throws ClientException {
//        SendSmsResponse response = smsDemo.sendSms("18206295380","000000");
//        System.out.println(response.getMessage());
//        System.out.println(response.getCode());
//        QuerySendDetailsResponse response =smsDemo.querySendDetails("18206295380");
        smsDemo.checkCode("18206295380","000000");
    }

}
