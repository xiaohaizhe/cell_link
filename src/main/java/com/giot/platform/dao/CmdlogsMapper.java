package com.giot.platform.dao;

import com.giot.platform.pojo.Cmdlogs;

public interface CmdlogsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cmdlogs record);

    int insertSelective(Cmdlogs record);

    Cmdlogs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cmdlogs record);

    int updateByPrimaryKey(Cmdlogs record);
}