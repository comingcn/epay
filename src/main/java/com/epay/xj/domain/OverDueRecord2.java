package com.epay.xj.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 逾期中间变量
 * @author dell
 *
 */
//@Entity
//@Table(name="CP_ODS.P1055_ODS_YQ_DTL2")
public class OverDueRecord2 {

//	@Id
	private String CERT_NO; //身份证号
	private Integer TYPE_ID;//商户类型
	private String MER_NO;//商户编码
	private Integer STR_DAYS;//逾期开始距离指定时间天数
	private Integer END_DAYS;//逾期介绍距离指定时间天数
	private BigDecimal AMT;//金额
	private Integer OVR_DAYS;//逾期天数
	public String getCERT_NO() {
		return CERT_NO;
	}
	public void setCERT_NO(String cERT_NO) {
		CERT_NO = cERT_NO;
	}
	public Integer getTYPE_ID() {
		return TYPE_ID;
	}
	public void setTYPE_ID(Integer tYPE_ID) {
		TYPE_ID = tYPE_ID;
	}
	public String getMER_NO() {
		return MER_NO;
	}
	public void setMER_NO(String mER_NO) {
		MER_NO = mER_NO;
	}
	public Integer getSTR_DAYS() {
		return STR_DAYS;
	}
	public void setSTR_DAYS(Integer sTR_DAYS) {
		STR_DAYS = sTR_DAYS;
	}
	public Integer getEND_DAYS() {
		return END_DAYS;
	}
	public void setEND_DAYS(Integer eND_DAYS) {
		END_DAYS = eND_DAYS;
	}
	public BigDecimal getAMT() {
		return AMT;
	}
	public void setAMT(BigDecimal aMT) {
		AMT = aMT;
	}
	public Integer getOVR_DAYS() {
		return OVR_DAYS;
	}
	public void setOVR_DAYS(Integer oVR_DAYS) {
		OVR_DAYS = oVR_DAYS;
	}
	
	
	
}
