package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.service.DatapointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value="/{device_id}/resolve_data",method = RequestMethod.POST)
    public JSONObject resolveDeviceData(@PathVariable Long device_id, @RequestBody JSONObject object) {
        return datapointService.resolveDatapoint(device_id,object);
    }

}
