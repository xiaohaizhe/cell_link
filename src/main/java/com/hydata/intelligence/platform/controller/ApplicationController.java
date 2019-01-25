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
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.service.ApplicationService;
import com.hydata.intelligence.platform.utils.CheckParams;

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
	
	@RequestMapping(value="/del_app",method=RequestMethod.GET)
	public JSONObject delApp(Integer id){
		JSONObject params = new JSONObject();
		params.put("id", id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return applicationService.deleteByAppId(id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
/*	@RequestMapping(value = "/del_analysis_app",method = RequestMethod.GET)
	public JSONObject deleteAnalysisApp(Integer aa_id) {
		return applicationService.deleteAnalysisApp(aa_id);
	}*/
	
	@RequestMapping(value = "/queryByProductId",method=RequestMethod.GET)
	public JSONObject queryByProductId(Integer product_id){
		JSONObject params = new JSONObject();
		params.put("product_id", product_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return applicationService.queryByProductId(product_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value= "/get_chart_app_detail" ,method = RequestMethod.GET)
	public JSONObject getChartAppDetail(Integer app_id){
		JSONObject params = new JSONObject();
		params.put("app_id", app_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return applicationService.getChartAppDetail(app_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value= "/modify_chart_app" ,method = RequestMethod.POST)
	public JSONObject modifyChartApp(@RequestBody ApplicationModel applicationModel) {
		return applicationService.modifyChartApp(applicationModel);
	}
	
	@RequestMapping(value = "/get_by_name" , method = RequestMethod.GET)
	public JSONObject getAppByProductIdAndName(Integer product_id, String app_name){
		JSONObject params = new JSONObject();
		params.put("product_id", product_id);
		params.put("app_name", app_name);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return applicationService.getAppByProductIdAndName(product_id, app_name);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}		
	}
	
	@RequestMapping(value = "/add_analysis_app",method = RequestMethod.POST)
	public JSONObject addAnalysisApp(@RequestBody AnalysisApplicationModel aa_model) {
		return applicationService.addAnalysisApp(aa_model);
	}
	
	@RequestMapping(value = "/get_analysis_app_detail",method = RequestMethod.GET)
	public JSONObject getAnalysisAppDetail(Integer application_id) {
		JSONObject params = new JSONObject();
		params.put("application_id", application_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return applicationService.getAnalysisAppDetail(application_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}		
	}
	
	@RequestMapping(value = "/get_analysis_app",method = RequestMethod.GET)
	public JSONObject getAnalysisApp(Integer product_id,String name) {
		JSONObject params = new JSONObject();
		params.put("product_id", product_id);
		params.put("name", name);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return applicationService.queryAnalysisApp(product_id, name);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}	
		
	}
	
	@RequestMapping(value = "/analysis",method = RequestMethod.GET)
	public JSONObject analysis() {
		System.out.println("开始分析》》》》》》》");
		double[] list1 = new double[] {1,1,1,1,1};
		double[] list2 = new double[] {2,2,2,2,2};
		return applicationService.CorrelationAnalyse(list1,list2);
	}
	
	@RequestMapping(value = "/get_chart_refresh_frequence",method = RequestMethod.GET)
	public JSONObject getChartRefreshFrequence(Integer ac_id) {
		JSONObject params = new JSONObject();
		params.put("ac_id", ac_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return applicationService.getChartRefreshFrequence(ac_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}
		
	}
	
	@RequestMapping(value = "/get_chart",method = RequestMethod.GET)
	public JSONObject getChart(Integer ac_id) {
		JSONObject params = new JSONObject();
		params.put("ac_id", ac_id);
		JSONObject result = CheckParams.checkParams(params);
		if((Integer)result.get("code")==0) {			
			return applicationService.getChart(ac_id);
		}else {
			return RESCODE.PARAM_MISSING.getJSONRES(result.get("data"));
		}		
	}
}

