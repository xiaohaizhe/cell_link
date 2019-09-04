package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.dict.Chart;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName ChartRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/4 17:14
 * @Version
 */
public interface ChartRepository extends JpaRepository<Chart,Integer> {
}
