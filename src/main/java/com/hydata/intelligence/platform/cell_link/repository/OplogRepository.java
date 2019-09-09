package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.Oplog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

/**
 * @ClassName OplogRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/9 10:40
 * @Version
 */
public interface OplogRepository extends JpaRepository<Oplog,Long> {
    @Query("select o from Oplog  o where o.user.userId = ?1")
    List<Oplog> findByUser(Long userId);

    @QueryHints(value = {@QueryHint(name = HINT_COMMENT, value = "a query for pageable")})
    @Query("select o from Oplog  o where o.user.userId = ?1")
    Page<Oplog> findByUser(Long userId, Pageable pageable);
}
