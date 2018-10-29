package com.hydata.intelligence.platform.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hydata.intelligence.platform.dto.User;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:22:59
 */
public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("select u from User u where u.name = ?1")
	Optional<User> findByName(String name);
}

