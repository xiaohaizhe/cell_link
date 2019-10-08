package com.hydata.intelligence.platform.cell_link.entity;

import com.hydata.intelligence.platform.cell_link.entity.dict.Chart;
import com.hydata.intelligence.platform.cell_link.validation.emailvalidation.EmailValidation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName AppChart
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/29 13:59
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class AppChart {
    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.hydata.intelligence.platform.cell_link.utils.IdGenerator",
            parameters = {})
    private Long acId;
    @ManyToOne(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "appId",name = "appId")
    private App app;
    @OneToOne
    @JoinColumn(referencedColumnName = "chartId",name = "chartId",nullable = false)
    private Chart chart;
//    @NotNull
    private Integer sequenceNumber; //图表顺序编号,1,2,3,4...
//    @NotNull
    private Float refreshFrequency; //图表刷新频率
//    @NotNull
    private Float timeFrame;        //显示数据时间范围
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "appChart")
    private List<AppDatastream> appDatastreamList;
}
