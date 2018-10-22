package com.hydata.intelligence.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.hydata.intelligence.platform.pojo.DatastreamModel;
@Mapper
public interface DatastreamModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DatastreamModel record);

    int insertSelective(DatastreamModel record);

    DatastreamModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DatastreamModel record);

    int updateByPrimaryKey(DatastreamModel record);
}