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
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
public class CmdLogs{
    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.hydata.intelligence.platform.cell_link.utils.IdGenerator",
            parameters = {})
    private long id;
    private String msg;

    private String cmd;

    private Date sendTime;

    private int res_code; //表示响应状态，0：正常， 1：命令已发往设备

    private String res_msg;

    @ManyToOne(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "dgId",name = "dgId")
    private DeviceGroup deviceGroup;

    private Long deviceId;
    private Long userId;
    private Long scenarioId;
}