package com.hydata.intelligence.platform.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hydata.intelligence.platform.dto.ApplicationChartDatastream;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:11:36
 */
public interface ApplicationChartDatastreamRepository extends JpaRepository<ApplicationChartDatastream, Long> {
	@Modifying
	@Transactional
	@Query("delete from ApplicationChartDatastream acd where acd.acId = ?1")
	int deleteByAc_id(long acId);
	
	@Query("select acd from ApplicationChartDatastream acd where acd.acId = ?1")
	List<ApplicationChartDatastream> findByAc_id(long ac_id);
	
	@Query("select acd from ApplicationChartDatastream acd where acd.ddId = ?1")
	List<ApplicationChartDatastream> findByDd_id(long dd_id);
}

