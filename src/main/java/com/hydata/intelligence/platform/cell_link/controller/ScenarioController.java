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
    public JSONObject delete(Long scenario_id) {
        return scenarioService.delete(scenario_id);
    }

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public JSONObject findById(Long scenario_id) {
        return scenarioService.findById(scenario_id);
    }

    @RequestMapping(value = "/findListByUser", method = RequestMethod.GET)
    public JSONObject findListByUser(Long user_id) {
        return scenarioService.findListByUser(user_id);
    }

    @GetMapping("findPageByUser")
    public JSONObject findPageByUser(Long user_id,Integer page,Integer number,String sorts,String scenario_name){
        return scenarioService.findPageByUser(user_id,page,number,sorts,scenario_name);
    }
}
