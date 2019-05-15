package com.hydata.intelligence.platform.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.hydata.intelligence.platform.dto.DatastreamModel;
import com.hydata.intelligence.platform.dto.DeviceDatastream;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:15:11
 */
public interface DeviceDatastreamRepository extends JpaRepository<DeviceDatastream, Long> ,JpaSpecificationExecutor<DeviceDatastream>{

	@Query("select dd from DeviceDatastream dd where dd.device_id = ?1 and dd.dm_name = ?2")
	Optional<DeviceDatastream> findByDeviceIdAndDm_name(Long device_id,String dm_name);
	
	@Query("select dd from DeviceDatastream dd where dd.device_id = ?1")
	List<DeviceDatastream> findByDeviceId(Long device_id);

	@QueryHints(value = {@QueryHint(name = HINT_COMMENT ,value= "a query for pageable")})
	@Query("select dd from DeviceDatastream dd where dd.device_id in (?1) and dd.dm_name like concat('%' ,?2,'%')")
	Page<DeviceDatastream> findByDevice_idInAndAndDm_nameLike(List<Long> ids,String name,Pageable page);

	@QueryHints(value = {@QueryHint(name = HINT_COMMENT ,value= "a query for pageable")})
	@Query("select dd from DeviceDatastream dd where (dd.device_id in (?1) and dd.dm_name like concat('%' ,?2,'%')) or dd.device_id in (?3)")
	Page<DeviceDatastream> findByDevice_idInAndAndDm_nameLike(List<Long> ids,String name,List<Long> ids1,Pageable page);

	@QueryHints(value = {@QueryHint(name = HINT_COMMENT ,value= "a query for pageable")})
	@Query("select dd from DeviceDatastream dd where dd.device_id in (?1)")
	Page<DeviceDatastream> findByDevice_idIn(List<Long> ids,Pageable page);

}

