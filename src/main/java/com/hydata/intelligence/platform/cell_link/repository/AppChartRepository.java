package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.AppChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @ClassName AppChartRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/4 17:43
 * @Version
 */
public interface AppChartRepository extends JpaRepository<AppChart,Long> {
    @Query("select s from AppChart s where s.app.appId = ?1 and s.sequenceNumber > ?2")
    List<AppChart> findByAppAndSequenceNumberGreaterThan(Long appId,Integer sequenceNumber);

    @Modifying
    @Query("delete from AppChart s where s.acId = ?1")
    int deleteByAcId(Long acId);
}
