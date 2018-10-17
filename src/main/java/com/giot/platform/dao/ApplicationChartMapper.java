package com.giot.platform.dao;

import com.giot.platform.pojo.ApplicationChart;

public interface ApplicationChartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplicationChart record);

    int insertSelective(ApplicationChart record);

    ApplicationChart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplicationChart record);

    int updateByPrimaryKey(ApplicationChart record);
}