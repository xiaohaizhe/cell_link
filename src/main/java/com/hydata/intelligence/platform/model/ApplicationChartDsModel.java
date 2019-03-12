package com.hydata.intelligence.platform.model;

/**
 * @author pyt
 * @createTime 2018年11月2日上午9:35:33
 */
public class ApplicationChartDsModel {
	private long id;

	private Long device_id;

	private long dd_id;//deviceDatastream,设备数据流id
	
	private long chart_id;//图表id

	public Long getDevice_id() {
		return device_id;
	}

	public void setDevice_id(Long device_id) {
		this.device_id = device_id;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getDd_id() {
		return dd_id;
	}
	public void setDd_id(long dd_id) {
		this.dd_id = dd_id;
	}
	public long getChart_id() {
		return chart_id;
	}
	public void setChart_id(long chart_id) {
		this.chart_id = chart_id;
	}
	
	
}

