package com.giot.platform.dao;

import com.giot.platform.pojo.Cmdlogs;
import com.giot.platform.pojo.CmdlogsExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface CmdlogsMapper {
    long countByExample(CmdlogsExample example);

    int deleteByExample(CmdlogsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Cmdlogs record);

    int insertSelective(Cmdlogs record);

    List<Cmdlogs> selectByExample(CmdlogsExample example);

    Cmdlogs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Cmdlogs record, @Param("example") CmdlogsExample example);

    int updateByExample(@Param("record") Cmdlogs record, @Param("example") CmdlogsExample example);

    int updateByPrimaryKeySelective(Cmdlogs record);

    int updateByPrimaryKey(Cmdlogs record);
}