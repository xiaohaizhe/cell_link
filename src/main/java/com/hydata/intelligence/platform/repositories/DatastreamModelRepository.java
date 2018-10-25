package com.hydata.intelligence.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hydata.intelligence.platform.dto.DatastreamModel;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:05:47
 */
@Repository
public interface DatastreamModelRepository extends JpaRepository<DatastreamModel, Integer> {
}

