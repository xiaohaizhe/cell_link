package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.Device;
import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import com.hydata.intelligence.platform.cell_link.service.CommandService;
import com.hydata.intelligence.platform.cell_link.service.DeviceService;
import com.hydata.intelligence.platform.cell_link.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    @Autowired
    private CommandService commandService;

    @PostMapping("add")
    public JSONObject add(@RequestBody @Validated Device device, BindingResult br) {
        return deviceService.add(device, br);
    }
    @PostMapping("update")
    public JSONObject update(@RequestBody @Validated Device device, BindingResult br) {
        return deviceService.update(device, br);
    }
    @DeleteMapping("delete")
    public JSONObject delete(Long deviceId){
        return deviceService.delete(deviceId);
    }

    @GetMapping("findById")
    public JSONObject findById(Long deviceId){
        return deviceService.findById(deviceId);
    }

    @GetMapping("findByDeviceName")
    public JSONObject findByDeviceName(Long userId,String deviceName,
                                       Integer page,Integer number,String  sorts,
                                       Long scenarioId,Long dgId,String start,String end,Integer status){
        return deviceService.findByDeviceName(userId,deviceName,page,number,sorts,scenarioId,dgId,start,end,status);
    }

    @GetMapping("export")
    public void export(Long userId, String deviceName, Long scenarioId, Long dgId, String start, String end, Integer status,
                             HttpServletRequest request, HttpServletResponse response){
        deviceService.export(userId,deviceName,scenarioId,dgId,start,end,status,request,response);
    }

    @GetMapping("/export_model")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        ExcelUtils.exportModel(request,response);
    }

    @PostMapping("import")
    public JSONObject importModel(MultipartFile file, Long dgId, HttpServletRequest request) {
        return deviceService.importFile(file, dgId, request);
    }

    @GetMapping("getOverview")
    public JSONObject getOverview(Long userId){
        return deviceService.getOverview(userId);
    }

    @GetMapping("getIncrement")
    public JSONObject getIncrement(Long userId,String start,String end){
        return deviceService.getIncrement(userId,start,end);
    }

    @RequestMapping("/sendcmd")
    public JSONObject sendCmd(long topic, String content, int type,long userid) {
        return commandService.send(topic, content, type,userid);
    }
    @RequestMapping(value= "/getStatus")
    public JSONObject getStatus(long ds_id) {
        return deviceService.checkStatus(ds_id);
    }
}
