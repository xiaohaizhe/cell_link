package com.giot.platform.dao;

import com.giot.platform.pojo.ApplicationChart;
import com.giot.platform.pojo.ApplicationChartExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ApplicationChartMapper {
    long countByExample(ApplicationChartExample example);

    int deleteByExample(ApplicationChartExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApplicationChart record);

    int insertSelective(ApplicationChart record);

    List<ApplicationChart> selectByExample(ApplicationChartExample example);

    ApplicationChart selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApplicationChart record, @Param("example") ApplicationChartExample example);

    int updateByExample(@Param("record") ApplicationChart record, @Param("example") ApplicationChartExample example);

    int updateByPrimaryKeySelective(ApplicationChart record);

    int updateByPrimaryKey(ApplicationChart record);
}