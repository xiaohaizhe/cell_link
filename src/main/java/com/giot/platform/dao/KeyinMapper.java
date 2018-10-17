package com.giot.platform.dao;

import com.giot.platform.pojo.Keyin;

public interface KeyinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Keyin record);

    int insertSelective(Keyin record);

    Keyin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Keyin record);

    int updateByPrimaryKey(Keyin record);
}