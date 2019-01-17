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
	public void test_add_chart_app() throws Exception {
		JSONArray applicationChartList = new JSONArray();
		JSONArray applicationChartDatastreamList = new JSONArray();
		JSONObject object = new JSONObject();
		object.put("dd_id", "1");
		applicationChartDatastreamList.add(object);
		JSONObject applicationChart = new JSONObject();
		applicationChart.put("chartId", 1);
		applicationChart.put("frequency", "1");
		applicationChart.put("sum", "10");
		applicationChart.put("applicationChartDatastreamList", applicationChartDatastreamList);
		applicationChartList.add(applicationChart);
		JSONObject  applicationModel = new JSONObject();
		applicationModel.put("name", "test1");
		applicationModel.put("productId", "3");
		applicationModel.put("applicationChartList", applicationChartList);
		 mvc.perform(MockMvcRequestBuilders
				 .post("/api/application/add_chart_app")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(applicationModel.toJSONString())
				 .characterEncoding("utf-8")
				 .accept(MediaType.APPLICATION_JSON)
				 )
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_del_app() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/application/del_app")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("id", "3"))
       .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_modify_chart_app() throws Exception {
		JSONArray applicationChartList = new JSONArray();
		JSONArray applicationChartDatastreamList = new JSONArray();
		JSONObject object = new JSONObject();
		object.put("dd_id", "2");
		applicationChartDatastreamList.add(object);
		JSONObject applicationChart = new JSONObject();
		applicationChart.put("chartId", 1);
		applicationChart.put("frequency", "1");
		applicationChart.put("sum", "10");
		applicationChart.put("applicationChartDatastreamList", applicationChartDatastreamList);
		applicationChartList.add(applicationChart);
		JSONObject  applicationModel = new JSONObject();
		applicationModel.put("id", "2");
		applicationModel.put("name", "test1");
		applicationModel.put("applicationChartList", applicationChartList);
		 mvc.perform(MockMvcRequestBuilders
				 .post("/api/application/modify_chart_app")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(applicationModel.toJSONString())
				 .characterEncoding("utf-8")
				 .accept(MediaType.APPLICATION_JSON)
				 )
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_get_chart_app_detail() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/application/get_chart_app_detail")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("app_id", "2"))
      .andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void test_get_by_name() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/application/get_by_name")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("product_id", "3")
				 .param("app_name", "test"))
     .andDo(MockMvcResultHandlers.print());
	}
	
	
	@Test
	public void test_get_chart_refresh_frequence() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/application/get_chart_refresh_frequence")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("ac_id", "12"))
    .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_get_chart() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/application/get_chart")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("ac_id", "12"))
   .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_add_analysis_app() throws Exception {
		/*{
			  "productId":3,
			  "name":"test",
			  "createTime":"2018-11-19T10:04:10.624+0000",
			  "applicationType":1,
			  "analysisDatastreams":[
			    {
			      "ddId":1,
			      "type":0,
			      "start":"2018-11-19T10:04:10.624+0000",
			      "end":"2018-11-19T10:04:10.624+0000",
			      "frequency":10
			    }
			  
			  ]
			}*/
		JSONArray analysisDatastreams = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ddId", "1");
		jsonObject.put("type", "0");
		jsonObject.put("start", "2018-11-19T10:04:10.624+0000");
		jsonObject.put("end", "2018-11-19T10:04:10.624+0000");
		jsonObject.put("frequency", "10");
		analysisDatastreams.add(jsonObject);
		JSONObject aa_model = new JSONObject();
		aa_model.put("productId","3");
		aa_model.put("name","test");
		aa_model.put("createTime","2018-11-19T10:04:10.624+0000");
		aa_model.put("applicationType","1");
		aa_model.put("analysisDatastreams", analysisDatastreams);
		 mvc.perform(MockMvcRequestBuilders
				 .post("/api/application/add_analysis_app")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(aa_model.toJSONString())
				 .characterEncoding("utf-8")
				 .accept(MediaType.APPLICATION_JSON)
				 )
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_get_analysis_app() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/application/get_analysis_app")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("product_id", "3")
				 .param("name", "test"))
  .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_get_analysis_app_detail() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/application/get_analysis_app_detail")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("application_id", "5"))
  .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_analysis() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/application/analysis")
				 .accept(MediaType.APPLICATION_JSON))
  .andDo(MockMvcResultHandlers.print());
	}
}

