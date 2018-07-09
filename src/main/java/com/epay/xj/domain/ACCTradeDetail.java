/**   
 * @Title: ACCTradeDetail.java 
 * @Package: com.epay.xj.domain 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018
 */
package com.epay.xj.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: ACC文件里的一条交易记录
 * @author LZG
 * @date 2018年07月06日 
 */
public class ACCTradeDetail {

    private Date createTime;
    private String id;
    private String idCard;
    private String accountNo;
    private String sourceMerno;
    private String merType;
    private BigDecimal amout;
    private String sfType;
    private String returnCode;
    
    /**
     * @Description: get createTime
     * @return createTime
     */
    public Date getCreateTime() {
        return createTime;
    }
    
    /**
     * @Description: get id
     * @return id
     */
    public String getId() {
        return id;
    }
    
    /**
     * @Description: get idCard
     * @return idCard
     */
    public String getIdCard() {
        return idCard;
    }
    
    /**
     * @Description: get accountNo
     * @return accountNo
     */
    public String getAccountNo() {
        return accountNo;
    }
    
    /**
     * @Description: get sourceMerno
     * @return sourceMerno
     */
    public String getSourceMerno() {
        return sourceMerno;
    }
    
    /**
     * @Description: get merType
     * @return merType
     */
    public String getMerType() {
        return merType;
    }
    
    /**
     * @Description: get amout
     * @return amout
     */
    public BigDecimal getAmout() {
        return amout;
    }
    
    /**
     * @Description: get sfType
     * @return sfType
     */
    public String getSfType() {
        return sfType;
    }
    
    /**
     * @Description: get returnCode
     * @return returnCode
     */
    public String getReturnCode() {
        return returnCode;
    }
    
    /** 
     * @Descprtion: set createTime 
     * @param createTime 
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    /** 
     * @Descprtion: set id 
     * @param id 
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /** 
     * @Descprtion: set idCard 
     * @param idCard 
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    
    /** 
     * @Descprtion: set accountNo 
     * @param accountNo 
     */
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
    
    /** 
     * @Descprtion: set sourceMerno 
     * @param sourceMerno 
     */
    public void setSourceMerno(String sourceMerno) {
        this.sourceMerno = sourceMerno;
    }
    
    /** 
     * @Descprtion: set merType 
     * @param merType 
     */
    public void setMerType(String merType) {
        this.merType = merType;
    }
    
    /** 
     * @Descprtion: set amout 
     * @param amout 
     */
    public void setAmout(BigDecimal amout) {
        this.amout = amout;
    }
    
    /** 
     * @Descprtion: set sfType 
     * @param sfType 
     */
    public void setSfType(String sfType) {
        this.sfType = sfType;
    }
    
    /** 
     * @Descprtion: set returnCode 
     * @param returnCode 
     */
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * @see java.lang.Object#toString() 
     * @return 
     */
    @Override
    public String toString() {
        return "ACCTradeDetail [createTime=" + createTime + ", id=" + id + ", idCard=" + idCard + ", accountNo=" + accountNo + ", sourceMerno=" + sourceMerno + ", merType=" + merType + ", amout="
                + amout + ", sfType=" + sfType + ", returnCode=" + returnCode + "]";
    }
    
}

