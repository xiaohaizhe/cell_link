package com.hydata.intelligence.platform.cell_link.entity;

import com.hydata.intelligence.platform.cell_link.entity.dict.EventLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @ClassName Event
 * @Description 事件
 * @Author pyt
 * @Date 2019/8/22 11:41
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.hydata.intelligence.platform.cell_link.utils.IdGenerator",
            parameters = {})
    private Long eventId;
    private Long userId;
    private String eventName;   //事件名称
    @ManyToOne(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "scenarioId",name = "scenarioId")
    private Scenario scenario;
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "el_id", referencedColumnName = "elId", nullable = false)
    private EventLevel el;  //事件等级
    private String eventLevel;
    private Integer triggerMode;    //触发模式：0-邮箱，1-url，2-下发命令
    private Long deviceId;          //触发模式为2，下发命令的设备
    private String modeValue;       //触发模式值：邮箱地址/url地址/命令
    private String vertifyCode;     //触发模式为0，邮箱验证码

    @CreationTimestamp
    @Column(updatable = false)
    private Date created;   //创建时间
    @UpdateTimestamp
    private Date modified;  //修改时间

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "event")
    private List<TriggerCondition> triggerConditionList;



}
