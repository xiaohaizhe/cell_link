package com.hydata.intelligence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.Trigger;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:07:07
 */
public interface TriggerRepository extends JpaRepository<Trigger, Integer> {

}

