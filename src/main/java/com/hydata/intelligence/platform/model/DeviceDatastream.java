package com.hydata.intelligence.platform.model;

import java.util.Date;

/**
 * @author pyt
 * @createTime 2018年11月27日下午5:33:09
 */
public class DeviceDatastream {
	private Long dd_id;
	private String name;
	private double value;
	private Date date;//最新的更新时间
	private String device_name;
	private Long device_id;

	public Long getDevice_id() {
		return device_id;
	}

	public void setDevice_id(Long device_id) {
		this.device_id = device_id;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public Long getDd_id() {
		return dd_id;
	}
	public void setDd_id(Long dd_id) {
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

