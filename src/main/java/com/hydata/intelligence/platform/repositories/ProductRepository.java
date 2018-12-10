package com.hydata.intelligence.platform.repositories;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.hydata.intelligence.platform.dto.Product;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:19:09
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {
	@Query("select p from Product p where p.userId = ?1 and p.name = ?2")
	Optional<Product> findByUserIdAndName(Integer user_id,String name);
	
	@QueryHints(value = {@QueryHint(name = HINT_COMMENT ,value= "a query for pageable")})
	@Query("select p from Product p where p.userId = ?1")
	Page<Product> queryByUserId(Integer user_id,Pageable pageable);
	
	@Query("select p from Product p where p.userId = ?1")
	List<Product> findByUserId(Integer user_id);
	
	@Query("select p from Product p where p.protocolId = ?1")
	List<Product> findByProtocolId(Integer protocolId);

	List<Product> findAllMqtt();

}

