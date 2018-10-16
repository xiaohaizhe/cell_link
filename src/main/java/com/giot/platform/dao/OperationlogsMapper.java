package com.giot.platform.dao;

import com.giot.platform.pojo.Operationlogs;
import com.giot.platform.pojo.OperationlogsExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface OperationlogsMapper {
    long countByExample(OperationlogsExample example);

    int deleteByExample(OperationlogsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Operationlogs record);

    int insertSelective(Operationlogs record);

    List<Operationlogs> selectByExample(OperationlogsExample example);

    Operationlogs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Operationlogs record, @Param("example") OperationlogsExample example);

    int updateByExample(@Param("record") Operationlogs record, @Param("example") OperationlogsExample example);

    int updateByPrimaryKeySelective(Operationlogs record);

    int updateByPrimaryKey(Operationlogs record);
}