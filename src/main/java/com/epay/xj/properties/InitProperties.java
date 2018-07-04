package com.epay.xj.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "initProperties")
public class InitProperties {

	private String fPathInput;

	private String fPathOutput;

	private String inputHeaderMtd;

	private String tradeDetail;

	private String inputHeaderBcl;

	private String bindCardLog;
	
	private String overDueType;
	
	private String[] overDueMouth;
	
	private String[] overDueDay;
	
	private Map<String, String[]> merTypeDic = new HashMap<String, String[]>();	
	
	
	private Map<String, String[]> returnCodeDic =  new HashMap<String, String[]>();	
	
	
	public String getfPathInput() {
		return fPathInput;
	}

	public void setfPathInput(String fPathInput) {
		this.fPathInput = fPathInput;
	}

	public String getInputHeaderMtd() {
		return inputHeaderMtd;
	}

	public void setInputHeaderMtd(String inputHeaderMtd) {
		this.inputHeaderMtd = inputHeaderMtd;
	}

	public String getfPathOutput() {
		return fPathOutput;
	}

	public void setfPathOutput(String fPathOutput) {
		this.fPathOutput = fPathOutput;
	}

	public String getTradeDetail() {
		return tradeDetail;
	}

	public void setTradeDetail(String tradeDetail) {
		this.tradeDetail = tradeDetail;
	}

	public String getInputHeaderBcl() {
		return inputHeaderBcl;
	}

	public void setInputHeaderBcl(String inputHeaderBcl) {
		this.inputHeaderBcl = inputHeaderBcl;
	}

	public String getBindCardLog() {
		return bindCardLog;
	}

	public void setBindCardLog(String bindCardLog) {
		this.bindCardLog = bindCardLog;
	}

	public String getOverDueType() {
		return overDueType;
	}

	public void setOverDueType(String overDueType) {
		this.overDueType = overDueType;
	}

	public String[] getOverDueMouth() {
		return overDueMouth;
	}

	public void setOverDueMouth(String[] overDueMouth) {
		this.overDueMouth = overDueMouth;
	}

	public String[] getOverDueDay() {
		return overDueDay;
	}

	public void setOverDueDay(String[] overDueDay) {
		this.overDueDay = overDueDay;
	}

	public Map<String, String[]> getMerTypeDic() {
		return merTypeDic;
	}

	public void setMerTypeDic(Map<String, String[]> merTypeDic) {
		this.merTypeDic = merTypeDic;
	}

	public Map<String, String[]> getReturnCodeDic() {
		return returnCodeDic;
	}

	public void setReturnCodeDic(Map<String, String[]> returnCodeDic) {
		this.returnCodeDic = returnCodeDic;
	}
}
