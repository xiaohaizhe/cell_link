package com.hydata.intelligence.platform.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;
import com.hydata.intelligence.platform.validation.emailvalidation.EmailValidation;
import com.hydata.intelligence.platform.validation.phonevalidation.PhoneValidation;
@Entity
public class User{
	@Id
	@GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator",strategy = "com.hydata.intelligence.platform.utils.IdGenerator",
                        parameters = {})
    private long id;

    private String name;

    private String pwd;

    private Integer type;
    @PhoneValidation
    private String phone;
    @EmailValidation
    private String email;

    private String defaultKey;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    private Byte isvalid;
    
    private Byte islogin;
    
    private Byte isvertifyphone;
    
    private Byte isvertifyemail;
    
    private String email_code;//邮箱验证码
    

	
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getDefaultKey() {
        return defaultKey;
    }

    public void setDefaultKey(String defaultKey) {
        this.defaultKey = defaultKey == null ? null : defaultKey.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Byte getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(Byte isvalid) {
        this.isvalid = isvalid;
    }
    
    
    public Byte getIslogin() {
		return islogin;
	}

	public void setIslogin(Byte islogin) {
		this.islogin = islogin;
	}
	

	public Byte getIsvertifyphone() {
		return isvertifyphone;
	}

	public void setIsvertifyphone(Byte isvertifyphone) {
		this.isvertifyphone = isvertifyphone;
	}

	public Byte getIsvertifyemail() {
		return isvertifyemail;
	}

	public void setIsvertifyemail(Byte isvertifyemail) {
		this.isvertifyemail = isvertifyemail;
	}
	
	

	public String getEmail_code() {
		return email_code;
	}

	public void setEmail_code(String email_code) {
		this.email_code = email_code;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", pwd=").append(pwd);
        sb.append(", type=").append(type);
        sb.append(", phone=").append(phone);
        sb.append(", email=").append(email);
        sb.append(", defaultKey=").append(defaultKey);
        sb.append(", createTime=").append(createTime);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", isvalid=").append(isvalid);
        sb.append(",islogin=").append(islogin);
        sb.append(",isvertifyphone=").append(isvertifyphone);
        sb.append(",isvertifyemail=").append(isvertifyemail);
        sb.append("]");
        return sb.toString();
    }
}