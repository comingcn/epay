package com.epay.xj.test;


import java.io.StringWriter;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.epay.xj.mapper.CertListMapper;
import com.epay.xj.service.CertListBeanService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CertNoServiceTest {
//    @Autowired
//    private CertNoService certNoService;
//    @Autowired
//    private InitProperties initProperties;
    @Autowired
    private CertListBeanService certListBeanService;
    
    @Resource
    private CertListMapper mp;
    Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    public void findOne() throws Exception {
    	List<String> list =certListBeanService.findAll();
    	for (String string : list) {
    		System.out.println(">>>>"+string);
    		logger.info(string);
    		List<Object> tradeDetailLst = certListBeanService.findTradeDetail(string);
    		StringWriter str=new StringWriter();
    		ObjectMapper om = new ObjectMapper();
    		om.writeValue(str, tradeDetailLst);
    		logger.info(str.toString());
    		System.out.println(">>>>_____"+ tradeDetailLst.size());
		}
    	System.out.println(list.size());
    }

    
    public void findOneqaqa() throws Exception {
    	mp.getAll();
    }
    	
}