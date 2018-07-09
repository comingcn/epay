package com.epay.xj.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epay.xj.dao.CertNoDao;
import com.epay.xj.dao.impl.CertNoDaoImpl;
import com.epay.xj.domain.CertNoDomain;
import com.epay.xj.utils.DateUtils;

@RestController
@RequestMapping("/count")
public class CountController {

	Logger logger = LoggerFactory.getLogger(getClass());
//	@Autowired
//	private OverDueService dutyService;
//	@Autowired
//	private BindCardLogService bindCardLogService;
	@RequestMapping("/getDemo/{myName}")
    void getDemo(@PathVariable String myName) {
		try {
		    
		    System.out.println("统计开始时间：" + DateUtils.getNow());
            System.out.println("统计正在进行中，请耐心等待……");
            //获取所有身份证号
            CertNoDao certNoDao = new CertNoDaoImpl();
            List<CertNoDomain> certNoList = certNoDao.finAllCertNo();
            int certNoListSize = certNoList.size();
            System.out.println("一共需要计算人数为：" + certNoList.size());
            
            for(int i = 0; i < certNoListSize; i++) {
                //dutyService.overDue(certNoList.get(i).getCertNo());
            }
            System.out.println("统计结束时间：" + DateUtils.getNow());
		    
		    
		    
			long beginTime = System.nanoTime();
			String updateTime = "20180703";
			//1.读取所有交易日志数据集	1。1 身份证号分布在号表中 1.2 身份证号文件对应所有交易记录
//			dutyService.readerMerTradeDetailTable();
//			dutyService.overDue("0346778E249BD924CDC658D06C512333C70989E855225FBABD3515AAB06D42FE");
//			bindCardLogService.readBindCardLogFile("20180101");
			//2.通过多线程，处理每个身份证号下所有交易记录指标，计算指标必须使用单线程处理 2。1 从文件中读取交易日志，放入表中计算，2.2 汇总计算结果 2.3 批量更新指标表记录  2.4 清空中间表记录，即每个线程的交易记录表
//			dutyService.addTradeDetail(updateTime);
			//3.数据收尾 3.1 批量导出指标表记录到指定文件
			
//			String useTime = String.valueOf((System.nanoTime() - beginTime)/Math.pow(10, 9));
//			logger.info("useTime:{}秒",useTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
