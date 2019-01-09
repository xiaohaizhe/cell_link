package com.hydata.intelligence.platform.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hydata.intelligence.platform.dto.OperationLogs;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:17:12
 */
public interface OperationLogsRepository extends JpaRepository<OperationLogs, Long> {
	@Query("select o from OperationLogs o where o.userId = ?1 and o.msg like concat('%' ,?2,'%')")
	List<OperationLogs> findByUserIdAndKeyWord(long userId,String keyWord);
}

