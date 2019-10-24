package com.hydata.intelligence.platform.cell_link.entity;

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
 * @ClassName Scenario
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/22 13:48
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class Scenario {
    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.hydata.intelligence.platform.cell_link.utils.IdGenerator",
            parameters = {})
    private Long scenarioId;
    @ManyToOne(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "userId",name = "userId")
    private User user;
    @NotNull
    @NotBlank
    @Length(min=2,max=10,message="场景名不能超过2至10个字符")
    private String scenarioName;    //场景名
    @Length(max=100,message="场景简介不能超过100个字符")
    @NotNull
    @NotBlank
    private String description;     //场景简介
    @CreationTimestamp
    @Column(updatable = false)
    private Date created;   //创建时间
    @UpdateTimestamp
    private Date modified;  //修改时间

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "scenario")
    private List<DeviceGroup> deviceGroupList;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "scenario")
    private List<Event> eventList;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "scenario")
    private List<App> appList;
}
