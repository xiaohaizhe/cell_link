package com.hydata.intelligence.platform.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;
@Entity
public class CmdLogs{
	@Id
	@GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator",strategy = "com.hydata.intelligence.platform.utils.IdGenerator",
                        parameters = {})
    private long id;

    private long device_id;

    private String msg;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    private long userId;

    private long productId;

    private int res_code; //表示响应状态，0：正常， 1：命令已发往设备

    private String res_msg;

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getDevice_id() {
		return device_id;
	}

	public void setDevice_id(Long device_id) {
		this.device_id = device_id;
	}

	public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public int getRes_code(){return res_code;}

    public void setRes_code(int res_code){this.res_code = res_code;}

    public String getRes_msg(){return res_msg;}

    public void setRes_msg(String res_msg){this.res_msg = res_msg;}


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", device_id=").append(device_id);
        sb.append(", msg=").append(msg);
        sb.append(", sendTime=").append(sendTime);
        sb.append(", userId=").append(userId);
        sb.append(", productId=").append(productId);
        sb.append(", res_code=").append(res_code);
        sb.append(", res_msg=").append(res_msg);
        sb.append("]");
        return sb.toString();
    }
}