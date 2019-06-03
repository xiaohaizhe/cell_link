package com.hydata.intelligence.platform.model;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.utils.Constants;

/**
 * @author pyt
 * @createTime 2018年10月24日下午3:22:04
 */
public enum RESCODE {
	SUCCESS(0, "成功"),
	FAILURE(1,"失败"),
	MODIFY_PWD_SUCCESS(0,"密码修改成功，请重新登陆"),
	MODIFY_PHONE_SUCCESS(0,"手机号修改成功"),
	PHONE_NO_CHANGE(1,"手机号未改变"),
	USER_ALREADY_IN(1,"用户已登陆"),
	USER_ALREADY_OUT(1,"用户未登陆"),
	ADMIN_ALREADYIN(1,"管理员已登录"),
	ADMIN_NAME_NOT_EXIST(2,"管理员账号名不存在"),
	ADMIN_ALREADYOUT(3,"管理员已登出"),
	VERTIFY_SMS_SUCCESS(0,"短信验证成功"),
	VERTIFY_SMS_AND_MODIFY_PHONE_SUCCESS(0,"短信验证成功,手机号已修改"),
	SEND_SMS_FAIL(1,"短信验证码发送失败"),
	VERTIFY_SMS_FAIL(1,"短信验证失败"),
	VERTIFY_SMS_TIMEOUT(4,"短信验证码超时"),
	VERTIFY_SMS_NULL(3,"短信验证码未发送或手机号无效"),
	ID_NOT_EXIST(2,"id不存在"),
	USER_ID_NOT_EXIST(2,"用户id不存在"),
	USER_IS_NOT_VALID(3,"用户无效"),
	PRODUCT_ID_NOT_EXIST(2,"产品id不存在"),
	PRODUCT_ID_CAN_NOT_CHANGE(3,"产品id不可修改"),
	APP_ID_NOT_EXIST(2,"应用id不存在"),
	NAME_EXIST(1,"账号名已存在"),
	PRODUCT_NAME_EXIST(1,"该产品名在用户名下已存在"),
	PRODUCT_NONE(1,"该用户未创建产品"),
	AUTH_INFO_EXIST(1,"鉴权信息重复"),
	DEVICE_SN_NOT_EXIST(1,"设备编码不存在"),
	API_KEY_ERROR(1,"api_key错误"),
	AUTH_INFO_NOT_EXIST(1,"鉴权信息不存在"),
	NAME_NOT_EXIST(2,"账号名不存在"),
	PRODUCT_NAME_NOT_EXIST(2,"该产品名在用户名下不存在"),
	PHONE_NOT_VERTIFY(4,"手机号未验证"),
	PARAM_MISSING(3,"参数不完整"),
	DSM_REPEAT(4,"数据流模板重复"),
	WRONG_PWD(5,"密码错误"),
	EMAIL_SEND_SUCCESS(0,"邮箱验证码发送成功"),
	EMAIL_WRONG_FORMAT(1,"邮箱格式错误"),
	EMAIL_EXCEPTION(2,"邮件发送发生异常"),
	EMAIL_VERTIFY_SUCCESS(0,"邮箱验证成功"),
	EMAIL_VERTIFY_FAILURE(1,"邮箱验证失败"),
	TRIGGER_ADD_FAILURE(1,"触发器添加失败"),
	TRIGGER_ID_NOT_EXIST(1,"触发器id不存在"),
	TRIGGER_TYPE_ID_NOT_EXIST(1,"触发器类型id不存在"),
	TRIGGER_DEVICE_ADD_FAILURE(2,"设备触发器关系添加失败"),
	TRIGGER_DEVICE_ALREADY_IN_RELATION(1,"设备触发器已关联"),
	APP_CHART_ID_NOT_EXIST(1,"图表应用id不存在"),
	APP_CHART_TYPE(0,"图表应用"),
	APP_ANALYSIS_TYPE(1,"智能分析应用"),
	DS_OUTPUT(0,"输出数据流"),
	DS_INPUT(1,"输入数据流"),
	CORRELATION_ANALYSE(0,"相关性分析"),
	LINEAR_REGRESSION_ANALYSE(1,"线性回归分析"),
	APP_NAME_EXIST(1,"应用名已存在"),
	DEVICE_DATASTREAM_NOT_EXIST(2,"设备下不存在该数据流"),
	DEVICE_ADD_MQTT_ERROR(1,"MQTT代理添加出错"),
	NO_CHANGES(1,"无变化"),
	PARAM_ERROR(400,"错误信息"),
	PARAM_NULL(500,"无参数"),
	TIME_PARSE_ERROR(1,"时间转换错误"),
	FORMAT_ERROR(1,"格式转换错误"),
	IO_ERROR(1,"文件读取失败"),
	INOUT_STREAM_IS_NULL(1,"输入/输出数据流未选择"),
	DEVICE_ID_NOT_EXIST(1,"设备ID不存在"),
	INSUFFICIENT_DATA(1,"数据不足"),
	TIME_SELECT_WRONG(1,"时间选择错误");
	private int code;
	private String msg;
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	RESCODE(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	/**
	 * 最新的返回json
	 */
	public JSONObject getJSONRES(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(Constants.RESPONSE_CODE_KEY, this.code);
		jsonObject.put(Constants.RESPONSE_MSG_KEY, this.msg);
		return jsonObject;
	}
	
	public JSONObject getJSONRES(String msg){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(Constants.RESPONSE_CODE_KEY, this.code);
		jsonObject.put(Constants.RESPONSE_MSG_KEY, msg);
		return jsonObject;
	}
	
	public JSONObject getJSONRES(Object entity){
		JSONObject jsonres = getJSONRES();		
		jsonres.put(Constants.RESPONSE_DATA_KEY, entity);
		return jsonres;
	}
	
	public JSONObject getJSONRES(Object entity,int pages,long count){
		JSONObject jsonres = getJSONRES();
		jsonres.put(Constants.RESPONSE_DATA_KEY, entity);
		jsonres.put(Constants.RESPONSE_SIZE_KEY, pages);
		jsonres.put(Constants.RESPONSE_REAL_SIZE_KEY, count);
		return jsonres;
	}
}

