package com.kingdee.letv.sync.model;

import java.io.Serializable;
import java.sql.Timestamp;

//import javax.persistence.Column;
//import javax.persistence.Table;

//@Table(name="T_MCL_SynTime")
public class SynTimeModel implements Serializable{
//	@Column(name="FID")
	private int id;
//	@Column(name="FIntNo")
	private String intNo;
//	@Column(name="FSynTime")
	private Timestamp synTime;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIntNo() {
		return intNo;
	}
	public void setIntNo(String intNo) {
		this.intNo = intNo;
	}
	public Timestamp getSynTime() {
		return synTime;
	}
	public void setSynTime(Timestamp synTime) {
		this.synTime = synTime;
	}
	
	
	
	
	
}
