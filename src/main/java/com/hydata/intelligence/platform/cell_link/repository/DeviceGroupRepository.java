package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

/**
 * @ClassName DeviceGroupRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/28 14:49
 * @Version
 */
public interface DeviceGroupRepository extends JpaRepository<DeviceGroup, Long> {
    @QueryHints(value = {@QueryHint(name = HINT_COMMENT, value = "a query for pageable")})
    @Query("select s from DeviceGroup s where s.scenario.scenarioId = ?1 and s.deviceGroupName like concat('%' ,?2,'%') ")
    Page<DeviceGroup> findByScenarioAndDeviceGroupName(Long scenarioId,String deviceGroupName,Pageable page);

    @Query("select s from DeviceGroup s where s.scenario.scenarioId = ?1 and s.deviceGroupName = ?2")
    List<DeviceGroup> findByScenarioAndDeviceGroupName(Long scenarioId,String deviceGroupName);

    @Query("select s from DeviceGroup s where s.scenario.scenarioId = ?1")
    List<DeviceGroup> findByScenario(Long scenarioId);

    @Query("select count(1) from DeviceGroup s where s.userId = ?1")
    Long findByUserId(Long userId);
}
