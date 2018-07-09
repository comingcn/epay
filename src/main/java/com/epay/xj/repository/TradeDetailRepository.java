//package com.epay.xj.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import com.epay.xj.domain.TradeDetailDO;
//
//
//public interface TradeDetailRepository extends JpaRepository<TradeDetailDO,Integer> {
//
//    //通过姓名查询
////    public List<TradeDetailDO> getCertNosById(String id);
////    @Modifying
////    @Query("update CertNoDO u set u.updateTime = ?1 where u.id = ?2")
////    int update(String updateTime, String id);
//	
//	@Query("select u.txtSeqId,u.certNo,u.cardNo,u.merType,u.merId,u.txtDate,u.sfType,u.amout,u.returnCode from TradeDetailDO u where u.certNo= ?1 and (u.txtDate between ?2 and ?3) and u.sfType='S'")
//	List<TradeDetailDO> getLists(String certNo,String beginTime,String endTime);
//	
//	List<TradeDetailDO> findByCertNoAndTxtDateBetween(String certNo,String beginTime,String endTime);
//}
