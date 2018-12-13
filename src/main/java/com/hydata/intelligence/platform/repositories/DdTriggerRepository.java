package com.hydata.intelligence.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.DdTrigger;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:13:51
 */
public interface DdTriggerRepository extends JpaRepository<DdTrigger, Integer> {

    @Query("select dt from DdTrigger dt where dt.ddId = ?1")
    Optional<DdTrigger> findByDdId(Integer ddId);
}

