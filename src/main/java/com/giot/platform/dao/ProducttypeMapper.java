package com.giot.platform.dao;

import com.giot.platform.pojo.Producttype;

public interface ProducttypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Producttype record);

    int insertSelective(Producttype record);

    Producttype selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Producttype record);

    int updateByPrimaryKey(Producttype record);
}