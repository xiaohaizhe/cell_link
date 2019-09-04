package com.hydata.intelligence.platform.cell_link.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.App;
import com.hydata.intelligence.platform.cell_link.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AppController
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/3 11:51
 * @Version
 */
@RestController
@RequestMapping("api/app")
public class AppController {
    @Autowired
    private AppService appService;

    @PostMapping("add_app")
    public JSONObject addApp(@RequestBody @Validated App app, BindingResult br){
        return appService.addApp(app, br);
    }

    @PostMapping("update_app")
    public JSONObject updateApp(@RequestBody @Validated App app, BindingResult br){
        return appService.updateApp(app, br);
    }
}
