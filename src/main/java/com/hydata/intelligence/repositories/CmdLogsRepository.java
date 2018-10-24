package com.hydata.intelligence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.CmdLogs;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:13:01
 */
public interface CmdLogsRepository extends JpaRepository<CmdLogs, Integer> {

}

