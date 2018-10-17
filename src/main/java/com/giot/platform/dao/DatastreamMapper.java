package com.giot.platform.dao;

import com.giot.platform.pojo.Datastream;

public interface DatastreamMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Datastream record);

    int insertSelective(Datastream record);

    Datastream selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Datastream record);

    int updateByPrimaryKey(Datastream record);
}