package com.epay.xj.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.epay.xj.domain.CertNoDO;
import com.epay.xj.properties.InitProperties;
import com.epay.xj.service.CertNoService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CertNoServiceTest {
    @Autowired
    private CertNoService certNoService;
    @Autowired
    private InitProperties initProperties;
    @Test
    public void findOne() throws Exception {
    	System.out.println(initProperties.getfPathInput());
    	CertNoDO c = new CertNoDO();
    	c.setId("1");
        boolean exists = certNoService.exists(c);
        System.out.println(exists);
        Assert.isTrue(exists);
    }

}