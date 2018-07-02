package com.epay.xj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.epay.xj.domain.CertNoDO;


public interface CertNoRepository extends JpaRepository<CertNoDO,Integer> {

    //通过姓名查询
    public List<CertNoDO> getCertNosById(String id);
    
    @Modifying
    @Query("update CertNoDO u set u.updateTime = ?1 where u.id = ?2")
    int update(String updateTime, String id);
}
