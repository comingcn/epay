package com.epay.xj;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.epay.xj.service.IAnalysisServer;
@SpringBootApplication
@EnableTransactionManagement
//@EnableConfigurationProperties
@MapperScan("com.epay.xj.dao")
public class EpayApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext cac = SpringApplication.run(EpayApplication.class, args);
		IAnalysisServer as = (IAnalysisServer) cac.getBean("analysisServer");
		as.readLineAndSave(args[1]);
//		as.getList();
	}
}
