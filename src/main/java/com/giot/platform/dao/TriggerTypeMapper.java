package com.giot.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.giot.platform.pojo.TriggerType;
@Mapper
public interface TriggerTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TriggerType record);

    int insertSelective(TriggerType record);

    TriggerType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TriggerType record);

    int updateByPrimaryKey(TriggerType record);
}