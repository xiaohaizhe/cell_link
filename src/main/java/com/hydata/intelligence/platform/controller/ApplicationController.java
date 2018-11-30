package com.hydata.intelligence.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.model.AnalysisApplicationModel;
import com.hydata.intelligence.platform.model.ApplicationModel;
import com.hydata.intelligence.platform.service.ApplicationService;

/**
 * @author pyt
 * @createTime 2018年11月1日下午5:34:07
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/application")
public class ApplicationController {
	@Autowired
	private ApplicationService applicationService;
	
	@RequestMapping(value="/add_chart_app",method=RequestMethod.POST)
	public JSONObject addApplication(@RequestBody ApplicationModel applicationModel){
		return applicationService.addApplication(applicationModel);
	}
	
	@RequestMapping(value="/del_chart_app",method=RequestMethod.GET)
	public JSONObject delChartApp(Integer id){
		return applicationService.delChartApp(id);
	}
	
	@RequestMapping(value = "/queryByProductId",method=RequestMethod.GET)
	public JSONObject queryByProductId(Integer product_id){
		return applicationService.queryByProductId(product_id);
	}
	
	@RequestMapping(value= "/get_chart_app_detail" ,method = RequestMethod.GET)
	public JSONObject getChartAppDetail(Integer app_id){
		return applicationService.getChartAppDetail(app_id);
	}
	
	@RequestMapping(value= "/modify_chart_app" ,method = RequestMethod.GET)
	public JSONObject modifyChartApp(@RequestBody ApplicationModel applicationModel) {
		return null;
	}
	
	@RequestMapping(value = "/get_by_name" , method = RequestMethod.GET)
	public JSONObject getAppByProductIdAndName(Integer product_id, String app_name){
		return applicationService.getAppByProductIdAndName(product_id, app_name);
	}
	
	@RequestMapping(value = "/add_analysis_app",method = RequestMethod.POST)
	public JSONObject addAnalysisApp(@RequestBody AnalysisApplicationModel aa_model) {
		return applicationService.addAnalysisApp(aa_model);
	}
	
	@RequestMapping(value = "/del_analysis_app",method = RequestMethod.GET)
	public JSONObject deleteAnalysisApp(Integer aa_id) {
		return applicationService.deleteAnalysisApp(aa_id);
	}
	
	@RequestMapping(value = "/get_analysis_app_detail",method = RequestMethod.GET)
	public JSONObject getAnalysisAppDetail(Integer application_id) {
		return applicationService.getAnalysisAppDetail(application_id);
	}
	
	@RequestMapping(value = "/get_analysis_app",method = RequestMethod.GET)
	public JSONObject getAnalysisApp(Integer product_id,String name) {
		return applicationService.queryAnalysisApp(product_id, name);
	}
	
	@RequestMapping(value = "/analysis",method = RequestMethod.GET)
	public JSONObject analysis() {
		System.out.println("开始分析》》》》》》》");
		double[] list1 = new double[] {1,1,1,1,1};
		double[] list2 = new double[] {2,2,2,2,2};
		return applicationService.CorrelationAnalyse(list1,list2);
	}
}

