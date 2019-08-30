package com.hydata.intelligence.platform.cell_link.entity.dict;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @ClassName Protocol
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/28 16:09
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class Protocol {
    @Id
    private Integer protocolId;
    private String protocolName;
}
