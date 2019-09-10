package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.*;
import com.hydata.intelligence.platform.cell_link.entity.dict.Chart;
import com.hydata.intelligence.platform.cell_link.model.AnalysisApplication;
import com.hydata.intelligence.platform.cell_link.model.AnalysisDatastream;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.*;
import com.hydata.intelligence.platform.cell_link.utils.Constants;
import com.hydata.intelligence.platform.cell_link.utils.HttpUtils;
import com.hydata.intelligence.platform.cell_link.utils.PageUtils;
import com.hydata.intelligence.platform.cell_link.utils.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName AppService
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/3 11:54
 * @Version
 */
@Service
@Transactional
public class AppService {
    @Autowired
    private AppRepository appRepository;
    @Autowired
    private ScenarioRepository scenarioRepository;
    @Autowired
    private ChartRepository chartRepository;
    @Autowired
    private AppChartRepository appChartRepository;
    @Autowired
    private DatastreamRepository datastreamRepository;
    @Autowired
    private AppDatastreamRepository appDatastreamRepository;
    @Autowired
    private OplogService oplogService;
    @Autowired
    private DatapointRepository datapointRepository;

    @Value("${python.url}")
    private String python_url;

    private static Logger logger = LogManager.getLogger(AppService.class);

    private JSONObject getApp(App app) {
        JSONObject object = new JSONObject();
        object.put("appId", app.getAppId());
        object.put("appName", app.getAppName());
        object.put("description", app.getDescription());
        object.put("created", app.getCreated());
        object.put("modified", app.getModified());
        return object;
    }

    private JSONObject getAppChart(AppChart appChart) {
        JSONObject object = new JSONObject();
        object.put("chart", appChart.getChart().getChartId());
        object.put("sequenceNumber", appChart.getSequenceNumber());
        JSONArray array = new JSONArray();
        for (AppDatastream appDatastream : appChart.getAppDatastreamList()) {
            array.add(getAppDatastream(appDatastream));
        }
        object.put("appDatastreamList", array);
        return object;
    }

    private JSONObject getAppDatastream(AppDatastream appDatastream) {
        JSONObject object = new JSONObject();
        object.put("deviceId", appDatastream.getDeviceId());
        object.put("datastreamId", appDatastream.getDatastream().getDatastreamId());
        return object;
    }

    private JSONObject getAppDetail(App app) {
        JSONObject object = getApp(app);
        JSONArray array = new JSONArray();
        for (AppChart appChart : app.getAppChartList()) {
            array.add(getAppChart(appChart));
        }
        object.put("appChartList", array);
        return object;
    }

    @CacheEvict(cacheNames = {"app", "user", "device"}, allEntries = true)
    public JSONObject addApp(App app, BindingResult br) {
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            App appNew = new App();
            if (app.getScenario() != null && app.getScenario().getScenarioId() != null) {
                Optional<Scenario> scenarioOptional = scenarioRepository.findById(app.getScenario().getScenarioId());
                if (scenarioOptional.isPresent()) {
                    Scenario scenario = scenarioOptional.get();
                    appNew.setScenario(scenario);
                    List<App> apps = appRepository.findByAppNameAndScenario(app.getAppName(),
                            app.getScenario().getScenarioId());
                    if (apps.size() < 1) {
                        appNew.setAppName(app.getAppName());
                        appNew.setDescription(app.getDescription());
                        appNew.setUserId(scenario.getUser().getUserId());

                        appNew = appRepository.save(appNew);
                        for (AppChart appChart : app.getAppChartList()) {
                            appChart.setApp(appNew);
                            JSONObject result = addChart(appChart);
                            if ((Integer) result.get(Constants.RESPONSE_CODE_KEY) != 0) {
                                return result;
                            }
                        }
                        oplogService.app(appNew.getUserId(), "添加应用:" + appNew.getAppName());
                        return RESCODE.SUCCESS.getJSONRES();
                    }
                    return RESCODE.APP_NAME_EXIST_IN_SCENARIO.getJSONRES();
                }
            }
            return RESCODE.SCENARIO_NOT_EXIST.getJSONRES();
        }
        return object;
    }

    @CacheEvict(cacheNames = {"app"}, allEntries = true)
    public JSONObject updateApp(App app, BindingResult br) {
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            if (app.getAppId() != null) {
                Optional<App> appOptional = appRepository.findById(app.getAppId());
                if (appOptional.isPresent()) {
                    App appOld = appOptional.get();
                    if (app.getAppName() != null && !app.getAppName().equals(appOld.getAppName())) {
                        List<App> apps = appRepository.findByAppNameAndScenario(app.getAppName(),
                                appOld.getScenario().getScenarioId());
                        if (apps.size() < 1) {
                            appOld.setAppName(app.getAppName());
                        }
                    }
                    if (app.getDescription() != null && !app.getDescription().equals(appOld.getDescription())) {
                        appOld.setDescription(app.getDescription());
                    }
                    App appNew = appRepository.saveAndFlush(appOld);
                    for (AppChart appChart : appOld.getAppChartList()) {
                        logger.info("删除图表");
                        for (AppDatastream appDatastream : appChart.getAppDatastreamList()) {
                            appDatastreamRepository.deleteByAdId(appDatastream.getAdId());
                        }
                        appChartRepository.deleteByAcId(appChart.getAcId());
                    }
                    for (AppChart appChart : app.getAppChartList()) {
                        appChart.setApp(appNew);
                        JSONObject result = addChart(appChart);
                        if ((Integer) result.get(Constants.RESPONSE_CODE_KEY) != 0) {
                            return result;
                        }
                    }
                    oplogService.app(appNew.getUserId(), "修改应用:" + appNew.getAppName());
                    return RESCODE.SUCCESS.getJSONRES(getApp(appNew));
                }
            }
            return RESCODE.APP_NOT_EXIST.getJSONRES();
        }
        return object;
    }

    @CacheEvict(cacheNames = {"app", "user", "device"}, allEntries = true)
    public JSONObject deleteApp(Long appId) {
        Optional<App> appOptional = appRepository.findById(appId);
        if (appOptional.isPresent()) {
            App app = appOptional.get();
            appRepository.deleteById(appId);
            oplogService.app(app.getUserId(), "删除应用:" + app.getAppName());
            return RESCODE.SUCCESS.getJSONRES();
        }
        return RESCODE.APP_NOT_EXIST.getJSONRES();
    }

    @Cacheable(value = "app", keyGenerator = "myKeyGenerator")
    public JSONObject findById(Long appId) {
        Optional<App> appOptional = appRepository.findById(appId);
        if (appOptional.isPresent()) {
            App app = appOptional.get();
            return RESCODE.SUCCESS.getJSONRES(getAppDetail(app));
        }
        return RESCODE.APP_NOT_EXIST.getJSONRES();
    }

    @Cacheable(value = "app", keyGenerator = "myKeyGenerator")
    public JSONObject findByScenario(Long scenarioId, String appName, Integer page, Integer number, String sorts) {
        if (scenarioRepository.existsById(scenarioId)) {
            Pageable pageable = PageUtils.getPage(page, number, sorts);
            Page<App> appPage = appRepository.findByScenarioAndAppNameLike(scenarioId, appName, pageable);
            List<JSONObject> appList = new ArrayList<>();
            for (App app : appPage.getContent()) {
                appList.add(getApp(app));
            }
            return RESCODE.SUCCESS.getJSONRES(appList, appPage.getTotalPages(), appPage.getTotalElements());
        }
        return RESCODE.SCENARIO_NOT_EXIST.getJSONRES();
    }

    @Cacheable(value = "app", keyGenerator = "myKeyGenerator")
    public JSONObject getChart() {
        List<Chart> charts = chartRepository.findAll();
        return RESCODE.SUCCESS.getJSONRES(charts);
    }

    private JSONObject addChart(AppChart appChart) {
        App app = appChart.getApp();
        if (appChart.getChart() != null && appChart.getChart().getChartId() != null) {
            Optional<Chart> chartOptional = chartRepository.findById(appChart.getChart().getChartId());
            if (chartOptional.isPresent()) {
                Chart chart = chartOptional.get();
                if (appChart.getRefreshFrequency() == null || appChart.getRefreshFrequency() == 0
                        || appChart.getTimeFrame() == null || appChart.getTimeFrame() <= 0
                        || appChart.getSequenceNumber() == null || appChart.getSequenceNumber() == 0) {
                    return RESCODE.PARAM_MISSING.getJSONRES();
                }

                //检查图表中数据流
                boolean flag = true;
                for (AppDatastream appDatastream : appChart.getAppDatastreamList()) {
                    if (appDatastream.getDatastream() != null
                            && appDatastream.getDatastream().getDatastreamId() != null) {
                        Optional<Datastream> datastreamOptional =
                                datastreamRepository.findById(appDatastream.getDatastream().getDatastreamId());
                        if (!datastreamOptional.isPresent()) {
                            flag = false;
                        }
                    } else {
                        flag = false;
                    }
                }

                if (flag) {
                    AppChart appChartNew = new AppChart();
                    appChartNew.setApp(app);
                    appChartNew.setChart(chart);
                    appChartNew.setRefreshFrequency(appChart.getRefreshFrequency());
                    appChartNew.setSequenceNumber(appChart.getSequenceNumber());
                    appChartNew.setTimeFrame(appChart.getTimeFrame());
                    appChartNew = appChartRepository.save(appChartNew);

                    for (AppDatastream appDatastream : appChart.getAppDatastreamList()) {
                        appDatastream.setAppChart(appChartNew);
                        Optional<Datastream> datastreamOptional =
                                datastreamRepository.findById(appDatastream.getDatastream().getDatastreamId());
                        if (datastreamOptional.isPresent()) {
                            Datastream datastream = datastreamOptional.get();
                            appDatastream.setDeviceId(datastream.getDevice().getDeviceId());
                            appDatastream.setDeviceName(datastream.getDeviceName());
                            appDatastreamRepository.save(appDatastream);
                        }
                    }
                    return RESCODE.SUCCESS.getJSONRES();
                }
                return RESCODE.DATASTREAM_NOT_EXIST.getJSONRES();
            }
        }
        return RESCODE.CHART_TYPE_NOT_EXIST.getJSONRES();
    }

/*    public JSONObject deleteChart(Long acId) {
        Optional<AppChart> appChartOptional = appChartRepository.findById(acId);
        if (appChartOptional.isPresent()) {
            AppChart appChart = appChartOptional.get();
            logger.info("删除图表");
            appChartRepository.delete(appChart);
            return RESCODE.SUCCESS.getJSONRES();
        }
        return RESCODE.CHART_NOT_EXIST.getJSONRES();
    }*/

    public JSONObject analysisApplication(AnalysisApplication analysisApplication) {
        logger.info(analysisApplication);
        JSONObject objectReturn = new JSONObject();
        JSONObject return_result = new JSONObject();
        switch (analysisApplication.getApplicationType()) {
            case 0://0-CORRELATION_ANALYSE
                JSONArray resultdata = new JSONArray();
                List<AnalysisDatastream> datastreamList = analysisApplication.getAnalysisDatastreams();
                logger.debug("开始处理数据");
                long ss = System.currentTimeMillis();
                JSONArray array = dealWithData(datastreamList);
                long ee = System.currentTimeMillis();
                logger.debug("共计耗时："+(ee-ss)+"ms");
                logger.debug("结束处理数据");
                objectReturn = CorrelationAnalyse(array);
                if ((Integer)objectReturn.get("code")==0){
                    if(datastreamList.size()==1){
                        JSONArray a = new JSONArray();
                        a.add(0);
                        a.add(0);
                        a.add(1);
                        resultdata.add(a);
                    }else{
                        //处理二维数组，处理成热力图所需数据格式
                        JSONArray result = (JSONArray)objectReturn.get("result");
                        logger.info(result.size());
                        for (int i = 0; i <result.size() ; i++) {
                            JSONArray r = (JSONArray)result.get(i);
                            logger.info(r.size());
                            for (int j = 0; j < r.size(); j++) {
                                JSONArray a = new JSONArray();
                                a.add(j);
                                a.add(result.size()-1-i);
                                float value = 0;
                                if (r.get(j) != null) {
                                    value = ((BigDecimal) r.get(j)).floatValue();
                                }
                                value = (float)Math.round(value*100)/100;
                                a.add(value);
//									logger.info(a);
                                resultdata.add(a);
                            }
                        }
                    }
//						logger.info(resultdata);
                    return_result.put("data",resultdata);
                }else{
                    return RESCODE.FAILURE.getJSONRES(objectReturn.get("msg"));
                }
                break;
            case 1://1-LINEAR_REGRESSION_ANALYSE
                List<AnalysisDatastream> datastreams = analysisApplication.getAnalysisDatastreams();
                List<AnalysisDatastream> datastreamso = new ArrayList<>();
                List<AnalysisDatastream> datastreamsi = new ArrayList<>();
                for(AnalysisDatastream ds:datastreams) {
                    switch(ds.getType()) {
                        case 0:
                            datastreamso.add(ds);
                            break;
                        default:
                            datastreamsi.add(ds);
                            break;
                    }
                }
					/*logger.info("进入数据分析");
					logger.debug("开始处理数据");
					long ss = System.currentTimeMillis();

					long ee = System.currentTimeMillis();
					logger.debug("共计耗时："+(ee-ss)+"ms");
					logger.debug("结束处理数据");*/
                JSONArray out = dealWithData(datastreamso);
                JSONArray input = dealWithData(datastreamsi);
                objectReturn = LinearRegressionAnalyse(out,input);
                logger.info(objectReturn);
                if ((Integer)objectReturn.get("code")==0){
                    return_result.put("data",objectReturn.get("result"));
                    JSONArray datapoints = new JSONArray();
                    if (datastreamsi.size()==1){
                        JSONArray in = (JSONArray)input.get(0);
                        double x_max = (double)in.get(0);
                        double x_min = (double)in.get(0);
                        double y_max = (double)out.get(0);
                        double y_min = (double)out.get(0);
                        for (int i = 0; i < in.size(); i++) {
                            if ((double)in.get(i) > x_max) x_max = (double)in.get(i);
                            if ((double)in.get(i) < x_min) x_min = (double)in.get(i);
                            if ((double)out.get(i) > y_max) y_max = (double)out.get(i);
                            if ((double)out.get(i) < y_min) y_min = (double)out.get(i);

                            JSONArray object = new JSONArray();
                            object.add(in.get(i));
                            object.add(out.get(i));
                            datapoints.add(object);
                        }
                        logger.info(datapoints);
                        return_result.put("x_max",x_max);
                        return_result.put("x_min",x_min);
                        return_result.put("y_max",y_max);
                        return_result.put("y_min",y_min);
                        return_result.put("datapoints",datapoints);
                    }
                }else{
                    return RESCODE.FAILURE.getJSONRES(objectReturn.get("msg"));
                }
                break;
            default:

        }
        return RESCODE.SUCCESS.getJSONRES(return_result);
    }

    private JSONArray dealWithData(List<AnalysisDatastream> datastreams) {
        if (datastreams.size() == 1 && datastreams.get(0).getType() == 0) {
            return dealWithData(datastreams.get(0));
        } else {
            JSONArray Array = new JSONArray();
            for (AnalysisDatastream datastream : datastreams) {
                JSONArray a = dealWithData(datastream);
                Array.add(a);
            }
            return Array;
        }
    }

    private JSONArray dealWithData(AnalysisDatastream datastream) {
        JSONArray a = new JSONArray();
        long ddId = datastream.getDatastreamId();
        Date dateE = StringUtil.getDate(datastream.getEnd());
        Date dateS = StringUtil.getDate(datastream.getStart());
        List<Datapoint> data_histories = datapointRepository.findByDatastreamIdAndCreatedBetween(ddId, dateS, dateE);

        double f = datastream.getFrequency();//单位s
        int times = ((dateE.getTime()-dateS.getTime())%(f*1000))==0?
                (int) ((dateE.getTime()-dateS.getTime())/(f*1000)):
                (int) ((dateE.getTime()-dateS.getTime())/(f*1000))+1;
		/*logger.info("开始处理数据流:"+ddId+"的历史数据");
		logger.info("根据数据流频率:"+f+"和选取时间段:"+sdf.format(dateS)+"-"+sdf.format(dateE));
		logger.info("可知处理后数组长度应为："+times);
		logger.info("历史数据size:"+data_histories.size());*/


        for(int i = 0 ; i<times ; i++) {
			/*logger.info("数组中第"+(i+1)+"个数据");
			logger.info("数据开始时间加："+i*f+"s");*/
            int count = 0;
            double sum = 0;
            for(Datapoint data : data_histories) {
                Date d = data.getCreated();
                double v = data.getValue();
                if(d.getTime()>=(dateS.getTime()+i*f*1000)&&d.getTime()<(dateS.getTime()+i*f*1000+f*1000)) {
//					logger.info(sdf.format(d)+":"+v);
                    count++;
                    sum+=v;
                }
            }
            a.add(count==0?0:sum/count);
        }
        return a;
    }

    //智能分析
    public JSONObject CorrelationAnalyse(JSONArray lists) {
        logger.debug("进入相关性分析》》》》》》");
        String url = python_url;
        url += "/correlation_analyse";
        JSONObject param = new JSONObject();
        param.put("params", lists);
        return HttpUtils.doPost(url, param);
    }

    public JSONObject LinearRegressionAnalyse(JSONArray output, JSONArray inputs) {
        logger.debug("进入线性回归分析》》》》》》");
        String url = python_url;
        url += "/linear_regression";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("input", inputs);
        jsonObject.put("output", output);
        return HttpUtils.doPost(url, jsonObject);
    }

}
