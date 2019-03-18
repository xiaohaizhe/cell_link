package com.hydata.intelligence.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.TriggerLogs;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:21:15
 */
public interface TriggerLogsRepository extends JpaRepository<TriggerLogs, Long> {

}

