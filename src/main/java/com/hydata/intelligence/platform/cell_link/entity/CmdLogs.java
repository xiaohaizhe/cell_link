package com.hydata.intelligence.platform.cell_link.entity;

/**
 * @author: Jasmine
 * @createTime: 2019-09-24 15:14
 * @description:
 * @modified:
 */
import java.util.Date;

import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;
@Entity
public class CmdLogs{
    @Id
    private long id;

    private long device_id;

    private String cmd;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    private long userId;

    private int res_code; //表示响应状态，0：正常， 1：命令已发往设备

    private String res_msg;

    @ManyToOne(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "dgId",name = "dgId")
    private DeviceGroup deviceGroup;

    private Long scenarioId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Long device_id) {
        this.device_id = device_id;
    }


    public void setCmd(String cmd) {
        this.cmd = cmd == null ? null : cmd.trim();
    }

    public String getCmd() {
        return cmd;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public int getRes_code(){return res_code;}

    public void setRes_code(int res_code){this.res_code = res_code;}

    public String getRes_msg(){return res_msg;}

    public void setRes_msg(String res_msg){this.res_msg = res_msg;}

    public long getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(long scenarioId) {
        this.scenarioId = scenarioId;
    }

    public DeviceGroup getDeviceGroup() {
        return deviceGroup;
    }

    public void setDeviceGroup(DeviceGroup deviceGroup) {
        this.deviceGroup = deviceGroup;
    }


}