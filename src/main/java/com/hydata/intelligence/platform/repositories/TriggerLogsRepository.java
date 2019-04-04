package com.hydata.intelligence.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.TriggerLogs;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:21:15
 */
public interface TriggerLogsRepository extends JpaRepository<TriggerLogs, Long> {
    @Query("select tl from TriggerLogs t where t.triggerId = ?1")
    List<TriggerLogs> findByTriggerId(long triggerId);

    @Query("select tl from TriggerLogs t where tt.triggerId = ?1 and t.sendTime>?2 and t.sendTime<?3")
    List<TriggerLogs> findByTriggerIdAndSendTimeBetween(Long triggerId, Date from, Date to);
/*

    @Query("{triggerId:?0,sendTime:{$gte:?1,$lte:?2}}")
    List<TriggerLogs> findByTriggerIdAndSendTimeBetween(Long triggerId, Date from, Date to);
*/

}

