package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.Datapoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @ClassName DatapointRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/5 16:48
 * @Version
 */
public interface DatapointRepository extends MongoRepository<Datapoint, String> {
    @Query("{datastreamId:?0,created:{$gte:?1,$lte:?2}}")
    List<Datapoint> findByDatastreamIdAndCreatedBetween(Long dd_id, Date from, Date to);

    @org.springframework.data.jpa.repository.Query("select dp from Datapoint dp where dp.dd_id = ?1 order by ?#{#page}")
    Page<Datapoint> findByDd_id(Long dd_id, Pageable page);

}
