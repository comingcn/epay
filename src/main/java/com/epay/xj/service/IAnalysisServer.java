package com.epay.xj.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.epay.xj.domain.CertNo;

public interface IAnalysisServer {

	@Transactional(propagation=Propagation.REQUIRED)
	public void insert(CertNo o);
	@Transactional(propagation=Propagation.REQUIRED)
	void readLineAndSave(String fp);
	
	
}
