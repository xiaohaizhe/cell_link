package com.giot.platform.dao;

import com.giot.platform.pojo.DsTrigger;
import com.giot.platform.pojo.DsTriggerExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface DsTriggerMapper {
    long countByExample(DsTriggerExample example);

    int deleteByExample(DsTriggerExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DsTrigger record);

    int insertSelective(DsTrigger record);

    List<DsTrigger> selectByExample(DsTriggerExample example);

    DsTrigger selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DsTrigger record, @Param("example") DsTriggerExample example);

    int updateByExample(@Param("record") DsTrigger record, @Param("example") DsTriggerExample example);

    int updateByPrimaryKeySelective(DsTrigger record);

    int updateByPrimaryKey(DsTrigger record);
}