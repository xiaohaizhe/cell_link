package com.hydata.intelligence.platform.cell_link.entity;

import com.hydata.intelligence.platform.cell_link.entity.dict.Logic;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * @ClassName TriggerGate
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/18 14:25
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class TriggerGate {
    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.hydata.intelligence.platform.cell_link.utils.IdGenerator",
            parameters = {})
    private Long tgId;
    private short type; //0-Event;1-TriggerCondition
    @OneToOne
    @JoinColumn(name = "logicId", referencedColumnName = "logicId", nullable = false)
    private Logic logic;
    /*@OneToMany(cascade = {CascadeType.ALL}, mappedBy = "TriggerGate")
    private List<TriggerCondition> triggerConditionList;*/
}
