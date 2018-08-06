package com.epay.xj.domain;

import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.Id;

//@Entity
//@Table(name="bind_card_log")//设置数据库中表名字
public class BindCardLog implements Comparable<BindCardLog>{

	private String TXN_DATE;          //VARCHAR(8),	交易日期
	@Id
	private String TXN_SEQ_ID;        //VARCHAR(8),	交易序列号
	private String CERT_NO;           //VARCHAR(256),		身份证号
	private String ENC;           //VARCHAR(256),		银行卡号
	private String DC_TYPE;           //VARCHAR(1),		借贷类型(0：借记卡，1：贷记卡)
	private String APP_SYS_ID;            //VARCHAR(5),		商户ID	
	private int MER_TYPE;          //INT,				商户类型
	private String VALID_STAT;       //VARCHAR(4)		交易状态码
	public String getTXN_DATE() {
		return TXN_DATE;
	}
	public void setTXN_DATE(String tXN_DATE) {
		TXN_DATE = tXN_DATE;
	}
	public String getTXN_SEQ_ID() {
		return TXN_SEQ_ID;
	}
	public void setTXN_SEQ_ID(String tXN_SEQ_ID) {
		TXN_SEQ_ID = tXN_SEQ_ID;
	}
	public String getCERT_NO() {
		return CERT_NO;
	}
	public void setCERT_NO(String cERT_NO) {
		CERT_NO = cERT_NO;
	}
	public String getENC() {
		return ENC;
	}
	public void setENC(String eNC) {
		ENC = eNC;
	}
	public String getDC_TYPE() {
		return DC_TYPE;
	}
	public void setDC_TYPE(String dC_TYPE) {
		DC_TYPE = dC_TYPE;
	}
	public String getAPP_SYS_ID() {
		return APP_SYS_ID;
	}
	public void setAPP_SYS_ID(String aPP_SYS_ID) {
		APP_SYS_ID = aPP_SYS_ID;
	}
	public int getMER_TYPE() {
		return MER_TYPE;
	}
	public void setMER_TYPE(int mER_TYPE) {
		MER_TYPE = mER_TYPE;
	}
	public String getVALID_STAT() {
		return VALID_STAT;
	}
	public void setVALID_STAT(String vALID_STAT) {
		VALID_STAT = vALID_STAT;
	}

	@Override
	public int compareTo(BindCardLog o) {
		// TODO Auto-generated method stub
		return this.getTXN_DATE().compareTo(o.getTXN_DATE());
	}
}
