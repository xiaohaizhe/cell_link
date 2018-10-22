package com.giot.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.giot.platform.pojo.OperationType;
@Mapper
public interface OperationTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OperationType record);

    int insertSelective(OperationType record);

    OperationType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OperationType record);

    int updateByPrimaryKey(OperationType record);
}