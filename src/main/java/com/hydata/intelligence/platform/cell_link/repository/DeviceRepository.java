package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @ClassName DeviceRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/30 14:07
 * @Version
 */
public interface DeviceRepository extends JpaRepository<Device,Long> {
    @Query("select s from Device s where s.deviceName = ?1 and s.deviceGroup.dgId = ?2")
    List<Device> findByDeviceNameAndDeviceGroup(String deviceName,Long dgId);

    @Query("select s from Device s where s.devicesn = ?1 and s.deviceGroup.dgId = ?2")
    List<Device> findByDevicesnAndDeviceGroup(String devicesn,Long dgId);
}
