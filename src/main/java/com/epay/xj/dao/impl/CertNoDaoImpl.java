/**   
 * @Title: CertNoDao.java 
 * @Package: com.epay.xj.dao.impl 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018
 */
package com.epay.xj.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.epay.xj.dao.CertNoDao;
import com.epay.xj.domain.CertNoDomain;
import com.epay.xj.utils.DBUtils;

/**
 * @Description: 
 * @author LZG
 * @date 2018年07月07日 
 */
public class CertNoDaoImpl implements CertNoDao {

    private QueryRunner runner = null; // 查询运行器

    public CertNoDaoImpl() {
        runner = new QueryRunner();
    }
    
    /**
     * @see com.epay.xj.dao.CertNoDao#add(com.epay.xj.domain.CertNoDO) 
     * @param certNoDo 
     */
    @Override
    public void add(List<CertNoDomain> certNoDomainList) {
        int certNoDomainListSize = certNoDomainList.size();
        for(int i = 0; i < certNoDomainListSize; i++) {
            CertNoDomain certNoDomain = certNoDomainList.get(i);
      
            // TODO Auto-generated method stub
            String sql = "insert into p1055_cert_list(cert_no, update_dt) values(?,?)";
            Connection connection =  DBUtils.getConnection();
            try {
                runner.update(connection, sql, certNoDomain.getCertNo(), certNoDomain.getUpdateTime());
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                DBUtils.close(connection);
            }
        }
    }

    /**
     * @see com.epay.xj.dao.CertNoDao#finAllCertNo() 
     * @return 
     */
    @Override
    public List<CertNoDomain> finAllCertNo() {
    
        String sql = "select cert_no, update_dt from p1055_cert_list";
        List<CertNoDomain> certNoList;
        try {
            certNoList = runner.query(DBUtils.getConnection(), sql, new BeanListHandler<CertNoDomain>(CertNoDomain.class));
            return certNoList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    
}
