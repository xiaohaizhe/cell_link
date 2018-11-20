package com.hydata.intelligence.platform.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hydata.intelligence.platform.dto.ApplicationAnalysisDatastream;

/**
 * @author pyt
 * @createTime 2018年11月19日下午4:01:49
 */
public interface ApplicationAnalysisDatastreamRepository extends JpaRepository<ApplicationAnalysisDatastream, Integer> {
	@Query("select aad from ApplicationAnalysisDatastream aad where aad.aaId = ?1")
	List<ApplicationAnalysisDatastream> findByAa_id(Integer aa_Id);
}

