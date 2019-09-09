package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.service.OplogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName LogController
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/9 10:21
 * @Version
 */
@RestController
@RequestMapping("api/log")
public class LogController {
    @Autowired
    private OplogService oplogService;

    @GetMapping("opList")
    public JSONObject getOplogList(Long userId){
        return oplogService.getOplogList(userId);
    }

    @GetMapping("opPage")
    public JSONObject getOplogPage(Long userId,Integer page,Integer number,String sorts){
        return oplogService.getOplogPage(userId, page, number, sorts);
    }
}
