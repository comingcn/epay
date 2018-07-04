package com.epay.xj.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epay.xj.domain.TradeDetailDO;
import com.epay.xj.repository.TradeDetailRepository;

@Service
public class TradeDetailService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TradeDetailRepository tradeDetailRepository;
	
	public void batchInsert(List<TradeDetailDO> list){
		tradeDetailRepository.save(list);
	}
	
	public void deleteAll(List<TradeDetailDO> list){
		tradeDetailRepository.deleteInBatch(list);
	}
	private void addTradeDetail(){
		
	}
	
	
}
