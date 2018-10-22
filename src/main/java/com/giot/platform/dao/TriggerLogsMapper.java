package com.giot.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.giot.platform.pojo.TriggerLogs;
@Mapper
public interface TriggerLogsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TriggerLogs record);

    int insertSelective(TriggerLogs record);

    TriggerLogs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TriggerLogs record);

    int updateByPrimaryKey(TriggerLogs record);
}