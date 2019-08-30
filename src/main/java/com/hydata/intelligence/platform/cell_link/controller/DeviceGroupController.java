package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import com.hydata.intelligence.platform.cell_link.entity.Scenario;
import com.hydata.intelligence.platform.cell_link.service.DeviceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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


}
