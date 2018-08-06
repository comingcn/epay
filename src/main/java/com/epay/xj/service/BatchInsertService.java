package com.epay.xj.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epay.xj.domain.OverDueRecord;

@Service
@Transactional
public class BatchInsertService {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TaskServer taskServer;
	
	@Autowired
	private DataServer dataServer;

	public void addList(final ThreadPoolExecutor execute,  List<OverDueRecord> list, String eltServer) {
		
		// 总数据条数
		int dataSize = list.size();
		// 线程数
		int threadNum = execute.getMaximumPoolSize();
		// 根据数据大小自动分配线程大小
		int threadSize = 1;//默认一个线程
		if(execute.getCorePoolSize()>1){
			threadSize = dataSize / threadNum;
		}
		// 定义标记,过滤threadNum为整数
		boolean special = dataSize % threadSize == 0;
		logger.info("taskSize:{},线程数：{},单个线程处理记录数量:{}", list.size(), threadNum, threadSize);
		List<OverDueRecord> cutList = null;
		List<WriteCallable> lst = new ArrayList<WriteCallable>();
		for (int i = 0; i < threadNum; i++) {
			if (i == threadNum - 1) {
				if (special) break;
				cutList = list.subList(threadSize * i, dataSize);
			} else {
				cutList = list.subList(threadSize * i, threadSize * (i + 1));
			}
			lst.add(new WriteCallable(cutList, eltServer));
		}
		try {
			execute.invokeAll(lst);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	class WriteCallable implements Callable<Integer> {

		Logger logger = LoggerFactory.getLogger(getClass());

		private List<OverDueRecord> list;

		private String etlServer;

		public WriteCallable(List<OverDueRecord> list, String etlServer) {
			super();
			this.list = list;
			this.etlServer = etlServer;
		}

		public Integer call() throws Exception {
			try {
				synchronized (this) {
					dataServer.batchInsert(list,etlServer);
				}
				return 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1;
		}
	}

}
