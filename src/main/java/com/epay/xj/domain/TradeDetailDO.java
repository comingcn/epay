package com.epay.xj.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.epay.xj.utils.DateUtils;

//@Entity
//@Table(name="trade_detail")//设置数据库中表名字
public class TradeDetailDO implements Comparable<TradeDetailDO>{

	private BigDecimal ID;
//	@Id
	private String IDCARD;
	private String ACCOUNT_NO;
	private Integer MER_TYPE;
	private String SOURCE_MERNO;
	private Timestamp CREATE_TIME;
	private String SF_TYPE;
	private BigDecimal AMOUNT;
	private String RETURN_CODE;
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
	public String getSF_TYPE() {
		return SF_TYPE;
	}
	public void setSF_TYPE(String sF_TYPE) {
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
		return this.CREATE_TIME.getNanos() -o.getCREATE_TIME().getNanos();
	}
	
	
	
}
