package com.hydata.intelligence.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.hydata.intelligence.platform.pojo.DeviceTrigger;
@Mapper
public interface DeviceTriggerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceTrigger record);

    int insertSelective(DeviceTrigger record);

    DeviceTrigger selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceTrigger record);

    int updateByPrimaryKey(DeviceTrigger record);
}