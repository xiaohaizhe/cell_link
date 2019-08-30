package com.hydata.intelligence.platform.cell_link.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @ClassName AppDatastream
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/29 14:27
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class AppDatastream {
    @Id
    private Long adId;
    @ManyToOne(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "acId",name = "acId")
    private AppChart appChart;
    private Long deviceId;
    private String deviceName;
    @OneToOne
    @JoinColumn(referencedColumnName = "datastreamId",name = "datastreamId")
    private Datastream datastream;
}