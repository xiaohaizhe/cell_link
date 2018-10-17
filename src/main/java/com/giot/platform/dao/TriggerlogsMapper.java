package com.giot.platform.dao;

import com.giot.platform.pojo.Triggerlogs;

public interface TriggerlogsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Triggerlogs record);

    int insertSelective(Triggerlogs record);

    Triggerlogs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Triggerlogs record);

    int updateByPrimaryKey(Triggerlogs record);
}