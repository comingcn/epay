package com.epay.xj.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epay.xj.common.BigFileReader;
import com.epay.xj.common.BigFileReader.Builder;
import com.epay.xj.common.IHandle;
import com.epay.xj.domain.CertNo;
import com.epay.xj.properties.InitProperties;

@Service
@Transactional
public class AnalysisServer {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private InitProperties initProperties;
	@PersistenceContext
	private EntityManager em;
	public static Set<String> readDataFromFile(String file) {
		BigFileReader.Builder builder = new BigFileReader.Builder(file, new IHandle() {
			@Override
			public void handle(String line) {
				String[] lines = line.split("-");
				String idNo = lines[0];
				System.out.println(idNo);
			}
		});
//		Runtime.getRuntime().availableProcessors()
		builder.withTreahdSize(Runtime.getRuntime().availableProcessors()*3-1).withCharset("utf-8")
				.withBufferSize(1024*1024 * 1024); // 设置读取缓冲区大小
		BigFileReader bigFileReader = builder.build();
		bigFileReader.start();
		return null;
	}

	public void getList() {
		String sql = "select * from cert_no";
		List<CertNo> list = em.createNativeQuery(sql, CertNo.class).getResultList();
		System.out.println(list.size());
	}

	public static void main(String[] args) {
		String filePath = "F:/yhfw/data/a.txt";
		File file = new File(filePath);
		Set<String> set = readDataFromFile(filePath);
	}
}
