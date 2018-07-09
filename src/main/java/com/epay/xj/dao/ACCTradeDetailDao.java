/**   
 * @Title: ACCTradeDetailDao.java 
 * @Package: com.epay.xj.dao 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018
 */
package com.epay.xj.dao;

import java.util.List;

import com.epay.xj.domain.ACCTradeDetail;


/**
 * @Description: 
 * @author LZG
 * @date 2018年07月07日 
 */
public interface ACCTradeDetailDao {
    
    // 添加方法
    public void add(List<ACCTradeDetail> tradeDetailList);

}
