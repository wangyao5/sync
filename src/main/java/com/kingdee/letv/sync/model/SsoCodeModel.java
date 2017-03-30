package com.kingdee.letv.sync.model;

import java.io.Serializable;

//import javax.persistence.Column;
//import javax.persistence.Table;

//@Table(name="t_sso_code")
public class SsoCodeModel implements Serializable{	
//	@Column(name="FID")
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
//	@Column(name="FCode")
	private String code;
//	@Column(name="FUrl")
	private String url;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
