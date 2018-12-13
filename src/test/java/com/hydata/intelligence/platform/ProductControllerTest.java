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

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.service.ProductService;

/**
 * @author pyt
 * @createTime 2018年11月13日上午10:25:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductControllerTest {
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private ProductService productService;
	
	private MockMvc mvc;
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
		productService.setProtocol();
	}
	@Test
	public void test_get_protocol() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/product/get_protocols").accept(MediaType.APPLICATION_JSON))
		 .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andReturn();
	}
	@Test
	public void test_add() throws Exception {
		JSONObject  product = new JSONObject();
		product.put("name", "product_test_1");
		product.put("latitude", "120.69");
		product.put("lontitude", "98.20");
		product.put("userId", "2");
		product.put("protocolId", "2");
		product.put("description", "产品测试");
		 mvc.perform(MockMvcRequestBuilders
				 .post("/api/product/add")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(product.toJSONString())
				 .characterEncoding("utf-8")
				 .accept(MediaType.APPLICATION_JSON)
				 )
         .andDo(MockMvcResultHandlers.print());
	}
	@Test
	public void test_modify() throws Exception {
		JSONObject  product = new JSONObject();
		product.put("id", "1");
		product.put("name", "product_test_1");
		product.put("latitude", "120.69");
		product.put("lontitude", "98.20");
		product.put("userId", "2");
		product.put("protocolId", "2");
		product.put("description", "产品测试");
		 mvc.perform(MockMvcRequestBuilders
				 .post("/api/product/modify")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(product.toJSONString())
				 .characterEncoding("utf-8")
				 .accept(MediaType.APPLICATION_JSON)
				 )
         .andDo(MockMvcResultHandlers.print());
	}
	@Test
	public void test_query() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/product/query")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("user_id", "2")
				 .param("page", "1")
				 .param("number", "10")
				 .param("sort","0"))
        .andDo(MockMvcResultHandlers.print());
	}
	@Test
	public void test_delete() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/product/delete")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("product_id", "1"))
        .andDo(MockMvcResultHandlers.print());
	}
	@Test
	public void test_get_detail() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/product/get_detail")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("product_id", "1"))
        .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_get_heatmap() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/product/get_heatmap")
				 .accept(MediaType.APPLICATION_JSON))
		 .andExpect(MockMvcResultMatchers.status().isOk())
         .andDo(MockMvcResultHandlers.print())
         .andReturn();
	}
	
	@Test
	public void test_get_product_overview() throws Exception {
		 mvc.perform(MockMvcRequestBuilders
				 .get("/api/product/get_product_overview")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("product_id", "2"))
		 .andExpect(MockMvcResultMatchers.status().isOk())
         .andDo(MockMvcResultHandlers.print())
         .andReturn();
	}
	
}

