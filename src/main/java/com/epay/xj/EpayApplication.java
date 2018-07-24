package com.epay.xj;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
@SpringBootApplication
@EnableConfigurationProperties
public class EpayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpayApplication.class, args);
//		String date = args[0];
//		System.out.println(date);
	}
}
