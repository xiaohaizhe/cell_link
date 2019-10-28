package com.hydata.intelligence.platform.cell_link.entity;

import com.hydata.intelligence.platform.cell_link.validation.locationvalidation.LocationValidation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @ClassName Device
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/22 13:45
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class Device {
    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.hydata.intelligence.platform.cell_link.utils.IdGenerator",
            parameters = {})
    private Long deviceId;
    private Long userId;
    private Long scenarioId;
    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "dgId", name = "dgId")
    private DeviceGroup deviceGroup;
    @NotBlank
    @NotNull
    @Length(min = 2,max = 10,message = "设备名称字符2~10")
    private String deviceName;  //设备名称
    @NotBlank
    @NotNull
    @Size(max = 20)
    private String devicesn;    //设备鉴权信息
    @LocationValidation(min = 73.66f, max = 135.06f)
    private Float longitude;    //经度
    @LocationValidation(min = 3.86f, max = 53.55f)
    private Float latitude;     //纬度
    @Length(max = 100,message = "设备描述字符不超过100")
    private String description; //设备描述
    private Integer status;     //设备状态,1-正常

    @CreationTimestamp
    @Column(updatable = false)
    private Date created;   //创建时间
    @UpdateTimestamp
    private Date modified;  //修改时间

    @OneToMany(mappedBy = "device")
    private List<Datastream> datastreamList;

}
