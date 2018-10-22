package com.giot.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.giot.platform.pojo.Application;
@Mapper
public interface ApplicationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Application record);

    int insertSelective(Application record);

    Application selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Application record);

    int updateByPrimaryKey(Application record);
}