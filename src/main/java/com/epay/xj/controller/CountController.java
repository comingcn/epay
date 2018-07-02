package com.epay.xj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epay.xj.service.DutyService;

@RestController
@RequestMapping("/count")
public class CountController {

	@Autowired
	private DutyService dutyService;
	
	@RequestMapping("/getDemo/{myName}")
    void getDemo(@PathVariable String myName) {
		try {
			dutyService.readerMerTradeDetailTable();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
