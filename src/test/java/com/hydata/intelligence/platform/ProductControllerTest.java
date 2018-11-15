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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.hydata.intelligence.platform.controller.ProductController;

/**
 * @author pyt
 * @createTime 2018年11月13日上午10:25:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductControllerTest {
	@Autowired
	private WebApplicationContext context;
	private MockMvc mvc;
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	@Test
	public void test_get_heatmap() throws Exception {
		 mvc.perform(MockMvcRequestBuilders.get("/api/product/get_heatmap").accept(MediaType.APPLICATION_JSON))
		 .andExpect(MockMvcResultMatchers.status().isOk())
         .andDo(MockMvcResultHandlers.print())
         .andReturn();
	}
	
	@Test
	public void test_get_increment() throws Exception {
		 mvc.perform(MockMvcRequestBuilders.get("/api/product/get_increment").accept(MediaType.APPLICATION_JSON)
				 .param("product_id", "1")
				 .param("type", "1"))
		 .andExpect(MockMvcResultMatchers.status().isOk())
         .andDo(MockMvcResultHandlers.print())
         .andReturn();
	}
}

