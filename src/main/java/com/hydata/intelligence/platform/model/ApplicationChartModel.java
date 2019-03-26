package com.hydata.intelligence.platform.model;
/**
 * @author pyt
 * @createTime 2018年11月1日下午6:02:52
 */

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class ApplicationChartModel {
	private long id;
	@NotNull
    private int chartId;//图表类型id

    private Date createTime;//创建时间
    
    private Integer frequency;//数据获取频率(s)
	
	private int sum;//总共展示数据点个数
	@NotNull
    private List<ApplicationChartDsModel> applicationChartDatastreamList;//图表中包含的数据流列表
    
   
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setChartId(int chartId) {
		this.chartId = chartId;
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

	
	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public List<ApplicationChartDsModel> getApplicationChartDatastreamList() {
		return applicationChartDatastreamList;
	}

	public void setApplicationChartDatastreamList(List<ApplicationChartDsModel> applicationChartDatastreamList) {
		this.applicationChartDatastreamList = applicationChartDatastreamList;
	}
		
}

