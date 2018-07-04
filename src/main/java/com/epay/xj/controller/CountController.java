package com.epay.xj.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epay.xj.service.BindCardLogService;
import com.epay.xj.service.DutyService;

@RestController
@RequestMapping("/count")
public class CountController {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private DutyService dutyService;
	@Autowired
	private BindCardLogService bindCardLogService;
	@RequestMapping("/getDemo/{myName}")
    void getDemo(@PathVariable String myName) {
		try {
			long beginTime = System.nanoTime();
			String updateTime = "20180703";
			//1.读取所有交易日志数据集	1。1 身份证号分布在号表中 1.2 身份证号文件对应所有交易记录
//			dutyService.readerMerTradeDetailTable();
			
			bindCardLogService.readBindCardLogFile("20180101");
			//2.通过多线程，处理每个身份证号下所有交易记录指标，计算指标必须使用单线程处理 2。1 从文件中读取交易日志，放入表中计算，2.2 汇总计算结果 2.3 批量更新指标表记录  2.4 清空中间表记录，即每个线程的交易记录表
//			dutyService.addTradeDetail(updateTime);
			//3.数据收尾 3.1 批量导出指标表记录到指定文件
			
			String useTime = String.valueOf((System.nanoTime() - beginTime)/Math.pow(10, 9));
			logger.info("useTime:{}秒",useTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
