package com.hydata.intelligence.platform.cell_link.entity;

import antlr.collections.impl.LList;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @ClassName App
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/29 13:55
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class App {
    @Id
    private Long appId;
    private Long userId;
    @ManyToOne(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "scenarioId",name = "scenarioId")
    private Scenario scenario;
    private String appName;
    @CreationTimestamp
    @Column(updatable = false)
    private Date created;   //创建时间
    @UpdateTimestamp
    private Date modified;  //修改时间
    private String description;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "app")
    private List<AppChart> appChartList;
}
