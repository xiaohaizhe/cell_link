package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @ClassName ScenarioRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/27 16:16
 * @Version
 */
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    @Modifying
    @Query("delete from Scenario s where s.scenarioId = ?1")
    int deleteByScenarioId(Long scenarioId);

    @Query("select s from Scenario s where s.user.userId = ?2 and s.scenarioName = ?1")
    List<Scenario> findByScenarioNameAndUser(String name, Long userId);

    @Query("select s from Scenario s where s.user.userId = ?1")
    List<Scenario> findByUser(Long userId);
}
