package com.giot.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.giot.platform.pojo.ApplicationChartDatastream;
@Mapper
public interface ApplicationChartDatastreamMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplicationChartDatastream record);

    int insertSelective(ApplicationChartDatastream record);

    ApplicationChartDatastream selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplicationChartDatastream record);

    int updateByPrimaryKey(ApplicationChartDatastream record);
}