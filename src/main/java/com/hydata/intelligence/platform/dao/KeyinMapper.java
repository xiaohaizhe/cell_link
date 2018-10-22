package com.hydata.intelligence.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.hydata.intelligence.platform.pojo.Keyin;
@Mapper
public interface KeyinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Keyin record);

    int insertSelective(Keyin record);

    Keyin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Keyin record);

    int updateByPrimaryKey(Keyin record);
}