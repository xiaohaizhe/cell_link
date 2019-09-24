package com.hydata.intelligence.platform.cell_link.entity;

import com.hydata.intelligence.platform.cell_link.entity.dict.Logic;
import com.hydata.intelligence.platform.cell_link.entity.dict.Symbol;
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
    @JoinColumn(name = "eventId",referencedColumnName = "eventId")
    private Event event;
    @ManyToOne(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(name = "tgId",referencedColumnName = "tgId")
    private TriggerGate triggerGate;
    private Long deviceId;
    private String deviceName;
    private Long datastreamId;
    private String datastreamName;

    @OneToOne
    @JoinColumn(name = "symbolId", referencedColumnName = "symbolId", nullable = false)
    private Symbol symbol;
    private Float threshold;
}
