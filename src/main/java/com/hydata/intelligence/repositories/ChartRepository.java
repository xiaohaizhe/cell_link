package com.hydata.intelligence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.Chart;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:12:22
 */
public interface ChartRepository extends JpaRepository<Chart, Integer> {

}

