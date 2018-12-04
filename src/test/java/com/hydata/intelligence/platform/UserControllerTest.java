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
 * @createTime 2018年10月22日下午2:19:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
	@Test
	public void test_login() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/user/login")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("name", "testtest")
				 .param("password", "test"))
		 .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_logout() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/user/logout")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("id", "2"))
		 .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_vertify_name() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/user/vertify_name")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("name", "test"))
		 .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_get_operation_logs() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/user/get_operation_logs")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("user_id", "2")
				 .param("key_word", ""))
		 .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_get_global_statistics() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/user/get_global_statistics")
				 .accept(MediaType.APPLICATION_JSON))
		 .andDo(MockMvcResultHandlers.print());
	}
	@Test
	public void test_get_product_quantity() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/user/get_product_quantity")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("user_id", "2"))
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_modify() throws Exception {
		JSONObject  user = new JSONObject();
		user.put("id", "3");
//		user.put("pwd", "test");
		user.put("phone", "15763582893");
//		user.put("email", "18206295380@126.com");
		 mvc.perform(MockMvcRequestBuilders
				 .post("/api/user/modify")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(user.toJSONString())
				 .characterEncoding("utf-8")
				 .accept(MediaType.APPLICATION_JSON)
				 )
         .andDo(MockMvcResultHandlers.print());
	}

}

