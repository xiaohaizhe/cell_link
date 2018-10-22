package com.hydata.intelligence.platform.pojo;

import java.io.Serializable;
import java.util.Date;

public class Trigger implements Serializable {
    private Integer id;

    private Integer productId;

    private String name;

    private Integer triggerTypeId;

    private Date createTime;

    private Date modifyTime;

    private String threshold;

    private Integer mode;

    private String modeMsg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getTriggerTypeId() {
        return triggerTypeId;
    }

    public void setTriggerTypeId(Integer triggerTypeId) {
        this.triggerTypeId = triggerTypeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold == null ? null : threshold.trim();
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
        sb.append(", productId=").append(productId);
        sb.append(", name=").append(name);
        sb.append(", triggerTypeId=").append(triggerTypeId);
        sb.append(", createTime=").append(createTime);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", threshold=").append(threshold);
        sb.append(", mode=").append(mode);
        sb.append(", modeMsg=").append(modeMsg);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}