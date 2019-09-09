package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.dict.OplogType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName OplogTypeRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/9 11:04
 * @Version
 */
public interface OplogTypeRepository extends JpaRepository<OplogType,Integer> {
}
