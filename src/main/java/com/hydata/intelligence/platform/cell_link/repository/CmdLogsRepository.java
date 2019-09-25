package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.CmdLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

public interface CmdLogsRepository extends JpaRepository<CmdLogs, Long> {
    @Query("select cl from CmdLogs cl where cl.device_id = ?1")
    List<CmdLogs> findByDeviceId(Long device_id);

    @Query("select cl from CmdLogs cl where cl.device_id = ?1 order by ?#{#page}")
    Page<CmdLogs> findByDeviceId(Long device_id, Pageable page);

    @Query("select cl from CmdLogs cl where cl.userId = ?1")
    List<CmdLogs> findByUserId(Long userId);

    @QueryHints(value = {@QueryHint(name = HINT_COMMENT, value = "a query for pageable")})
    @Query("select cl from CmdLogs cl where cl.userId = ?1")
    Page<CmdLogs> findByUserId(Long userId, Pageable pageable);

}
