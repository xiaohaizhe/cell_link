package com.hydata.intelligence.platform.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
@Entity
public class ApplicationChartDatastream{
	@Id
	@GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator",strategy = "com.example.spring_data_jpa_demo.utils.IdGenerator",
                        parameters = {})
    private long id;

    private long acId;

    private long ddId;

    

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public long getAcId() {
		return acId;
	}



	public void setAcId(long acId) {
		this.acId = acId;
	}



	public long getDdId() {
		return ddId;
	}



	public void setDdId(long ddId) {
		this.ddId = ddId;
	}



	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", acId=").append(acId);
        sb.append(", ddId=").append(ddId);
        sb.append("]");
        return sb.toString();
    }
}