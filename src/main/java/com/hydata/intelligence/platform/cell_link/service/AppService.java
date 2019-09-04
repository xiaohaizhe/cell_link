package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.App;
import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import com.hydata.intelligence.platform.cell_link.entity.Scenario;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.AppRepository;
import com.hydata.intelligence.platform.cell_link.repository.ScenarioRepository;
import com.hydata.intelligence.platform.cell_link.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

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

    private JSONObject getApp(App app){
        JSONObject object = new JSONObject();
        object.put("appId",app.getAppId());
        object.put("appName",app.getAppName());
        object.put("description",app.getDescription());
        object.put("created",app.getCreated());
        object.put("modified",app.getModified());
        return object;
    }

    public JSONObject addApp(App app, BindingResult br){
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            if (app.getScenario()!=null && app.getScenario().getScenarioId()!=null){
                Optional<Scenario> scenarioOptional = scenarioRepository.findById(app.getScenario().getScenarioId());
                if (scenarioOptional.isPresent()){
                    Scenario scenario = scenarioOptional.get();
                    List<App> apps = appRepository.findByAppNameAndScenario(app.getAppName(),
                            app.getScenario().getScenarioId());
                    if (apps.size()<1){
                        app.setUserId(scenario.getUser().getUserId());
                        App appNew = appRepository.save(app);
                        return RESCODE.SUCCESS.getJSONRES(appNew);
                    }
                    return RESCODE.APP_NAME_EXIST_IN_SCENARIO.getJSONRES();
                }
            }return RESCODE.SCENARIO_NOT_EXIST.getJSONRES();
        }
        return object;
    }

    public JSONObject updateApp(App app, BindingResult br){
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            if (app.getAppId()!=null ){
                Optional<App> appOptional = appRepository.findById(app.getAppId());
                if (appOptional.isPresent()){
                    App appOld = appOptional.get();
                    if (app.getAppName()!=null && !app.getAppName().equals(appOld.getAppName())){
                        List<App> apps = appRepository.findByAppNameAndScenario(app.getAppName(),
                                appOld.getScenario().getScenarioId());
                        if (apps.size()<1){
                            appOld.setAppName(app.getAppName());
                        }
                    }
                    if (app.getDescription()!=null && !app.getDescription().equals(appOld.getDescription())){
                        appOld.setDescription(app.getDescription());
                    }
                    App appNew = appRepository.saveAndFlush(appOld);
                    return RESCODE.SUCCESS.getJSONRES(getApp(appNew));
                }
            }return RESCODE.APP_NOT_EXIST.getJSONRES();
        }
        return object;
    }
}
