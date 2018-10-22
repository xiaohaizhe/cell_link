package com.hydata.intelligence.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.hydata.intelligence.platform.pojo.ApplicationChart;
@Mapper
public interface ApplicationChartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplicationChart record);

    int insertSelective(ApplicationChart record);

    ApplicationChart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplicationChart record);

    int updateByPrimaryKey(ApplicationChart record);
}