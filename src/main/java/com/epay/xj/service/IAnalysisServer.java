package com.epay.xj.service;

import java.util.List;

import com.epay.xj.domain.CertNo;
import com.epay.xj.domain.TradeDetail;

public interface IAnalysisServer {

	public void insert(CertNo o);
	public void insert(TradeDetail o);
	
	public void batchInsert(List<TradeDetail> list);
	void readLineAndSave(String fp);
	
	
}
