package com.hydata.intelligence.platform.cell_link.entity.dict;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @ClassName EventType
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/22 11:46
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class EventLevel {
    @Id
    private Integer elId;
    private String eventLevel;   //事件等级:严重、警告、提示
}
