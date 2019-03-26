package com.hydata.intelligence.platform.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author pyt
 * @createTime 2018年10月25日下午4:28:48
 */
public class DataStreamModel {
	private long id;
	@NotNull
	private long product_id;
	@NotNull
	@NotBlank
	private String name;
	private Date createTime;
	@NotNull
	@NotBlank
	private String unit_name;
	@NotNull
	@NotBlank
	private String unit_symbol;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getProduct_id() {
		return product_id;
	}
	public void setProduct_id(long product_id) {
		this.product_id = product_id;
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
	public String getUnit_name() {
		return unit_name;
	}
	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}
	public String getUnit_symbol() {
		return unit_symbol;
	}
	public void setUnit_symbol(String unit_symbol) {
		this.unit_symbol = unit_symbol;
	}
}

