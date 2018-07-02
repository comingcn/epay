/**   
 * @Title: BindLog.java 
 * @Package: com.zshy.datacleaning.domain 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018 北京中数合一科技有限公司
 */
package com.zshy.datacleaning.domain;

/**
 * @Description:
 * @author LZG
 * @date 2018年07月02日
 */
public class BindLog {

	private String txn_date; // 交易日期
	private String txn_seq_id; // 交易流水号
	private String cert_no; // 身份证号
	private String card_no; // 绑定的银行卡号
	private String dc_type; // 借贷标识
	private String mer_id; // 商户号
	private int mer_type; // 商户类型
	private String return_code; // 返回码

	/**
	 * @Description: get txn_date
	 * @return txn_date
	 */
	public String getTxn_date() {
		return txn_date;
	}
	/**
	 * @Description: get txn_seq_id
	 * @return txn_seq_id
	 */
	public String getTxn_seq_id() {
		return txn_seq_id;
	}
	/**
	 * @Description: get cert_no
	 * @return cert_no
	 */
	public String getCert_no() {
		return cert_no;
	}
	/**
	 * @Description: get card_no
	 * @return card_no
	 */
	public String getCard_no() {
		return card_no;
	}
	/**
	 * @Description: get dc_type
	 * @return dc_type
	 */
	public String getDc_type() {
		return dc_type;
	}
	/**
	 * @Description: get mer_id
	 * @return mer_id
	 */
	public String getMer_id() {
		return mer_id;
	}
	/**
	 * @Description: get mer_type
	 * @return mer_type
	 */
	public int getMer_type() {
		return mer_type;
	}
	/**
	 * @Description: get return_code
	 * @return return_code
	 */
	public String getReturn_code() {
		return return_code;
	}
	/**
	 * @Descprtion: set txn_date
	 * @param txn_date
	 */
	public void setTxn_date(String txn_date) {
		this.txn_date = txn_date;
	}
	/**
	 * @Descprtion: set txn_seq_id
	 * @param txn_seq_id
	 */
	public void setTxn_seq_id(String txn_seq_id) {
		this.txn_seq_id = txn_seq_id;
	}
	/**
	 * @Descprtion: set cert_no
	 * @param cert_no
	 */
	public void setCert_no(String cert_no) {
		this.cert_no = cert_no;
	}
	/**
	 * @Descprtion: set card_no
	 * @param card_no
	 */
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	/**
	 * @Descprtion: set dc_type
	 * @param dc_type
	 */
	public void setDc_type(String dc_type) {
		this.dc_type = dc_type;
	}
	/**
	 * @Descprtion: set mer_id
	 * @param mer_id
	 */
	public void setMer_id(String mer_id) {
		this.mer_id = mer_id;
	}
	/**
	 * @Descprtion: set mer_type
	 * @param mer_type
	 */
	public void setMer_type(int mer_type) {
		this.mer_type = mer_type;
	}
	/**
	 * @Descprtion: set return_code
	 * @param return_code
	 */
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	/**
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		return "BindLog [txn_date=" + txn_date + ", txn_seq_id=" + txn_seq_id
				+ ", cert_no=" + cert_no + ", card_no=" + card_no + ", dc_type="
				+ dc_type + ", mer_id=" + mer_id + ", mer_type=" + mer_type
				+ ", return_code=" + return_code + "]";
	}

}
