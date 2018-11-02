package com.hydata.intelligence.platform.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class DeviceDatastream{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer deviceId;

    private String dm_name;

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

    

    public String getDm_name() {
		return dm_name;
	}

	public void setDm_name(String dm_name) {
		this.dm_name = dm_name;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", dm_name=").append(dm_name);
        sb.append("]");
        return sb.toString();
    }
}