package com.hydata.intelligence.platform.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
@Entity
public class DeviceTrigger{
	@Id
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator",strategy = "com.hydata.intelligence.platform.utils.IdGenerator",
			parameters = {})
	private long id;

	private long triggerId;

	private Long deviceId;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(long triggerId) {
		this.triggerId = triggerId;
	}


	public Long getDevice_id() {
		return deviceId;
	}

	public void setDevice_id(Long device_id) {
		this.deviceId = device_id;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", triggerId=").append(triggerId);
		sb.append(", deviceId=").append(deviceId);
		sb.append("]");
		return sb.toString();
	}
}