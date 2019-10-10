package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.CmdLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

public interface CmdLogsRepository extends JpaRepository<CmdLogs,Long> , JpaSpecificationExecutor<CmdLogs> {
    @Query("select cl from CmdLogs cl where cl.deviceId = ?1")
    List<CmdLogs> findByDeviceId(Long deviceId);

    @Query("select cl from CmdLogs cl where cl.userId = ?1")
    List<CmdLogs> findByUserId(Long userId);

    @QueryHints(value = {@QueryHint(name = HINT_COMMENT, value = "a query for pageable")})
    @Query("select cl from CmdLogs cl where cl.userId = ?1")
    Page<CmdLogs> findByUserId(Long userId, Pageable pageable);

}
