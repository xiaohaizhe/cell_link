package com.hydata.intelligence.platform.dto;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author pyt
 * @createTime 2019年1月15日下午4:51:30
 */
public class Data_history {
	@Id
	private Long id;
	private String name;
	private Long dd_id;
	private Double value;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date create_time;
	private int status; //0为正常, 1为<50%, 2为>150%




	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getDd_id() {
		return dd_id;
	}
	public void setDd_id(Long dd_id) {
		this.dd_id = dd_id;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Data_history [name=" + name + ", dd_id=" + dd_id + ", value=" + value + ", create_time=" + create_time+
				", status=" + status+ "]";
	} 
	

}

