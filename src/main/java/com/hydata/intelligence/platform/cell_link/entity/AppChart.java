package com.hydata.intelligence.platform.cell_link.entity;

import com.hydata.intelligence.platform.cell_link.entity.dict.Chart;
import com.hydata.intelligence.platform.cell_link.validation.emailvalidation.EmailValidation;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private Long acId;
    @ManyToOne(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "appId",name = "appId")
    private App app;
    @OneToOne
    @JoinColumn(referencedColumnName = "chartId",name = "chartId")
    private Chart chart;
    private Integer sequenceNumber; //图表顺序编号
    private Float refreshFrequency;
    private Float timeFrame;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "appChart")
    private List<AppDatastream> appDatastreamList;
}
