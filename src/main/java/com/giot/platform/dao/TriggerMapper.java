package com.giot.platform.dao;

import com.giot.platform.pojo.Trigger;

public interface TriggerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Trigger record);

    int insertSelective(Trigger record);

    Trigger selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Trigger record);

    int updateByPrimaryKey(Trigger record);
}