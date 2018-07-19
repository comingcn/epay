/**   
 * @Title: MathUtil.java 
 * @Package: com.epay.xj.utils 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018 https://onezg.cnblogs.com
 */
package com.epay.xj.utils;

import java.math.BigDecimal;

/**
 * @Description: 计算工具类
 * @author LZG
 * @date 2018年07月10日
 */
public class MathUtil {

    /**
     * @Description: 除(保留两位小数,四舍五入)
     * @param number1
     * @param number2
     * @return
     * @author LZG
     * @date 2018年07月10日
     */
    public static BigDecimal divide(int number1, int number2) {
        // 一家逾期机构都没有,说明这个人没有逾期,平均逾期自然是0
        if (number2 == 0)
            return new BigDecimal("0").setScale(0,BigDecimal.ROUND_HALF_UP);

        double result = (double) number1 / number2;

        if (result == 0.00)
            return new BigDecimal("0").setScale(0,BigDecimal.ROUND_HALF_UP);
        
        BigDecimal value = new BigDecimal(result).setScale(4, BigDecimal.ROUND_HALF_UP);
        return value;
    }
    
    /**
     * @Description: 除(保留两位小数,四舍五入)
     * @param number1
     * @param number2
     * @return
     * @author LZG
     * @date 2018年07月10日
     */
    public static BigDecimal divide(BigDecimal number1, BigDecimal number2) {
        // 一家逾期机构都没有,说明这个人没有逾期,平均逾期自然是0
        if (number2.equals(BigDecimal.ZERO)) 
            return new BigDecimal("0").setScale(0,BigDecimal.ROUND_HALF_UP);

        BigDecimal result = number1.divide(number2, 4, BigDecimal.ROUND_HALF_UP);
        return result;
    }
    
    /**
     * @Description: 加计算
     * @param args
     * @return 
     * @author LZG
     * @date 2018年07月19日
     */
    public static int plus(BigDecimal ...args) {
        
        BigDecimal result = new BigDecimal("0");
        for(int i = 0; i < args.length; i++) {
            result = result.add(args[i]);
        }
        //四舍五入
        result = result.setScale(0, BigDecimal.ROUND_HALF_UP);
        return Integer.parseInt(result.toString());
    }

    // 测试
    public static void main(String[] args) {
//        System.out.println(MathUtil.divide(10, 0));
//        System.out.println(MathUtil.divide(9, 8));
//        System.out.println(MathUtil.divide(0, 8));
//        System.out.println(MathUtil.divide(1, 8));
        
        BigDecimal number1 = new BigDecimal("10");
        BigDecimal number2 = new BigDecimal("0");
        BigDecimal number3 = new BigDecimal("8");
        
        System.out.println(MathUtil.divide(number1, number2));
        System.out.println(MathUtil.divide(number1, number3));
        
        System.out.println(MathUtil.plus(number1, number2, new BigDecimal("0.33")));
        System.out.println(MathUtil.plus(number1, number2, new BigDecimal("0.52")));
      
    }

}
