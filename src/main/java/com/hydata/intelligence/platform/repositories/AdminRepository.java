package com.hydata.intelligence.platform.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hydata.intelligence.platform.dto.Admin;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:08:11
 */
public interface AdminRepository extends JpaRepository<Admin, Integer> {
	@Query("select a from Admin a where a.name = ?1 and a.pwd = ?2")
	Optional<Admin> findByNameAndPwd(String name,String pwd);
	
	@Query("select a from Admin a where a.name = ?1")
	Optional<Admin> findByName(String name);

}

