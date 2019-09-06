package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.App;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

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

    @QueryHints(value = {@QueryHint(name = HINT_COMMENT, value = "a query for pageable")})
    @Query("select s from App s where s.scenario.scenarioId=?1 and s.appName like concat('%' ,?2,'%') ")
    Page<App> findByScenarioAndAppNameLike(Long scenarioId,String appName, Pageable page);

    @Query("select count(1) from App s where s.userId = ?1")
    Long findByUserId(Long userId);
}
