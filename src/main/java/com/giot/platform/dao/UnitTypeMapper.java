package com.giot.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.giot.platform.pojo.UnitType;
@Mapper
public interface UnitTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UnitType record);

    int insertSelective(UnitType record);

    UnitType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UnitType record);

    int updateByPrimaryKey(UnitType record);
}