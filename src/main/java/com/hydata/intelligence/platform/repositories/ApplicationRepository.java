package com.hydata.intelligence.platform.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hydata.intelligence.platform.dto.Application;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:08:51
 */
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
	@Query("select a from Application a where a.productId = ?1")
	List<Application> findByProduct_id(Integer product_id);
	
	@Query("select a from Application a where a.productId = ?1 and a.applicationType = ?2")
	List<Application> findByProductIdAndType(Integer productId,Integer type);
	
	@Query("select a from Application a where a.productId = ?1 and a.applicationType = 0 and a.name like concat('%' ,?2,'%')")
	List<Application> findByProduct_idAndName1(Integer product_id,String name);
	
	@Query("select a from Application a where a.productId = ?1 and a.applicationType = 1 and a.name like concat('%' ,?2,'%')")
	List<Application> findByProduct_idAndName2(Integer product_id,String name);
	
	@Query("select a from Application a where a.applicationType = ?2")
	List<Application> findByType(Integer type);
}

