package com.hydata.intelligence.platform.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hydata.intelligence.platform.dto.ApplicationAnalysisDatastream;

/**
 * @author pyt
 * @createTime 2018年11月19日下午4:01:49
 */
public interface ApplicationAnalysisDatastreamRepository extends JpaRepository<ApplicationAnalysisDatastream, Long> {
	@Modifying
	@Transactional
	@Query("delete from ApplicationAnalysisDatastream aad where aad.aaId = ?1")
	int deleteByAa_id(long aaId);
	
	@Query("select aad from ApplicationAnalysisDatastream aad where aad.aaId = ?1")
	List<ApplicationAnalysisDatastream> findByAa_id(long aa_Id);	
	
	@Query("select aad from ApplicationAnalysisDatastream aad where aad.ddId = ?1")
	List<ApplicationAnalysisDatastream> findByDd_id(long dd_id);
}

