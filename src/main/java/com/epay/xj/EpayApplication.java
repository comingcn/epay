package com.epay.xj;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import com.epay.xj.properties.InitProperties;
import com.epay.xj.service.TaskServer;
import com.epay.xj.utils.DateUtils;
@SpringBootApplication
@EnableConfigurationProperties
public class EpayApplication {
	static Logger logger = LoggerFactory.getLogger(EpayApplication.class);
	
	public static void main(String[] args) {
		ConfigurableApplicationContext cac = SpringApplication.run(EpayApplication.class, args);
		TaskServer ts = (TaskServer) cac.getBean("taskServer");
		InitProperties ip = (InitProperties) cac.getBean("initProperties");
		if(chk(args) && args.length!=4){
			System.out.println("请输入正确参数");
			System.exit(0);
		}
//		  #计算指标线单线程处理集合大小
//		  threadIndexSize: 5000
//		  #计算指标线程池大小
//		  threadIndexPoolSize: 20
//		  #入库线单线程处理集合大小
//		  threadStorageSize: 1000
//		  #入库线程池大小
//		  threadStoragePoolSize: 5
		if(args[2]!=null){//预留cup核心数 必须小于cup总核心数
			ip.setUnusedCupCount(Integer.valueOf(args[2]));
		}
		long beginTime = System.nanoTime();
		logger.info("开始时间：{}", DateUtils.getNow());
		ts.deal(args[1], args[3]);
		String useTime = String.valueOf((System.nanoTime() - beginTime)/Math.pow(10, 9));
		logger.info("useTime:{}秒",useTime);
		System.exit(0);
	}
	
	public static boolean chk(String[] args){
		if(args ==null || args.length==0){
			return true;
		}
		return false;
	}
}
