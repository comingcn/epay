/**   
 * @Title: TimestampUtil.java 
 * @Package: com.epay.xj.utils 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018
 */
package com.epay.xj.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 时间戳工具类
 * @author LZG
 * @date 2018年07月06日
 */
public class TimestampUtil {

    // 测试
    public static void main(String[] args) {
        String dateStr = stampToDate("1507424201217");
        System.out.println(dateStr);
    }

    /**
     * @Description:将时间戳转换为时间Str
     * @param s
     * @return
     * @author LZG
     * @date 2018年07月06日
     */
    public static String stampToDate(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        long lt = new Long(s);
        Date date = new Date(lt);
        return simpleDateFormat.format(date);
    }
    
}
