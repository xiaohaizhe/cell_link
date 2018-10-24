package com.hydata.intelligence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.Device;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:14:29
 */
public interface DeviceRepository extends JpaRepository<Device, Integer> {

}

