/**   
 * @Title: ACCTradeDetailDaoImpl.java 
 * @Package: com.epay.xj.dao.impl 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018
 */
package com.epay.xj.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;

import com.epay.xj.dao.ACCTradeDetailDao;
import com.epay.xj.domain.ACCTradeDetail;
import com.epay.xj.utils.DBUtils;

/**
 * @Description:
 * @author LZG
 * @date 2018年07月07日
 */
public class ACCTradeDetailDaoImpl implements ACCTradeDetailDao {

    private QueryRunner runner = null; // 查询运行器

    public ACCTradeDetailDaoImpl() {
        runner = new QueryRunner();
    }

    /**
     * @see com.epay.xj.dao.ACCTradeDetailDao#add(com.epay.xj.domain.ACCTradeDetail)
     * @param tradeDetail
     * @throws SQLException
     */
    @Override
    public void add(List<ACCTradeDetail> tradeDetailList) {
        int tradeDetailListSize = tradeDetailList.size();
        
        String sql = "insert into p1055_tra_trade_detail_para("
                    + " create_time,id,idcard,account_no,source_merno,mer_type,amount,sf_type,return_code)"
                    + " values(?,?,?,?,?,?,?,?,?)";
        Connection connection =  DBUtils.getConnection();
        Object[][] params = new Object[tradeDetailListSize][9];  //对应数据集的长度，和每个数据项具有的属性个数
        
        for (int i = 0; i < tradeDetailListSize ; i++) {
            
            params[i][0] = tradeDetailList.get(i).getCreateTime();
            params[i][1] = tradeDetailList.get(i).getId();
            params[i][2] = tradeDetailList.get(i).getIdCard();
            params[i][3] = tradeDetailList.get(i).getAccountNo();
            params[i][4] = tradeDetailList.get(i).getSourceMerno();
            params[i][5] = tradeDetailList.get(i).getMerType();
            params[i][6] = tradeDetailList.get(i).getAmout();
            params[i][7] = tradeDetailList.get(i).getSfType();
            params[i][8] = tradeDetailList.get(i).getReturnCode();
        }
        
        /**
        runner.update(connection, sql, tradeDetail.getCreateTime(), tradeDetail.getId(), tradeDetail.getIdCard(),
                tradeDetail.getAccountNo(), tradeDetail.getSourceMerno(), tradeDetail.getMerType(), tradeDetail.getAmout(),
                tradeDetail.getSfType(), tradeDetail.getReturnCode());
        */
        
        try {
            runner.batch(connection,sql, params);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
