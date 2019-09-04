package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.Scenario;
import com.hydata.intelligence.platform.cell_link.service.ScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ScenarioController
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/27 16:15
 * @Version
 */
@RestController
@RequestMapping("api/scenario")
public class ScenarioController {
    @Autowired
    private ScenarioService scenarioService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JSONObject add(@RequestBody @Validated Scenario scenario, BindingResult br) {
        return scenarioService.add(scenario, br);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JSONObject update(@RequestBody @Validated Scenario scenario, BindingResult br) {
        return scenarioService.update(scenario, br);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public JSONObject delete(Long scenarioId) {
        return scenarioService.delete(scenarioId);
    }

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public JSONObject findById(Long scenarioId) {
        return scenarioService.findById(scenarioId);
    }

    @RequestMapping(value = "/findListByUser", method = RequestMethod.GET)
    public JSONObject findListByUser(Long userId) {
        return scenarioService.findListByUser(userId);
    }

    @GetMapping("findPageByUser")
    public JSONObject findPageByUser(Long userId,Integer page,Integer number,String sorts,String scenarioName){
        return scenarioService.findPageByUser(userId,page,number,sorts,scenarioName);
    }
}
