package com.hydata.intelligence.platform.repositories;

import java.util.List;
import java.util.Optional;

import com.hydata.intelligence.platform.dto.TriggerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * @author pyt
 * @createTime 2018年10月24日下午2:07:07
 */
public interface TriggerRepository extends JpaRepository<TriggerModel, Integer> {
	@Query("select t from TriggerModel t where t.id = ?1 and t.modeValue = ?2")
	Optional<TriggerModel> findByIdAndEmail(Integer id,String email);

	@Query("select t from TriggerModel t where t.productId = ?1")
	List<TriggerModel> findByProductId(Integer productId);

	@Query("select t from TriggerModel t where t.deviceId = ?1")
	List<TriggerModel> findByDeviceId(Integer deviceId);

}