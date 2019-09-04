package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @ClassName AppRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/3 11:52
 * @Version
 */
public interface AppRepository extends JpaRepository<App,Long> {
    @Query("select s from App s where s.appName = ?1 and s.scenario.scenarioId = ?2")
    List<App> findByAppNameAndScenario(String appName,Long scenarioId);
}
