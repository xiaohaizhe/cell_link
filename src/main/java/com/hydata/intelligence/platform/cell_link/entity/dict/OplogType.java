package com.hydata.intelligence.platform.cell_link.entity.dict;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @ClassName OplogType
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/9 10:36
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class OplogType {
    @Id
    private Integer otId;
    private String type;
}
