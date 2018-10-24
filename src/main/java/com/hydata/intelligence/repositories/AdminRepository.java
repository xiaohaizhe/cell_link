package com.hydata.intelligence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.Admin;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:08:11
 */
public interface AdminRepository extends JpaRepository<Admin, Integer> {

}

