package com.hydata.intelligence.platform.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.hydata.intelligence.platform.dto.Device;

/**
 * @author pyt
 * @createTime 2019年1月14日上午10:02:33
 */
public interface DeviceRepository extends MongoRepository<Device,Long>{
	/*@Query("{device_sn:?0}")
	Optional<Device> findByDevice_sn(String device_sn);*/

	@Query("{id:?0}")
	Optional<Device> findById(Long id);

	@Query("{'product_id':?0}")
	Page<Device> findDeviceByProductid(Long product_id,Pageable page);

	@Query("{product_id:?0}")
	List<Device> findByProductId(Long product_id);

	@Query("{product_id:?0,name:{$regex:?1}}")
	Page<Device> findDeviceByName(Long product_id,String name,Pageable page);

	@Query("{product_id:?0,name:{$regex:?1},create_time:{$gte:?2,$lte:?3}}")
	Page<Device> findDeviceByNameAndTime(Long product_id,String name,Date from,Date to,Pageable page);


	@Query("{product_id:?0,create_time:{$gte:?1,$lte:?2}}")
	Page<Device> findDeviceByTime(Long product_id,Date from,Date to,Pageable page);

	@Query("{'product_id':?0,'device_sn':?1}")
	Page<Device> findDeviceByDevice_sn(Long product_id,String device_sn,Pageable page);

	@Query("{product_id:?0,create_time:{$gte:?1,$lte:?2}}")
	List<Device> findByCreate_timeBetween(Long product_id,Date from,Date to);

	@Query("{'product_id':?0,create_time:{$gte:?1,$lte:?2}}")
	List<Device> findByProductIdAndCreate_timeBetween(Long prodouct_id,Date from,Date to);

	@Query("{'id':{$nin:?0},'name':{$regex:?1},'product_id':?2,create_time:{$gte:?3,$lte:?4}}")
	Page<Device> findByNameNotIn(List<Long> ids,String name,Long prodouct_id,Date from,Date to,Pageable page);

	@Query("{'id':{$nin:?0},'product_id':?1}")
	List<Device> findByIdNotInAndProduct_id(List<Long> ids,Long product_id);

	@Query("{'id':{$in:?0},create_time:{$gte:?1,$lte:?2}}")
	Page<Device> findByIdInAndAndCreate_timeIsBetween(List<Long> ids,Date from,Date to,Pageable page);

	@Query("{'id':{$in:?0}}")
	List<Device> findByIdIn(List<Long> ids);

	@Query("{device_sn:?0,product_id:?1}")
	Optional<Device> findByDevice_sn(String device_sn,Long prodouct_id);
}


