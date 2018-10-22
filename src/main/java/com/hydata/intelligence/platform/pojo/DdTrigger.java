package com.hydata.intelligence.platform.pojo;

import java.io.Serializable;

public class DdTrigger implements Serializable {
    private Integer id;

    private Integer ddId;

    private Integer triggerId;

    private String dmName;

    private Integer mode;

    private String modeMsg;

    private Integer productId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDdId() {
        return ddId;
    }

    public void setDdId(Integer ddId) {
        this.ddId = ddId;
    }

    public Integer getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(Integer triggerId) {
        this.triggerId = triggerId;
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}