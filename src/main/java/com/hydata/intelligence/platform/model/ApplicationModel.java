package com.hydata.intelligence.platform.model;
import java.util.Date;
import java.util.List;
/**
 * @author pyt
 * @createTime 2018年11月1日下午5:56:15
 */
public class ApplicationModel {
	private long id;//应用id
	private long productId;//产品id
	private String name;//应用名称
	private Date createTime;   //创建时间 
    private Integer applicationType;//应用类型：0-图表应用；1-智能分析应用
    private List<ApplicationChartModel> applicationChartList;//应用中的图表列表
	
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
	public List<ApplicationChartModel> getApplicationChartList() {
		return applicationChartList;
	}
	public void setApplicationChartList(List<ApplicationChartModel> applicationChartList) {
		this.applicationChartList = applicationChartList;
	}
}

