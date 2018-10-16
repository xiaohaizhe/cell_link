package com.giot.platform.dao;

import com.giot.platform.pojo.Unittype;
import com.giot.platform.pojo.UnittypeExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface UnittypeMapper {
    long countByExample(UnittypeExample example);

    int deleteByExample(UnittypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Unittype record);

    int insertSelective(Unittype record);

    List<Unittype> selectByExample(UnittypeExample example);

    Unittype selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Unittype record, @Param("example") UnittypeExample example);

    int updateByExample(@Param("record") Unittype record, @Param("example") UnittypeExample example);

    int updateByPrimaryKeySelective(Unittype record);

    int updateByPrimaryKey(Unittype record);
}