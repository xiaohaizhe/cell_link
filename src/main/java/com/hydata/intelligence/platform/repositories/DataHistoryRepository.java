package com.hydata.intelligence.platform.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.hydata.intelligence.platform.dto.Device;
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
	
	@Query("{dd_id:?0}")
	List<Data_history> findByDd_id(Long dd_id);

	@Query("{'dd_id':{$in:?0},'create_time':{$gte:?1,$lte:?2}}")
	List<Data_history> findByDd_idInAndCreate_timeBetween(List<Long> ids, Date from,Date to);
	
}

