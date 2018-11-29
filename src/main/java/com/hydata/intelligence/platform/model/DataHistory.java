package com.hydata.intelligence.platform.model;
/**
 * @author pyt
 * @createTime 2018年11月29日上午11:18:32
 */

import java.util.Date;

public class DataHistory {
	private Integer dd_id;
	private Date date;
	private double value;
	private String name;
	public Integer getDd_id() {
		return dd_id;
	}
	public void setDd_id(Integer dd_id) {
		this.dd_id = dd_id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

