package com.giot.platform.dao;

import com.giot.platform.pojo.Operationtype;
import com.giot.platform.pojo.OperationtypeExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface OperationtypeMapper {
    long countByExample(OperationtypeExample example);

    int deleteByExample(OperationtypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Operationtype record);

    int insertSelective(Operationtype record);

    List<Operationtype> selectByExample(OperationtypeExample example);

    Operationtype selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Operationtype record, @Param("example") OperationtypeExample example);

    int updateByExample(@Param("record") Operationtype record, @Param("example") OperationtypeExample example);

    int updateByPrimaryKeySelective(Operationtype record);

    int updateByPrimaryKey(Operationtype record);
}