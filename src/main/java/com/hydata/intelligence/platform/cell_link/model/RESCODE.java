package com.hydata.intelligence.platform.cell_link.model;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.utils.Constants;

public enum RESCODE {
    SUCCESS(0, "成功"),
    FAILURE(1,"失败"),

    NAME_EXIST(1,"账号名已存在"),
    USER_NOT_EXIST(1,"用户不存在"),
    EMAIL_NOT_EXIST(2,"用户邮箱不存在"),
    NAME_OR_PASSWORD_WRONG(1,"用户名或密码错误"),
    PHONE_NOT_VERTIFY(2,"手机号未验证"),
    PWD_NOT_MODIFY(1,"初始密码未修改"),

    SCENARIO_NOT_EXIST(1,"场景不存在"),
    SCENARIO_EXIST(1,"场景已存在"),
    APP_NAME_EXIST_IN_SCENARIO(1,"该应用名已存在于场景下"),
    DEVICE_GROUP_NAME_EXIST_IN_SCENARIO(1,"该设备组名已存在于场景下"),

    DEVICE_GROUP_EXIST(1,"设备组已存在"),
    DEVICE_GROUP_NOT_EXIST(1,"设备组不存在"),
    DEVICE_NAME_EXIST_IN_DEVICE_GROUP(1,"该设备名已存在于设备组下"),


    APP_NOT_EXIST(1,"应用不存在"),
    APP_CHART_NOT_EXIST(1,"应用图表不存在"),
    CHART_TYPE_NOT_EXIST(1,"图表类型不存在"),
    CHART_NOT_EXIST(1,"图表不存在"),
    DATASTREAM_NOT_EXIST(1,"数据流不存在"),
    APP_NOT_SAME(1,"应用不一致"),
    SEQUENCE_NUMBER_WRONG(1,"图表序列号错误"),
    SEQUENCE_NUMBER_OVERFLOW(1,"图表序列号溢出"),


    DEVICESN_EXIST_IN_DEVICE_GROUP(1,"设备鉴权信息重复"),
    DEVICE_NOT_EXIST(1,"设备不存在"),

    NO_CHANGES(1,"无变化"),
    MQTT_DISCONNECTED(1,"与broker失去连接"),

    IO_ERROR(1,"文件读取失败"),
    FORMAT_ERROR(1,"格式转换错误"),
    PROTOCOL_NOT_MATCH(1,"协议不支持"),
    INSUFFICIENT_DATA(400,"数据量不足"),

    PARAM_ERROR(400,"参数错误"),
    PARAM_MISSING(2,"参数不完整"),
    TIME_PARSE_WRONG(1,"时间格式转换错误");

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

    public JSONObject getJSONRES(Object entity,Integer pages,Long count){
        JSONObject jsonres = getJSONRES();
        jsonres.put(Constants.RESPONSE_DATA_KEY, entity);
        jsonres.put(Constants.RESPONSE_SIZE_KEY, pages);
        jsonres.put(Constants.RESPONSE_REAL_SIZE_KEY, count);
        return jsonres;
    }
}
