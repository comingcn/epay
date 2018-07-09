/**   
 * @Title: ACCFileReader.java 
 * @Package: com.epay.xj.datafromyltolocal 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018
 */
package com.epay.xj.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.epay.xj.dao.ACCTradeDetailDao;
import com.epay.xj.dao.CertNoDao;
import com.epay.xj.dao.impl.ACCTradeDetailDaoImpl;
import com.epay.xj.dao.impl.CertNoDaoImpl;
import com.epay.xj.domain.ACCTradeDetail;
import com.epay.xj.domain.CertNoDomain;
import com.epay.xj.utils.DateUtils;
import com.epay.xj.utils.TimestampUtil;

/**
 * @Description: 银联数据从ACC进本地数据库处理类
 * @author LZG
 * @date 2018年07月06日
 */
public class ACCFileHandler {
    
    private static ACCTradeDetailDao accTradeDetailDao;
    private static CertNoDao certNoDao;
    
    static {
        accTradeDetailDao  = new ACCTradeDetailDaoImpl();
        certNoDao = new CertNoDaoImpl();
    }
  

    /**
     * @Description: 入口
     * @param args 
     * @author LZG
     * @date 2018年07月06日
     */
    public static void main(String[] args) {
        System.out.println("开始时间：" + DateUtils.getNow());
        
        System.out.println("ACC文件正在导入数据库，请耐心等待……");
        
        File acaFile = new File("E:\\ACA");
        doImport(acaFile);
        System.out.println("结束时间：" + DateUtils.getNow());
    }

    /**
     * @Description: 1、读取 2、处理 3、导入
     * @param file
     * @return boolean 导入数据库是否成功
     * @author LZG
     * @date 2018年07月06日
     */
    public static boolean doImport(File file) {
        
        
        System.out.println("正在生成trade_detail表,请耐心等候……");
    

        Set<String> idCardSet = new HashSet<String>();
        StringBuffer str = new StringBuffer("");

        try {
            FileInputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader in = new BufferedReader(isr);

            String line = null;
            String onePersonDataStr = "";

            while ((line = in.readLine()) != null) {
                
                str.append(line);
                
                //取出一条数据往数据库存储
                if(str.toString().endsWith("]]")) {
                    
                    onePersonDataStr = str.toString();
                    str = new StringBuffer("");
                    onePersonDataStr = onePersonDataStr.substring(onePersonDataStr.indexOf("[["));
                    //把开头的[和结尾的]砍掉
                    onePersonDataStr = onePersonDataStr.substring(1);
                    String[] detailArray = onePersonDataStr.split("],");
                    
                    //这个人 的身份证号
                    String idCardStr = dataDeal(detailArray);
                    idCardSet.add(idCardStr);
                    
                    onePersonDataStr = "";
                    
                }
            }
           
            //关闭操作
            in.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        System.out.println("生成trade_detail结束");
        
        //身份证表处理
        System.out.println("导入身份证表开始,请耐心等候……");
        
        idCardDeal(idCardSet);
        
        System.out.println("导入身份证表结束");
        
        return true;
    }

    /**
     * @Description: 身份证表数据处理
     * @param idCardSet 
     * @author LZG
     * @date 2018年07月07日
     */
    private static void idCardDeal(Set<String> idCardSet) {
        
        List<CertNoDomain> certNoDomainList = new ArrayList<>();
        
        Iterator<String> it = idCardSet.iterator();
        
        while (it.hasNext()) {
          String idCardStr = it.next();
          
          CertNoDomain certNoDomain = new CertNoDomain();
          certNoDomain.setCertNo(idCardStr);
          certNoDomain.setUpdateTime(DateUtils.yyyyMMddToString(new Date()));
          
          certNoDomainList.add(certNoDomain);
        }
        
//        int size = certNoDomainList.size();
//        for(int i = 0; i < size; i++) {
//            System.out.println(certNoDomainList.get(i).getCertNo());
//        }
        
        //插入
        certNoDao.add(certNoDomainList);
    }

    /**
     * @Description: tradeDetail 处理
     * @param idCardSet
     * @param detailArray 
     * @return
     * @author LZG
     * @date 2018年07月07日
     */
    private static String dataDeal(String[] detailArray) {
        
        String idCardResultStr = "";
        
        List<ACCTradeDetail> accTradeDetailList = new ArrayList<ACCTradeDetail>();
        //一个人的所有交易记录数据
        for (int i = 0; i < detailArray.length; i++) {
            String oneDetail = detailArray[i].substring(1);
            //System.out.println(oneDetail);
            String[] attributeArray = oneDetail.split(",");
            
            ACCTradeDetail accTradeDetail = new ACCTradeDetail();
            
            String dateStr = TimestampUtil.stampToDate(attributeArray[0]);
            Date dataDate = DateUtils.toFullDateTime(dateStr);
            accTradeDetail.setCreateTime(new Timestamp(dataDate.getTime()));
            
            accTradeDetail.setId(attributeArray[1]);
            
            String idCardStr = attributeArray[2].replace("\"", "");
            accTradeDetail.setIdCard(idCardStr);
            
            idCardResultStr = idCardStr;
            
            accTradeDetail.setAccountNo(attributeArray[3].replace("\"", "").trim());
            accTradeDetail.setSourceMerno(attributeArray[4].replace("\"", "").trim());
            accTradeDetail.setMerType(attributeArray[5]);
            accTradeDetail.setAmout(new BigDecimal(attributeArray[6]));
            accTradeDetail.setSfType(attributeArray[7].replace("\"", "").trim());
            accTradeDetail.setReturnCode(attributeArray[8].replace("\"", "").trim());
            
//            String returnCode = attributeArray[8].replace("\"", "");
//            if(returnCode.startsWith("0"))
//                returnCode = "0000";
//            if(returnCode.startsWith("8") || returnCode.startsWith("3"))
//                returnCode = "3008";
//            accTradeDetail.setReturnCode(returnCode);
            
            accTradeDetailList.add(accTradeDetail);
        }
        
        //插入数据
        accTradeDetailDao.add(accTradeDetailList);
        
        return idCardResultStr;
    }
}
