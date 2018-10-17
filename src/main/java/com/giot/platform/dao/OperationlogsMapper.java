package com.giot.platform.dao;

import com.giot.platform.pojo.Operationlogs;

public interface OperationlogsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Operationlogs record);

    int insertSelective(Operationlogs record);

    Operationlogs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Operationlogs record);

    int updateByPrimaryKey(Operationlogs record);
}