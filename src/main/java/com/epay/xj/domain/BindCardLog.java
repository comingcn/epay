package com.epay.xj.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="bind_card_log")//设置数据库中表名字
public class BindCardLog {

	private String txnDate;          //VARCHAR(8),	交易日期
	@Id
	private String txnSeqId;        //VARCHAR(8),	交易序列号
	private String certNo;           //VARCHAR(256),		身份证号
	private String cardNo;           //VARCHAR(256),		银行卡号
	private String dcType;           //VARCHAR(1),		借贷类型(0：借记卡，1：贷记卡)
	private String merId;            //VARCHAR(5),		商户ID	
	private int merType;          //INT,				商户类型
	private String returnCode;       //VARCHAR(4)		交易状态码
	public String getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	public String getTxnSeqId() {
		return txnSeqId;
	}
	public void setTxnSeqId(String txnSeqId) {
		this.txnSeqId = txnSeqId;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getDcType() {
		return dcType;
	}
	public void setDcType(String dcType) {
		this.dcType = dcType;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public int getMerType() {
		return merType;
	}
	public void setMerType(int merType) {
		this.merType = merType;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	@Override
	public String toString() {
		return "BindCardLog [txnDate=" + txnDate + ", txnSeqId=" + txnSeqId + ", certNo=" + certNo + ", cardNo="
				+ cardNo + ", dcType=" + dcType + ", merId=" + merId + ", merType=" + merType + ", returnCode="
				+ returnCode + "]";
	}

}
