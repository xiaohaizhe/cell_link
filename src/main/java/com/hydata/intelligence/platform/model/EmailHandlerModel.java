package com.hydata.intelligence.platform.model;

import java.util.Date;

public class EmailHandlerModel {
    private String deviceSn;
    private int criticalValue;
    private String email;
    private String triggerSymbol;
    private Date createTime;
    private String dmName;

    public String getDeviceSn() { return deviceSn;}
    public void setDeviceSn(String deviceSn){this.deviceSn = deviceSn;}
    public int getCriticalValue(){return criticalValue;}
    public void setCriticalValue(int criticalValue){this.criticalValue = criticalValue;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}
    public String getTriggerSymbol(){return triggerSymbol;}
    public void setTriggerSymbol(String symbol){this.triggerSymbol = symbol;}
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getDmName(){return dmName;}
    public void setDmName(String dmName){this.dmName = dmName;}
}
