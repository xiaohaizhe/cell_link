package com.hydata.intelligence.platform.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.hydata.intelligence.platform.dto.CmdLogs;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:13:01
 */
public interface CmdLogsRepository extends JpaRepository<CmdLogs, Integer> {
	@Query("select cl from CmdLogs cl where cl.device_sn = ?1")
	List<CmdLogs> findByDeviceSn(String device_sn);
}

