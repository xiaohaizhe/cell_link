package com.hydata.intelligence.platform.service;

import java.text.SimpleDateFormat;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hydata.intelligence.platform.dto.Application;
import com.hydata.intelligence.platform.dto.ApplicationAnalysis;
import com.hydata.intelligence.platform.dto.ApplicationAnalysisDatastream;
import com.hydata.intelligence.platform.dto.ApplicationChart;
import com.hydata.intelligence.platform.dto.ApplicationChartDatastream;
import com.hydata.intelligence.platform.dto.Chart;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.DeviceDatastream;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.AnalysisApplicationModel;
import com.hydata.intelligence.platform.model.ApplicationChartDsModel;
import com.hydata.intelligence.platform.model.ApplicationChartModel;
import com.hydata.intelligence.platform.model.ApplicationModel;
import com.hydata.intelligence.platform.model.DataHistory;
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
import com.hydata.intelligence.platform.utils.Constants;
import com.hydata.intelligence.platform.utils.HttpUtils;
import com.hydata.intelligence.platform.utils.MongoDBUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
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
	
	@Autowired
	private DeviceService deviceService;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();
	private static MongoClient meiyaClient = mongoDBUtil.getMongoConnect("127.0.0.1",27017);
	private static MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","data_history");
	
	private static Logger logger = LogManager.getLogger(ApplicationService.class);
	
	/**
	 * 添加图表应用
	 * @param applicationModel
	 * @return
	 */
	public JSONObject addApplication(ApplicationModel applicationModel){
		logger.debug("进入添加图表应用");
		Optional<Product> productOptional =  porductRepository.findById(applicationModel.getProductId());
		if(productOptional.isPresent()) {
			logger.debug("产品id："+applicationModel.getProductId()+"存在");
			//1.存application表，获取应用id
			Application application = new Application();
			application.setProductId(applicationModel.getProductId());
			application.setName(applicationModel.getName());
			application.setApplicationType(RESCODE.APP_CHART_TYPE.getCode());
			application.setCreateTime(new Date());
			Application applicationReturn = applicationRepository.save(application);
			
			List<ApplicationChartModel> applicationChartList = applicationModel.getApplicationChartList();
			JSONObject savingResult = new JSONObject();
			//2.存application_chart表，获得acId
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
					applicationChart.setRefresh_frequence(ac.getFrequency());
					applicationChart.setCount(ac.getSum());
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
							logger.debug("图表设备数据流id："+acd.getDd_id()+"不存在");
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
					logger.debug("图表id："+ac.getChartId()+"不存在");
				}
				ApplicationChartSavingResult.add(ApplicationChartSingleResult);
			}
			savingResult.put("ApplicationChart", ApplicationChartSavingResult);
			
			logger.debug("图表应用成功保存");
			logger.debug("保存结果："+savingResult.toJSONString());			
			
			return RESCODE.SUCCESS.getJSONRES(savingResult);
		}
		logger.debug("产品id"+applicationModel.getProductId()+"不存在");
		return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
	}
	
	/**
	 * 删除图表应用
	 * @param id
	 * @return
	 */
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
			logger.debug("成功删除图表应用");
			return RESCODE.SUCCESS.getJSONRES();
		}
		logger.debug("应用id"+id+"不存在");
		return RESCODE.APP_ID_NOT_EXIST.getJSONRES();	
	}
	/**
	 * 修改图表应用
	 * @param applicationModel
	 * @return
	 */
	public JSONObject modifyChartApp(ApplicationModel applicationModel) {
		Optional<Application> appOptional = applicationRepository.findById(applicationModel.getId());
		if(appOptional.isPresent()) {
			//修改应用名
			Application app = appOptional.get();
			if(applicationModel.getName()!=null&&applicationModel.getName()!="") {
				app.setName(applicationModel.getName());
			}
			applicationRepository.saveAndFlush(app);
			//修改应用图表
			List<ApplicationChart> applicationCharts = applicationChartRepository.findByAppId(app.getId());
			List<ApplicationChartModel> appCharts = applicationModel.getApplicationChartList();
			for(ApplicationChart ac:applicationCharts) {
				List<ApplicationChartDatastream> acdList = applicationChartDatastreamRepository.findByAc_id(ac.getId());
				for(ApplicationChartDatastream acd:acdList) {
					applicationChartDatastreamRepository.delete(acd);
				}
				applicationChartRepository.delete(ac);
			}
			for(ApplicationChartModel ac:appCharts) {
				Optional<Chart> chartOptional = chartRepository.findById(ac.getChartId());
				if(chartOptional.isPresent()) {
					logger.debug("图表id："+ac.getChartId()+"存在");
					ApplicationChart applicationChart = new ApplicationChart();
					applicationChart.setApplicationId(applicationModel.getId());
					applicationChart.setApplicationName(applicationModel.getName());
					applicationChart.setName(applicationModel.getName());
					applicationChart.setCreateTime(new Date());
					applicationChart.setChartId(ac.getChartId());
					ApplicationChart applicationChartReturn = applicationChartRepository.save(applicationChart);					
					//3.存数据流
					List<ApplicationChartDsModel> acdList = ac.getApplicationChartDatastreamList();
					for(ApplicationChartDsModel acd:acdList) {
						Optional<DeviceDatastream> deviceDatastreamOptional = deviceDatastreamRepository.findById(acd.getDd_id());
						if(deviceDatastreamOptional.isPresent()) {
							logger.debug("图表设备数据流id："+acd.getDd_id()+"存在");
							ApplicationChartDatastream acDatastream = new ApplicationChartDatastream();
							acDatastream.setDdId(acd.getDd_id());
							acDatastream.setAcId(applicationChartReturn.getId());
							applicationChartDatastreamRepository.save(acDatastream);
						}
					}
				}
			}
			
			
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
	 * 查询图表应用列表
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
		logger.debug("应用id"+app_id+"不存在");
		return RESCODE.APP_ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 获取图表数据
	 * @param ac_id(application_chart_id)
	 * @return
	 */
	public JSONObject getChart(Integer ac_id) {
		Optional< ApplicationChart> optional = applicationChartRepository.findById(ac_id);
		if(optional.isPresent()) {
			ApplicationChart applicationChart = optional.get();
			int count = applicationChart.getCount();
			List<ApplicationChartDatastream> appChartDsList = applicationChartDatastreamRepository.findByAc_id(ac_id);
			JSONArray array = new JSONArray();
			for(ApplicationChartDatastream acd:appChartDsList) {
				Integer dd_id = acd.getDdId();
				BasicDBObject query = new BasicDBObject(); 
				query.put("dd_id", dd_id);
				FindIterable<Document> documents1 = collection.find(query).limit(count);
				List<DataHistory> datas = new ArrayList<>();
				for (Document d : documents1) {
					DataHistory dataHistory = deviceService.returnData(d);
					datas.add(dataHistory);			
			    }
				array.add(datas);
			}
			return RESCODE.SUCCESS.getJSONRES(array);
		}else {
			return RESCODE.APP_CHART_ID_NOT_EXIST.getJSONRES();
		}		
	}
	/**
	 * 获取图表刷新频率（s）
	 * @param ac_id
	 * @return
	 */
	public JSONObject getChartRefreshFrequence(Integer ac_id) {
		Optional< ApplicationChart> optional = applicationChartRepository.findById(ac_id);
		if(optional.isPresent()) {
			ApplicationChart applicationChart = optional.get();
			int frequence = applicationChart.getRefresh_frequence();
			return RESCODE.SUCCESS.getJSONRES(frequence);
		}else {
			return RESCODE.APP_CHART_ID_NOT_EXIST.getJSONRES();
		}	
	}

	/**
	 * 添加智能分析应用
	 * @param analysisApplicationModel
	 * @return
	 */
	public JSONObject addAnalysisApp(AnalysisApplicationModel analysisApplicationModel) {
		logger.debug("开始添加智能分析应用~");
		logger.debug(analysisApplicationModel.toString());
		Optional<Product> productOptional = porductRepository.findById(analysisApplicationModel.getProductId());
		if(productOptional.isPresent()) {
			logger.debug("产品id:"+analysisApplicationModel.getProductId()+"存在");
//			1.存Application表
			Application application = new Application();
			application.setProductId(analysisApplicationModel.getProductId());
			application.setCreateTime(new Date());
			application.setApplicationType(RESCODE.APP_ANALYSIS_TYPE.getCode());
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
			if(analysisApplicationModel.getApplicationType()==RESCODE.CORRELATION_ANALYSE.getCode()) {
				List<ApplicationAnalysisDatastream> datastreams = analysisApplicationModel.getAnalysisDatastreams();
				double[][] array = dealWithData(datastreams);
				objectReturn = CorrelationAnalyse(array);
			}else if(analysisApplicationModel.getApplicationType()==RESCODE.LINEAR_REGRESSION_ANALYSE.getCode()) {
				List<ApplicationAnalysisDatastream> datastreams = analysisApplicationModel.getAnalysisDatastreams();
				List<ApplicationAnalysisDatastream> datastreamso = new ArrayList<>();
				List<ApplicationAnalysisDatastream> datastreamsi = new ArrayList<>();
				for(ApplicationAnalysisDatastream ds:datastreams) {
					switch(ds.getType()) {
						case 0:
							datastreamso.add(ds);
							break;
						default:
							datastreamsi.add(ds);
							break;
					}					
				}
				double[][] out = dealWithData(datastreamso);
				double[][] input = dealWithData(datastreamsi);
				objectReturn = LinearRegressionAnalyse(out[0],input);
			}			
			return RESCODE.SUCCESS.getJSONRES(objectReturn);
		}else {
			return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
		}		
	}
	
	public double[][] dealWithData(List<ApplicationAnalysisDatastream> datastreams) {
		double[][] result = new double[datastreams.size()][];
		for(ApplicationAnalysisDatastream datastream : datastreams) {
			int i=0;
			Integer ddId = datastream.getDdId();
			Date dateE = datastream.getEnd();
			Date dateS = datastream.getStart();
			//从MongoDB中获取起始时间内全部数据
			BasicDBObject query = new BasicDBObject(); 
			query.put("ddId", ddId);
			query.put("date",BasicDBObjectBuilder.start("$gte", dateS).add("$lte", dateE).get());//key为表字段名
			FindIterable<Document> documents1 = collection.find(query);
			List<com.hydata.intelligence.platform.model.DeviceDatastream> dss = 
					new ArrayList<>();
			for (Document d : documents1) {
				com.hydata.intelligence.platform.model.DeviceDatastream ds = 
						returnDatastream(d);
				dss.add(ds);			
		    }
			/*
			 * 数据处理 
			 */
			double f = datastream.getFrequency();
			double interval = 1/f;
			
			int times = (int) ((dateE.getTime()-dateS.getTime())/(interval*1000));
			
			double value = 0;
			int count = 0;
			int j = 0;
			
			Date t = new Date();
			t.setTime(dateS.getTime());
			for(com.hydata.intelligence.platform.model.DeviceDatastream data : dss) {
				Date d = data.getDate();
				double v = data.getValue();
				if(d.getTime()>=t.getTime()&&d.getTime()<(t.getTime()+interval*1000)==false) {
					value = (value/count);
					result[i][j] = value;
					j++;
					value = 0;
					count = 0;
					t.setTime((long) (t.getTime()+interval*1000));					
				}
				value += v;
				count ++;
			}
			i++;
		}
		return result;
	}
	
	public com.hydata.intelligence.platform.model.DeviceDatastream returnDatastream(Document d) {
		com.hydata.intelligence.platform.model.DeviceDatastream datastream = 
				new com.hydata.intelligence.platform.model.DeviceDatastream();
		datastream.setDd_id(d.getInteger("dd_id"));
		datastream.setName(d.getString("name"));
		datastream.setValue(d.getDouble("value"));
		return datastream;	
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
	
	public JSONObject CorrelationAnalyse(double[]...lists) {
		logger.debug("进入相关性分析》》》》》》");
		String url = Config.getString("python.url");
		url += "/correlation_analyse";
		JSONObject param = new JSONObject();
		param.put("params", lists);
		JSONObject jsonReturn = HttpUtils.sendPost(url, param.toJSONString());
		System.out.println("获取分析结果《《《《《《"+jsonReturn);
		return jsonReturn;
	} 
	
	public JSONObject LinearRegressionAnalyse(double[] output,double[]...inputs) {
		logger.debug("进入线性回归分析》》》》》》");
		String url = Config.getString("python.url");
		url += "/linear_analyse";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("input",inputs);
		jsonObject.put("output",output);
		JSONObject jsonReturn = HttpUtils.sendPost(url, jsonObject.toJSONString());
		return jsonReturn;
	}
	
	
}

