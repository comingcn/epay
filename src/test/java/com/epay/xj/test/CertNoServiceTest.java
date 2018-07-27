package com.epay.xj.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.epay.xj.service.TaskServer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CertNoServiceTest {
	
    @Autowired
    private TaskServer taskServer;
    
    Logger logger = LoggerFactory.getLogger(getClass());
    
    
    @Test
    public void dealOverDue(){
    	long beginTime = System.nanoTime();
//    	taskServer.truncate();
    	taskServer.deal("20180723", null);
    	String useTime = String.valueOf((System.nanoTime() - beginTime)/Math.pow(10, 9));
		logger.info("useTime:{}秒",useTime);
    }
    
}