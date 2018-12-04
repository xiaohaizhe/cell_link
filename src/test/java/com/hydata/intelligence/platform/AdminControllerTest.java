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
 * @createTime 2018年11月21日上午10:44:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminControllerTest {
	@Autowired
	private WebApplicationContext context;
	private MockMvc mvc;
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
	@Test
	public void test_login() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/admin/login")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("name", "cladmin")
				 .param("pwd", "1234"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_logout() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/admin/logout")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("name", "cladmin"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_add_user() throws Exception {	
		JSONObject  user = new JSONObject();
		user.put("name", "test9");
		user.put("pwd", "1234");
		user.put("phone", "18206295380");
		 mvc.perform(MockMvcRequestBuilders
				 .post("/api/admin/add")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(user.toJSONString())
				 .characterEncoding("utf-8")
				 .accept(MediaType.APPLICATION_JSON)
				 )
         .andDo(MockMvcResultHandlers.print());
	}
	@Test
	public void test_delete() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/admin/delete")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("user_id","1")
				 .param("admin_name", "cladmin"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_query() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/admin/query")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("page","1")
				 .param("number", "10"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_query_by_uname() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/admin/query_by_uname")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("user_name", "1")
				 .param("page","1")
				 .param("number", "10"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	
	@Test
	public void test_change_effectiveness() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/admin/change_effectiveness")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("user_id", "2")
				 .param("admin_name", "cladmin"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_modify() throws Exception {
		JSONObject  user = new JSONObject();
		user.put("id", "6");
		user.put("name", "testtest");
		user.put("pwd", "test");
		user.put("phone", "18206295380");
		user.put("email", "18206295380@126.com");
		 mvc.perform(MockMvcRequestBuilders
				 .post("/api/admin/modify")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(user.toJSONString())
				 .characterEncoding("utf-8")
				 .accept(MediaType.APPLICATION_JSON)
				 )
         .andDo(MockMvcResultHandlers.print());
	}
	

}

