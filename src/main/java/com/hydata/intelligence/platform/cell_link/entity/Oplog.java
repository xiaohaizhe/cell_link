package com.hydata.intelligence.platform.cell_link.entity;

import com.hydata.intelligence.platform.cell_link.entity.dict.OplogType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName OpLog
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/9 10:32
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class Oplog {
    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.hydata.intelligence.platform.cell_link.utils.IdGenerator",
            parameters = {})
    private Long oplogId;
    @ManyToOne(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "userId",name = "userId")
    private User user;
    @OneToOne
    @JoinColumn(referencedColumnName = "otId", nullable = false,name = "otId")
    private OplogType oplogType;
    private String otType;  //操作类型
    private String msg;     //操作详情
    @CreationTimestamp
    @Column(updatable = false)
    private Date created;   //创建时间
}
