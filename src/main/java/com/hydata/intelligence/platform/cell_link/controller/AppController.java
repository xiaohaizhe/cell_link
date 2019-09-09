package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.App;
import com.hydata.intelligence.platform.cell_link.entity.AppChart;
import com.hydata.intelligence.platform.cell_link.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName AppController
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/3 11:51
 * @Version
 */
@RestController
@RequestMapping("api/app")
public class AppController {
    @Autowired
    private AppService appService;

    @PostMapping("add_app")
    public JSONObject addApp(@RequestBody @Validated App app, BindingResult br) {
        return appService.addApp(app, br);
    }

    @PostMapping("update_app")
    public JSONObject updateApp(@RequestBody @Validated App app, BindingResult br) {
        return appService.updateApp(app, br);
    }

    @DeleteMapping("delete_app")
    public JSONObject deleteApp(Long appId) {
        return appService.deleteApp(appId);
    }

    @GetMapping("findById")
    public JSONObject findById(Long appId){
        return appService.findById(appId);
    }

    @GetMapping("getChart")
    public JSONObject getChart(){
        return appService.getChart();
    }

    @GetMapping("findByScenario")
    public JSONObject findByScenario(Long scenarioId,String appName,Integer page,Integer number,String sorts){
        return appService.findByScenario(scenarioId,appName,page,number,sorts);
    }

/*    @PostMapping("add_chart")
    public JSONObject addChart(@RequestBody @Validated AppChart appChart, BindingResult br) {
        return appService.addChart(appChart, br);
    }

    @PostMapping("modify_chart")
    public JSONObject modifyChart(){

    }

    @DeleteMapping("delete_chart")
    public JSONObject deleteChart(Long acId){
        return appService.deleteChart(acId);
    }*/
    /*@DeleteMapping("delete_chart")
    public JSONObject deleteChart(Long acId){
        return appService.deleteChart(acId);
    }*/
}
