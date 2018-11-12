package com.hydata.intelligence.platform.repositories;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.hydata.intelligence.platform.dto.Device;


/**
 * @author pyt
 * @createTime 2018年10月24日下午2:14:29
 */
public interface DeviceRepository extends JpaRepository<Device, Integer> ,JpaSpecificationExecutor<Device>{
	
	@QueryHints(value = {@QueryHint(name = HINT_COMMENT ,value= "a query for pageable")})
	@Query("select d from Device d where d.productId = ?1")
	Page<Device> findByProductId(Integer product_id,Pageable pageable);
	
	@QueryHints(value = {@QueryHint(name = HINT_COMMENT ,value= "a query for pageable")})
	@Query("select d from Device d where  d.name like concat('%' ,?2,'%')")
	Page<Device> findByDeviceSnOrName(Integer product_id,String deviceSnOrName,Pageable pageable);
	
	@Query("select d from Device d where d.device_sn=?2 and d.productId = ?1")
	Optional<Device> findByProductIdAndDeviceSn(Integer productId,String device_sn);
	
	@Query("select d from Device d where d.productId = ?1")
	List<Device> findByProductId(Integer product_id);
}

