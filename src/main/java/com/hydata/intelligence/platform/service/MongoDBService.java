package com.hydata.intelligence.platform.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

/**
 * @author pyt
 * @createTime 2018年11月8日上午11:54:26
 */
@Transactional
@Service
public class MongoDBService {
	/*@Autowired
	private MongoDB mongoDB;
	
	 private static final MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();
	 private static final MongoClient meiyaClient = mongoDBUtil.getMongoConnect();	 
	 private static final MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","data_history");
	 
	 *//**
	  * 插入数据
	  * @param object
	  * 需要包含：device_datastream_id
	  * 	value
	  * 	date
	  *//*
	 public void insert(JSONObject object){
		 MongoClient meiyaClient = mongoDBUtil.getMongoConnect(mongoDB.getHost(),mongoDB.getPort());
			MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","data_history");
		mongoDBUtil.insertDoucument(collection,object);
	 }
	 
	 *//**
	  * 根据
	  * @param dsName
	  * @return
	  *//*
	 public JSONObject findByDsName(Integer device_datastream_id,Date start,Date end) {
		 MongoClient meiyaClient = mongoDBUtil.getMongoConnect(mongoDB.getHost(),mongoDB.getPort());
			MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","data_history");
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
	 }*/
	 
}

