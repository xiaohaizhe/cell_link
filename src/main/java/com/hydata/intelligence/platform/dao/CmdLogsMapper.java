package com.hydata.intelligence.platform.dao;

import org.apache.ibatis.annotations.Mapper;

import com.hydata.intelligence.platform.pojo.CmdLogs;
@Mapper
public interface CmdLogsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CmdLogs record);

    int insertSelective(CmdLogs record);

    CmdLogs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CmdLogs record);

    int updateByPrimaryKey(CmdLogs record);
}