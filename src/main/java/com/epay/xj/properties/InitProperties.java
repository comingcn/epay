package com.epay.xj.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "initProperties")
public class InitProperties {

	private String fPathInput;

    private String inputHeaderMtd;
    
    private String TradeDetail;

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

	public String getTradeDetail() {
		return TradeDetail;
	}

	public void setTradeDetail(String tradeDetail) {
		TradeDetail = tradeDetail;
	}
    
}
