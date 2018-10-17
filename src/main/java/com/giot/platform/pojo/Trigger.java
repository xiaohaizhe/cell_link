package com.giot.platform.pojo;

import java.io.Serializable;
import java.util.Date;

public class Trigger implements Serializable {
    private Integer id;

    private Integer productid;

    private String name;

    private Integer type;

    private Date createtime;

    private Date modifytime;

    private String value;

    private Integer mode;

    private String modemsg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productid=").append(productid);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", createtime=").append(createtime);
        sb.append(", modifytime=").append(modifytime);
        sb.append(", value=").append(value);
        sb.append(", mode=").append(mode);
        sb.append(", modemsg=").append(modemsg);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}