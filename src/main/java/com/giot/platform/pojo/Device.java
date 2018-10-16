package com.giot.platform.pojo;

import java.io.Serializable;
import java.util.Date;

public class Device implements Serializable {
    private Integer id;

    private String name;

    private Integer productid;

    private String iconurl;

    private Date createtime;

    private Date modifytime;

    private Integer protocolid;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl == null ? null : iconurl.trim();
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

    public Integer getProtocolid() {
        return protocolid;
    }

    public void setProtocolid(Integer protocolid) {
        this.protocolid = protocolid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", productid=").append(productid);
        sb.append(", iconurl=").append(iconurl);
        sb.append(", createtime=").append(createtime);
        sb.append(", modifytime=").append(modifytime);
        sb.append(", protocolid=").append(protocolid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}