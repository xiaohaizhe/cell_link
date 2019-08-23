package com.hydata.intelligence.platform.cell_link.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @ClassName Device
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/22 13:45
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class Device {
    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.hydata.intelligence.platform.cell_link.utils.IdGenerator",
            parameters = {})
    private Long deviceId;
    private Long userId;
    private Long scenarioId;
    @ManyToOne(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    private DeviceGroup deviceGroup;
    private String deviceName;  //设备名称
    private String devicesn;    //设备鉴权信息
    private Float longitude;    //经度
    private Float latitude;     //纬度
    private String description; //设备描述
    private Integer status;     //设备状态

    @CreationTimestamp
    @Column(updatable = false)
    private Date created;   //创建时间
    @UpdateTimestamp
    private Date modified;  //修改时间

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "device")
    private List<Datastream> datastreamList;
}
