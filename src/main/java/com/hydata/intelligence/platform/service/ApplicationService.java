package com.hydata.intelligence.platform.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hydata.intelligence.platform.dto.Application;
import com.hydata.intelligence.platform.dto.ApplicationAnalysis;
import com.hydata.intelligence.platform.dto.ApplicationAnalysisDatastream;
import com.hydata.intelligence.platform.dto.ApplicationChart;
import com.hydata.intelligence.platform.dto.ApplicationChartDatastream;
import com.hydata.intelligence.platform.dto.Chart;
import com.hydata.intelligence.platform.dto.DeviceDatastream;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.AnalysisApplicationModel;
import com.hydata.intelligence.platform.model.ApplicationChartDsModel;
import com.hydata.intelligence.platform.model.ApplicationChartModel;
import com.hydata.intelligence.platform.model.ApplicationModel;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.ApplicationAnalysisDatastreamRepository;
import com.hydata.intelligence.platform.repositories.ApplicationAnalysisRepository;
import com.hydata.intelligence.platform.repositories.ApplicationChartDatastreamRepository;
import com.hydata.intelligence.platform.repositories.ApplicationChartRepository;
import com.hydata.intelligence.platform.repositories.ApplicationRepository;
import com.hydata.intelligence.platform.repositories.ChartRepository;
import com.hydata.intelligence.platform.repositories.DeviceDatastreamRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.utils.Config;
import com.hydata.intelligence.platform.utils.HttpUtils;
import com.hydata.intelligence.platform.utils.MongoDBUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;


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

	@Autowired
	private ApplicationAnalysisRepository applicationAnalysisRepository;
	
	@Autowired
	private ApplicationAnalysisDatastreamRepository analysisDatastreamRepository; 
	
	private static Logger logger = LogManager.getLogger(ApplicationService.class);
	
	/**
	 * 添加图表应用
	 * @param applicationModel
	 * @return
	 */
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
	
	/**
	 * 删除图表应用
	 * @param id
	 * @return
	 */
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
	
	/**
	 * 查询图表应用
	 * @param product_id
	 * @return
	 */
	public JSONObject queryByProductId(Integer product_id){
		List<Application> appList = applicationRepository.findByProductIdAndType(product_id, 0);
		return RESCODE.SUCCESS.getJSONRES(appList);		
	}
	
	/**
	 * 获取图表应用
	 * @param product_id
	 * @param app_name
	 * @return
	 */
	public JSONObject getAppByProductIdAndName(Integer product_id,String app_name){
		List<Application> appList = applicationRepository.findByProduct_idAndName1(product_id, app_name);
		return RESCODE.SUCCESS.getJSONRES(appList);		
	}
	
	/**
	 * 获取其图表应用详情
	 * @param app_id
	 * @return
	 */
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
	
	/**
	 * 添加智能分析应用
	 * @param analysisApplicationModel
	 * @return
	 */
	public JSONObject Add_aa(AnalysisApplicationModel analysisApplicationModel) {
		logger.debug("开始~");
		logger.debug(analysisApplicationModel.toString());
		Optional<Product> productOptional = porductRepository.findById(analysisApplicationModel.getProductId());
		if(productOptional.isPresent()) {
			logger.debug("产品id:"+analysisApplicationModel.getProductId()+"存在");
//			1.存Application表
			Application application = new Application();
			application.setProductId(analysisApplicationModel.getProductId());
			application.setCreateTime(new Date());
			application.setApplicationType(1);
			application.setName(analysisApplicationModel.getName());
			Application app = applicationRepository.save(application);
//			2.存ApplicationAnalysis表
			ApplicationAnalysis applicationAnalysis = new ApplicationAnalysis();
			applicationAnalysis.setAaType(analysisApplicationModel.getApplicationType());
			applicationAnalysis.setApplicationId(app.getId());
			applicationAnalysis.setApplicationName(analysisApplicationModel.getName());
			applicationAnalysis.setCreateTime(new Date());
			ApplicationAnalysis aaReturn = applicationAnalysisRepository.save(applicationAnalysis);
//			3.存ApplicationAnalysisDatastream表
			List<ApplicationAnalysisDatastream> aadLsit = analysisApplicationModel.getAnalysisDatastreams();
			for(ApplicationAnalysisDatastream aad : aadLsit) {
				ApplicationAnalysisDatastream analysisDatastream = new ApplicationAnalysisDatastream();
				analysisDatastream.setAaId(aaReturn.getId());
				analysisDatastream.setDdId(aad.getDdId());
				analysisDatastream.setType(aad.getType());
				analysisDatastream.setStart(aad.getStart());
				analysisDatastream.setEnd(aad.getEnd());
				analysisDatastream.setFrequency(aad.getFrequency());
				ApplicationAnalysisDatastream aadReturn = analysisDatastreamRepository.save(analysisDatastream);
			}
			JSONObject objectReturn = new JSONObject();
			if(analysisApplicationModel.getApplicationType()==0) {
				objectReturn = CorrelationAnalyse(null);
			}else if(analysisApplicationModel.getApplicationType()==1) {
				objectReturn = LinearRegressionAnalyse(null,null);
			}			
			return RESCODE.SUCCESS.getJSONRES(objectReturn);
		}else {
			return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
		}		
	}
	
	/**
	 * 删除智能分析应用
	 * @param aaId
	 * @return
	 */
	public JSONObject deleteAnalysisApp(Integer aaId) {
		Optional<ApplicationAnalysis> applicationAnalysisOptional = applicationAnalysisRepository.findById(aaId);
		if(applicationAnalysisOptional.isPresent()) {
			List<ApplicationAnalysisDatastream> applicationAnalysisDatastreams = analysisDatastreamRepository.findByAa_id(aaId);
			if(applicationAnalysisDatastreams!=null&&applicationAnalysisDatastreams.size()>0) {
				for(ApplicationAnalysisDatastream aad : applicationAnalysisDatastreams) {
					analysisDatastreamRepository.deleteById(aad.getId());
				}			
			}
			applicationRepository.deleteById(applicationAnalysisOptional.get().getApplicationId());
			applicationAnalysisRepository.deleteById(aaId);
			return RESCODE.SUCCESS.getJSONRES();
		}
		return RESCODE.APP_ID_NOT_EXIST.getJSONRES();
	}
	
	/**
	 * 查询智能分析应用
	 * @param productId
	 * @param name
	 * @return
	 */
	public JSONObject queryAnalysisApp(Integer productId,String name) {
		List<Application> applications=  applicationRepository.findByProduct_idAndName2(productId, name);
		return RESCODE.SUCCESS.getJSONRES(applications);
	}
	
	/**
	 * 查询智能应用详情
	 * @param applicationId
	 * @return
	 */
	public JSONObject getAnalysisAppDetail(Integer applicationId) {
		Optional<ApplicationAnalysis> aaOptional = applicationAnalysisRepository.findByApplicationId(applicationId);
		AnalysisApplicationModel model = new AnalysisApplicationModel();
		Optional<Application> AppOptional = applicationRepository.findById(applicationId);	
		if(AppOptional.isPresent()) {
			if(aaOptional.isPresent()) {
				List<ApplicationAnalysisDatastream> analysisDatastreams = analysisDatastreamRepository.findByAa_id(aaOptional.get().getId());
				model.setId(aaOptional.get().getId());
				model.setName(aaOptional.get().getApplicationName());
				model.setCreateTime(aaOptional.get().getCreateTime());
				model.setProductId(AppOptional.get().getProductId());
				model.setApplicationType(1);
				model.setAnalysisDatastreams(analysisDatastreams);
				return RESCODE.SUCCESS.getJSONRES(model);
			}
			return RESCODE.APP_ID_NOT_EXIST.getJSONRES();
		}else {
			return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
		}		
	}
	/**
	 * 历史数据处理
	 * @param lists
	 * @return
	 */
	public JSONObject dataProcessing(List<ApplicationAnalysisDatastream> lists) {
		for(ApplicationAnalysisDatastream aad:lists) {
			Integer ddId = aad.getDdId();
			Date start = aad.getStart();
			Date end = aad.getEnd();
			 MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();
		        MongoClient meiyaClient = mongoDBUtil.getMongoConnect("127.0.0.1",27017);
		 
		        try {
		            MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","data_history");
		           
		            Map<String,Object> conditions = Maps.newHashMap();
		            conditions.put("dd_id",1);
		            
		            FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,conditions,null,null,null,null,null,5);
		            mongoDBUtil.printDocuments(documents);
		           
		        } catch (Exception e) {
		            System.err.println(e.getClass().getName() + ": " + e.getMessage());
		        }
			
		}
		return null;
	}
	
	public JSONObject CorrelationAnalyse(Integer[]...lists) {
		String url = Config.getString("python.url");
		url += "/correlation_analyse";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("params",lists);
		JSONObject jsonReturn = HttpUtils.sendGet(url, jsonObject.toJSONString());
		return jsonReturn;
	} 
	
	public JSONObject LinearRegressionAnalyse(Integer[] input,Integer[]...output) {
		String url = Config.getString("python.url");
		url += "/linear_analyse";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("input",input);
		jsonObject.put("output",output);
		JSONObject jsonReturn = HttpUtils.sendGet(url, jsonObject.toJSONString());
		return jsonReturn;
	}
	
	
}

