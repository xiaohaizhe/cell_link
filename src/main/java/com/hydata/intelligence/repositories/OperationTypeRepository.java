package com.hydata.intelligence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.OperationType;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:18:21
 */
public interface OperationTypeRepository extends JpaRepository<OperationType, Integer> {

}

