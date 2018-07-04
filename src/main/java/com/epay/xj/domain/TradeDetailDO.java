package com.epay.xj.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="trade_detail")//设置数据库中表名字
public class TradeDetailDO {

	@Id
	private String txtSeqId;
	private String certNo;
	private String cardNo;
	private String merType;
	private String merId;
	private String txtDate;
	private String sfType;
	private BigDecimal amout;
	private String returnCode;
	
	public String getTxtSeqId() {
		return txtSeqId;
	}
	public void setTxtSeqId(String txtSeqId) {
		this.txtSeqId = txtSeqId;
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
	public String getMerType() {
		return merType;
	}
	public void setMerType(String merType) {
		this.merType = merType;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getTxtDate() {
		return txtDate;
	}
	public void setTxtDate(String txtDate) {
		this.txtDate = txtDate;
	}
	public BigDecimal getAmout() {
		return amout;
	}
	public void setAmout(BigDecimal amout) {
		this.amout = amout;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	
	public String getSfType() {
		return sfType;
	}
	public void setSfType(String sfType) {
		this.sfType = sfType;
	}
	@Override
	public String toString() {
		return "TradeDetailDO [txtSeqId=" + txtSeqId + ", certNo=" + certNo + ", cardNo=" + cardNo + ", merType="
				+ merType + ", merId=" + merId + ", txtDate=" + txtDate + ", sfType=" + sfType + ", amout=" + amout
				+ ", returnCode=" + returnCode + "]";
	}
	
}
