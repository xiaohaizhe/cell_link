package com.giot.platform.pojo;

import java.io.Serializable;
import java.util.Date;

public class ApplicationChart implements Serializable {
    private Integer id;

    private Integer applicationid;

    private String applicationName;

    private Integer chartid;

    private Integer dsid;

    private String name;

    private Date createtime;

    private String logourl;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplicationid() {
        return applicationid;
    }

    public void setApplicationid(Integer applicationid) {
        this.applicationid = applicationid;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName == null ? null : applicationName.trim();
    }

    public Integer getChartid() {
        return chartid;
    }

    public void setChartid(Integer chartid) {
        this.chartid = chartid;
    }

    public Integer getDsid() {
        return dsid;
    }

    public void setDsid(Integer dsid) {
        this.dsid = dsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl == null ? null : logourl.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", applicationid=").append(applicationid);
        sb.append(", applicationName=").append(applicationName);
        sb.append(", chartid=").append(chartid);
        sb.append(", dsid=").append(dsid);
        sb.append(", name=").append(name);
        sb.append(", createtime=").append(createtime);
        sb.append(", logourl=").append(logourl);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}