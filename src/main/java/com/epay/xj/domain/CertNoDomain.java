/**   
 * @Title: CertNoDomain.java 
 * @Package: com.epay.xj.domain 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018
 */
package com.epay.xj.domain;


/**
 * @Description: 
 * @author LZG
 * @date 2018年07月07日 
 */
public class CertNoDomain {
    private String certNo;
    private String updateTime;
    
    /**
     * @see java.lang.Object#toString() 
     * @return 
     */
    @Override
    public String toString() {
        return "CertNoDomain [certNo=" + certNo + ", updateTime=" + updateTime + "]";
    }


    
    /**
     * @Description: get certNo
     * @return certNo
     */
    public String getCertNo() {
        return certNo;
    }


    
    /**
     * @Description: get updateTime
     * @return updateTime
     */
    public String getUpdateTime() {
        return updateTime;
    }


    
    /** 
     * @Descprtion: set certNo 
     * @param certNo 
     */
    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }


    
    /** 
     * @Descprtion: set updateTime 
     * @param updateTime 
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
