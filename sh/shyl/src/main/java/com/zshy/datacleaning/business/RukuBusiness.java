/**   
 * @Title: RukuBusiness.java 
 * @Package: com.zshy.datacleaning.business 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018 北京中数合一科技有限公司
 */
package com.zshy.datacleaning.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;

import com.zshy.datacleaning.dao.TradeDetailDao;
import com.zshy.datacleaning.dao.TradeDetailDaoImpl;
import com.zshy.datacleaning.domain.TradeDetail;

/**
 * @Description: 入库
 * @author LZG
 * @date 2018年07月02日
 */
public class RukuBusiness {

	public static void main(String[] args) {
		ruku(new File("d:\\hello.txt"));
	}

	/**
	 * @Description:
	 * @param file
	 * @author LZG
	 * @throws Exception
	 * @date 2018年07月02日
	 */
	public static void ruku(File file) {

		long startTime = System.currentTimeMillis();

		TradeDetailDao tradeDetailDao = new TradeDetailDaoImpl();

		FileInputStream fis = null;
		BufferedReader reader = null;

		try {
			File testFile = new File(
					"e:\\上海\\EXP_TRA_TRADE_DETAIL_PARA_20180102.DEL"); // 1.8G数据
			File testFile2 = new File(
					"e:\\上海\\EXP_TRA_TRADE_DETAIL_PARA_20180101.DEL"); // 几条测试数据

			fis = new FileInputStream(testFile2);

			// 用5M的缓冲读取文本文件
			reader = new BufferedReader(new InputStreamReader(fis, "utf-8"),
					5 * 1024 * 1024);

			String line = "";
			TradeDetail tradeDetail = new TradeDetail();
			while ((line = reader.readLine()) != null) {
				
				// System.out.println(line);
				String[] tradeDetailArr = line.split(",");
				tradeDetail.setTxn_date(tradeDetailArr[0].replace("\"", ""));
				String txn_seq_id = tradeDetailArr[1];
				// System.out.println(txn_seq_id + ", length: " +
				// txn_seq_id.length());
				tradeDetail.setTxn_seq_id(txn_seq_id);
				tradeDetail.setCert_no(tradeDetailArr[2].replace("\"", ""));
				tradeDetail.setCard_no(tradeDetailArr[3].replace("\"", ""));
				tradeDetail.setMer_id(tradeDetailArr[4].replace("\"", ""));
				tradeDetail.setMer_type(Integer.valueOf(tradeDetailArr[5]));
				tradeDetail.setAmount(new BigDecimal(tradeDetailArr[6]));
				tradeDetail.setSF_TYPE(tradeDetailArr[7].replace("\"", ""));
				tradeDetail.setReturn_code(tradeDetailArr[8].replace("\"", ""));
				// System.out.println(tradeDetail);
				
				try {
					tradeDetailDao.add(tradeDetail); // 往数据库表写数据
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		long endTime = System.currentTimeMillis();
		float excSecondTime = (float) (endTime - startTime) / 1000;
		float excMinuteTime = (float) (endTime - startTime) / 1000 / 60;

		System.out.println("ruku()执行时间：" + excSecondTime + "秒");
		System.out.println("ruku()执行时间：" + excMinuteTime + "分");

	}
}
