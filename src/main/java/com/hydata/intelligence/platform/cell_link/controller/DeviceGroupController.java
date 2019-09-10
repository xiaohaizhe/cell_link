package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import com.hydata.intelligence.platform.cell_link.service.DeviceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName DeviceGroupController
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/28 14:45
 * @Version
 */
@RestController
@RequestMapping("api/dg")
public class DeviceGroupController {
    @Autowired
    private DeviceGroupService deviceGroupService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JSONObject add(@RequestBody @Validated DeviceGroup deviceGroup, BindingResult br) {
        return deviceGroupService.add(deviceGroup, br);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JSONObject update(@RequestBody @Validated DeviceGroup deviceGroup, BindingResult br) {
        return deviceGroupService.update(deviceGroup, br);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public JSONObject delete(Long dgId) {
        return deviceGroupService.delete(dgId);
    }

    @GetMapping("findById")
    public JSONObject findById(Long dgId) {
        return deviceGroupService.findById(dgId);
    }

    @GetMapping("findListByScenario")
    public JSONObject findListByScenario(Long scenarioId) {
        return deviceGroupService.findListByScenario(scenarioId);
    }

    @GetMapping("findByScenario")
    public JSONObject findByScenario(Long scenarioId, Integer page, Integer number, String sorts, String deviceGroupName) {
        return deviceGroupService.findByScenario(scenarioId, page, number, sorts, deviceGroupName);
    }

}
