package com.hydata.intelligence.platform.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hydata.intelligence.platform.dto.Application;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:08:51
 */
public interface ApplicationRepository extends JpaRepository<Application, Long> {
	@Query("select a from Application a where a.productId = ?1")
	List<Application> findByProduct_id(long product_id);
	
	@Query("select a from Application a where a.productId = ?1 and a.applicationType = ?2")
	List<Application> findByProductIdAndType(long productId,Integer type);
	
	@Query("select a from Application a where a.productId = ?1 and a.applicationType = 0 and a.name = ?2")
	List<Application> findByProduct_idAndName1(long product_id,String name);
	
	@Query("select a from Application a where a.productId = ?1 and a.applicationType = 1 and a.name = ?2")
	List<Application> findByProduct_idAndName2(long product_id,String name);
	
	@Query("select a from Application a where a.productId = ?1 and a.applicationType = 0 and a.name like concat('%' ,?2,'%')")
	List<Application> findByProduct_idAndLikeName1(long product_id,String name);
	
	@Query("select a from Application a where a.productId = ?1 and a.applicationType = 1 and a.name like concat('%' ,?2,'%')")
	List<Application> findByProduct_idAndLikeName2(long product_id,String name);
	
	@Query("select a from Application a where a.applicationType = ?2")
	List<Application> findByType(Integer type);
}

