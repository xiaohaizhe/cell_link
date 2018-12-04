package com.hydata.intelligence.platform.model;

/**
 * @author pyt
 * @createTime 2018年11月2日上午9:35:33
 */
public class ApplicationChartDsModel {
	private Integer id;
	
	private Integer dd_id;//deviceDatastream,设备数据流id
	
	private Integer chart_id;//图表id
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDd_id() {
		return dd_id;
	}
	public void setDd_id(Integer dd_id) {
		this.dd_id = dd_id;
	}	
	public Integer getChart_id() {
		return chart_id;
	}
	public void setChart_id(Integer chart_id) {
		this.chart_id = chart_id;
	}
	
}

