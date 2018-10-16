package com.giot.platform.dao;

import com.giot.platform.pojo.Chart;
import com.giot.platform.pojo.ChartExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ChartMapper {
    long countByExample(ChartExample example);

    int deleteByExample(ChartExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Chart record);

    int insertSelective(Chart record);

    List<Chart> selectByExample(ChartExample example);

    Chart selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Chart record, @Param("example") ChartExample example);

    int updateByExample(@Param("record") Chart record, @Param("example") ChartExample example);

    int updateByPrimaryKeySelective(Chart record);

    int updateByPrimaryKey(Chart record);
}