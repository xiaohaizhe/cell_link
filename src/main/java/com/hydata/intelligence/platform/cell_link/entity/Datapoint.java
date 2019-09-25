package com.hydata.intelligence.platform.cell_link.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName Datapoint
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/22 14:35
 * @Version
 */
@Data
public class Datapoint implements Serializable {
    private Datastream datastream;
    private Long deviceId;
    private Long datastreamId;
    private String datastreamName;
    private Date created;   //创建时间
    private Float value;    //数值
    private Integer status; //数据点状态//0为正常, 1为<50%, 2为>150%


    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getsStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
