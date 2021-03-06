package com.hydata.intelligence.platform.dto;

import javax.persistence.*;

@Entity
public class Admin{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;

    private String pwd;

    private String email;

    private String phone;
    
    private byte islogin;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }
    
    

    public byte getIslogin() {
		return islogin;
	}

	public void setIslogin(byte islogin) {
		this.islogin = islogin;
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
        sb.append(", email=").append(email);
        sb.append(", phone=").append(phone);
        sb.append(", islogin=").append(islogin);
        sb.append("]");
        return sb.toString();
    }
}