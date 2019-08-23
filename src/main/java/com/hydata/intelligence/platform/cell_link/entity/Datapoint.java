package com.hydata.intelligence.platform.cell_link.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import java.util.Date;

/**
 * @ClassName Datapoint
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/22 14:35
 * @Version
 */
@Data
public class Datapoint {
    private String datapointId;
    private Datastream datastream;
    private String datastreamName;
    @CreationTimestamp
    @Column(updatable = false)
    private Date created;   //创建时间
    private Float value;    //数值
    private Integer status; //数据点状态

}
