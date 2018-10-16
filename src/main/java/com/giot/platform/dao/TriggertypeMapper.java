package com.giot.platform.dao;

import com.giot.platform.pojo.Triggertype;
import com.giot.platform.pojo.TriggertypeExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface TriggertypeMapper {
    long countByExample(TriggertypeExample example);

    int deleteByExample(TriggertypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Triggertype record);

    int insertSelective(Triggertype record);

    List<Triggertype> selectByExample(TriggertypeExample example);

    Triggertype selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Triggertype record, @Param("example") TriggertypeExample example);

    int updateByExample(@Param("record") Triggertype record, @Param("example") TriggertypeExample example);

    int updateByPrimaryKeySelective(Triggertype record);

    int updateByPrimaryKey(Triggertype record);
}