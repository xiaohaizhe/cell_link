package com.hydata.intelligence.platform.model;
/**
 * @author pyt
 * @createTime 2018年11月1日下午6:02:52
 */

import java.util.Date;
import java.util.List;

public class ApplicationChartModel {
	private Integer id;

    private Integer chartId;//图表类型id

    private Date createTime;//创建时间
    
    private List<ApplicationChartDsModel> applicationChartDatastreamList;//图表中包含的数据流列表
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getChartId() {
		return chartId;
	}

	public void setChartId(Integer chartId) {
		this.chartId = chartId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<ApplicationChartDsModel> getApplicationChartDatastreamList() {
		return applicationChartDatastreamList;
	}

	public void setApplicationChartDatastreamList(List<ApplicationChartDsModel> applicationChartDatastreamList) {
		this.applicationChartDatastreamList = applicationChartDatastreamList;
	}
		
}

