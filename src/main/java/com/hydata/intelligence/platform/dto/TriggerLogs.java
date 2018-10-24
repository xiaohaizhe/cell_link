package com.hydata.intelligence.platform.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class TriggerLogs {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer triggerId;

    private String msg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(Integer triggerId) {
        this.triggerId = triggerId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", triggerId=").append(triggerId);
        sb.append(", msg=").append(msg);
        sb.append("]");
        return sb.toString();
    }
}