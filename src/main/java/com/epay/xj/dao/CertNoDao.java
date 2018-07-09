/**   
 * @Title: CertNoDao.java 
 * @Package: com.epay.xj.dao 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018
 */
package com.epay.xj.dao;

import java.util.Date;
import java.util.List;

import com.epay.xj.domain.CertNoDomain;

/**
 * @Description: 
 * @author LZG
 * @date 2018年07月07日 
 */
public interface CertNoDao {

    // 添加方法
    public void add(List<CertNoDomain> certNoDomainList);
    
    // 根据结束日期查找所有
    public List<CertNoDomain> finAllCertNo();
}
