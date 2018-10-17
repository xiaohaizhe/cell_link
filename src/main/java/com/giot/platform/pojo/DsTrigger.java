package com.giot.platform.pojo;

import java.io.Serializable;

public class DsTrigger implements Serializable {
    private Integer id;

    private Integer dsid;

    private Integer triggerid;

    private String dsName;

    private Integer mode;

    private String modemsg;

    private Integer productid;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDsid() {
        return dsid;
    }

    public void setDsid(Integer dsid) {
        this.dsid = dsid;
    }

    public Integer getTriggerid() {
        return triggerid;
    }

    public void setTriggerid(Integer triggerid) {
        this.triggerid = triggerid;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName == null ? null : dsName.trim();
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getModemsg() {
        return modemsg;
    }

    public void setModemsg(String modemsg) {
        this.modemsg = modemsg == null ? null : modemsg.trim();
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", dsid=").append(dsid);
        sb.append(", triggerid=").append(triggerid);
        sb.append(", dsName=").append(dsName);
        sb.append(", mode=").append(mode);
        sb.append(", modemsg=").append(modemsg);
        sb.append(", productid=").append(productid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}