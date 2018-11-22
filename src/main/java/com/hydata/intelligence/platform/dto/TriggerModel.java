package com.hydata.intelligence.platform.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class TriggerModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;

    private String name;

    private Integer triggerTypeId;

    private Date createTime;

    private Date modifyTime;

    private String criticalValue;//触发阈值

    private Integer triggerMode;//触发方式：0：邮箱；1：url

	private String modeValue;//触发邮箱/url
    
    private String vertifyCode;//接受信息方式为邮箱时的验证码
    
    private Integer deviceId;//设备id
    
    private Integer datastreamId;//设备数据流id，非数据流模板

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getProductId() {
		return productId;
	}



	public void setProductId(Integer productId) {
		this.productId = productId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public Integer getTriggerTypeId() {
		return triggerTypeId;
	}



	public void setTriggerTypeId(Integer triggerTypeId) {
		this.triggerTypeId = triggerTypeId;
	}



	public Date getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}



	public Date getModifyTime() {
		return modifyTime;
	}



	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}



	public String getCriticalValue() {
		return criticalValue;
	}



	public void setCriticalValue(String criticalValue) {
		this.criticalValue = criticalValue;
	}



	public Integer getTriggerMode() {
		return triggerMode;
	}



	public void setTriggerMode(Integer triggerMode) {
		this.triggerMode = triggerMode;
	}
	public String getVertifyCode() {
		return vertifyCode;
	}



	public void setVertifyCode(String vertifyCode) {
		this.vertifyCode = vertifyCode;
	}



	public Integer getDeviceId() {
		return deviceId;
	}



	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}



	public Integer getDatastreamId() {
		return datastreamId;
	}



	public void setDatastreamId(Integer datastreamId) {
		this.datastreamId = datastreamId;
	}

	public String getModeValue() {
		return modeValue;
	}

	public void setModeValue(String modeValue) {
		this.modeValue = modeValue;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productId=").append(productId);
        sb.append(", name=").append(name);
        sb.append(", triggerTypeId=").append(triggerTypeId);
        sb.append(", createTime=").append(createTime);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", criticalValue=").append(criticalValue);
        sb.append(", triggerMode=").append(triggerMode);
        sb.append(", vertifyCode=").append(vertifyCode);
        sb.append(", deviceId=").append(deviceId);
		sb.append(", modeValue=").append(modeValue);
		sb.append(", datastreamId=").append(datastreamId);
        sb.append("]");
        return sb.toString();
    }
}