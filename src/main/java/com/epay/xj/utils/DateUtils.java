package com.epay.xj.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat();
    
    /** "yyyy-MM-dd HH:mm:ss"格式类型 */
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

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
     * @Title: yyyyMMdd
     * @Description:
     * @param: @param dateText
     * @param: @return
     * @return: Date
     * @author yanghf
     * @Date 2018年6月28日 上午11:43:35
     */
    public synchronized static Date formatTimeStamp(String timeStampStr) {
        dateFormat.applyPattern(DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS);
        try {
            return dateFormat.parse(timeStampStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description:当前时间加上指定天数后的时间
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
    public synchronized static String yyyyMMddToString(Date date) {
        dateFormat.applyPattern(DateUtils.yyyyMMdd);
        return dateFormat.format(date);
    }

    public static String[] getOneYearDate(String date, int days) {
        List<String> dateList = new ArrayList<String>();
        Date cDate = yyyyMMddToDate(date);
        dateList.add(yyyyMMddToString(cDate));
        for (int i = 1; i < days; i++) {
            Date eDate = currentAddDays(cDate, -i);
            dateList.add(yyyyMMddToString(eDate));
        }
        String[] arrayDate = new String[dateList.size()];
        return dateList.toArray(arrayDate);
    }
    
    /**
     * 判断当前时间是否在指定时间范围内
     * @param begin
     * @param end
     * @param now
     * @return
     */
    public synchronized static boolean judge(Timestamp begin,Timestamp end,Timestamp now)
    {
    	long b = begin.getTime();
    	long e = end.getTime();
    	long n = now.getTime();
    	if(n>=b&& n<=e)return true;
    	return false;
    }
    
    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Timestamp date1,Timestamp date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }
    
    
    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int compare(Date date1,Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()));
        return days;
    }
    
    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int compare(String dateBegin,String dateEnd)
    {
    	Date date1 = formatTimeStamp(dateBegin);
    	Date date2 = formatTimeStamp(dateEnd);
        int days = (int) ((date2.getTime() - date1.getTime()));
        return days;
    }

    /**
     * @Description: 获取某个日期N个月前的日期
     * @param date
     * @param monthAmount
     * @return 
     * @author LZG
     * @date 2018年07月03日
     */
    public static synchronized String getDateOfXMonthsAgo(String date, int monthAmount) {

        Date currentDate = yyyyMMddToDate(date);
        // 获得一个日历的实例
        Calendar c = Calendar.getInstance();
        // 设置日历时间
        c.setTime(currentDate);
        // 在日历的月份上减去N个月
        c.add(Calendar.MONTH, -monthAmount);
        return DateUtils.yyyyMMddToString(c.getTime());
    }
    
    
    public static synchronized Timestamp getDateOfXMonthsAgo(Timestamp date, int monthAmount) {
    	Timestamp now = new Timestamp(System.currentTimeMillis());   
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");   
        // 获得一个日历的实例
        Calendar c = Calendar.getInstance();
        // 设置日历时间
        c.setTime(date);
        // 在日历的月份上减去N个月
        c.add(Calendar.MONTH, -monthAmount);
        return new Timestamp(c.getTimeInMillis());
    }
    
    /**
     * 获取当前天数
     * @param date
     * @param days
     */
    public static synchronized Timestamp getDateOfXDaysAgo(Timestamp date, int days) {
        // 获得一个日历的实例
        Calendar c = Calendar.getInstance();
        // 设置日历时间
        c.setTime(date);
        // 在日历的月份上减去N个月
        c.add(Calendar.DAY_OF_MONTH, -days);
        return new Timestamp(c.getTimeInMillis());
    }
    
    
    
    /**
     * @Description: 获取现在时间str
     * @param pattern
     * @return 
     * @author LZG
     * @date 2018年07月07日
     */
    public static synchronized String getNow() {
        dateFormat.applyPattern(DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS);
        return dateFormat.format(new Date());
    }
    
    /**
     * @Description: 转换为全时间
     * @param dateText
     * @return 
     * @author LZG
     * @date 2018年07月07日
     */
    public synchronized static Date toFullDateTime(String dateText) {
        dateFormat.applyPattern(DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS);
        try {
            return dateFormat.parse(dateText);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * @Description: 通过日期计算两个日期之间的天数(20160909 - 20181001)
     * @param dateBegin
     * @param dateEnd
     * @return 
     * @author LZG
     * @date 2018年07月11日
     */
    public static  int getIntervalDayAmount(Date dateBegin, Date dateEnd) {
      
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(dateBegin);
        
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(dateEnd);
        
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        //同一年
        if(year1 != year2) {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++) {
                //闰年
                if(i%4==0 && i%100!=0 || i%400==0) {
                    timeDistance += 366;
                } else  {    //不是闰年
                    timeDistance += 365;
                }
            }
            
            return timeDistance + (day2-day1) ;
        } else { //不同年
            return day2-day1;
        }
        
    }

    
    public static int getTrueDays(Timestamp end,Timestamp start){
		Calendar aCalendar = Calendar.getInstance();
        Calendar bCalendar = Calendar.getInstance();
        aCalendar.setTime(end);
        bCalendar.setTime(start);
        int days = 0;
        while(aCalendar.before(bCalendar)){
            days++;
            aCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return days;
    }
    public static void main(String[] args) {
        //test1();
        //test2();
        //test3();
        test4();
    }


    /**
     * @Description:  
     * @author LZG
     * @date 2018年07月12日
     */
      	
    private static void test4() {
        int result = DateUtils.getTrueDays(new Timestamp(DateUtils.yyyyMMddToDate("20180711").getTime()), new Timestamp(DateUtils.yyyyMMddToDate("20180911").getTime()));
        System.out.println(result);
    }


    /**
     * @Description:  
     * @author LZG
     * @date 2018年07月12日
     */
      	
    private static void test3() {
        Timestamp date = new Timestamp(DateUtils.yyyyMMddToDate("20180101").getTime()) ;
        Timestamp t = getDateOfXMonthsAgo(date, 3);
        t.getTime();
    }
    
    public static void test1() {
        String[] arr = getOneYearDate("20180401", 365);
        for (String string : arr) {
            System.out.println(string);
        }
    }
    
    public static void test2() {
        //12个月前的日期
        String str_12m_before = getDateOfXMonthsAgo("20180631", 12);
        System.out.println("20180631, 12个月前的日期是：" + str_12m_before);
        
        //6个月前的日期
        String str_6m_before = getDateOfXMonthsAgo("20180631", 6);
        System.out.println("20180631, 6个月前的日期是：" + str_6m_before);
        
        //3个月前的日期
        String str_3m_before = getDateOfXMonthsAgo("20180631", 3);
        System.out.println("20180631, 3个月前的日期是：" + str_3m_before);
        
    }

}
