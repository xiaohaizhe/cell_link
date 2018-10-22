package com.hydata.intelligence.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.hydata.intelligence.platform.pojo.Chart;
@Mapper
public interface ChartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Chart record);

    int insertSelective(Chart record);

    Chart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Chart record);

    int updateByPrimaryKey(Chart record);
}