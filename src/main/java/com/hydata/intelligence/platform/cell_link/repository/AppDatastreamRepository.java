package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.AppDatastream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @ClassName AppDatastreamRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/4 18:02
 * @Version
 */
public interface AppDatastreamRepository extends JpaRepository<AppDatastream, Long> {
    @Modifying
    @Query("delete from AppDatastream s where s.adId = ?1")
    int deleteByAdId(Long adId);

    @Query("select s from AppDatastream s where s.adId=?1")
    List<AppDatastream> findByAcId(Long acId);


}
