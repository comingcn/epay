package com.epay.xj.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
//@Table(name="trade_detail")//设置数据库中表名字
public class TradeDetailDO implements Comparable<TradeDetailDO>{

	@Id
	private BigDecimal ID;//记录ID
	private String IDCARD;//身份证号码
	private String ACCOUNT_NO;//银行卡号码
	private Integer MER_TYPE;//商户类型
	private String SOURCE_MERNO;//商户编码
	private Timestamp CREATE_TIME;//创建时间
	private Character SF_TYPE;//收付类型
	private BigDecimal AMOUNT;//金额
	private String RETURN_CODE;//返回码
	public BigDecimal getID() {
		return ID;
	}
	public void setID(BigDecimal iD) {
		ID = iD;
	}
	public String getIDCARD() {
		return IDCARD;
	}
	public void setIDCARD(String iDCARD) {
		IDCARD = iDCARD;
	}
	public String getACCOUNT_NO() {
		return ACCOUNT_NO;
	}
	public void setACCOUNT_NO(String aCCOUNT_NO) {
		ACCOUNT_NO = aCCOUNT_NO;
	}
	public Integer getMER_TYPE() {
		return MER_TYPE;
	}
	public void setMER_TYPE(Integer mER_TYPE) {
		MER_TYPE = mER_TYPE;
	}
	public String getSOURCE_MERNO() {
		return SOURCE_MERNO;
	}
	public void setSOURCE_MERNO(String sOURCE_MERNO) {
		SOURCE_MERNO = sOURCE_MERNO;
	}
	
	
	public Timestamp getCREATE_TIME() {
		return CREATE_TIME;
	}
	public void setCREATE_TIME(Timestamp cREATE_TIME) {
		CREATE_TIME = cREATE_TIME;
	}
	public Character getSF_TYPE() {
		return SF_TYPE;
	}
	public void setSF_TYPE(Character sF_TYPE) {
		SF_TYPE = sF_TYPE;
	}
	public BigDecimal getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(BigDecimal aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public String getRETURN_CODE() {
		return RETURN_CODE;
	}
	public void setRETURN_CODE(String rETURN_CODE) {
		RETURN_CODE = rETURN_CODE;
	}
	@Override
	public String toString() {
		return "TradeDetailDO [ID=" + ID + ", IDCARD=" + IDCARD + ", ACCOUNT_NO=" + ACCOUNT_NO + ", MER_TYPE="
				+ MER_TYPE + ", SOURCE_MERNO=" + SOURCE_MERNO + ", CREATE_TIME=" + CREATE_TIME + ", SF_TYPE=" + SF_TYPE
				+ ", AMOUNT=" + AMOUNT + ", RETURN_CODE=" + RETURN_CODE + "]";
	}
	@Override
	public int compareTo(TradeDetailDO o) {
		return this.CREATE_TIME.compareTo(o.getCREATE_TIME());
	}
	
}
