package com.hydata.intelligence.platform.cell_link.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName AnalysisApplication
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/10 10:31
 * @Version
 */
@Data
@NoArgsConstructor
public class AnalysisApplication {
    private Integer applicationType;//智能分析应用类型,0-CORRELATION_ANALYSE,1-LINEAR_REGRESSION_ANALYSE
    private List<AnalysisDatastream> analysisDatastreams;
}
