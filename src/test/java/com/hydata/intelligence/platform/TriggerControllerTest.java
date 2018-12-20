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

import com.alibaba.fastjson.JSONObject;

/**
 * @author pyt
 * @createTime 2018年12月17日下午1:54:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TriggerControllerTest {

	@Autowired
	private WebApplicationContext context;
	private MockMvc mvc;
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	@Test
	public void test_add() throws Exception {
		JSONObject  trigger = new JSONObject();
		trigger.put("name", "trigger_add_test");
		trigger.put("productId", "3");
		trigger.put("triggerTypeId", "1");
		trigger.put("criticalValue", "2");
		trigger.put("triggerMode", "1");
		trigger.put("modeValue", "http://localhost:8080/api/test");
		trigger.put("device_sn", "18206295381");
		trigger.put("datastreamId", "3");
		 mvc.perform(MockMvcRequestBuilders
				 .post("/api/trigger/add")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(trigger.toJSONString())
				 .characterEncoding("utf-8")
				 .accept(MediaType.APPLICATION_JSON)
				 )
         .andDo(MockMvcResultHandlers.print());
	}
	@Test
	public void test_modify() throws Exception {
		JSONObject  trigger = new JSONObject();
		trigger.put("id", "7");
		
		trigger.put("productId", "3");
		trigger.put("name", "trigger_modify_test");
		trigger.put("triggerTypeId", "1");
		trigger.put("criticalValue", "2");
		trigger.put("triggerMode", "1");
		trigger.put("modeValue", "http://localhost:8080/api/test");
		trigger.put("device_sn", "18206295380");
		trigger.put("datastreamId", "2");
		 mvc.perform(MockMvcRequestBuilders
				 .post("/api/trigger/modify")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(trigger.toJSONString())
				 .characterEncoding("utf-8")
				 .accept(MediaType.APPLICATION_JSON)
				 )
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_delete() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/trigger/delete")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("id", "4"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_get_by_productId() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/trigger/get_by_productId")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("productId", "3"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_get_associated_devices() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/trigger/get_associated_devices")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("trigger_id", "5")
				 .param("page", "1")
				 .param("number", "10"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_get_not_associated_devices() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/trigger/get_not_associated_devices")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("productId", "3")
				 .param("trigger_id", "5")
				 .param("page", "1")
				 .param("number", "10"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_get_associated_triggers() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/trigger/get_associated_triggers")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("device_sn", "18206295380")
				 .param("page", "1")
				 .param("number", "10"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	
	
	@Test
	public void test_get_by_name() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/trigger/get_by_name")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("product_id", "3")
				 .param("name", "test")
				 .param("page", "1")
				 .param("number", "10"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_trigger_associated_device() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/trigger/trigger_associated_device")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("trigger_id", "7")
				 .param("device_sn", "18206295380"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	
	
	
}

