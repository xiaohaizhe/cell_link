package com.hydata.intelligence.platform.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hydata.intelligence.platform.utils.Config;
import com.hydata.intelligence.platform.utils.MongoConst;
import com.hydata.intelligence.platform.utils.MongoDBUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

/**
 * @author pyt
 * @createTime 2018年11月8日上午11:54:26
 */
@Transactional
@Service
public class MongoDBService {
	
	 private static final MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();
	 private static final MongoClient meiyaClient = mongoDBUtil.getMongoConnect(Config.getString("mongodb.server.host"),Config.getInt("mongodb.server.port"));	 
	 private static final MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","data_history");
	 
	 /**
	  * 插入数据
	  * @param object
	  * 需要包含：device_datastream_id
	  * 	value
	  * 	date
	  */
	 public void insert(JSONObject object){
		mongoDBUtil.insertDoucument(collection,object);
	 }
	 
	 /**
	  * 根据
	  * @param dsName
	  * @return
	  */
	 public JSONObject findByDsName(Integer device_datastream_id,Date start,Date end) {
		 Map<String,Object> conditions = Maps.newHashMap();
	     conditions.put("name","test");
	     Map<String,Integer> compares = Maps.newHashMap();
	     compares.put(MongoConst.GT.getCompareIdentify(),20);
	     compares.put(MongoConst.LTE.getCompareIdentify(),28);
	     String opAnd = MongoConst.AND.getCompareIdentify();
	     Map<String,Object> sortParams = Maps.newHashMap();
	     sortParams.put("id",-1);
	     FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,sortParams,null,2);
	     mongoDBUtil.printDocuments(documents);
	     return null;
	 }
	 
}

