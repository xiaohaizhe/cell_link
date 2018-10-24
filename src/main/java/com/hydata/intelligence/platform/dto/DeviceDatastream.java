package com.hydata.intelligence.platform.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class DeviceDatastream{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer deviceId;

    private String dmId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDmId() {
        return dmId;
    }

    public void setDmId(String dmId) {
        this.dmId = dmId == null ? null : dmId.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", dmId=").append(dmId);
        sb.append("]");
        return sb.toString();
    }
}