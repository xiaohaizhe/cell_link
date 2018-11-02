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

import com.hydata.intelligence.platform.dto.User;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:22:59
 */
public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("select u from User u where u.name = ?1")
	Optional<User> findByName(String name);
	
	@QueryHints(value = {@QueryHint(name = HINT_COMMENT ,value= "a query for pageable")})
	@Query("select u from User u where u.name like concat('%' ,?1,'%')")
	Page<User> findByName(String name,Pageable page);
	
	@Query("select u from User u where u.name = ?1 and u.pwd = ?2")
	Optional<User> findByNameAndPWD(String name,String pwd);
	
	@Query("select u from User u where u.id = ?1 and u.phone = ?2")
	Optional<User> findByIdAndPhone(Integer id,String phone);
	
	@Query("select u from User u where u.id = ?1 and u.email = ?2")
	Optional<User> findByIdAndEmail(Integer id,String email);
}

