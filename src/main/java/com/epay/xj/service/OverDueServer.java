package com.epay.xj.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.epay.xj.domain.OverDueRecord;
import com.epay.xj.domain.TradeDetailDO;
import com.epay.xj.properties.InitProperties;
import com.epay.xj.utils.DateUtils;

@Service
@Transactional
public class OverDueServer {

	Logger logger = LoggerFactory.getLogger(getClass());

	// 参数初始化
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	@Autowired
	private InitProperties initProperties;

	@Autowired
	private BatchInsertService batchInsertService2;
	
	@Autowired
	private DataServer dataServer;
	
	static List<String> dataScope = null;
	
	static List<String> yebzScope = null;
	/**
	 * 任务切分 切分taskList,交有不同的线程计算指标
	 * 
	 * @param taskList
	 * @param updateTime
	 * @param etlServer
	 * @throws InterruptedException
	 */
	public void sliceTask(List<String> taskList, Date updateTime, String etlServer) throws InterruptedException {
		// 预留cup核心数 必须小于cup总核心数
		int unusedCupCount = initProperties.getUnusedCupCount();
		// 核心线程数量大小
		int corePoolSize = Math.max(2, Math.min(CPU_COUNT - unusedCupCount, 4));
		// 线程池最大容纳线程数
		int maximumPoolSize = (CPU_COUNT * 2 - unusedCupCount) + 1;
		logger.info("CPU_COUNT:{},corePoolSize:{},maximumPoolSize:{}", CPU_COUNT, corePoolSize, maximumPoolSize);
		// 线程空闲后的存活时长
		final int keepAliveTime = 30;
		// 总数据条数
		int dataSize = taskList.size();
		// 线程数
		int threadNum = maximumPoolSize;
		// 根据数据大小自动分配线程大小
		int threadSize = 1;// 默认一个线程
		if (threadNum > 1) {
			threadSize = dataSize / threadNum;
		}
		logger.info("dataSize:{},threadNum:{},Thread processing records:{},", dataSize, threadNum, threadSize);
		// 定义标记,过滤threadNum为整数
		boolean special = dataSize % threadSize == 0;
		// 线程池任务满载后采取的任务强行策略
		RejectedExecutionHandler indexRejectHandler = new ThreadPoolExecutor.CallerRunsPolicy();
		BlockingQueue<Runnable> indexWorkQueue = new SynchronousQueue<>();
		ExecutorService exec =  new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
				TimeUnit.SECONDS, indexWorkQueue, indexRejectHandler);
		// 任务过多后，存储任务的一个阻塞队列
		BlockingQueue<Runnable> workQueue = new SynchronousQueue<>();
		// 线程池任务满载后采取的任务强行策略
		RejectedExecutionHandler rejectHandler = new ThreadPoolExecutor.CallerRunsPolicy();
		// 线程池对象，创建线程
		final ThreadPoolExecutor execute = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
				TimeUnit.SECONDS, workQueue, rejectHandler);
		// 定义一个任务集合
		List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
		Callable<Integer> task = null;
		List<String> cutList = null;
		// 确定每条线程的数据
		for (int i = 0; i < threadNum; i++) {
			if (i == threadNum - 1) {
				if (special) {
					break;
				}
				cutList = taskList.subList(threadSize * i, dataSize);
			} else {
				cutList = taskList.subList(threadSize * i, threadSize * (i + 1));
			}
			final List<String> listStr = cutList;
			final Date updateTimes = updateTime;
			final String etlServers = etlServer;
			final String active = dataServer.activeTableParam();
			task = new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					List<OverDueRecord> outPutList = new ArrayList<OverDueRecord>();
					for (int i = 0; i < listStr.size(); i++) {
						// long sysBeginTime = System.nanoTime();
						String certNo = listStr.get(i);
//						logger.info("certNo:{}", certNo);
						// 获取一个人在每个机构下的所有记录
						Map<String,List<TradeDetailDO>> mapTredeDetail = everyOrgTradeDetails(certNo,  active);
						for (Map.Entry<String, List<TradeDetailDO>> entry : mapTredeDetail.entrySet()) {
							List<TradeDetailDO> list = entry.getValue();
							if(!list.isEmpty()){
								Collections.sort(list);
								overDueRecords(certNo,list, outPutList, updateTimes);
							}
						}
					}
					logger.info("size:{}", outPutList.size());
					batchInsertService2.addList(execute, outPutList, etlServers);
					return outPutList.size();
				}
			};
			// 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
			tasks.add(task);
		}
		exec.invokeAll(tasks);
		// 关闭线程池
		exec.shutdown();
		execute.shutdown();
		while (true) {
			if (execute.isTerminated() && exec.isTerminated()) {
				System.out.println("所有的线程都结束了！");
				break;
			}
		}
	}

	
	public void onePersonCaculate(String certNo, List<OverDueRecord> outPutList, Date updateTime, String etlServer) throws InterruptedException {
		// 获取人的所有记录
		Map<String,List<TradeDetailDO>> mapTredeDetail = everyOrgTradeDetails(certNo, dataServer.activeTableParam());
		for (Map.Entry<String, List<TradeDetailDO>> entry : mapTredeDetail.entrySet()) {
//			String key = entry.getKey();
			List<TradeDetailDO> list = entry.getValue();
			if(!list.isEmpty()){
				Collections.sort(list);
//				logger.info("originData>key:{}",key);
//				for (TradeDetailDO o : list) {
//					logger.info("{}",JSON.toJSONString(o,SerializerFeature.WriteDateUseDateFormat));
//				}
				overDueRecords(certNo,list, outPutList, updateTime);
			}
		}
	}
	
	/**
	 * 获取指定结构下面的所有逾期记录
	 * @param list
	 * @return
	 */
	public void overDueRecords(String certNo , List<TradeDetailDO> list,List<OverDueRecord> outPutList, Date updateTime) {
		for (int i = 0; i < list.size(); i++) {
			TradeDetailDO o = list.get(i);
			boolean currentYebzFlag = yebzScope.contains(o.getRETURN_CODE());//是余而不足的记录
			if (!currentYebzFlag ) {
				continue;
			} else {
				if(i!=0 && o.getAMOUNT().toString().equals(list.get(i - 1).getAMOUNT().toString())
						&& yebzScope.contains(o.getRETURN_CODE())
						&& yebzScope.contains(list.get(i - 1).getRETURN_CODE())){
					continue;
				}else{
					if(i==list.size()-1 && o.getRETURN_CODE().equals("0000")){//最后一个就为成功记录跳过
						continue;
					}
				}
				List<TradeDetailDO> tmp = list.subList(i==(list.size()-1)?i:i+1, list.size());
				TradeDetailDO end = getNextRecordOfList(tmp, o);
				if (null == end)
					continue;
				Date beginTime = DateUtils.yyyyMMddToDate(DateUtils.yyyyMMddToString(o.getCREATE_TIME()));
				Date entTime = DateUtils.yyyyMMddToDate(DateUtils.yyyyMMddToString(end.getCREATE_TIME()));
				OverDueRecord odr = new OverDueRecord();
				odr.setCERT_NO(certNo);
				odr.setTYPE_ID(o.getMER_TYPE());
				odr.setMER_NO(o.getSOURCE_MERNO());
				odr.setSTR_DAYS(DateUtils.getIntervalDayAmount(beginTime, updateTime));
				odr.setEND_DAYS(DateUtils.getIntervalDayAmount(entTime, updateTime));
				odr.setOVR_DAYS(DateUtils.getIntervalDayAmount(beginTime, entTime));
				odr.setAMT(o.getAMOUNT());
				outPutList.add(odr);
			}
		}
	}
	
	/**
	 * 口径3标准
	 * 
	 * @param tmp
	 * @param o
	 * @return
	 */
	public TradeDetailDO getNextRecordOfList(List<TradeDetailDO> tmp, TradeDetailDO o) {
		if(tmp.size()==0)return null;
		TradeDetailDO tradeDetailDO = judgeFailRecord(tmp);
		if(null!=tradeDetailDO)return tradeDetailDO;
		for (TradeDetailDO to : tmp) {
			if ("0000".equals(to.getRETURN_CODE())) {
				return to;
			}
		}
		return null;
	}
	
	/**
	 * 判断集合中的集合是否全部是余而不足，如果有获取最大的记录
	 * @param tmp
	 * @return
	 */
	public TradeDetailDO judgeFailRecord(List<TradeDetailDO> tmp){
		List<TradeDetailDO> listSize = new ArrayList<TradeDetailDO>(tmp.size());
		listSize.addAll(tmp);
		int i = 0;
		for (TradeDetailDO tradeDetailDO : tmp) {
			if(tradeDetailDO.getRETURN_CODE().equals("0000"))continue;
			i++;
		}
		if(i==listSize.size()){
			return tmp.get(listSize.size()-1);
		}else{
			return null;
		}
		
	}
	
	/**
	 * 获取每个机构下所有逾期的数据
	 * @param certNo
	 * @param udpateTimes
	 * @param active
	 * @return
	 */
//	public Map<String,List<OverDueRecord>> everyOrgOverDueTradeDetails(String certNo, Map<String,List<TradeDetailDO>> map,Date udpateTime){
//		Map<String,List<OverDueRecord>> tmpMap = new HashMap<String,List<OverDueRecord>>();
//		for (Map.Entry<String, List<TradeDetailDO>> entry : map.entrySet()) {
//			List<TradeDetailDO> tmpList = entry.getValue();
//			if(!tmpList.isEmpty())Collections.sort(tmpList);
//			tmpMap.put(entry.getKey(), overDueRecords(certNo,tmpList, udpateTime));
//		}
//		return tmpMap;
//	}
	
	/**
	 * 获取每个机构下所有的数据
	 * @param certNo 身份证号
	 * @param udpateTimes 指定时间
	 * @param active
	 * @return
	 */
	public Map<String,List<TradeDetailDO>> everyOrgTradeDetails(String certNo, String active){
		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
		List<TradeDetailDO> list = dataServer.getTradeDetailList(certNo, active);
		for (TradeDetailDO o : list) {
			String key = o.getSOURCE_MERNO();
			if(map.containsKey(key)){
				map.get(key).add(o);
			}else{
				List<TradeDetailDO> tmpList = new ArrayList<TradeDetailDO>();
				tmpList.add(o);
				map.put(key, tmpList);
			}
		}
		return map;
	}
	

	/**
	 * 处理指标计算入口
	 * 
	 * @param updateTime
	 * @param etlServer:0/1
	 */
	public void deal(String certNo,Date updateTime, String etlServer) {
		try {
			dataScope = Arrays.asList(initProperties.getMerTypeDic().get("dk"));
			yebzScope = Arrays.asList(initProperties.getReturnCodeDic().get("yebz"));
			List<String> taskList = dataServer.getTaskList(certNo, etlServer);
			sliceTask(taskList, updateTime, etlServer);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 处理指标计算入口
	 * 
	 * @param updateTime
	 * @param etlServer:0/1
	 */
	public void compare(String certNo,Date updateTime, String etlServer) {
		dataScope = Arrays.asList(initProperties.getMerTypeDic().get("dk"));
		yebzScope = Arrays.asList(initProperties.getReturnCodeDic().get("yebz"));
		List<String> taskList = dataServer.getTaskList(certNo, etlServer);
		List<OverDueRecord> javaList = null;
		List<OverDueRecord> pythonList = null;
		Map<String,List<OverDueRecord>> mapForJava = new HashMap<String,List<OverDueRecord>>();
		Map<String,List<String>> mapForPython = new HashMap<String,List<String>>();
		for (String string : taskList) {
			javaList = dataServer.getJavaOverDueRecordList(string);
			pythonList = dataServer.getPythonOverDueRecordList(string);
			if(javaList.size()!=pythonList.size()){
				logger.info("java diff python certNo:{}",string);
			}
			for (OverDueRecord java : javaList) {
				String key = java.getCERT_NO();
				if(mapForJava.containsKey(key)){
					mapForJava.get(key).add(java);
				}else{
					List<OverDueRecord> pyList = new ArrayList<OverDueRecord>();
					pyList.add(java);
					mapForJava.put(key, pyList);
				}
			}
			for (OverDueRecord python : pythonList) {
				String key = python.getCERT_NO();
				if(mapForPython.containsKey(key)){
					mapForPython.get(key).add(JSON.toJSONString(python));
				}else{
					List<String> pyList = new ArrayList<String>();
					pyList.add(JSON.toJSONString(python));
					mapForPython.put(key, pyList);
				}
			}
		}
		
		/*******************************************/
		for (Map.Entry<String, List<OverDueRecord>> entry : mapForJava.entrySet()) {
			String key = entry.getKey();
			List<OverDueRecord> jtmp = entry.getValue();
			List<String> ptmp = mapForPython.get(key);
			for (OverDueRecord o : jtmp) {
				if(!ptmp.contains(JSON.toJSONString(o))){
					logger.info("certNo:{},java:{},python:{}",JSON.toJSONString(jtmp),JSON.toJSONString(ptmp));
				}
			}
		}
	}
	
}
