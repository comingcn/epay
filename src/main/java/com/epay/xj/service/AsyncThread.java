package com.epay.xj.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epay.xj.domain.Variables;
import com.epay.xj.properties.InitProperties;
import com.epay.xj.service.CertNoService;
import com.epay.xj.service.DutyService;
import com.epay.xj.service.TradeDetailService;


@Service
public class AsyncThread {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private InitProperties initProperties;
	@Autowired
	private CertNoService certNoService;
	@Autowired
	private TradeDetailService tradeDetailService;
	@Autowired
	private DutyService dutyService;
	
	public void setCertNoService(CertNoService certNoService) {
		this.certNoService = certNoService;
	}

	public void setInitProperties(InitProperties initProperties) {
		this.initProperties = initProperties;
	}



	public void setTradeDetailService(TradeDetailService tradeDetailService) {
		this.tradeDetailService = tradeDetailService;
	}



	public void setDutyService(DutyService dutyService) {
		this.dutyService = dutyService;
	}



	public static void main(String[] args) {
		 AsyncThread t = new AsyncThread();
	        List<Future<Variables>> futureList = new ArrayList<Future<Variables>>(3);
	        List<String> certNoList = new ArrayList<String>(3);
	        t.builder(3, futureList,certNoList);
	        t.doOtherThings();
	        t.getResult(futureList);
	}
	
	/**
     * other things
     */
    public void doOtherThings() {
        try {
            for (int i = 0; i < 3; i++) {
                System.out.println("do thing no:" + i);
                Thread.sleep(1000 * (new Random().nextInt(10)));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
	/**
     * 生成指定数量的线程，都放入future数组
     * 
     * @param threadNum
     * @param fList
     */
    public void builder(int threadNum, List<Future<Variables>> fList,List<String> certNoList) {
        ExecutorService service = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; i++) {
            Future<Variables> f = service.submit(getJob(i,certNoList.get(i),dutyService));
            fList.add(f);
        }
//        service.shutdown();
    }
    
   /**
    * 汇总单个指标集合
    * @param i
    * @param certNoList
    * @return
    */
    public Callable<Variables> getJob(final int i,final String certNo,final DutyService dutyService) {
        return new Callable<Variables>() {
            @Override
            public Variables call() throws Exception {
                Variables v = dutyService.dealSingleCertNoData(certNo,i);
                return v;
            }
        };
    }

    /**
     * 从future中获取线程结果，打印结果
     * 
     * @param fList
     */
    public void getResult(List<Future<Variables>> fList) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(getCollectJob(fList));
        service.shutdown();
    }
    
    
    /**
     * 生成结果收集线程对象
     * 
     * @param fList
     * @return
     */
    public Runnable getCollectJob(final List<Future<Variables>> fList) {
        return new Runnable() {
            public void run() {
                for (Future<Variables> future : fList) {
                    try {
                        while (true) {
                            if (future.isDone() && !future.isCancelled()) {
                                System.out.println("Future:" + future
                                        + ",Result:" + future.get().getCertNo());
                                break;
                            } else {
                                Thread.sleep(1000);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
