package com.epay.xj.service;

import com.epay.xj.domain.CertNo;
import com.epay.xj.domain.TradeDetail;

public interface IAnalysisServer {

	public void insert(CertNo o);
	public void insert(TradeDetail o);
	void readLineAndSave(String fp);
	
	
}
