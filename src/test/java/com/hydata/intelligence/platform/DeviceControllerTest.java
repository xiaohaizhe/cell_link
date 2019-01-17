package com.hydata.intelligence.platform;

import static org.junit.Assert.*;

import java.util.Date;

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
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.repositories.DeviceRepository;
import com.hydata.intelligence.platform.service.ProductService;

/**
 * @author pyt
 * @createTime 2018年12月13日下午3:36:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceControllerTest {
	@Autowired
	private WebApplicationContext context;
	

	
	private MockMvc mvc;
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	@Test
	public void test_add() throws Exception {
		JSONObject  device = new JSONObject();
		device.put("name", "test1");
		device.put("productId", "3");
		device.put("device_sn", "123456");
		 mvc.perform(MockMvcRequestBuilders
				 .post("/api/device/add")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(device.toJSONString())
				 .characterEncoding("utf-8")
				 .accept(MediaType.APPLICATION_JSON)
				 )
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_modify() throws Exception {
		JSONObject  device = new JSONObject();
		device.put("device_sn", "123456");
		device.put("name", "test_modify");		
		 mvc.perform(MockMvcRequestBuilders
				 .post("/api/device/modify")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(device.toJSONString())
				 .characterEncoding("utf-8")
				 .accept(MediaType.APPLICATION_JSON)
				 )
         .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_delete() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/device/delete")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("device_sn", "123456"))
        .andDo(MockMvcResultHandlers.print());
	}
	@Test
	public void test_show() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/device/show")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("product_id", "3")
				 .param("page", "1")
				 .param("number","10")
				 .param("sort", "-1"))
       .andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	public void test_query_by_sn_or_name() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/device/query_by_sn_or_name")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("product_id", "3")
				 .param("page", "1")
				 .param("number","10")
				 .param("device_idOrName", "add1"))
       .andDo(MockMvcResultHandlers.print());		
	}
	
	@Test
	public void test_get_devicelist() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/device/get_devicelist")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("productId", "3"))
      .andDo(MockMvcResultHandlers.print());		
	}
	
	
	@Test
	public void test_get_devicedslist() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/device/get_devicedslist")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("device_sn", "12345690890"))
     .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_get_device_ds_data() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/device/get_device_ds_data")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("dd_id", "1")
				 .param("start",new Date().toGMTString())
				 .param("end",new Date().toGMTString()))
    .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_get_cmd_logs() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/device/get_cmd_logs")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("device_sn", "12345690890"))
   .andDo(MockMvcResultHandlers.print());
	}
		
	@Test 
	public void test_get_increment() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/device/get_increment")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("product_id", "3")
				 .param("start", "2018/12/01 00:00:00")
				 .param("end", "2018/12/14 00:00:00"))
  .andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	public void test_export_excel() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/device/export_excel")
				 .accept(MediaType.APPLICATION_JSON))
 .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void test_import_excel() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				 .get("/api/device/import_excel")
				 .accept(MediaType.APPLICATION_JSON)
				 .param("url", "C:\\Users\\26304\\git\\cell_link\\cell_link设备导入模板.xls")
				 .param("productId", "3"))
.andDo(MockMvcResultHandlers.print());
	}
	
	

}

