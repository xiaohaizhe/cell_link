package com.hydata.intelligence.platform.model;
/**
 * @author pyt
 * @createTime 2018年11月1日下午5:56:15
 */

import java.util.Date;
import java.util.List;

import com.hydata.intelligence.platform.dto.ApplicationChart;

public class ApplicationModel {
	private Integer id;
	private Integer productId;
	private String name;
	private Date createTime;    
    private Integer applicationType;
    private List<ApplicationChartModel> applicationChartList;
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
	public List<ApplicationChartModel> getApplicationChartList() {
		return applicationChartList;
	}
	public void setApplicationChartList(List<ApplicationChartModel> applicationChartList) {
		this.applicationChartList = applicationChartList;
	}
}

