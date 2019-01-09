package com.hydata.intelligence.platform.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
@Entity
public class DdTrigger{
	@Id
	@GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator",strategy = "com.hydata.intelligence.platform.utils.IdGenerator",
                        parameters = {})
    private long id;

    private long ddId;

    private long triggerId;

    private String dmName;

    private Integer mode;

    private String modeMsg;

    private long productId;
    

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDdId() {
		return ddId;
	}

	public void setDdId(long ddId) {
		this.ddId = ddId;
	}

	public long getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(long triggerId) {
		this.triggerId = triggerId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getDmName() {
        return dmName;
    }

    public void setDmName(String dmName) {
        this.dmName = dmName == null ? null : dmName.trim();
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getModeMsg() {
        return modeMsg;
    }

    public void setModeMsg(String modeMsg) {
        this.modeMsg = modeMsg == null ? null : modeMsg.trim();
    }

    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ddId=").append(ddId);
        sb.append(", triggerId=").append(triggerId);
        sb.append(", dmName=").append(dmName);
        sb.append(", mode=").append(mode);
        sb.append(", modeMsg=").append(modeMsg);
        sb.append(", productId=").append(productId);
        sb.append("]");
        return sb.toString();
    }
}