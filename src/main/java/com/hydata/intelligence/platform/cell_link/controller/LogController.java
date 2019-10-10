package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.service.CommandService;
import com.hydata.intelligence.platform.cell_link.service.OplogService;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
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
    @Autowired
    private CommandService commandService;

    @GetMapping("opList")
    public JSONObject getOplogList(Long userId) {
        return oplogService.getOplogList(userId);
    }

    @GetMapping("opPage")
    public JSONObject getOplogPage(Long userId, Integer page, Integer number, String sorts) {
        return oplogService.getOplogPage(userId, page, number, sorts);
    }

    @GetMapping("findByCmd")
    public JSONObject findByCmd(Long userId,String cmd,
                                       Integer page,Integer number,String  sorts,
                                       Long scenarioId,Long dgId,String start,String end,Integer status){
        return commandService.findByCmd(userId,cmd,page,number,sorts,scenarioId,dgId,start,end,status);
    }


}
