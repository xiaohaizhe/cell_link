package com.hydata.intelligence.platform.cell_link.repository;

import com.hydata.intelligence.platform.cell_link.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName AppRepository
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/3 11:52
 * @Version
 */
public interface AppRepository extends JpaRepository<App,Long> {
}
