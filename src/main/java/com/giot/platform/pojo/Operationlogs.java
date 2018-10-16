package com.giot.platform.pojo;

import java.io.Serializable;

public class Operationlogs implements Serializable {
    private Integer id;

    private Integer operationtypeid;

    private Integer userid;

    private String msg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOperationtypeid() {
        return operationtypeid;
    }

    public void setOperationtypeid(Integer operationtypeid) {
        this.operationtypeid = operationtypeid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", operationtypeid=").append(operationtypeid);
        sb.append(", userid=").append(userid);
        sb.append(", msg=").append(msg);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}