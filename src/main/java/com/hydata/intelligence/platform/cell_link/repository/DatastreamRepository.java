package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.Datastream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

import static org.hibernate.jpa.QueryHints.HINT_COMMENT;

/**
 * @ClassName DatastreamRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/4 17:49
 * @Version
 */
public interface DatastreamRepository extends JpaRepository<Datastream, Long> {
    @QueryHints(value = {@QueryHint(name = HINT_COMMENT, value = "a query for pageable")})
    @Query("select s from Datastream s where s.device.deviceId=?1 and s.datastreamName like concat('%' ,?2,'%') ")
    Page<Datastream> findByDeviceAndDatastreamName(Long deviceId, String datastreamName, Pageable page);

    @Query("select s from Datastream s where s.device.deviceId=?1 and s.datastreamName like concat('%' ,?2,'%') ")
    List<Datastream> findListByDeviceAndDatastreamName(Long deviceId, String datastreamName);

    @Query("select s from Datastream s where s.device.deviceId=?1 and s.datastreamName=?2")
    Optional<Datastream> findByDeviceAndDatastreamName(Long deviceId,String datastreamName);
}
