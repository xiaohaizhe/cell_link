package com.giot.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.giot.platform.pojo.CmdLogs;
@Mapper
public interface CmdLogsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CmdLogs record);

    int insertSelective(CmdLogs record);

    CmdLogs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CmdLogs record);

    int updateByPrimaryKey(CmdLogs record);
}