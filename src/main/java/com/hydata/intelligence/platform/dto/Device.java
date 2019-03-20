package com.hydata.intelligence.platform.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

public class Device{
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator",strategy = "com.hydata.intelligence.platform.utils.IdGenerator",
            parameters = {})
	private Long id;
	private String device_sn;
    private String name;
    
    private Long product_id;

    private String iconUrl;
    
    private Integer status;//设备状态：1：在线/0：离线

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date create_time;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date modify_time;

    private Integer protocolId;
    
    

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
    
    

	public long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(long product_id) {
		this.product_id = product_id;
	}

	public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl == null ? null : iconUrl.trim();
    }

    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	

    public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public Integer getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Integer protocolId) {
        this.protocolId = protocolId;
    }
    
    

    public String getDevice_sn() {
		return device_sn;
	}

	public void setDevice_sn(String device_sn) {
		this.device_sn = device_sn;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", productId=").append(product_id);
        sb.append(", iconUrl=").append(iconUrl);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(create_time);
        sb.append(", modifyTime=").append(modify_time);
        sb.append(", protocolId=").append(protocolId);
        sb.append(", device_sn=").append(device_sn);
        sb.append("]");
        return sb.toString();
    }
}