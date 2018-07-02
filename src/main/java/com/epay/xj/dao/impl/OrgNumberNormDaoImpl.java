/**   
 * @Title: OrgNumberNormDaoImpl.java 
 * @Package: com.epay.xj.dao.impl 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018 北京中数合一科技有限公司
 */
package com.epay.xj.dao.impl;

import java.sql.SQLException;

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
	 * 
	 * @see com.epay.xj.dao.OrgNumberNormDao#yl_card_audit_all_merdint_all_j12m_cnt(java.lang.String, java.lang.String) 
	 * @param currentDayStr
	 * @param beforeOneYearDayStr
	 * @return
	 */
	@Override
	public String yl_card_audit_all_merdint_all_j12m_cnt(String currentDayStr, String beforeOneYearDayStr) {
		String sql = "select count(merId) from trade_detail where txtDate between ? and ? group by merId";
		try {
			return runner.query(DBUtils.getConnection(), sql, new ScalarHandler<String>(), currentDayStr, beforeOneYearDayStr);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "-1";
	}

}
