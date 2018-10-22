package com.giot.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.giot.platform.pojo.OperationLogs;
@Mapper
public interface OperationLogsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OperationLogs record);

    int insertSelective(OperationLogs record);

    OperationLogs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OperationLogs record);

    int updateByPrimaryKey(OperationLogs record);
}