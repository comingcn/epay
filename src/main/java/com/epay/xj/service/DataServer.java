package com.epay.xj.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.epay.xj.domain.OverDueRecord;
import com.epay.xj.domain.OverDueRecord2;
import com.epay.xj.domain.TradeDetailDO;
import com.epay.xj.properties.InitProperties;

@Service
@Transactional
public class DataServer {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private InitProperties initProperties;

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 获取指定机器的任务列表
	 * 
	 * @param updateTime
	 * @param etlServer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getTaskList(String certNo, String etlServer) {
		StringBuffer sql = new StringBuffer();
		sql.append("select CERT_NO from CP_ODS.P1055_CERT_LIST_PY where ETL_SERVER='" + etlServer + "'");
		if(!StringUtils.isEmpty(certNo)){
			sql.append(" and CERT_NO='").append(certNo).append("'");
		}
		return entityManager.createNativeQuery(sql.toString()).getResultList();
	}

	/**
	 * 开发和正式环境的表名区分
	 * 
	 * @return
	 */
	public String activeTableParam() {
		if (initProperties.getActive().equals("dev"))
			return "_PY";
		return null;
	}

	/**
	 * batchInsert 批处理
	 * 
	 * @param etlServer
	 *            服务器标志
	 * 
	 * @param list
	 */
	public <T> void batchInsert(List<T> list, String etlServer) {
		int size = list.size();
		try {
			for (int i = 0; i < size; i++) {
				if (etlServer.equals("1")) {// 保存到一号机器对应的表中
					OverDueRecord dd = (OverDueRecord) list.get(i);
					Query query = entityManager.createNativeQuery("insert into CP_ODS.P1055_ODS_YQ_DTL2 "
							+ "(CERT_NO, MER_NO,TYPE_ID,STR_DAYS,END_DAYS,OVR_DAYS,AMT) values (?, ?, ?, ?, ?, ?, ?)")
					.setParameter(1, dd.getCERT_NO())
					.setParameter(2, dd.getMER_NO())
					.setParameter(3, dd.getTYPE_ID())
					.setParameter(4, dd.getSTR_DAYS())
					.setParameter(5, dd.getEND_DAYS())
					.setParameter(6, dd.getOVR_DAYS())
					.setParameter(7, dd.getAMT());
					query.executeUpdate();
				} else if (etlServer.equals("2")) {
					OverDueRecord2 dd = (OverDueRecord2) list.get(i);
					Query query = entityManager.createNativeQuery("insert into CP_ODS.P1055_ODS_YQ_DTL2 "
							+ "(CERT_NO, MER_NO,TYPE_ID,STR_DAYS,END_DAYS,OVR_DAYS,AMT) values (?, ?, ?, ?, ?, ?, ?)")
					.setParameter(1, dd.getCERT_NO())
					.setParameter(2, dd.getMER_NO())
					.setParameter(3, dd.getTYPE_ID())
					.setParameter(4, dd.getSTR_DAYS())
					.setParameter(5, dd.getEND_DAYS())
					.setParameter(6, dd.getOVR_DAYS())
					.setParameter(7, dd.getAMT());
					query.executeUpdate();
				}
			}
			entityManager.flush();
			entityManager.clear();
		} catch (Exception e) {
			logger.error("batchInsert:{}", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 获取certNo下的不同月份下的所有
	 * 
	 * @param certNo
	 * @param updateTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TradeDetailDO> getTradeDetailList(String certNo, String active) {
		 List<TradeDetailDO> tradeDetailList = new ArrayList<TradeDetailDO>();
		String sql = "select * from CP_ODS.P1055_TRA_TRADE_DETAIL_PARA" + active + " where IDCARD='" + certNo + "'";
		try {
			tradeDetailList = entityManager.createNativeQuery(sql, TradeDetailDO.class)
					.getResultList();
		} catch (Exception e) {
			logger.error("执行sql:{},error:{}", sql, e.getMessage());
			e.printStackTrace();
		}
		return tradeDetailList;
	}
	
	
	/**
	 * 获取certNo下的不同月份下的所有
	 * 
	 * @param certNo
	 * @param updateTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OverDueRecord> getJavaOverDueRecordList(String certNo) {
		List<OverDueRecord> tradeDetailList = new ArrayList<OverDueRecord>();
		String sql = "select * from CP_ODS.P1055_ODS_YQ_DTL2 where CERT_NO='"+certNo+"'";
		try {
			List tmpList = entityManager.createNativeQuery(sql)
					.getResultList();
			for (Object object : tmpList) {
				OverDueRecord o = new OverDueRecord();
				Object[] ob = (Object[]) object;
				o.setCERT_NO((String)ob[0]);
				o.setMER_NO((String)ob[1]);
				o.setTYPE_ID((Integer)ob[2]);
				o.setSTR_DAYS((Integer)ob[3]);
				o.setEND_DAYS((Integer)ob[4]);
				o.setOVR_DAYS((Integer)ob[5]);
				o.setAMT((BigDecimal)ob[6]);
				tradeDetailList.add(o);
			}
		} catch (Exception e) {
			logger.error("执行sql:{},error:{}", sql, e.getMessage());
			e.printStackTrace();
		}
		return tradeDetailList;
	}
	
	/**
	 * 获取certNo下的不同月份下的所有
	 * 
	 * @param certNo
	 * @param updateTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OverDueRecord> getPythonOverDueRecordList(String certNo) {
		 List<OverDueRecord> tradeDetailList = new ArrayList<OverDueRecord>();
		String sql = "select * from CP_ODS.P1055_ODS_YQ_DTL1 where CERT_NO='"+certNo+"'";
		try {
			List tmpList = entityManager.createNativeQuery(sql)
					.getResultList();
			for (Object object : tmpList) {
				OverDueRecord o = new OverDueRecord();
				Object[] ob = (Object[]) object;
				o.setCERT_NO((String)ob[0]);
				o.setMER_NO((String)ob[1]);
				o.setTYPE_ID((Integer)ob[2]);
				o.setSTR_DAYS((Integer)ob[3]);
				o.setEND_DAYS((Integer)ob[4]);
				o.setOVR_DAYS((Integer)ob[5]);
				o.setAMT((BigDecimal)ob[6]);
				tradeDetailList.add(o);
			}
		} catch (Exception e) {
			logger.error("执行sql:{},error:{}", sql, e.getMessage());
			e.printStackTrace();
		}
		return tradeDetailList;
	}
}
