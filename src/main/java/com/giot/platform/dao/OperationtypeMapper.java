package com.giot.platform.dao;

import com.giot.platform.pojo.Operationtype;

public interface OperationtypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Operationtype record);

    int insertSelective(Operationtype record);

    Operationtype selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Operationtype record);

    int updateByPrimaryKey(Operationtype record);
}