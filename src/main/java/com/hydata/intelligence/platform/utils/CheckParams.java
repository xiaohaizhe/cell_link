package com.hydata.intelligence.platform.utils;

import java.util.ArrayList;
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
		 for (Entry<String, Object> entry : params.entrySet()) {
			 if(entry.getValue()==null||entry.getValue()=="") {
				 param_names.add(entry.getKey());
			 }
		 }		 
		 return param_names.size()==0? RESCODE.SUCCESS.getJSONRES():
			 						RESCODE.PARAM_MISSING.getJSONRES(param_names);
	}

}

