package com.hydata.intelligence.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.hydata.intelligence.platform.pojo.ApplicationAnalysis;
@Mapper
public interface ApplicationAnalysisMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplicationAnalysis record);

    int insertSelective(ApplicationAnalysis record);

    ApplicationAnalysis selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplicationAnalysis record);

    int updateByPrimaryKey(ApplicationAnalysis record);
}