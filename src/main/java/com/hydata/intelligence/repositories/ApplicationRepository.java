package com.hydata.intelligence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.Application;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:08:51
 */
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

}

