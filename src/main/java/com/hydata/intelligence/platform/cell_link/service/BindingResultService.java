package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @ClassName BindingResultService
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/21 15:02
 * @Version
 */
@Component
public class BindingResultService {
    public static JSONObject dealWithBindingResult(BindingResult br){
        if (br.hasErrors()) {
            List<FieldError> errors = br.getFieldErrors();
            JSONArray array = new JSONArray();
            for (FieldError error : errors) {
                JSONObject object = new JSONObject();
                object.put(error.getField(),error.getDefaultMessage());
                array.add(object);
            }
            return RESCODE.PARAM_ERROR.getJSONRES(array);
        }
        return RESCODE.SUCCESS.getJSONRES();
    }
}
