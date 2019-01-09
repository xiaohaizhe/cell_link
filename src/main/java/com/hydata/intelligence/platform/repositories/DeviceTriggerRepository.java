package com.hydata.intelligence.platform.repositories;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.hydata.intelligence.platform.dto.DeviceTrigger;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:15:54
 */
public interface DeviceTriggerRepository extends JpaRepository<DeviceTrigger, Long> {
	@Modifying
	@Transactional
	@Query("delete from DeviceTrigger dt where dt.triggerId = ?1")
	int deleteByTriggerId(long triggerId);
	
	@Query("select dt from DeviceTrigger dt where dt.triggerId = ?1")
	List<DeviceTrigger> findByTriggerId(long triggerId);
	
	@Query("select dt from DeviceTrigger dt where dt.device_sn = ?1")
	List<DeviceTrigger> findByDeviceSn(String device_sn);
	
	@Query("select dt from DeviceTrigger dt where dt.device_sn = ?1 and dt.triggerId = ?2")
	Optional<DeviceTrigger> findByDeviceSnAndTriggerId(String device_sn,long triggerId);
	
	@QueryHints(value = {@QueryHint(name = HINT_COMMENT ,value= "a query for pageable")})
	@Query("select dt from DeviceTrigger dt where dt.triggerId = ?1")
	Page<DeviceTrigger> queryByTriggerId(long trigger_id,Pageable pageable);
	
	@QueryHints(value = {@QueryHint(name = HINT_COMMENT ,value= "a query for pageable")})
	@Query("select dt from DeviceTrigger dt where dt.device_sn = ?1")
	Page<DeviceTrigger> queryByDeviceSn(String device_sn,Pageable pageable);
}

