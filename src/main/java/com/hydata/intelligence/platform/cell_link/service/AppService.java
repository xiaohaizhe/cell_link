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

    public JSONObject addApp(App app, BindingResult br){
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            if (app.getScenario()!=null && app.getScenario().getScenarioId()!=null){
                Optional<Scenario> scenarioOptional = scenarioRepository.findById(app.getScenario().getScenarioId());
                if (scenarioOptional.isPresent()){
                    Scenario scenario = scenarioOptional.get();
                    app.setUserId(scenario.getUser().getUserId());
                    App appNew = appRepository.save(app);
                    return RESCODE.SUCCESS.getJSONRES(appNew);
                }
            }return RESCODE.SCENARIO_NOT_EXIST.getJSONRES();
        }
        return object;
    }

    public JSONObject updateApp(App app, BindingResult br){
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {

        }
        return object;
    }
}
