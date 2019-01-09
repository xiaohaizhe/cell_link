package com.hydata.intelligence.platform.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hydata.intelligence.platform.dto.ApplicationAnalysis;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:09:33
 */
public interface ApplicationAnalysisRepository extends JpaRepository<ApplicationAnalysis, Long> {
	@Query("select aa from ApplicationAnalysis aa where aa.applicationId = ?1")
	Optional<ApplicationAnalysis> findByApplicationId(long application_id);
}

