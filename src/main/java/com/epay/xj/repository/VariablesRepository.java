package com.epay.xj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epay.xj.domain.Variables;


public interface VariablesRepository extends JpaRepository<Variables,Integer> {

    //通过姓名查询
//    public List<Variables> getCertNosById(String id);
    	
//    @Modifying
//    @Query("update CertNoDO u set u.updateTime = ?1 where u.id = ?2")
//    int update(String updateTime, String id);
}
