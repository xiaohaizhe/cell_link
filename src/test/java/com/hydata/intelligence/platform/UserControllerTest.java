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

}

