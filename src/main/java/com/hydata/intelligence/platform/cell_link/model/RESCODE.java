package com.hydata.intelligence.platform.cell_link.model;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.utils.Constants;

public enum RESCODE {
    SUCCESS(0, "成功"),
    FAILURE(1,"失败"),
    USER_NOT_EXIST(1,"用户不存在"),
    NAME_OR_PASSWORD_WRONG(1,"用户名或密码错误"),
    NAME_EXIST(1,"账号名已存在"),
    PARAM_ERROR(400,"错误信息"),
    PHONE_NOT_VERTIFY(2,"手机号未验证");

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

    public JSONObject getJSONRES(Object entity,int pages,int count){
        JSONObject jsonres = getJSONRES();
        jsonres.put(Constants.RESPONSE_DATA_KEY, entity);
        jsonres.put(Constants.RESPONSE_SIZE_KEY, pages);
        jsonres.put(Constants.RESPONSE_REAL_SIZE_KEY, count);
        return jsonres;
    }
}
