package com.hydata.intelligence.platform.cell_link.entity.dict;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @ClassName Symbol
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/29 13:39
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class Symbol {
    @Id
    private Integer symbolId;
    private String conditionSymbol;
}
