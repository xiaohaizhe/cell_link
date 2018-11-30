package com.hydata.intelligence.platform.model;

/**
 * @author pyt
 * @createTime 2018年11月2日上午9:35:33
 */
public class ApplicationChartDsModel {
	private Integer id;
	
	private Integer dd_id;//deviceDatastream,设备数据流id
	
	private Integer chart_id;//图表id
	
	private double frequency;//数据获取频率
	
	private int sum;//总共展示数据点个数
	
	
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
	public double getFrequency() {
		return frequency;
	}
	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	
}

