package com.giot.platform.dao;

import com.giot.platform.pojo.ApplicationAnalysis;

public interface ApplicationAnalysisMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplicationAnalysis record);

    int insertSelective(ApplicationAnalysis record);

    ApplicationAnalysis selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplicationAnalysis record);

    int updateByPrimaryKey(ApplicationAnalysis record);
}