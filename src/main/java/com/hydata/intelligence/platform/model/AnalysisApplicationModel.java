package com.hydata.intelligence.platform.model;

import java.util.Date;
import java.util.List;

import com.hydata.intelligence.platform.dto.ApplicationAnalysisDatastream;

/**
 * @author pyt
 * @createTime 2018年11月19日上午10:53:40
 */
public class AnalysisApplicationModel {
	private long id;
	private long productId;
	private String name;
	private Date createTime;    
    private Integer applicationType;//智能分析应用类型
    private List<ApplicationAnalysisDatastream> analysisDatastreams;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(Integer applicationType) {
		this.applicationType = applicationType;
	}
	public List<ApplicationAnalysisDatastream> getAnalysisDatastreams() {
		return analysisDatastreams;
	}
	public void setAnalysisDatastreams(List<ApplicationAnalysisDatastream> analysisDatastreams) {
		this.analysisDatastreams = analysisDatastreams;
	}
	@Override
	public String toString() {
		return "AnalysisApplicationModel [id=" + id + ", productId=" + productId + ", name=" + name + ", createTime="
				+ createTime + ", applicationType=" + applicationType + ", analysisDatastreams=" + analysisDatastreams
				+ "]";
	}
    
}

