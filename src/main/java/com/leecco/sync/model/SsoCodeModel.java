package com.leecco.sync.model;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="t_sso_code")
public class SsoCodeModel implements Serializable{	
	@Column(name="FID")
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="FCode")
	private String code;
	@Column(name="FUrl")
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