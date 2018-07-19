/**   
 * @Title: CreditScoreUtil.java 
 * @Package: com.epay.xj.utils 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018 https://onezg.cnblogs.com
 */
package com.epay.xj.utils;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @Description: 信用分工具
 * @author LZG
 * @date 2018年07月19日 
 */
public class CreditScoreUtil {
    
    public static String yl_s_rcd_yebz_j3m_pct; //1
    public static String yl_s_amt_success_j2m_pct; //2
    public static String yl_s_latesttn_fail_xj; //3
    public static String overdue3_1d_amt_sum_j3m; //4
    public static String sf_s_rcd_suces_dk_cnt_j6m; //5
    public static String yl_f_mer_success_j12m_dk_cnt; //6
    public static String yl_card_audit_all_rcd_disntcd_all_j1m; //7
    public static String yl_s_rcd_fail_j12m_pct; //8
    public static String yl_card_audit_dbt_rcd_nearist_days; //9
    public static String yl_card_audit_dbt_rcd_disntcd_all_j6m_min; //10
    
    public static HashMap<String, String> dataMap = new HashMap<String, String>();
    public static HashMap<String, HashMap<String, String>> creditScoreMap = new HashMap<String, HashMap<String, String>>();
    
    static {
        
        //1
        dataMap.put("-1.0, -1.0", "60.7644311470233");
        dataMap.put("0.0127388535032, 0.321428571429", "38.299444542964");
        dataMap.put("0.333333333333, 0.647058823529", "58.8007974564224");
        dataMap.put("0.658536585366, 1.0", "80.4072861528077");
        creditScoreMap.put(yl_s_rcd_yebz_j3m_pct, dataMap);
        dataMap.clear();
        
        //2
        dataMap.put("-1.0, -1.0", "63.8992411344539");
        dataMap.put("0.00140829614456, 0.679771571928", "63.5680264844452");
        dataMap.put("0.680848181852, 1.0", "49.2914363654322");
        creditScoreMap.put(yl_s_amt_success_j2m_pct, dataMap);
        dataMap.clear();
        
        //3
        dataMap.put("-1.0, -1.0", "62.8680479874307");
        dataMap.put("1.0, 24.0", "75.1543180568734");
        dataMap.put("134.0, 365.0", "45.7248621905461");
        dataMap.put("25.0, 89.0", "52.5463682181521");
        dataMap.put("90.0, 133.0", "47.7773604602879");
        creditScoreMap.put(yl_s_latesttn_fail_xj, dataMap);
        dataMap.clear();
        
        //4
        dataMap.put("-1.0, -1.0", "60.5163994507447");
        dataMap.put("0.0, 394.41", "45.4366343971397");
        dataMap.put("396.79, 6460906.31", "90.5579086533974");
        creditScoreMap.put(overdue3_1d_amt_sum_j3m, dataMap);
        dataMap.clear();
        
        //5
        dataMap.put("-1.0, -1.0", "75.2527416028418");
        dataMap.put("1.0, 1.0", "62.6476356799331");
        dataMap.put("13.0, 176.0", "27.190561376491");
        dataMap.put("2.0, 3.0", "60.4276698407474");
        dataMap.put("4.0, 8.0", "54.0058303733566");
        dataMap.put("9.0, 12.0", "40.9307631492528");
        creditScoreMap.put(sf_s_rcd_suces_dk_cnt_j6m, dataMap);
        dataMap.clear();
        
        //6
        dataMap.put("-1.0, -1.0", "71.5579972732241");
        dataMap.put("1.0, 1.0", "55.8360319897017");
        dataMap.put("2.0, 2.0", "51.9836470892321");
        dataMap.put("3.0, 3.0", "44.5346831980094");
        dataMap.put("4.0, 11.0", "32.655837013259");
        creditScoreMap.put(yl_f_mer_success_j12m_dk_cnt, dataMap);
        dataMap.clear();
        
        //7
        dataMap.put("-1.0, -1.0", "56.0002971405606");
        dataMap.put("1.0, 1.66666666667", "61.5888087222431");
        dataMap.put("2.0, 2.75", "69.4604919736587");
        dataMap.put("3.0, 124.0", "78.9387038825824");
        creditScoreMap.put(yl_card_audit_all_rcd_disntcd_all_j1m, dataMap);
        dataMap.clear();
        
        //8
        dataMap.put("-1.0, -1.0", "69.4650200783725");
        dataMap.put("0.0105263157895, 0.12389380531", "26.4187820921789");
        dataMap.put("0.125, 0.177777777778", "42.9469952996731");
        dataMap.put("0.178571428571, 0.415584415584", "52.6998359024811");
        dataMap.put("0.416666666667, 0.631578947368", "62.7998989534916");
        dataMap.put("0.632653061224, 1.0", "84.0901007222972");
        creditScoreMap.put(yl_s_rcd_fail_j12m_pct, dataMap);
        dataMap.clear();
        
        //9
        dataMap.put("-1.0, -1.0", "91.864615560968");
        dataMap.put("1.0, 3.0", "80.1131529682269");
        dataMap.put("13.0, 92.0", "58.1029383414308");
        dataMap.put("4.0, 12.0", "64.6321197936348");
        dataMap.put("93.0, 339.0", "50.8153181460366");
        creditScoreMap.put(yl_card_audit_dbt_rcd_nearist_days, dataMap);
        dataMap.clear();
        
        //10
        dataMap.put("-1.0, -1.0", "79.2283807546826");
        dataMap.put("1.0, 1.0", "65.3555631309926");
        dataMap.put("2.0, 3.0", "61.6570004507229");
        dataMap.put("4.0, 132.0", "53.9294105020816");
        creditScoreMap.put(yl_card_audit_dbt_rcd_disntcd_all_j6m_min, dataMap);
        dataMap.clear();
        
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
        HashMap<String, String> certScoreTypeMap = creditScoreMap.get(cerditSocreType);
        
        for(String key : certScoreTypeMap.keySet()) {
            String[] rangeArray = key.split(",");
            BigDecimal firstRange = new BigDecimal(rangeArray[0]);
            BigDecimal secondRange = new BigDecimal(rangeArray[1]);
            if(compareRange(odiValue, firstRange, secondRange)) {
                String resultStr = certScoreTypeMap.get(key);
                return new BigDecimal(resultStr);
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
    
    //test compareRange
    public static void main(String[] args) {
        
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
