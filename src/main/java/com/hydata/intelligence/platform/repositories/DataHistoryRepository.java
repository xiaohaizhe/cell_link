package com.hydata.intelligence.platform.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.hydata.intelligence.platform.dto.Data_history;

/**
 * @author pyt
 * @createTime 2019年1月15日下午4:55:31
 */
public interface DataHistoryRepository extends MongoRepository<Data_history, Long>{
	@Query("{dd_id:?0}")
	Page<Data_history> findByDd_id(Long dd_id,Pageable page);

	@Query("{dd_id:?0,create_time:{$gte:?1,$lte:?2}}")
	List<Data_history> findByDd_idAndCreate_timeBetween(Long dd_id,Date from,Date to);
}

