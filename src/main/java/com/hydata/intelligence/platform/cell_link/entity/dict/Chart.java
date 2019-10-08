package com.hydata.intelligence.platform.cell_link.entity.dict;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @ClassName Chart
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/29 14:01
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class Chart implements Serializable {
    @Id
    private Integer chartId;
    private String chartName;
}
