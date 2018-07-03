/**   
 * @Title: OrgNumberNormDaoImpl.java 
 * @Package: com.epay.xj.dao.impl 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018 北京中数合一科技有限公司
 */
package com.epay.xj.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.epay.xj.dao.OrgNumberNormDao;
import com.epay.xj.utils.DBUtils;

/**
 * @Description:
 * @author LZG
 * @date 2018年07月03日
 */
public class OrgNumberNormDaoImpl implements OrgNumberNormDao {

    private QueryRunner runner = null;// 查询运行器

    public OrgNumberNormDaoImpl() {
        runner = new QueryRunner();
    }

    /**
     * @see com.epay.xj.dao.OrgNumberNormDao#yl_card_audit_all_merdint_all_j12m_cnt(java.lang.String, java.lang.String)
     * @param certNoStr
     * @param currentDayStr
     * @param beforeOneYearDayStr
     * @return
     */
    @Override
    public String yl_card_audit_all_merdint_all_j12m_cnt(String certNoStr, String currentDayStr, String beforeOneYearDayStr) {
        String sql = "select count(mer_id) from trade_detail where cert_no = ? and txt_date between ? and ? group by mer_id";
        try {
            Number result = runner.query(DBUtils.getConnection(), sql, new ScalarHandler<Number>(), certNoStr, beforeOneYearDayStr, currentDayStr);
            if (null != result)
                return result.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * @see com.epay.xj.dao.OrgNumberNormDao#yl_card_audit_dk_merdint_all_j12m_cnt(java.lang.String, java.lang.String)
     * @param certNoStr
     * @param currentDayStr
     * @param beforeOneYearDayStr
     * @return
     */
    @Override
    public String yl_card_audit_dk_merdint_all_j12m_cnt(String certNoStr, String currentDayStr, String beforeOneYearDayStr) {

        String mer_type_collection_str = "90 , 80, 70, 13";
        String sql = "select count(mer_id) from trade_detail where cert_no = ? and mer_type in (" + mer_type_collection_str + ") and txt_date between ? and ? group by mer_id";

        try {
            Number result = runner.query(DBUtils.getConnection(), sql, new ScalarHandler<Number>(), certNoStr, beforeOneYearDayStr, currentDayStr);
            if (null != result)
                return result.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * @see com.epay.xj.dao.OrgNumberNormDao#yl_card_audit_yh_merdint_all_j12m_cnt(java.lang.String, java.lang.String, java.lang.String)
     * @param certNoStr
     * @param currentDayStr
     * @param beforeOneYearDayStr
     * @return
     */
    @Override
    public String yl_card_audit_yh_merdint_all_j12m_cnt(String certNoStr, String currentDayStr, String beforeOneYearDayStr) {

        String mer_type_collection_str = "90 , 80";
        String sql = "select count(mer_id) from trade_detail where cert_no = ? and mer_type in (" + mer_type_collection_str + ") and txt_date between ? and ? group by mer_id";

        try {
            Number result = runner.query(DBUtils.getConnection(), sql, new ScalarHandler<Number>(), certNoStr, beforeOneYearDayStr, currentDayStr);
            if (null != result)
                return result.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";

    }

    /**
     * @see com.epay.xj.dao.OrgNumberNormDao#yl_card_audit_all_merdint_all_j6m_cnt(java.lang.String, java.lang.String, java.lang.String)
     * @param certNoStr
     * @param currentDayStr
     * @param beforeDayStr
     * @return
     */
    @Override
    public String yl_card_audit_all_merdint_all_j6m_cnt(String certNoStr, String currentDayStr, String beforeDayStr) {
        String sql = "select count(mer_id) from trade_detail where cert_no = ? and txt_date between ? and ? group by mer_id";
        
        try {
            Number result = runner.query(DBUtils.getConnection(), sql, new ScalarHandler<Number>(), certNoStr, beforeDayStr, currentDayStr);
            if (null != result)
                return result.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return "0";
    }

    /**
     * @see com.epay.xj.dao.OrgNumberNormDao#yl_card_audit_dk_merdint_all_j6m_cnt(java.lang.String, java.lang.String, java.lang.String) 
     * @param certNoStr
     * @param currentDayStr
     * @param beforeDayStr
     * @return 
     */
    @Override
    public String yl_card_audit_dk_merdint_all_j6m_cnt(String certNoStr, String currentDayStr, String beforeDayStr) {
        String mer_type_collection_str = "90 , 80, 70, 13";
        String sql = "select count(mer_id) from trade_detail where cert_no = ? and mer_type in (" + mer_type_collection_str + ") and txt_date between ? and ? group by mer_id";

        try {
            Number result = runner.query(DBUtils.getConnection(), sql, new ScalarHandler<Number>(), certNoStr, beforeDayStr, currentDayStr);
            if (null != result)
                return result.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }

}
