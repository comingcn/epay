/**   
 * @Title: OrgNumberNormService.java 
 * @Package: com.epay.xj.service 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018 北京中数合一科技有限公司
 */
package com.epay.xj.service;

import java.util.Date;

import com.epay.xj.dao.OrgNumberNormDao;
import com.epay.xj.dao.impl.OrgNumberNormDaoImpl;
import com.epay.xj.utils.DateUtils;

/**
 * @Description: 机构数指标
 * @author LZG
 * @date 2018年07月02日
 */
public class OrgNumberNormService {

    private static OrgNumberNormDao orgNumberNormDao;

    static {
        orgNumberNormDao = new OrgNumberNormDaoImpl();
    }

    public static void main(String[] args) {

        // 测试身份证号
        String certNoStr = "C4DD4A924E159B6253D95627C1238365A5407FC2481C6F0BAD154E1485FC6E44";

        //---12个月机构数统计
        //yl_card_audit_all_merdint_all_j12m_cnt(certNoStr);
        //yl_card_audit_dk_merdint_all_j12m_cnt(certNoStr);
        //yl_card_audit_yh_merdint_all_j12m_cnt(certNoStr);
        
        //---6个月机构数统计
        //yl_card_audit_all_merdint_all_j6m_cnt(certNoStr);
        yl_card_audit_dk_merdint_all_j6m_cnt(certNoStr);
        //yl_card_audit_yh_merdint_all_j6m_cnt(certNoStr);

    }

    /**
     * @Description: 近12个月申请认证的不同机构数 --1
     * @param certNoStr
     * @author LZG
     * @date 2018年07月02日
     */
    public static void yl_card_audit_all_merdint_all_j12m_cnt(String certNoStr) {

        String currentDayStr = DateUtils.yyyyMMddToString(new Date());
        String[] oneYearDateArr = DateUtils.getOneYearDate(currentDayStr, 365);
        String beforeOneYearDayStr = oneYearDateArr[oneYearDateArr.length - 1];

        // 统计结果
        String result = orgNumberNormDao.yl_card_audit_all_merdint_all_j12m_cnt(certNoStr, currentDayStr, beforeOneYearDayStr);
        System.out.println(result);

        // 更新到数据库操作

    }

    /**
     * @Description: 近12个月申请的不同贷款类机构数 --2
     * @param certNoStr
     * @author LZG
     * @date 2018年07月02日
     */
    public static void yl_card_audit_dk_merdint_all_j12m_cnt(String certNoStr) {

        String currentDayStr = DateUtils.yyyyMMddToString(new Date());
        String[] oneYearDateArr = DateUtils.getOneYearDate(currentDayStr, 365);
        String beforeOneYearDayStr = oneYearDateArr[oneYearDateArr.length - 1];

        // 统计结果
        String result = orgNumberNormDao.yl_card_audit_dk_merdint_all_j12m_cnt(certNoStr, currentDayStr, beforeOneYearDayStr);
        System.out.println(result);
        
        // 更新到数据库操作

    }

    /**
     * @Description: 近12个月申请的不同银行类机构数 --3
     * @param certNoStr
     * @author LZG
     * @date 2018年07月02日
     */
    public static void yl_card_audit_yh_merdint_all_j12m_cnt(String certNoStr) {
        String currentDayStr = DateUtils.yyyyMMddToString(new Date());
        String[] oneYearDateArr = DateUtils.getOneYearDate(currentDayStr, 365);
        String beforeOneYearDayStr = oneYearDateArr[oneYearDateArr.length - 1];

        // 统计结果
        String result = orgNumberNormDao.yl_card_audit_yh_merdint_all_j12m_cnt(certNoStr, currentDayStr, beforeOneYearDayStr);
        System.out.println(result);
        // 更新到数据库操作
        
    }

    // ------------------------------------------------------------------------
    /**
     * @Description: 近6个月申请认证的不同机构数
     * @param certNoStr
     * @author LZG
     * @date 2018年07月02日
     */
    public static void yl_card_audit_all_merdint_all_j6m_cnt(String certNoStr) {
        String currentDayStr = DateUtils.yyyyMMddToString(new Date());
        String beforeDayStr = DateUtils.getDateOfXMonthsAgo(currentDayStr, 6);

        // 统计结果
        String result = orgNumberNormDao.yl_card_audit_all_merdint_all_j6m_cnt(certNoStr, currentDayStr, beforeDayStr);
        System.out.println(result);

        // 更新到数据库操作
        
    }

    /**
     * @Description: 近6个月申请认证的贷款类机构数
     * @param certNoStr
     * @author LZG
     * @date 2018年07月02日
     */
    public static void yl_card_audit_dk_merdint_all_j6m_cnt(String certNoStr) {
        String currentDayStr = DateUtils.yyyyMMddToString(new Date());
        String beforeDayStr = DateUtils.getDateOfXMonthsAgo(currentDayStr, 6);

        // 统计结果
        String result = orgNumberNormDao.yl_card_audit_dk_merdint_all_j6m_cnt(certNoStr, currentDayStr, beforeDayStr);
        System.out.println(result);

        // 更新到数据库操作
    }

    /**
     * @Description: 近6个月申请认证的贷款类机构数
     * @param md5CertNoStr
     * @return
     * @author LZG
     * @date 2018年07月02日
     */
    public static int yl_card_audit_yh_merdint_all_j6m_cnt(String md5CertNoStr) {
        return -1;
    }
}
