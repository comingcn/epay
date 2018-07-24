package com.epay.xj.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "initProperties")
public class InitProperties {

	private String bindCardLog;
	
	private String overDueType;
	//计算指标线单线程处理集合大小
	private int threadIndexSize;
	//计算指标线程池大小
	private int threadIndexPoolSize;
	//入库线单线程处理集合大小
	private int threadStorageSize;
	//入库线程池大小
	private int threadStoragePoolSize;
	
	private Map<String,Integer>  overDueMonth = new HashMap<String,Integer>();
	
	private Map<String,Integer> overDueDayDic = new HashMap<String,Integer>();
	
	private Map<String, String[]> merTypeDic = new HashMap<String, String[]>();	
	
	
	private Map<String, String[]> returnCodeDic =  new HashMap<String, String[]>();


	public String getBindCardLog() {
		return bindCardLog;
	}


	public void setBindCardLog(String bindCardLog) {
		this.bindCardLog = bindCardLog;
	}


	public String getOverDueType() {
		return overDueType;
	}


	public void setOverDueType(String overDueType) {
		this.overDueType = overDueType;
	}


	public int getThreadIndexSize() {
		return threadIndexSize;
	}


	public void setThreadIndexSize(int threadIndexSize) {
		this.threadIndexSize = threadIndexSize;
	}


	public int getThreadIndexPoolSize() {
		return threadIndexPoolSize;
	}


	public void setThreadIndexPoolSize(int threadIndexPoolSize) {
		this.threadIndexPoolSize = threadIndexPoolSize;
	}


	public int getThreadStorageSize() {
		return threadStorageSize;
	}


	public void setThreadStorageSize(int threadStorageSize) {
		this.threadStorageSize = threadStorageSize;
	}


	public int getThreadStoragePoolSize() {
		return threadStoragePoolSize;
	}


	public void setThreadStoragePoolSize(int threadStoragePoolSize) {
		this.threadStoragePoolSize = threadStoragePoolSize;
	}


	public Map<String, Integer> getOverDueMonth() {
		return overDueMonth;
	}


	public void setOverDueMonth(Map<String, Integer> overDueMonth) {
		this.overDueMonth = overDueMonth;
	}


	public Map<String, Integer> getOverDueDayDic() {
		return overDueDayDic;
	}


	public void setOverDueDayDic(Map<String, Integer> overDueDayDic) {
		this.overDueDayDic = overDueDayDic;
	}


	public Map<String, String[]> getMerTypeDic() {
		return merTypeDic;
	}


	public void setMerTypeDic(Map<String, String[]> merTypeDic) {
		this.merTypeDic = merTypeDic;
	}


	public Map<String, String[]> getReturnCodeDic() {
		return returnCodeDic;
	}


	public void setReturnCodeDic(Map<String, String[]> returnCodeDic) {
		this.returnCodeDic = returnCodeDic;
	}	
	
}
