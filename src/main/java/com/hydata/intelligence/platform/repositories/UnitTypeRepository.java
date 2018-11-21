package com.hydata.intelligence.platform.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hydata.intelligence.platform.dto.UnitType;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:22:22
 */
@Repository
public interface UnitTypeRepository extends JpaRepository<UnitType, Integer> {
	@Query("select ut from UnitType ut where ut.name = ?1 and ut.symbol= ?2")
	List<UnitType> findByNameAndSymbol(String unit_name,String unit_symbol);
}

