package com.hydata.intelligence.platform.repositories;

import java.util.List;

import javax.persistence.QueryHint;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.hydata.intelligence.platform.dto.DatastreamModel;
import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:05:47
 */
@Repository
public interface DatastreamModelRepository extends JpaRepository<DatastreamModel, Long> ,JpaSpecificationExecutor<DatastreamModel>{
	@Query("select d from DatastreamModel d where d.productId = ?1 and d.name = ?2")
	List<DatastreamModel> findByProductIdAndName(long product_id,String name);
	
	@QueryHints(value = {@QueryHint(name = HINT_COMMENT ,value= "a query for pageable")})
	@Query("select d from DatastreamModel d where d.productId = ?1")
	Page<DatastreamModel> queryByProductId(long product_id,Pageable pageable);

	@Query("select d from DatastreamModel d where d.productId = ?1")
	List<DatastreamModel> findByProductId(long product_id);

}

