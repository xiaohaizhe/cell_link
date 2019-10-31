package com.hydata.intelligence.platform.cell_link.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName Datastream
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/22 13:44
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class Datastream {
    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.hydata.intelligence.platform.cell_link.utils.IdGeneratorDatapoint",
            parameters = {})
    private Long datastreamId;
    private Long userId;
    private Long scenarioId;
    private Long dgId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "deviceId",name = "deviceId")
    private Device device;
    private String deviceName;
    private String datastreamName;  //数据流名称
    private Float frequency;        //数据点上传频率

    @CreationTimestamp
    @Column(updatable = false)
    private Date created;   //创建时间
    private Date updateTime;  //数据流最新更新时间

//    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "datastream")
//    private List<Datapoint> datapointList;
}
