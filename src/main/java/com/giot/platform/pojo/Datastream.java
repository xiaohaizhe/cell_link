package com.giot.platform.pojo;

import java.io.Serializable;
import java.util.Date;

public class Datastream implements Serializable {
    private Integer id;

    private String name;

    private Integer deviceid;

    private Date createtime;

    private Integer unittypeid;

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

    public Integer getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Integer deviceid) {
        this.deviceid = deviceid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getUnittypeid() {
        return unittypeid;
    }

    public void setUnittypeid(Integer unittypeid) {
        this.unittypeid = unittypeid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", deviceid=").append(deviceid);
        sb.append(", createtime=").append(createtime);
        sb.append(", unittypeid=").append(unittypeid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}