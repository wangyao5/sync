package com.leecco.sync.model;

import java.io.Serializable;
//import java.sql.Timestamp;

//import javax.persistence.Column;

public class OrgModel implements Serializable{
//	@Column(name="FID")
	private int id;
//	@Column(name="Forg_num")
	private String org_num;
//	@Column(name="Forg_name")
	private String org_name;
//	@Column(name="Forg_pnum")
	private String pNum;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrg_num() {
		return org_num;
	}
	public void setOrg_num(String org_num) {
		this.org_num = org_num;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public String getpNum() {
		return pNum;
	}
	public void setpNum(String pNum) {
		this.pNum = pNum;
	}

	
	
}
