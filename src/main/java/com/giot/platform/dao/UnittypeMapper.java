package com.giot.platform.dao;

import com.giot.platform.pojo.Unittype;

public interface UnittypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Unittype record);

    int insertSelective(Unittype record);

    Unittype selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Unittype record);

    int updateByPrimaryKey(Unittype record);
}