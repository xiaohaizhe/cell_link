package com.hydata.intelligence.platform.model;

import java.util.HashMap;
import java.util.Map;

import com.hydata.intelligence.platform.utils.Constants;

/**
 * @author pyt
 * @createTime 2018年10月24日下午3:22:04
 */
public enum RESCODE {
	SUCCESS(0, "成功"),
	FAILURE(1,"失败"),
	ID_NOT_EXIST(2,"id不存在"),
	PARAM_MISSING(3,"参数不完整"),
	DSM_REPEAT(4,"数据流模板重复");
	private int code;
	private String msg;
	private RESCODE(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	/**
	 * 最新的返回json
	 */
	public Map<String,Object> getJSONRES(){
		Map<String,Object> map = new HashMap<>();
		map.put(Constants.RESPONSE_CODE_KEY, this.code);
		map.put(Constants.RESPONSE_MSG_KEY, this.msg);
		return map;
	}
	
	public Map<String,Object> getJSONRES(Object entity){
		Map<String, Object> jsonres = getJSONRES();
		jsonres.put(Constants.RESPONSE_DATA_KEY, entity);
		return jsonres;
	}
	
	public Map<String,Object> getJSONRES(Object entity,int pages,long count){
		Map<String, Object> jsonres = getJSONRES();
		jsonres.put(Constants.RESPONSE_DATA_KEY, entity);
		jsonres.put(Constants.RESPONSE_SIZE_KEY, pages);
		jsonres.put(Constants.RESPONSE_REAL_SIZE_KEY, count);
		return jsonres;
	}
}

