package com.hydata.intelligence.platform.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
@Entity
public class TriggerLogs {
	@Id
	@GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator",strategy = "com.hydata.intelligence.platform.utils.IdGenerator",
                        parameters = {})
    private long id;

    private long triggerId;

    private String msg;

    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(long triggerId) {
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