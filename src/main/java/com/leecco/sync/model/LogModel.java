package com.leecco.sync.model;

import java.io.Serializable;

//import javax.persistence.Column;
//import javax.persistence.Table;

//@Table(name="T_MCL_Log")
public class LogModel implements Serializable{
	//����
//	@Column(name="FID")
	private int id;
	//ͬ��ʱ��
//	@Column(name="FSynTime")
	private String synTime;
	//�ӿڱ�ʶ
//	@Column(name="FIntNo")
	private String intNo;
	//����״̬
//	@Column(name="FStatus")
	private int status;//1 ���ȫ���ɹ���2 ��?�ֳɹ� -1 ���ʧ�� 0 ���δ����
	//���ݵĲ���ֵ
//	@Column(name="FParams")
	private String params;
	//����Ľ��
//	@Column(name="FResult")
	private String result;
	//ת���󴫵ݸ���Ľ��
//	@Column(name="FMidParams")
	private String midParams;
	
	//�����ܼ�¼����
//	@Column(name="FTotalCount")
	private int totalCount;
	
	//���ݳɹ���¼����
//	@Column(name="FSucessCount")
	private int successCount;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSynTime() {
		return synTime;
	}
	public void setSynTime(String synTime) {
		this.synTime = synTime;
	}
	public String getIntNo() {
		return intNo;
	}
	public void setIntNo(String intNo) {
		this.intNo = intNo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMidParams() {
		return midParams;
	}
	public void setMidParams(String midParams) {
		this.midParams = midParams;
	}
	
	
	
	
}
