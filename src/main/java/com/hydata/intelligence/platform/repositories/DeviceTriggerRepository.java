package com.hydata.intelligence.platform.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hydata.intelligence.platform.dto.DeviceTrigger;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:15:54
 */
public interface DeviceTriggerRepository extends JpaRepository<DeviceTrigger, Integer> {
	@Modifying
	@Transactional
	@Query("delete from DeviceTrigger dt where dt.triggerId = ?1")
	int deleteByTriggerId(Integer triggerId);
	
	List<DeviceTrigger> findByTriggerId(Integer triggerId);
}

