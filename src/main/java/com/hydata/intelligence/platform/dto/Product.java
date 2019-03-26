package com.hydata.intelligence.platform.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;
@Entity
public class Product {
	@Id
	@GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator",strategy = "com.hydata.intelligence.platform.utils.IdGenerator",
                        parameters = {})
    private long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private Integer protocolId;

    private Integer productTypeId;//字段闲置，不使用
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private long userId;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @NotNull
    private Double latitude;
    @NotNull
    private Double lontitude;
    @NotNull
    private int cityCode;
    
    private String registrationCode;
    
    

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Integer protocolId) {
        this.protocolId = protocolId;
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLontitude() {
		return lontitude;
	}

	public void setLontitude(Double lontitude) {
		this.lontitude = lontitude;
	}

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
    
    

    public String getRegistrationCode() {
		return registrationCode;
	}

	public void setRegistrationCode(String registrationCode) {
		this.registrationCode = registrationCode;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", protocolId=" + protocolId + ", productTypeId="
				+ productTypeId + ", description=" + description + ", userId=" + userId + ", createTime=" + createTime
				+ ", latitude=" + latitude + ", lontitude=" + lontitude + ", cityCode=" + cityCode
				+ ", registrationCode=" + registrationCode + "]";
	}
	
	

	
}