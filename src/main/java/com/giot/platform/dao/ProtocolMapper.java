package com.giot.platform.dao;

import com.giot.platform.pojo.Protocol;

public interface ProtocolMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Protocol record);

    int insertSelective(Protocol record);

    Protocol selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Protocol record);

    int updateByPrimaryKey(Protocol record);
}