package com.epay.xj.properties;

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

}
