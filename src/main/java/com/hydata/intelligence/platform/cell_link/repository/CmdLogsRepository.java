package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.CmdLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CmdLogsRepository extends JpaRepository<CmdLogs, Long> {
    @Query("select cl from CmdLogs cl where cl.device_id = ?1")
    List<CmdLogs> findByDeviceId(Long device_id);

    @Query("select cl from CmdLogs cl where cl.device_id = ?1 order by ?#{#page}")
    Page<CmdLogs> findByDeviceId(Long device_id, Pageable page);

    @Query("select cl from CmdLogs cl where cl.cmd = ?1")
    Optional<CmdLogs> findByCmd(String cmd);
}
