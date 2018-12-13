package com.hydata.intelligence.platform;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.hydata.intelligence.platform.controller.DataStreamModelController;

/**
 * @author pyt
 * @createTime 2018年10月26日上午9:48:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DatastreamModelTest {
	private MockMvc mvc;
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.standaloneSetup(new DataStreamModelController()).build();
	}
	@Test
	public void test() throws Exception {
		 mvc.perform(MockMvcRequestBuilders.get("/api/dsmCon/add").accept(MediaType.APPLICATION_JSON))
         .andExpect(MockMvcResultMatchers.status().isOk())
         .andDo(MockMvcResultHandlers.print())
         .andReturn();
	}

}

