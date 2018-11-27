package com.hydata.intelligence.platform.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author pyt
 * @createTime 2018年11月19日下午3:06:03
 */
@Entity
public class ApplicationAnalysisDatastream {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	private Integer aaId;	//智能分析应用
	private Integer ddId;	//设备数据流
	private Integer type;	//数据流类型
	private Date start;		//数据流开始时间
	private Date end;		//数据流结束时间
	private double frequency;	//频率
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAaId() {
		return aaId;
	}
	public void setAaId(Integer aaId) {
		this.aaId = aaId;
	}
	public Integer getDdId() {
		return ddId;
	}
	public void setDdId(Integer ddId) {
		this.ddId = ddId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public double getFrequency() {
		return frequency;
	}
	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", aaId=").append(aaId);
        sb.append(", ddId=").append(ddId);
        sb.append(", type=").append(type);
        sb.append(", start=").append(start);
        sb.append(", end=").append(end);
        sb.append(", frequency=").append(frequency);
        sb.append("]");
        return sb.toString();
    }
}

