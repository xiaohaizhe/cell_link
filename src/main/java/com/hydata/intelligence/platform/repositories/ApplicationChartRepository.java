package com.hydata.intelligence.platform.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hydata.intelligence.platform.dto.ApplicationChart;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:10:39
 */
public interface ApplicationChartRepository extends JpaRepository<ApplicationChart, Integer> {
	@Query("select ac from ApplicationChart ac where ac.applicationId = ?1")
	List<ApplicationChart> findByAppId(Integer appId);
}

