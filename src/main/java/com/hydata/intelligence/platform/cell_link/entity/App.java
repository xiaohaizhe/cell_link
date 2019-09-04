package com.hydata.intelligence.platform.cell_link.entity;

import antlr.collections.impl.LList;
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
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.hydata.intelligence.platform.cell_link.utils.IdGenerator",
            parameters = {})
    private Long appId;
    private Long userId;
    @ManyToOne(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "scenarioId",name = "scenarioId")
    private Scenario scenario;
    @NotNull
    @NotBlank
    @Length(min=2,max=10,message="应用名称不能超过2至10个字符")
    private String appName;
    @CreationTimestamp
    @Column(updatable = false)
    private Date created;   //创建时间
    @UpdateTimestamp
    private Date modified;  //修改时间
    @Length(max=100,message="应用描述不能超过2至10个字符")
    private String description;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "app")
    private List<AppChart> appChartList;
}
