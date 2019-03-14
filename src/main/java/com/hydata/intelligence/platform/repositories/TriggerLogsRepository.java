package com.hydata.intelligence.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.TriggerLogs;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:21:15
 */
public interface TriggerLogsRepository extends JpaRepository<TriggerLogs, Long> {
    @Query("{triggerId:?0,send_time:{$gte:?1,$lte:?2}}")
    List<TriggerLogs> findByTriggerIdAndSend_timeBetween(Long triggerId, Date from, Date to);

}

