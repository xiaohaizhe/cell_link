package com.hydata.intelligence.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.User;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:22:59
 */
public interface UserRepository extends JpaRepository<User, Integer> {

}

