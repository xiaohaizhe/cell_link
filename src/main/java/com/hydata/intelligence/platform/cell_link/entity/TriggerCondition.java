package com.hydata.intelligence.platform.cell_link.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @ClassName TriggerCondition
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/22 13:35
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class TriggerCondition {
    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.hydata.intelligence.platform.cell_link.utils.IdGenerator",
            parameters = {})
    private Long tcId;
    @ManyToOne(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    private Event event;
    private Long deviceId;
    private String deviceName;
}
