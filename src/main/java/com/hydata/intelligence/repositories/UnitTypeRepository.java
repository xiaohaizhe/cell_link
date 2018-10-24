package com.hydata.intelligence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hydata.intelligence.platform.dto.UnitType;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:22:22
 */
public interface UnitTypeRepository extends JpaRepository<UnitType, Integer> {
	@Query("select ut from unit_type ut where ut.name=:name and ut.symbol=:symbol")
	List<UnitType> findByNameAndSymbol(@Param("name")String name,@Param("symbol")String symbol);
}

