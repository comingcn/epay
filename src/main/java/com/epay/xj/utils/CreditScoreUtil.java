/**   
 * @Title: CreditScoreUtil.java 
 * @Package: com.epay.xj.utils 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018 https://onezg.cnblogs.com
 */
package com.epay.xj.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Description: 信用分工具类
 * @author LZG
 * @date 2018年07月19日 
 */
public class CreditScoreUtil {
    
    public static String sf_s_rcd_yebz_pct_j3m = "sf_s_rcd_yebz_pct_j3m"; //1
    public static String sf_s_rcd_suces_j2m_pct = "sf_s_rcd_suces_j2m_pct"; //2
    public static String sf_s_latesttn_fail_xj = "sf_s_latesttn_fail_xj"; //3
    public static String ovd3_1d_dk_amt_sum_j3m = "ovd3_1d_dk_amt_sum_j3m"; //4
    public static String sf_s_rcd_suces_dk_cnt_j6m = "sf_s_rcd_suces_dk_cnt_j6m"; //5
    public static String sf_s_mer_suces_dk_cnt_j12m = "sf_s_mer_suces_dk_cnt_j12m"; //6
    public static String aud_all_rcd_disntcd_all_avg_j1m = "aud_all_rcd_disntcd_all_avg_j1m"; //7
    public static String sf_s_rcd_fail_pct_j12m = "sf_s_rcd_fail_pct_j12m"; //8
    public static String aud_dbt_rcd_nearist_days = "aud_dbt_rcd_nearist_days"; //9
    public static String aud_dbt_rcd_disntcd_all_min_j6m = "aud_dbt_rcd_disntcd_all_min_j6m"; //10
    
    public static HashMap<String, LinkedHashMap<String, String>> creditScoreMap = new HashMap<String, LinkedHashMap<String, String>>();
    
    static {
        
        //1
        LinkedHashMap<String, String> firstDataMap = new LinkedHashMap<String, String>();
        firstDataMap.put("-1.0, -1.0", "60.7644311470233");
        firstDataMap.put("0.0127388535032, 0.321428571429", "38.299444542964");
        firstDataMap.put("0.333333333333, 0.647058823529", "58.8007974564224");
        firstDataMap.put("0.658536585366, 1.0", "80.4072861528077");
        creditScoreMap.put(sf_s_rcd_yebz_pct_j3m, firstDataMap);
        
        //2
        LinkedHashMap<String, String> secondDataMap = new LinkedHashMap<String, String>();
        secondDataMap.put("-1.0, -1.0", "63.8992411344539");
        secondDataMap.put("0.00140829614456, 0.679771571928", "63.5680264844452");
        secondDataMap.put("0.680848181852, 1.0", "49.2914363654322");
        creditScoreMap.put(sf_s_rcd_suces_j2m_pct, secondDataMap);
        
        //3
        LinkedHashMap<String, String> thirdDataMap = new LinkedHashMap<String, String>();
        thirdDataMap.put("-1.0, -1.0", "62.8680479874307");
        thirdDataMap.put("1.0, 24.0", "75.1543180568734");
        thirdDataMap.put("134.0, 365.0", "45.7248621905461");
        thirdDataMap.put("25.0, 89.0", "52.5463682181521");
        thirdDataMap.put("90.0, 133.0", "47.7773604602879");
        creditScoreMap.put(sf_s_latesttn_fail_xj, thirdDataMap);
        
        //4
        LinkedHashMap<String, String> fourthDataMap = new LinkedHashMap<String, String>();
        fourthDataMap.put("-1.0, -1.0", "60.5163994507447");
        fourthDataMap.put("0.0, 394.41", "45.4366343971397");
        fourthDataMap.put("396.79, 6460906.31", "90.5579086533974");
        creditScoreMap.put(ovd3_1d_dk_amt_sum_j3m, fourthDataMap);
        
        //5
        LinkedHashMap<String, String> fifthDataMap = new LinkedHashMap<String, String>();
        fifthDataMap.put("-1.0, -1.0", "75.2527416028418");
        fifthDataMap.put("1.0, 1.0", "62.6476356799331");
        fifthDataMap.put("13.0, 176.0", "27.190561376491");
        fifthDataMap.put("2.0, 3.0", "60.4276698407474");
        fifthDataMap.put("4.0, 8.0", "54.0058303733566");
        fifthDataMap.put("9.0, 12.0", "40.9307631492528");
        creditScoreMap.put(sf_s_rcd_suces_dk_cnt_j6m, fifthDataMap);
        
        //6
        LinkedHashMap<String, String> sixthDataMap = new LinkedHashMap<String, String>();
        sixthDataMap.put("-1.0, -1.0", "71.5579972732241");
        sixthDataMap.put("1.0, 1.0", "55.8360319897017");
        sixthDataMap.put("2.0, 2.0", "51.9836470892321");
        sixthDataMap.put("3.0, 3.0", "44.5346831980094");
        sixthDataMap.put("4.0, 11.0", "32.655837013259");
        creditScoreMap.put(sf_s_mer_suces_dk_cnt_j12m, sixthDataMap);
        
        //7
        LinkedHashMap<String, String> seventhDataMap = new LinkedHashMap<String, String>();
        seventhDataMap.put("-1.0, -1.0", "56.0002971405606");
        seventhDataMap.put("1.0, 1.66666666667", "61.5888087222431");
        seventhDataMap.put("2.0, 2.75", "69.4604919736587");
        seventhDataMap.put("3.0, 124.0", "78.9387038825824");
        creditScoreMap.put(aud_all_rcd_disntcd_all_avg_j1m, seventhDataMap);
        
        //8
        LinkedHashMap<String, String> eighthDataMap = new LinkedHashMap<String, String>();
        eighthDataMap.put("-1.0, -1.0", "69.4650200783725");
        eighthDataMap.put("0.0105263157895, 0.12389380531", "26.4187820921789");
        eighthDataMap.put("0.125, 0.177777777778", "42.9469952996731");
        eighthDataMap.put("0.178571428571, 0.415584415584", "52.6998359024811");
        eighthDataMap.put("0.416666666667, 0.631578947368", "62.7998989534916");
        eighthDataMap.put("0.632653061224, 1.0", "84.0901007222972");
        creditScoreMap.put(sf_s_rcd_fail_pct_j12m, eighthDataMap);
        //dataMap.clear();
        
        //9
        LinkedHashMap<String, String> ninthDataMap = new LinkedHashMap<String, String>();
        ninthDataMap.put("-1.0, -1.0", "91.864615560968");
        ninthDataMap.put("1.0, 3.0", "80.1131529682269");
        ninthDataMap.put("13.0, 92.0", "58.1029383414308");
        ninthDataMap.put("4.0, 12.0", "64.6321197936348");
        ninthDataMap.put("93.0, 339.0", "50.8153181460366");
        creditScoreMap.put(aud_dbt_rcd_nearist_days, ninthDataMap);
        
        //10
        LinkedHashMap<String, String> tenthDataMap = new LinkedHashMap<String, String>();
        tenthDataMap.put("-1.0, -1.0", "79.2283807546826");
        tenthDataMap.put("1.0, 1.0", "65.3555631309926");
        tenthDataMap.put("2.0, 3.0", "61.6570004507229");
        tenthDataMap.put("4.0, 132.0", "53.9294105020816");
        creditScoreMap.put(aud_dbt_rcd_disntcd_all_min_j6m, tenthDataMap);
        /**
         * {1.0, 1.0=65.3555631309926, 2.0, 3.0=61.6570004507229,
         * -1.0, -1.0=79.2283807546826, 4.0, 132.0=53.9294105020816}
         */
        
    }
    
    /**
     * @Description: 根据信用分类型和odi值获取信用分。
     * @param cerditSocreType
     * @param odiValue
     * @return 
     * @author LZG
     * @date 2018年07月19日
     */
    public static BigDecimal getCreditScoreByCertScoreType(String cerditSocreType, BigDecimal odiValue) {
        HashMap<String, String> cerditScoreTypeMap = creditScoreMap.get(cerditSocreType);
        
        if(null == odiValue) {
            return new BigDecimal("0");
        }
        
        List<String> rangeKeyList = new ArrayList<String>();
        for(String key : cerditScoreTypeMap.keySet()) {
            rangeKeyList.add(key);
        }
        
        for(int i = 0; i < rangeKeyList.size(); i++) {
            String[] rangeArray  = rangeKeyList.get(i).split(",");
            BigDecimal firstRange = new BigDecimal(rangeArray[0].trim());
            BigDecimal secondRange = new BigDecimal(rangeArray[1].trim());
            
            
            if(odiValue.compareTo(firstRange) < 0 || odiValue.compareTo(firstRange) == 0) {
                return new BigDecimal(cerditScoreTypeMap.get(rangeKeyList.get(i)));
            }
            
            if(odiValue.compareTo(secondRange) > 0) {
                if(i + 1 < rangeKeyList.size()) {
                    String[] nextRangeArray = rangeKeyList.get(i + 1).split(",");
                    BigDecimal nextFirstRange = new BigDecimal(nextRangeArray[0].trim());
                    if(compareRange(odiValue, secondRange, nextFirstRange)) {
                        return new BigDecimal(cerditScoreTypeMap.get(rangeKeyList.get(i)));
                    } 
                    
                } else {
                    return new BigDecimal(cerditScoreTypeMap.get(rangeKeyList.get(i)));
                }
            }
            
        }
        
        return new BigDecimal("0");
        
    }
    
    /**
     * @Description: 判断odi的值所在的范围
     * @param odiValue
     * @param firstValue
     * @param secondValue
     * @return 
     * @author LZG
     * @date 2018年07月19日
     */
    private static boolean compareRange(BigDecimal odiValue, BigDecimal firstValue, BigDecimal secondValue) {
        
        if(odiValue.compareTo(firstValue) == 0 || odiValue.compareTo(secondValue) == 0) 
            return true;
        
        if(odiValue.compareTo(firstValue) > 0 && odiValue.compareTo(secondValue) < 0) 
            return true;
        
        return false;
    }
    
    // test
    public static void main(String[] args) {
        
        test1();
        System.out.println("--------------------------------------");
        
        //第1种情况：等于边界值
        BigDecimal number1 = new BigDecimal("-1.0000");
        BigDecimal r1 = CreditScoreUtil.getCreditScoreByCertScoreType(CreditScoreUtil.aud_dbt_rcd_disntcd_all_min_j6m, number1);
        System.out.println(r1);
        
        //第2种情况：位于边界值后，但又小于下一个范围首值
        BigDecimal number2 = new BigDecimal("3.5000");
        BigDecimal r2 = CreditScoreUtil.getCreditScoreByCertScoreType(CreditScoreUtil.aud_dbt_rcd_disntcd_all_min_j6m, number2);
        System.out.println(r2);
        
        //第3种情况：大于最大值
        BigDecimal number3 = new BigDecimal("140.0000");
        BigDecimal r3 = CreditScoreUtil.getCreditScoreByCertScoreType(CreditScoreUtil.aud_dbt_rcd_disntcd_all_min_j6m, number3);
        System.out.println(r3);
        
        //10
//        dataMap.put("-1.0, -1.0", "79.2283807546826");
//        dataMap.put("1.0, 1.0", "65.3555631309926");
//        dataMap.put("2.0, 3.0", "61.6570004507229");
//        dataMap.put("4.0, 132.0", "53.9294105020816");
//        creditScoreMap.put(yl_card_audit_dbt_rcd_disntcd_all_j6m_min, dataMap);
//        dataMap.clear();

    }

    /**
     * @Description: compareRange方法测试
     * @author LZG
     * @date 2018年07月19日
     */
    private static void test1() {
        BigDecimal odiValue1 = new BigDecimal("-1.0000");
        BigDecimal firstValue1 = new BigDecimal("-1.0");
        BigDecimal secondValue1 = new BigDecimal("-1.0");
        System.out.println(compareRange(odiValue1, firstValue1, secondValue1)); //true
        
        //0.125, 0.177777777778
        BigDecimal odiValue2 = new BigDecimal("0.1262");
        BigDecimal firstValue2 = new BigDecimal("0.125");
        BigDecimal secondValue2 = new BigDecimal("0.177777777778");
        System.out.println(compareRange(odiValue2, firstValue2, secondValue2)); //true
    }
    
}
