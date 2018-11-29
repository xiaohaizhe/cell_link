package com.hydata.intelligence.platform.model;

import java.util.Date;

/**
 * @author pyt
 * @createTime 2018年11月27日下午5:33:09
 */
public class DeviceDatastream {
	private Integer dd_id;
	private String name;
	private double value;
	private Date date;
	public Integer getDd_id() {
		return dd_id;
	}
	public void setDd_id(Integer dd_id) {
		this.dd_id = dd_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}

