package com.hydata.intelligence.platform.model;
/**
 * @author pyt
 * @createTime 2018年11月29日上午11:18:32
 */

import java.util.Date;

public class DataHistory {
	private Long id;
	private long dd_id;
	private Date date;
	private double value;
	private String name;
		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getDd_id() {
		return dd_id;
	}
	public void setDd_id(long dd_id) {
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

