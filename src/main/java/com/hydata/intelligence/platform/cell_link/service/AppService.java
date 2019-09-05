package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.*;
import com.hydata.intelligence.platform.cell_link.entity.dict.Chart;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.*;
import com.hydata.intelligence.platform.cell_link.utils.Constants;
import com.hydata.intelligence.platform.cell_link.utils.PageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.persistence.EntityManager;
import java.util.ArrayList;
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

    private JSONObject getAppChart(AppChart appChart){
        JSONObject object = new JSONObject();
        object.put("chart", appChart.getChart().getChartId());
        object.put("sequenceNumber", appChart.getSequenceNumber());
        JSONArray array = new JSONArray();
        for (AppDatastream appDatastream:appChart.getAppDatastreamList()){
            array.add(getAppDatastream(appDatastream));
        }
        object.put("appDatastreamList", array);
        return object;
    }

    private JSONObject getAppDatastream(AppDatastream appDatastream){
        JSONObject object = new JSONObject();
        object.put("deviceId", appDatastream.getDeviceId());
        object.put("datastreamId", appDatastream.getDatastream().getDatastreamId());
        return object;
    }

    private JSONObject getAppDetail(App app){
        JSONObject object = getApp(app);
        JSONArray array = new JSONArray();
        for (AppChart appChart : app.getAppChartList()){
            array.add(getAppChart(appChart));
        }
        object.put("appChartList",array);
        return object;
    }

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
                        for (AppChart appChart : app.getAppChartList()){
                            appChart.setApp(appNew);
                            JSONObject result = addChart(appChart);
                            if ((Integer) result.get(Constants.RESPONSE_CODE_KEY)!=0){
                                return result;
                            }
                        }
                        return RESCODE.SUCCESS.getJSONRES();
                    }
                    return RESCODE.APP_NAME_EXIST_IN_SCENARIO.getJSONRES();
                }
            }
            return RESCODE.SCENARIO_NOT_EXIST.getJSONRES();
        }
        return object;
    }

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
                    for (AppChart appChart:appOld.getAppChartList()) {
                        logger.info("删除图表");
                        for (AppDatastream appDatastream:appChart.getAppDatastreamList()){
                            appDatastreamRepository.deleteByAdId(appDatastream.getAdId());
                        }
                        appChartRepository.deleteByAcId(appChart.getAcId());
                    }
                    for (AppChart appChart:app.getAppChartList()){
                        appChart.setApp(appNew);
                        JSONObject result = addChart(appChart);
                        if ((Integer) result.get(Constants.RESPONSE_CODE_KEY)!=0){
                            return result;
                        }
                    }
                    return RESCODE.SUCCESS.getJSONRES(getApp(appNew));
                }
            }
            return RESCODE.APP_NOT_EXIST.getJSONRES();
        }
        return object;
    }

    public JSONObject deleteApp(Long appId) {
        if (appRepository.existsById(appId)) {
            appRepository.deleteById(appId);
            return RESCODE.SUCCESS.getJSONRES();
        }
        return RESCODE.APP_NOT_EXIST.getJSONRES();
    }

    public JSONObject findById(Long appId){
        Optional<App> appOptional = appRepository.findById(appId);
        if (appOptional.isPresent()){
            App app = appOptional.get();
            return RESCODE.SUCCESS.getJSONRES(getAppDetail(app));
        }return RESCODE.APP_NOT_EXIST.getJSONRES();
    }

    public JSONObject findByScenario(Long scenarioId,String appName,Integer page,Integer number,String sorts){
        if (scenarioRepository.existsById(scenarioId)){
            Pageable pageable = PageUtils.getPage(page, number, sorts);
            Page<App> appPage = appRepository.findByScenarioAndAppNameLike(scenarioId,appName,pageable);
            List<JSONObject> appList = new ArrayList<>();
            for (App app : appPage.getContent()){
                appList.add(getApp(app));
            }
            return RESCODE.SUCCESS.getJSONRES(appList,appPage.getTotalPages(),appPage.getTotalElements());
        }return RESCODE.SCENARIO_NOT_EXIST.getJSONRES();
    }

    public JSONObject getChart(){
        List<Chart> charts = chartRepository.findAll();
        return RESCODE.SUCCESS.getJSONRES(charts);
    }

    public JSONObject addChart(AppChart appChart) {
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

    public JSONObject deleteChart(Long acId) {
        Optional<AppChart> appChartOptional = appChartRepository.findById(acId);
        if (appChartOptional.isPresent()) {
            AppChart appChart = appChartOptional.get();
            logger.info("删除图表");
            appChartRepository.delete(appChart);
            return RESCODE.SUCCESS.getJSONRES();
        }
        return RESCODE.CHART_NOT_EXIST.getJSONRES();
    }

}
