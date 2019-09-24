package com.hydata.intelligence.platform.cell_link.entity;

import com.hydata.intelligence.platform.cell_link.entity.dict.Protocol;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @ClassName DeviceGroup
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/22 13:47
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class DeviceGroup {
    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.hydata.intelligence.platform.cell_link.utils.IdGenerator",
            parameters = {})
    private Long dgId;
    private Long userId;
    @ManyToOne(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "scenarioId",name = "scenarioId")
    private Scenario scenario;
    @NotNull
    @NotBlank
    @Length(min=2,max=10,message="设备组名称不能超过2至10个字符")
    private String deviceGroupName; //设备组名称
    private String description;     //设备组简介
    @OneToOne
    @JoinColumn(referencedColumnName = "protocolId", nullable = false,name = "protocolId")
    private Protocol protocol;        //设备接入协议
    private String serialNumber;    //设备序列号
    private String factory;         //生产厂家
    private String specification;   //设备规格
    private String parameters;      //生产参数
    private String registrationCode;    //设备注册码
    @CreationTimestamp
    @Column(updatable = false)
    private Date created;   //创建时间
    @UpdateTimestamp
    private Date modified;  //修改时间

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "deviceGroup")
    private List<Device> deviceList;

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public Protocol getProtocol() {
        return protocol;
    }

}
