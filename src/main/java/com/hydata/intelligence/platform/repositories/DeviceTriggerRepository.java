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
	
	@Query("select dt from DeviceTrigger dt where dt.deviceId = ?1")
	List<DeviceTrigger> findByDeviceId(Long device_id);

	@Query("select dt from DeviceTrigger dt where dt.triggerId = ?1 and dt.deviceName like concat('%',?2 ,'%') ")
	List<DeviceTrigger> findByTriggerIdAndDeviceName(Long trigger_id,String device_name);

	@Query("select dt from DeviceTrigger dt where dt.deviceId = ?1 and dt.triggerId = ?2")
	Optional<DeviceTrigger> findByDeviceIdAndTriggerId(Long device_id,long triggerId);
	
	@QueryHints(value = {@QueryHint(name = HINT_COMMENT ,value= "a query for pageable")})
	@Query("select dt from DeviceTrigger dt where dt.triggerId = ?1 and dt.deviceName like concat('%',?2 ,'%') ")
	Page<DeviceTrigger> queryByTriggerId(long trigger_id,String name,Pageable pageable);
	
	@QueryHints(value = {@QueryHint(name = HINT_COMMENT ,value= "a query for pageable")})
	@Query("select dt from DeviceTrigger dt where dt.deviceId = ?1 and dt.deviceName like concat('%' ,?2,'%') ")
	Page<DeviceTrigger> queryByDeviceId(Long device_id,String name,Pageable pageable);
}

