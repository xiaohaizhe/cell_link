package com.hydata.intelligence.platform.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
@Entity
public class DeviceDatastream{
	@Id
	@GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator",strategy = "com.hydata.intelligence.platform.utils.IdGenerator",
                        parameters = {})
    private long id;

    private long device_id;

    private String dm_name;
    
	

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public long getDevice_id() {
		return device_id;
	}



	public void setDevice_id(long device_id) {
		this.device_id = device_id;
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
        sb.append(", device_id=").append(device_id);
        sb.append(", dm_name=").append(dm_name);
        sb.append("]");
        return sb.toString();
    }
}