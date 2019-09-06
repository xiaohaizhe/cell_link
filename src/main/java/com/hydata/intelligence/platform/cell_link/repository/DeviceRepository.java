package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.Device;
import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.Date;
import java.util.List;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

/**
 * @ClassName DeviceRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/30 14:07
 * @Version
 */
public interface DeviceRepository extends JpaRepository<Device,Long> , JpaSpecificationExecutor<Device> {
    @Query("select s from Device s where s.userId = ?1")
    List<Device> findByUserId(Long userId);

    @Query("select s from Device s where s.deviceName = ?1 and s.deviceGroup.dgId = ?2")
    List<Device> findByDeviceNameAndDeviceGroup(String deviceName,Long dgId);

    @Query("select s from Device s where s.devicesn = ?1 and s.deviceGroup.dgId = ?2")
    List<Device> findByDevicesnAndDeviceGroup(String devicesn,Long dgId);

    @QueryHints(value = {@QueryHint(name = HINT_COMMENT, value = "a query for pageable")})
    @Query("select s from Device s where s.userId=?1 and s.deviceName like concat('%' ,?2,'%') ")
    Page<Device> findByUserIdAndDeviceName(Long userId, String deviceName, Pageable page);

    @Query("select s from Device s where s.userId=?1 and s.created>= ?2 and s.created<=?3 ")
    List<Device> findByUserIdAndCreatedBetween(Long userId, Date start,Date end);


}
