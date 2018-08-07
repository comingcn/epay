package com.epay.xj;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import com.epay.xj.service.AnalysisServer;
@SpringBootApplication
@EnableConfigurationProperties
public class EpayApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext cac = SpringApplication.run(EpayApplication.class, args);
		AnalysisServer as = (AnalysisServer) cac.getBean("analysisServer");
		as.getList();
	}
}
