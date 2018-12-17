package com.hydata.intelligence.platform.repositories;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;

import com.hydata.intelligence.platform.dto.DeviceTrigger;
import com.hydata.intelligence.platform.dto.TriggerModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;


/**
 * @author pyt
 * @createTime 2018年10月24日下午2:07:07
 */
public interface TriggerRepository extends JpaRepository<TriggerModel, Integer> {
	@Query("select t from TriggerModel t where t.id = ?1 and t.modeValue = ?2")
	Optional<TriggerModel> findByIdAndEmail(Integer id,String email);

	@Query("select t from TriggerModel t where t.productId = ?1")
	List<TriggerModel> findByProductId(Integer productId);

	@Query("select t from TriggerModel t where t.device_sn = ?1")
	List<TriggerModel> findByDeviceSn(String device_sn);
	
	@QueryHints(value = {@QueryHint(name = HINT_COMMENT ,value= "a query for pageable")})
	@Query("select t from TriggerModel t where t.productId = ?1 and t.name like concat('%' ,?2,'%')")
	Page<TriggerModel> queryByProductIdAndName(Integer product_id,String name,Pageable pageable);

}