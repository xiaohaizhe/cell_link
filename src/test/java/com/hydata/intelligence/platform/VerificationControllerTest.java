package com.hydata.intelligence.platform;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.hydata.intelligence.platform.utils.MongoConst;
import com.hydata.intelligence.platform.utils.MongoDBUtils;
import com.hydata.intelligence.platform.utils.SendMailUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

/**
 * @author pyt
 * @createTime 2018年10月30日下午2:13:39
 */
public class VerificationControllerTest {
	@Test
	public void test2() {
		//fail("Not yet implemented");
		SendMailUtils.sendMail("puyuting@hiynn.com", "1234", "智能感知平台");		
	}
	@Test
	public void testMongoDB() {
		 MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();
	        MongoClient meiyaClient = mongoDBUtil.getMongoConnect("127.0.0.1",27017);
	 
	        try {
	            MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","data_history");
	           /* Map<String,Object> insert = new HashMap<>();
	               //1、测试增加
	            insert.put("dd_id",2);
	            insert.put("name", "test1");
	            insert.put("value",1);
	            insert.put("date",new Date());
	            mongoDBUtil.insertDoucument(collection,insert);*/
	            //2、测试条件、范围、排序查询
	           /* String date = "2018-11-20 16:55:36";
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            Date d = sdf.parse(date);*/
	            Map<String,Object> conditions = Maps.newHashMap();
	            conditions.put("dd_id",1);
	          /*   Map<String,Object> compares = Maps.newHashMap();
//	           compares.put(MongoConst.GT.getCompareIdentify(),20);
	            compares.put(MongoConst.LTE.getCompareIdentify(),d);	             
	            String opAnd = MongoConst.AND.getCompareIdentify();
	            Map<String,Object> sortParams = Maps.newHashMap();
	            sortParams.put("age",-1);*/
	            FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,null,null,5);
	            mongoDBUtil.printDocuments(documents);
	           //3、in查询
	            /*List<String> names = Lists.newArrayList("张媛","zy","zyy");
	            FindIterable<Document> documents = mongoDBUtil.queryDocumentIn(collection,"name",names);
	            mongoDBUtil.printDocuments(documents);*/
	            //4 批量删除
	            /*Map<String,Object> conditionParams = Maps.newHashMap();
	            conditionParams.put("school","厦门理工");
	            long count = mongoDBUtil.deleteDocument(collection,true,conditionParams);
	            System.out.println(count);*/
	            //更新
	           /* Map<String,Object> queryParams = Maps.newHashMap();
	            queryParams.put("school","修改了学校");
	            Map<String,Object> updateParams = Maps.newHashMap();
	            updateParams.put("name","修改了名字");
	            mongoDBUtil.updateDocument(collection,queryParams,updateParams,false);*/
	        } catch (Exception e) {
	            System.err.println(e.getClass().getName() + ": " + e.getMessage());
	        }
	}


}

 