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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author pyt
 * @createTime 2018年11月13日下午3:42:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationControllerTest {
	@Autowired
	private WebApplicationContext context;
	private MockMvc mvc;
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	@Test
	public void test() throws Exception {
		JSONArray applicationChartDatastreamList = new JSONArray();
		 JSONArray applicationChartList = new JSONArray();
		 JSONObject applicationChart = new JSONObject();
		 applicationChart.put("chartId", 1);
		 applicationChart.put("applicationChartDatastreamList", null);
		 mvc.perform(MockMvcRequestBuilders.post("/api/application/add_ca").accept(MediaType.APPLICATION_JSON)
				 .param("productId", "1")
				 .param("name", "test")
				 .param("applicationChartList", null))
		 .andExpect(MockMvcResultMatchers.status().isOk())
         .andDo(MockMvcResultHandlers.print())
         .andReturn();
	}
}

