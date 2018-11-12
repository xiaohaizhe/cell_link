package com.hydata.intelligence.platform.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hydata.intelligence.platform.dto.DeviceDatastream;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:15:11
 */
public interface DeviceDatastreamRepository extends JpaRepository<DeviceDatastream, Integer> {
	@Query("select dd from DeviceDatastream dd where dd.deviceId = ?1 and dd.dm_name = ?2")
	Optional<DeviceDatastream> findByDeviceIdAndDm_name(Integer deviceId,String dm_name);
	
	@Query("select dd from DeviceDatastream dd where dd.deviceId = ?1")
	List<DeviceDatastream> findByDeviceId(Integer deviceId);

}

