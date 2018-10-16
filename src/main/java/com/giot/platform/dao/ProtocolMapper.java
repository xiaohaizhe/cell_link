package com.giot.platform.dao;

import com.giot.platform.pojo.Protocol;
import com.giot.platform.pojo.ProtocolExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ProtocolMapper {
    long countByExample(ProtocolExample example);

    int deleteByExample(ProtocolExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Protocol record);

    int insertSelective(Protocol record);

    List<Protocol> selectByExample(ProtocolExample example);

    Protocol selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Protocol record, @Param("example") ProtocolExample example);

    int updateByExample(@Param("record") Protocol record, @Param("example") ProtocolExample example);

    int updateByPrimaryKeySelective(Protocol record);

    int updateByPrimaryKey(Protocol record);
}