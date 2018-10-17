package com.giot.platform.dao;

import com.giot.platform.pojo.Triggertype;

public interface TriggertypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Triggertype record);

    int insertSelective(Triggertype record);

    Triggertype selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Triggertype record);

    int updateByPrimaryKey(Triggertype record);
}