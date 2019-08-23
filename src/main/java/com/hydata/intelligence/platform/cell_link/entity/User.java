package com.hydata.intelligence.platform.cell_link.entity;

import com.hydata.intelligence.platform.cell_link.validation.emailvalidation.EmailValidation;
import com.hydata.intelligence.platform.cell_link.validation.phonevalidation.PhoneValidation;
import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @ClassName User
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/21 11:08
 * @Version
 */
@Data
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.hydata.intelligence.platform.cell_link.utils.IdGenerator",
            parameters = {})
    private Long userId;
    @NotNull
    @NotBlank
    private String name;    //用户名
    @NotNull
    @NotBlank
    private String pwd;     //用户密码
    private Integer type;   //用户类型：0-管理员，1-普通用户
    @PhoneValidation
    private String phone;   //用户手机号
    @EmailValidation
    private String email;   //用户邮箱
    private String emailCode;  //邮箱验证码
    @CreationTimestamp
    @Column(updatable = false)
    private Date created;   //创建时间
    @UpdateTimestamp
    private Date modified;  //修改时间
    private Byte isPwdModified;     //初始密码是否修改
    private Byte isVertifyPhone;    //手机号是否验证:0-未验证，1-已验证
    private Byte isVertifyEmail;    //邮箱是否验证:0-未验证，1-已验证
    private Integer status;     //用户状态：0-禁用，1-正常
    private Byte isRemember;    //用户是否记住密码:0-不记,1-记住

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "user")
    private List<Scenario> scenarioList;


}
