package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.service.DatastreamService;
import com.hydata.intelligence.platform.cell_link.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DatastreamController
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/5 15:04
 * @Version
 */
@RestController
@RequestMapping("api/datastream")
public class DatastreamController {
    @Autowired
    private DatastreamService datastreamService;

    @GetMapping("findByDevice")
    public JSONObject findByDevice(Long deviceId, String datastreamName, Integer page, Integer number, String sorts) {
        return datastreamService.findByDevice(deviceId, datastreamName, page, number, sorts);
    }

    @GetMapping("findByDatastream")
    public JSONObject findByDatastream(Long datastreamId, String start, String end) {
        return datastreamService.findByDatastream(datastreamId,start,end);
    }

    @GetMapping("findByDeviceId")
    public JSONObject findByDeviceId(Long deviceId){
        return datastreamService.findByDeviceId(deviceId);
    }

    @RequestMapping(value= "/getStatus")
    public JSONObject getStatus(long ds_id) {
        return datastreamService.checkStatus(ds_id);
    }

}
