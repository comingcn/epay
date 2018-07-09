package com.epay.xj.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epay.xj.mapper.CertListMapper;


@Service
public class CertListBeanService {

	Logger logger = LoggerFactory.getLogger(getClass());
//	@Autowired
//	private InitProperties initProperties;
//	@Autowired
//	private CertListBeanRepository certListBeanRepository;
	@Autowired
	private CertListMapper cartListMapper;	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<String> findAll(){
//		cartListMapper.getAll();
		String sql = "select CERT_NO from CP_ODS.P1055_CERT_LIST";
		List<String> list = entityManager.createNativeQuery(sql).getResultList();
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object> findTradeDetail(String ACCOUNT_NO){
		String sql = "select * from CP_ODS.P1055_TRA_TRADE_DETAIL_PARA where IDCARD="+ACCOUNT_NO+"";
		List list = entityManager.createNativeQuery(sql).getResultList();
		
		return list;
	}
	
	
	
}
