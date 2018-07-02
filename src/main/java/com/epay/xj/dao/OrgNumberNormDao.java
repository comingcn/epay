/**   
 * @Title: OrgNumberNormDao.java 
 * @Package: com.epay.xj.dao 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018 北京中数合一科技有限公司
 */
package com.epay.xj.dao;

import java.sql.SQLException;

/**
 * @Description: 
 * @author LZG
 * @date 2018年07月03日 
 */
public interface OrgNumberNormDao {
	
	/**
	 * @Description: 近12个月申请认证的不同机构数
	 * @param currentDayStr 当前日期
	 * @param beforeOneYearDayStr 一年前的这个日期
	 * @return
	 * @throws SQLException 
	 * @author LZG
	 * @date 2018年07月03日
	 */
	public String yl_card_audit_all_merdint_all_j12m_cnt(String currentDayStr, String beforeOneYearDayStr);

}
