package com.hydata.intelligence.platform.cell_link.entity.dict;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @ClassName Logic
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/29 13:37
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class Logic {
    @Id
    private Integer logicId;
    private String logicValue;
}
