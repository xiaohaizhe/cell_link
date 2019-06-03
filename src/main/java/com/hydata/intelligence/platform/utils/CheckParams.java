package com.hydata.intelligence.platform.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.model.RESCODE;

/**
 * @author pyt
 * @createTime 2019年1月25日上午10:56:34
 */
@Component
public class CheckParams {
	public static JSONObject checkParams(JSONObject params) {
		 List<String> param_names = new ArrayList<>();
		 Date start = new Date();
		 Date end = new Date();
		 Boolean flag = false;
		 for (Entry<String, Object> entry : params.entrySet()) {
			 if(entry.getValue()==null||entry.getValue()=="") {
				 param_names.add(entry.getKey());
			 }
			 if (entry.getKey().equals("start")){
				 flag = true;
				 start = (Date) entry.getValue();
			 }
			 if (entry.getKey().equals("end")){
				 flag = true;
				 end = (Date) entry.getValue();
			 }
		 }
		if (flag && start.getTime()>end.getTime()){
			return RESCODE.TIME_SELECT_WRONG.getJSONRES();
		}
		return param_names.size()==0? RESCODE.SUCCESS.getJSONRES():
				RESCODE.PARAM_MISSING.getJSONRES(param_names);
	}
}

