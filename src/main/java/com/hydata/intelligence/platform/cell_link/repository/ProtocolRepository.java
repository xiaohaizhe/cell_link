package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.dict.Protocol;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName ProtocolRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/28 16:11
 * @Version
 */
public interface ProtocolRepository extends JpaRepository<Protocol, Integer> {
}
