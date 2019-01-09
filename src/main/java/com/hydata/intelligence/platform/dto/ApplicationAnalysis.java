package com.hydata.intelligence.platform.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
@Entity
public class ApplicationAnalysis{
	@Id
	@GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator",strategy = "com.example.spring_data_jpa_demo.utils.IdGenerator",
                        parameters = {})
    private long id;

    private long applicationId;

    private String applicationName;

    private String name;
    
    private int aaType;//智能分析应用类型

    private Date createTime;    

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName == null ? null : applicationName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
    

    public int getAaType() {
		return aaType;
	}

	public void setAaType(int aaType) {
		this.aaType = aaType;
	}

	public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", applicationId=").append(applicationId);
        sb.append(", applicationName=").append(applicationName);
        sb.append(", name=").append(name);
        sb.append(", aaType=").append(aaType);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}