package com.epay.xj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epay.xj.service.CertNoService;

@RestController
@RequestMapping("/count")
public class CountController {

	@Autowired
	private CertNoService certNoService;
	
	@RequestMapping("/getDemo/{myName}")
    void getDemo(@PathVariable String myName) {
		certNoService.insertTwo();
    }
}
