package com.hydata.intelligence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.OperationLogs;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:17:12
 */
public interface OperationLogsRepository extends JpaRepository<OperationLogs, Integer> {

}

