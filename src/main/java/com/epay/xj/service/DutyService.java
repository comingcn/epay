package com.epay.xj.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.epay.xj.properties.InitProperties;
import com.epay.xj.utils.FileUtils;

@Service
public class DutyService {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private InitProperties initProperties;
	@Autowired
	private CertNoService certNoService;

	public void readerMerTradeDetailTable() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append(initProperties.getfPathInput()).append(initProperties.getTradeDetail()).append("20180102").append(".DEL");
		File file = new File(sb.toString());
		CsvReader reader = new CsvReader(new FileReader(file.getAbsolutePath()));
		String[] headerInfo = initProperties.getInputHeaderMtd().split(",");
		
		while (reader.readRecord()) {
			certNoService.updateOrInsert(reader);
//			logger.info(MD5Util.encrypt(certNo));
		}
	}
	
	
	public void appendTradeDetail(String fileName, String[] record) throws IOException{
		StringBuffer sb = new StringBuffer();
		//该目录必须存在
		sb.append(initProperties.getfPathOutput()).append("certNo//");
		//文件不存在，创建目录
		if(!FileUtils.existsDirectory(sb.toString()))FileUtils.forceDirectory(sb.toString());
		sb.append(fileName).append(".DEL");
		File certNoFile = new File(sb.toString());// 指定要写入的文件
		//如果文件不存在，创建文件
		if(!FileUtils.existsFile(certNoFile.getAbsolutePath()))certNoFile.createNewFile();
        // 获取该文件的缓冲输出流  
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(certNoFile.getAbsolutePath(), true), Charset.forName("ISO-8859-1")));
		CsvWriter csvWriter = new CsvWriter(bufferedWriter, ',');
		csvWriter.writeRecord(record);
		bufferedWriter.close();
		csvWriter.close();
	}

}
