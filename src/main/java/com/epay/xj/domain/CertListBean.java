package com.epay.xj.domain;

//@Entity
//@Table(name="CP_ODS.P1055_CERT_LIST")//设置数据库中表名字
public class CertListBean {

//	@Id
	private String certNo;
	private String updateDt;
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getUpdateDt() {
		return updateDt;
	}
	public void setUpdateDt(String updateDt) {
		this.updateDt = updateDt;
	}
	
	
	
}
