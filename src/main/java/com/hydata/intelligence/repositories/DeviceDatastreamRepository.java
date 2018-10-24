package com.hydata.intelligence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.DeviceDatastream;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:15:11
 */
public interface DeviceDatastreamRepository extends JpaRepository<DeviceDatastream, Integer> {

}

