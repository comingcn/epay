package com.epay.xj.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epay.xj.common.BigFileReader;
import com.epay.xj.common.IHandle;
import com.epay.xj.domain.BindCardLog;
import com.epay.xj.properties.InitProperties;
import com.epay.xj.repository.BindCardLogRepository;

@Service
public class BindCardLogService {

	@Autowired
	private InitProperties initProperties;
	@Autowired
	private BindCardLogRepository bindCardLogRepository;
	
	public void batchInsert(List<BindCardLog> o){
		bindCardLogRepository.save(o);
	}
	
	public void batchInsert(BindCardLog o){
		bindCardLogRepository.save(o);
	}
	
	public void batchAdd(List<String> records){
		List<BindCardLog> o = new ArrayList<BindCardLog>();
		for (String str : records) {
			String[] record = str.split(",");
			BindCardLog bindCardLog = new BindCardLog();
			String txnDate = record[0];
			String txnSeqId = record[1];
			String certNo = record[2];
			String cardNo = record[3];
			String dcType = record[4];
			String merId = record[5];
			String merType = record[6];
			String returnCode = record[7];
			bindCardLog.setCardNo(cardNo);
			bindCardLog.setTxnDate(txnDate);
			bindCardLog.setTxnSeqId(txnSeqId);
			bindCardLog.setCertNo(certNo);
			bindCardLog.setDcType(dcType);
			bindCardLog.setMerId(merId);
			bindCardLog.setMerType(Integer.valueOf(merType));
			bindCardLog.setReturnCode(returnCode);
			o.add(bindCardLog);
		}
		batchInsert(o);
	}
	
	public void insert(String[] record){
		BindCardLog bindCardLog = new BindCardLog();
		String txnDate = record[0];
		String txnSeqId = record[1];
		String certNo = record[2];
		String cardNo = record[3];
		String dcType = record[4];
		String merId = record[5];
		String merType = record[6];
		String returnCode = record[7];
		bindCardLog.setCardNo(cardNo);
		bindCardLog.setTxnDate(txnDate);
		bindCardLog.setTxnSeqId(txnSeqId);
		bindCardLog.setCertNo(certNo);
		bindCardLog.setDcType(dcType);
		bindCardLog.setMerId(merId);
		bindCardLog.setMerType(Integer.valueOf(merType));
		bindCardLog.setReturnCode(returnCode);
		batchInsert(bindCardLog);
	}
	
	
	public void readBindCardLogFile(String fileName){
		StringBuffer sb = new StringBuffer();
		Map<String,String[]> maps = initProperties.getMerTypeDic();
		sb.append(initProperties.getfPathInput()).append(initProperties.getBindCardLog()).append(fileName).append(".DEL");
		final Map<String,Runnable> map = null;
		BigFileReader.Builder builder = new BigFileReader.Builder(sb.toString(), new IHandle() {
			public void handle(String line) {
				try {
//					map = new ConcurrentHashMap<String, Runnable> ();
//					Thread t = Thread.currentThread();
//					String key = t.getName();
//					map.put(key, t);
//					Iterator<String> it = (Iterator<String>) map.keySet();
//					while(it.hasNext()){
//						
//					}
					String[] str = line.split(",");
					insert(str);
//					certNoService.updateOrInsertRecord(line.split(","));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				String s = line.split(",")[3];
//				System.out.println(s);
//				set.add(s);
//				log.info(MD5Util.encrypt(s));
//				System.out.println(set.size());
			}
		});
		builder.withTreahdSize(2).withCharset("ISO-8859-1").withBufferSize(1024 * 1024);
		BigFileReader bigFileReader = builder.build();
		bigFileReader.start();
	}
	
}
