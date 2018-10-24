package com.hydata.intelligence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.Protocol;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:20:37
 */
public interface ProtocolRepository extends JpaRepository<Protocol, Integer> {

}

