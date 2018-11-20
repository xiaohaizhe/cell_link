package com.hydata.intelligence.platform.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hydata.intelligence.platform.dto.ApplicationAnalysis;
import com.hydata.intelligence.platform.dto.ApplicationChart;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:09:33
 */
public interface ApplicationAnalysisRepository extends JpaRepository<ApplicationAnalysis, Integer> {
	@Query("select aa from ApplicationAnalysis aa where aa.applicationId = ?1")
	Optional<ApplicationAnalysis> findByApplicationId(Integer application_id);
}

