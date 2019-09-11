package com.hydata.intelligence.platform.cell_link.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author pyt
 * @createTime 2018年11月20日下午3:59:08
 */
public class HttpUtils {
    private static final String CHARSET_UTF_8 = "UTF-8";
    //声明client变量，用于执行请求
    private static HttpClient client;
    private static Logger logger = LogManager.getLogger(HttpUtils.class);

    static {
        //将参数提取为变量，方便之后修改
        int connectionTimeOut = 25000;
        int socketTimeOut = 25000;
        int maxConnectionPerHost = 20;
        int maxTotalConnections = 20;
        //声明一个多线程安全连接管理类变量，为了不用考虑多线程带来的安全问题
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
        connectionManager.getParams().setSoTimeout(socketTimeOut);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
        connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
        client = new HttpClient(connectionManager);
    }

    /**
     * POST方式提交数据
     *
     * @param url    请求url
     * @param params 提交数据
     * @return 结果
     */
    public static JSONObject doPost(String url, JSONObject params) {
        JSONObject object = new JSONObject();
        try {
            String response;
            //1.创建请求方式对象
            PostMethod postMethod = new PostMethod(url);
            if (params != null) {
                RequestEntity se = new StringRequestEntity(params.toJSONString(), "application/json", CHARSET_UTF_8);
                postMethod.setRequestEntity(se);
            }
            //2.执行方式
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = postMethod.getResponseBodyAsString();
                object = JSONObject.parseObject(response);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return object;
    }

    public static void main(String[] args) {
        int[][] a = new int[][]{{1, 2, 3}, {1, 2, 3}};
        JSONObject param = new JSONObject();
        param.put("params", a);
        JSONObject result = doPost("http://localhost:5000/correlation_analyse", param);
        System.out.println(result);
    }
}