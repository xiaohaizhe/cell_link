package com.hydata.intelligence.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.hydata.intelligence.platform.pojo.DeviceDatastream;
@Mapper
public interface DeviceDatastreamMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceDatastream record);

    int insertSelective(DeviceDatastream record);

    DeviceDatastream selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceDatastream record);

    int updateByPrimaryKey(DeviceDatastream record);
}