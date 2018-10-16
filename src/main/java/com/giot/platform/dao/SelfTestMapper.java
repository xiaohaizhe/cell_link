package com.giot.platform.dao;

import com.giot.platform.pojo.SelfTest;
import com.giot.platform.pojo.SelfTestExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface SelfTestMapper {
    long countByExample(SelfTestExample example);

    int deleteByExample(SelfTestExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SelfTest record);

    int insertSelective(SelfTest record);

    List<SelfTest> selectByExample(SelfTestExample example);

    SelfTest selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SelfTest record, @Param("example") SelfTestExample example);

    int updateByExample(@Param("record") SelfTest record, @Param("example") SelfTestExample example);

    int updateByPrimaryKeySelective(SelfTest record);

    int updateByPrimaryKey(SelfTest record);
}