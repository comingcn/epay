package com.epay.xj.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.epay.xj.domain.TradeDetailDO;


@Service
public class CertListBeanService {

	Logger logger = LoggerFactory.getLogger(getClass());
//	@Autowired
//	private InitProperties initProperties;
//	@Autowired
//	private CertListBeanRepository certListBeanRepository;
//	@Autowired
//	private CertListMapper cartListMapper;	
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
		List<TradeDetailDO> tradeDetailList = new ArrayList<TradeDetailDO>();
		for (Object object : list) {
			Object[] arr = (Object[]) object;
			TradeDetailDO t = new TradeDetailDO();
			Timestamp timeStamp = (Timestamp) arr[0];
			t.setCREATE_TIME(timeStamp);
			t.setID((BigDecimal)arr[1]);
			t.setIDCARD((String)arr[2]);
			t.setACCOUNT_NO((String)arr[3]);
			t.setSOURCE_MERNO((String)arr[4]);
			t.setMER_TYPE((Integer)arr[5]);
			t.setAMOUNT((BigDecimal)arr[6]);
			t.setSF_TYPE(arr[7].toString());
			t.setRETURN_CODE(arr[8].toString());
			tradeDetailList.add(t);
		}
		return list;
	}
	
	
	
}
