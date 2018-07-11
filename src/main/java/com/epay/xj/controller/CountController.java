package com.epay.xj.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epay.xj.service.TaskServer;
import com.epay.xj.service.TaskServer2;

@RestController
@RequestMapping("/count")
public class CountController {

	Logger logger = LoggerFactory.getLogger(getClass());
//	@Autowired
//	private OverDueService dutyService;
	@Autowired
	private TaskServer taskServer;
	
	@Autowired
	private TaskServer2 taskServer2;
	
	@RequestMapping("/{updateTime}")
    String getDemo(@PathVariable String updateTime) {
		try {
			long beginTime = System.nanoTime();
			logger.info("开始时间：{}", beginTime);
			taskServer.deal1(updateTime, null);
			String useTime = String.valueOf((System.nanoTime() - beginTime)/Math.pow(10, 9));
			logger.info("useTime:{}秒",useTime);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fail";
    }
	
	@RequestMapping("get/{updateTime}")
    String get(@PathVariable String updateTime) {
		try {
			long beginTime = System.nanoTime();
			List<String> list = taskServer2.getTaskList(updateTime,null);
			String useTime = String.valueOf((System.nanoTime() - beginTime)/Math.pow(10, 9));
			logger.info("listsize:{},useTime:{}秒",list.size(),useTime);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fail";
    }
}
