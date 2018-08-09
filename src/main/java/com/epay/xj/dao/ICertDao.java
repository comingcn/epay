package com.epay.xj.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.epay.xj.domain.CertNo;
@Mapper
public interface ICertDao {
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public void insert(CertNo o);
	
	public List<CertNo> findAll();
}
