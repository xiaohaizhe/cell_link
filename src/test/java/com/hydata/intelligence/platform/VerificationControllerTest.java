package com.hydata.intelligence.platform;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.hydata.intelligence.platform.utils.Config;
import com.hydata.intelligence.platform.utils.SendMailUtils;
import com.hydata.intelligence.platform.utils.SmsDemo2;

/**
 * @author pyt
 * @createTime 2018年10月30日下午2:13:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VerificationControllerTest {
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private SmsDemo2 sd;
	private MockMvc mvc;
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
	@Test
	public void test_verification() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/verification/verification")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("user_id", "3"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_send_code() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/verification/send_code")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("user_id", "3")
				 .param("phone", "18206295380"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	
	@Test
	public void test_user_vertify_sms() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/verification/user_vertify_sms")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("user_id", "3")
				 .param("code", "576464"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_vertify_sms() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/verification/vertify_sms")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("user_id", "3")
				 .param("code", "576464"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_vertify_code() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/verification/vertify_code")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("user_id", "3")
				 .param("phone", "18206295380")
				 .param("code", "226700"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_send_email() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/verification/send_email")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("user_id", "3")
				 .param("email", "1647126351@qq.com"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_vertify_email() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/verification/vertify_email")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("user_id", "3")
				 .param("email", "2630451673@qq.com")
				 .param("code", "794976"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_vertify_for_trigger() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/verification/vertify_for_trigger")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("id", "3")
				 .param("code", "423823"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	
	@Test
	public void test2() {
		//fail("Not yet implemented");
		SendMailUtils.sendMail("puyuting@hiynn.com", "1234", "智能感知平台");		
	}
	
	@Test
	public void test3() {
		sd.sendSms("18206295380");
	}
	@Test
	public void test4() {
		 System.out.println(sd.verifySMSCode("18206295380", "214448"));
	}
	/*@Test
	public void testMongoDB() {
		 MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();
	        MongoClient meiyaClient = mongoDBUtil.getMongoConnect(Config.getString("mongodb.server.host"),Config.getInt("mongodb.server.port"));
	 
	        try {
	            MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");
	            Map<String,Object> insert = new HashMap<>();
	               //1、测试增加
	            insert.put("name","test");
	            insert.put("device_sn", "281908210");
	            insert.put("product_id",1);
	            insert.put("create_time",new Date());
	            mongoDBUtil.insertDoucument(collection,insert);
	            //2、测试条件、范围、排序查询
	            String date = "2018-11-20 16:55:36";
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            Date d = sdf.parse(date);
	            Map<String,Object> conditions = Maps.newHashMap();
	            conditions.put("dd_id",1);
	             Map<String,Object> compares = Maps.newHashMap();
//	           compares.put(MongoConst.GT.getCompareIdentify(),20);
	            compares.put(MongoConst.LTE.getCompareIdentify(),d);	             
	            String opAnd = MongoConst.AND.getCompareIdentify();
	            Map<String,Object> sortParams = Maps.newHashMap();
	            sortParams.put("age",-1);
	            FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,null,null,5);
	            mongoDBUtil.printDocuments(documents);
	           //3、in查询
	            List<String> names = Lists.newArrayList("张媛","zy","zyy");
	            FindIterable<Document> documents = mongoDBUtil.queryDocumentIn(collection,"name",names);
	            mongoDBUtil.printDocuments(documents);
	            //4 批量删除
	            Map<String,Object> conditionParams = Maps.newHashMap();
	            conditionParams.put("school","厦门理工");
	            long count = mongoDBUtil.deleteDocument(collection,true,conditionParams);
	            System.out.println(count);
	            //更新
	            Map<String,Object> queryParams = Maps.newHashMap();
	            queryParams.put("school","修改了学校");
	            Map<String,Object> updateParams = Maps.newHashMap();
	            updateParams.put("name","修改了名字");
	            mongoDBUtil.updateDocument(collection,queryParams,updateParams,false);
	        } catch (Exception e) {
	            System.err.println(e.getClass().getName() + ": " + e.getMessage());
	        }
	}*/


}

 