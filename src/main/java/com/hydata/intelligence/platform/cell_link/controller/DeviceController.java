package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.Device;
import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import com.hydata.intelligence.platform.cell_link.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName DeviceController
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/30 10:22
 * @Version
 */
@RestController
@RequestMapping("api/device")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;
    @PostMapping("add")
    public JSONObject add(@RequestBody @Validated Device device, BindingResult br) {
        return deviceService.add(device, br);
    }
    @PostMapping("update")
    public JSONObject update(@RequestBody @Validated Device device, BindingResult br) {
        return deviceService.update(device, br);
    }
    @DeleteMapping("delete")
    public JSONObject delete(Long device_id){
        return deviceService.delete(device_id);
    }

    @GetMapping("findById")
    public JSONObject findById(Long device_id){
        return deviceService.findById(device_id);
    }
}
