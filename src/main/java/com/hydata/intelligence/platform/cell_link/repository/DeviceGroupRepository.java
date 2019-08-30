package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @ClassName DeviceGroupRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/28 14:49
 * @Version
 */
public interface DeviceGroupRepository extends JpaRepository<DeviceGroup, Long> {
    @Query("select s from DeviceGroup s where s.scenario.scenarioId = ?1 and s.deviceGroupName = ?2")
    List<DeviceGroup> findByScenarioAndDeviceGroupName(Long scenarioId,String deviceGroupName);
}
