package com.hydata.intelligence.platform.cell_link.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName AnalysisDatastream
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/10 10:46
 * @Version
 */
@Data
@NoArgsConstructor
public class AnalysisDatastream {
    private Long datastreamId;  //数据流id
    private Integer type;	    //数据流类型:0-out/1-input
    private String start;       //数据流开始时间
    private String end;         //数据流结束时间
    private Float frequency;    //单位s
}
