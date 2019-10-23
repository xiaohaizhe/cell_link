package com.hydata.intelligence.platform.cell_link.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author pyt
 *
 */
@Component
public class SmsDemo {
	//阿里云配置
    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    @Value("${aliyun.accessKeyId}")
	private String accessKeyId;
	@Value("${aliyun.accessKeySecret}")
	private String accessKeySecret;
	@Value("${aliyun.verifyCode}")
	private String verifyCode;
	@Value("${vertify.time}")
    private Integer vertifyTime;

    private static Logger logger = LogManager.getLogger(SmsDemo.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SendSmsResponse sendSms(String phone,String code) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("CellLink");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(verifyCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\""+code+"\"}");
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId(code);
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        logger.info(sendSmsResponse.getMessage());
        return sendSmsResponse;
    }


    public QuerySendDetailsResponse querySendDetails(String phone) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(phone);
        //可选-流水号
        //request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);
        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
        return querySendDetailsResponse;
    }

    public Boolean sendCode(String phone,Integer code){
        SendSmsResponse sendsmsresponse;
        logger.info("验证码发送结果");
        try {
            sendsmsresponse = sendSms(phone, Integer.toString(code));
            logger.info(sendsmsresponse.toString());
        } catch (ClientException e) {
           logger.error(e.getMessage());
           return false;
        }
        return sendsmsresponse.getCode().equals("OK");
    }

    public JSONObject checkCode(String phone, String code){
        List<QuerySendDetailsResponse.SmsSendDetailDTO> codelist = new ArrayList<>();
        QuerySendDetailsResponse response;
        try {
            response = querySendDetails(phone);
            logger.info(response.getMessage());
            codelist = response.getSmsSendDetailDTOs();
            if (codelist.size()>0){
                QuerySendDetailsResponse.SmsSendDetailDTO smsDetail = codelist.get(0);
                logger.info(smsDetail.toString());
                logger.info(smsDetail.getContent());
                String codeOut = smsDetail.getOutId();
                String receiveDate = smsDetail.getReceiveDate();
                logger.info("接收到的时间为："+receiveDate +".");
                Date date = sdf.parse(receiveDate);
                Date now = new Date();
                long cost = now.getTime()-date.getTime();
                int min = (int) (cost/1000/60);
                if (min < vertifyTime && codeOut.trim().equals(code.trim())){
                    return RESCODE.SUCCESS.getJSONRES();
                }return RESCODE.FAILURE.getJSONRES("验证码超时");
            }return RESCODE.FAILURE.getJSONRES();
        } catch (ClientException | ParseException e) {
            logger.error("获取手机验证码异常,"+e.getMessage());
            return RESCODE.FAILURE.getJSONRES(e.getMessage());
        }
    }


}
