package com.hydata.intelligence.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.hydata.intelligence.platform.pojo.DdTrigger;
@Mapper
public interface DdTriggerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DdTrigger record);

    int insertSelective(DdTrigger record);

    DdTrigger selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DdTrigger record);

    int updateByPrimaryKey(DdTrigger record);
}