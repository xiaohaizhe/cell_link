package com.giot.platform.dao;

import com.giot.platform.pojo.DsTrigger;

public interface DsTriggerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DsTrigger record);

    int insertSelective(DsTrigger record);

    DsTrigger selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DsTrigger record);

    int updateByPrimaryKey(DsTrigger record);
}