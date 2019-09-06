package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.service.DatapointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DatapointController
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/5 16:50
 * @Version
 */
@RestController
@RequestMapping("datapoint")
public class DatapointController {
    @Autowired
    private DatapointService datapointService;

    @PostMapping("/add")
    public void add(@RequestBody JSONObject object) {
        Long deviceId = object.getLongValue("deviceId");
        JSONArray data = object.getJSONArray("data");
        datapointService.dealWithData(deviceId,data);
    }
}
