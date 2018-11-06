package com.hydata.intelligence.platform.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.Application;
import com.hydata.intelligence.platform.dto.ApplicationChart;
import com.hydata.intelligence.platform.dto.ApplicationChartDatastream;
import com.hydata.intelligence.platform.dto.Chart;
import com.hydata.intelligence.platform.dto.DeviceDatastream;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.ApplicationChartDsModel;
import com.hydata.intelligence.platform.model.ApplicationChartModel;
import com.hydata.intelligence.platform.model.ApplicationModel;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.ApplicationChartDatastreamRepository;
import com.hydata.intelligence.platform.repositories.ApplicationChartRepository;
import com.hydata.intelligence.platform.repositories.ApplicationRepository;
import com.hydata.intelligence.platform.repositories.ChartRepository;
import com.hydata.intelligence.platform.repositories.DeviceDatastreamRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;


/**
 * @author pyt
 * @createTime 2018年11月1日下午5:34:54
 */
@Transactional
@Service
public class ApplicationService {
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Autowired
	private ApplicationChartRepository applicationChartRepository;
	
	@Autowired
	private ApplicationChartDatastreamRepository applicationChartDatastreamRepository;
	
	@Autowired 
	private ProductRepository porductRepository;
	
	@Autowired
	private ChartRepository chartRepository;
	
	@Autowired
	private DeviceDatastreamRepository deviceDatastreamRepository;
	
	
	private static Logger logger = LogManager.getLogger(ApplicationService.class);
	@Transactional
	public JSONObject addApplication(ApplicationModel applicationModel){       
		Optional<Product> productOptional =  porductRepository.findById(applicationModel.getProductId());
		if(productOptional.isPresent()) {
			logger.debug("产品id："+applicationModel.getProductId()+"存在");
			//1.存application表
			Application application = new Application();
			application.setProductId(applicationModel.getProductId());
			application.setName(applicationModel.getName());
			application.setApplicationType(0);
			application.setCreateTime(new Date());
			Application applicationReturn = applicationRepository.save(application);
			
			List<ApplicationChartModel> applicationChartList = applicationModel.getApplicationChartList();
			JSONObject savingResult = new JSONObject();
			//2.存application_chart表
			List<JSONObject> ApplicationChartSavingResult = new ArrayList<>();
			for(ApplicationChartModel ac:applicationChartList) {
				Optional<Chart> chartOptional = chartRepository.findById(ac.getChartId());
				JSONObject ApplicationChartSingleResult = new JSONObject();
				if(chartOptional.isPresent()) {
					logger.debug("图表id："+ac.getChartId()+"存在");
					ApplicationChart applicationChart = new ApplicationChart();
					applicationChart.setApplicationId(applicationReturn.getId());
					applicationChart.setApplicationName(applicationModel.getName());
					applicationChart.setName(applicationModel.getName());
					applicationChart.setCreateTime(new Date());
					applicationChart.setChartId(ac.getChartId());
					ApplicationChart applicationChartReturn = applicationChartRepository.save(applicationChart);					
					//3.存数据流
					List<JSONObject> applicationChartDatastreamSavingResult = new ArrayList<>();
					List<ApplicationChartDsModel> acdList = ac.getApplicationChartDatastreamList();
					for(ApplicationChartDsModel acd:acdList) {
						Optional<DeviceDatastream> deviceDatastreamOptional = deviceDatastreamRepository.findById(acd.getDd_id());
						JSONObject applicationChartDatastreamSingleResult = new JSONObject();
						if(deviceDatastreamOptional.isPresent()) {
							logger.debug("图表设备数据流id："+acd.getDd_id()+"存在");
							ApplicationChartDatastream acDatastream = new ApplicationChartDatastream();
							acDatastream.setDdId(acd.getDd_id());
							acDatastream.setAcId(applicationChartReturn.getId());
							applicationChartDatastreamRepository.save(acDatastream);
							applicationChartDatastreamSingleResult.put("DeviceDatastreamId", acd.getDd_id());
							applicationChartDatastreamSingleResult.put("result", "存入");
						}else {
							applicationChartDatastreamSingleResult.put("DeviceDatastreamId", acd.getDd_id());
							applicationChartDatastreamSingleResult.put("result", "不存在");
			
						}
						applicationChartDatastreamSavingResult.add(applicationChartDatastreamSingleResult);
					}
					ApplicationChartSingleResult.put("applicationChartDatastream", applicationChartDatastreamSavingResult);
					ApplicationChartSingleResult.put("ChartId", ac.getChartId());
					ApplicationChartSingleResult.put("result", "存入");
				}else {
					ApplicationChartSingleResult.put("ChartId", ac.getChartId());
					ApplicationChartSingleResult.put("result", "不存在");
				}
				ApplicationChartSavingResult.add(ApplicationChartSingleResult);
			}
			savingResult.put("ApplicationChart", ApplicationChartSavingResult);
			return RESCODE.SUCCESS.getJSONRES(savingResult);
		}
		return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
	}
	
	@Transactional
	public JSONObject delChartApp(Integer id){
		Optional<Application> applicationOptional = applicationRepository.findById(id); 
		if(applicationOptional.isPresent()) {
			List<ApplicationChart> applicationChartList = applicationChartRepository.findByAppId(id);
			for(ApplicationChart ac:applicationChartList) {
				int result = applicationChartDatastreamRepository.deleteByAc_id(ac.getId());
				logger.debug("删除图表应用相关数据流，删除表"+ac.getId()+"相关数据流总数为："+result);
				applicationChartRepository.deleteById(ac.getId());
			}
			applicationRepository.deleteById(id);
			return RESCODE.SUCCESS.getJSONRES();
		}
		return RESCODE.APP_ID_NOT_EXIST.getJSONRES();	
	}
	
	/**
	 * 不做接口使用
	 * @param productId
	 * @return
	 */
	public JSONObject queryDetail(Integer productId){
		List<ApplicationModel> appModelList = new ArrayList<>();
		List<Application> applicationList = applicationRepository.findByProduct_id(productId);
		for(Application app:applicationList) {
			ApplicationModel appModel = new ApplicationModel();			
			//图表应用图表信息
			List<ApplicationChartModel> acmList = new ArrayList<>();
			List<ApplicationChart> acList = applicationChartRepository.findByAppId(app.getId());
			for(ApplicationChart ac:acList) {
				ApplicationChartModel acm = new ApplicationChartModel();
				//图表应用图表数据流
				List<ApplicationChartDsModel> acdmList = new ArrayList<>();
				List<ApplicationChartDatastream> acdList = applicationChartDatastreamRepository.findByAc_id(ac.getId());
				for(ApplicationChartDatastream acd : acdList) {
					ApplicationChartDsModel acdm = new ApplicationChartDsModel();
					acdm.setId(acd.getId());
					acdm.setChart_id(acd.getAcId());
					acdm.setDd_id(acd.getDdId());
					acdmList.add(acdm);
				}	
				acm.setId(ac.getId());
				acm.setChartId(ac.getChartId());
				acm.setCreateTime(ac.getCreateTime());
				acm.setApplicationChartDatastreamList(acdmList);
				acmList.add(acm);
			}	
			appModel.setApplicationChartList(acmList);
			//图表应用基本信息
			appModel.setId(app.getId());
			appModel.setName(app.getName());
			appModel.setProductId(app.getProductId());
			appModel.setCreateTime(app.getCreateTime());
			appModel.setApplicationType(app.getApplicationType());
			appModelList.add(appModel);
		}
		return RESCODE.SUCCESS.getJSONRES(appModelList);		
	}
	
	public JSONObject queryByProductId(Integer product_id){
		List<Application> appList = applicationRepository.findByProductIdAndType(product_id, 0);
		return RESCODE.SUCCESS.getJSONRES(appList);
		
	}
	
	public JSONObject getAppByProductIdAndName(Integer product_id,String app_name){
		List<Application> appList = applicationRepository.findByProduct_idAndName(product_id, app_name);
		return RESCODE.SUCCESS.getJSONRES(appList);		
	}
	
	public JSONObject getChartAppDetail(Integer app_id){
		Optional<Application> appOptional = applicationRepository.findById(app_id);
		if(appOptional.isPresent()) {
			Application app = appOptional.get();
			ApplicationModel appModel = new ApplicationModel();			
			//图表应用图表信息
			List<ApplicationChartModel> acmList = new ArrayList<>();
			List<ApplicationChart> acList = applicationChartRepository.findByAppId(app.getId());
			for(ApplicationChart ac:acList) {
				ApplicationChartModel acm = new ApplicationChartModel();
				//图表应用图表数据流
				List<ApplicationChartDsModel> acdmList = new ArrayList<>();
				List<ApplicationChartDatastream> acdList = applicationChartDatastreamRepository.findByAc_id(ac.getId());
				for(ApplicationChartDatastream acd : acdList) {
					ApplicationChartDsModel acdm = new ApplicationChartDsModel();
					acdm.setId(acd.getId());
					acdm.setChart_id(acd.getAcId());
					acdm.setDd_id(acd.getDdId());
					acdmList.add(acdm);
				}	
				acm.setId(ac.getId());
				acm.setChartId(ac.getChartId());
				acm.setCreateTime(ac.getCreateTime());
				acm.setApplicationChartDatastreamList(acdmList);
				acmList.add(acm);
			}	
			appModel.setApplicationChartList(acmList);
			//图表应用基本信息
			appModel.setId(app.getId());
			appModel.setName(app.getName());
			appModel.setProductId(app.getProductId());
			appModel.setCreateTime(app.getCreateTime());
			appModel.setApplicationType(app.getApplicationType());
			return RESCODE.SUCCESS.getJSONRES(appModel);
		}
		return RESCODE.APP_ID_NOT_EXIST.getJSONRES();
	}

}

