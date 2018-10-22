package com.hydata.intelligence.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.hydata.intelligence.platform.pojo.User;
@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}