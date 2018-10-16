package com.giot.platform.pojo;

import java.io.Serializable;

public class Application implements Serializable {
    private Integer id;

    private String name;

    private String logourl;

    private String description;

    private String createtime;

    private Integer productid;

    private Integer applicationtype;

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

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl == null ? null : logourl.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public Integer getApplicationtype() {
        return applicationtype;
    }

    public void setApplicationtype(Integer applicationtype) {
        this.applicationtype = applicationtype;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", logourl=").append(logourl);
        sb.append(", description=").append(description);
        sb.append(", createtime=").append(createtime);
        sb.append(", productid=").append(productid);
        sb.append(", applicationtype=").append(applicationtype);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}