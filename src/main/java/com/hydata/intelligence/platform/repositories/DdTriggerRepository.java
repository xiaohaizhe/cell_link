package com.hydata.intelligence.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.hydata.intelligence.platform.dto.DdTrigger;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:13:51
 */
public interface DdTriggerRepository extends JpaRepository<DdTrigger, Long> {
	@Modifying
	@Transactional
	@Query("delete from DdTrigger dt where dt.triggerId = ?1")
	int deleteByTriggerId(long triggerId);
	
    @Query("select dt from DdTrigger dt where dt.ddId = ?1")
    List<DdTrigger> findByDdId(long ddId);
    
    @Query("select dt from DdTrigger dt where dt.triggerId = ?1")
    List<DdTrigger> findByTriggerId(long triggerId);
    
    @Query("select dt from DdTrigger dt where dt.triggerId = ?1 and dt.ddId = ?2")
    Optional<DdTrigger> findByTriggerIdAndDdId(long triggerId,long ddId);
}

