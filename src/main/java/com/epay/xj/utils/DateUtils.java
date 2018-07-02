package com.epay.xj.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat();
	
	/**
	 * yyyyMMdd时间格式
	 */
	private static final String yyyyMMdd = "yyyyMMdd";
	
	
	/**
	 * @Title: yyyyMMdd 
	 * @Description: 
	 * @param: @param dateText
	 * @param: @return    
	 * @return: Date    
	 * @author yanghf
	 * @Date 2018年6月28日 上午11:43:35
	 */
	public synchronized static Date yyyyMMddToDate(String dateText) {
		dateFormat.applyPattern(DateUtils.yyyyMMdd);
		try {
			return dateFormat.parse(dateText);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @Description:当前时间加上指定天数后的时间
	 * 
	 */
	public static Date currentAddDays(Date date, int days) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.DATE, days);
		return ca.getTime();
	}
	
	/**
	 * @Title: yyyyMMdd 
	 * @Description: 时间转换
	 * @param: @param date
	 * @return: String    
	 * @author yanghf
	 * @Date 2018年5月10日 下午2:17:18
	 */
	public synchronized static String yyyyMMddToString(Date date){
		dateFormat.applyPattern(DateUtils.yyyyMMdd);
		return dateFormat.format(date);
	}
	public static String[] getOneYearDate(String date,int days){
		List<String> dateList = new ArrayList<String>();
		Date cDate = yyyyMMddToDate(date);
		dateList.add(yyyyMMddToString(cDate));
		for(int i=1;i<days;i++){
			Date eDate = currentAddDays(cDate,-i);
			dateList.add(yyyyMMddToString(eDate));
		}
		String[] arrayDate = new String[dateList.size()];
		return dateList.toArray(arrayDate);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] arr =  getOneYearDate("20180401",365);
		for (String string : arr) {
			System.out.println(string);
		}
	}

	
}
