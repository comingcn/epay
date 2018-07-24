package com.epay.xj.service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epay.xj.domain.OverDueIndex;
import com.epay.xj.properties.InitProperties;


@Service
@Transactional
public class BatchInsertService {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private InitProperties initProperties;//
	@Autowired
	private TaskServer taskServer;
	
	public void addList(List<OverDueIndex> list){
		ExecutorService executor = Executors.newFixedThreadPool(initProperties.getThreadStoragePoolSize());//给定线程池数量
		int MAX_DEAL = initProperties.getThreadStorageSize();//对多数据进行分组，10000条一组，一组使用一个线程进行执行
		if(null==list || list.isEmpty())return;
		int times = (list.size() + MAX_DEAL - 1) / MAX_DEAL;
		//一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
		CountDownLatch countDownLatch = new CountDownLatch(times);
		try {
       	 for (int i = 0; i < times; i++) {
                if (i == times - 1) {
                	executor.execute(new ListRunnable(list.subList(i * MAX_DEAL, list.size()), countDownLatch));//调用业务逻辑
                } else {
                	executor.execute(new ListRunnable(list.subList(i * MAX_DEAL, (i + 1) * MAX_DEAL), countDownLatch));
                }
            }
            countDownLatch.await();//一个线程(或者多个)， 等待另外N个线程完成某个事情之后才能执行
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	class ListRunnable  implements Runnable{
		
		Logger logger = LoggerFactory.getLogger(getClass());
		
		private List<OverDueIndex> list;
		
		private CountDownLatch countDownLatch;
		
		public ListRunnable(List<OverDueIndex> list,CountDownLatch countDownLatch){
			super();
			this.list = list;
			this.countDownLatch = countDownLatch;
		}
		
		@Override
		public void run() {
			try {
//				taskServer.batchInsert(list);
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
	            countDownLatch.countDown();//完成一次操作，计数减一  
	        }
			
		}
		
	}

}
