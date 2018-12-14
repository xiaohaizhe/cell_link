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

	@Query("select dd from DeviceDatastream dd where dd.device_sn = ?1 and dd.dm_name = ?2")
	Optional<DeviceDatastream> findByDeviceSnAndDm_name(String deviceSn,String dm_name);
	
	@Query("select dd from DeviceDatastream dd where dd.device_sn = ?1")
	List<DeviceDatastream> findByDeviceSn(String device_sn);

}

