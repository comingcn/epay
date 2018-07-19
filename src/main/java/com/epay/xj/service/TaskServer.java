package com.epay.xj.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.epay.xj.domain.BindCardLog;
import com.epay.xj.domain.OverDueIndex;
import com.epay.xj.domain.TradeDetailDO;
import com.epay.xj.properties.InitProperties;
import com.epay.xj.utils.CreditScoreUtil;
import com.epay.xj.utils.DateUtils;
import com.epay.xj.utils.MathUtil;

@Service
@Transactional
public class TaskServer {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private InitProperties initProperties;

	@PersistenceContext
	private EntityManager entityManager;

	public List<String> getTaskList(String updateTime, String flag) {
		String sql = "select CERT_NO from CP_ODS.P1055_CERT_LIST_PY";
		return entityManager.createNativeQuery(sql).getResultList();
	}

	/**
	 * batchInsert
	 * 
	 * @param list
	 */
	public void batchInsert(List<OverDueIndex> list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			OverDueIndex dd = list.get(i);
			entityManager.persist(dd);
			if (i % 10000 == 0 || i == (size - 1)) { // 每1000条数据执行一次，或者最后不足1000条时执行
				entityManager.flush();
				entityManager.clear();
			}
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
	public Map<Integer, List<TradeDetailDO>> fatherList(String certNo, String updateTime) {
		// List<TradeDetailDO> tradeDetailList = new ArrayList<TradeDetailDO>();
		Map<Integer, List<TradeDetailDO>> tradeMap = new HashMap<Integer, List<TradeDetailDO>>();
		String sql = "select * from CP_ODS.P1055_TRA_TRADE_DETAIL_PARA_PY where IDCARD='" + certNo + "'";
		try {
			List<TradeDetailDO> tradeDetailList = entityManager.createNativeQuery(sql, TradeDetailDO.class)
					.getResultList();
			Collections.sort(tradeDetailList);
			Map<String, Integer> overDueMouth = initProperties.getOverDueMonth();
			for (int month : overDueMouth.values()) {
				tradeMap.put(month, getListByMonth(tradeDetailList, month, updateTime));
			}
		} catch (Exception e) {
			logger.error("执行sql:{},error:{}", sql, e.getMessage());
			e.printStackTrace();
		}
		return tradeMap;
	}

	/**
	 * @param certNo
	 * @param updateTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, List<BindCardLog>> getBindCardLog(String certNo, String updateTime) {
		String sql = "select * from CP_ODS.P1055_UMP_BIND_LOG_PARA where CERT_NO='" + certNo + "'";
		List<BindCardLog> tradeDetailList = entityManager.createNativeQuery(sql, BindCardLog.class).getResultList();
		Map<Integer, List<BindCardLog>> tradeMap = new HashMap<Integer, List<BindCardLog>>();
		if (tradeDetailList.size() == 0)
			return tradeMap;
		Map<String, Integer> overDueMouth = initProperties.getOverDueMonth();
		for (int month : overDueMouth.values()) {
			tradeMap.put(month, getBindCardLogListByMonthOrDays(tradeDetailList, month, updateTime));
		}
		return tradeMap;
	}

	/**
	 * 绑卡类日志的按月统计
	 * 
	 * @param tradeDetailList
	 * @param month
	 * @param updateTime
	 * @return
	 */
	private List<BindCardLog> getBindCardLogListByMonthOrDays(List<BindCardLog> tradeDetailList, int month,
			String updateTime) {
		List<BindCardLog> list = new ArrayList<BindCardLog>();
		Timestamp end = new Timestamp(DateUtils.yyyyMMddToDate(updateTime).getTime());
		Timestamp begin = null;
		if (month == 15) {// 按天计算日期
			begin = DateUtils.getDateOfXDaysAgo(end, month);
		} else {
			begin = DateUtils.getDateOfXMonthsAgo(end, month);
		}
		for (BindCardLog o : tradeDetailList) {
			Timestamp current = new Timestamp(DateUtils.yyyyMMddToDate(o.getTXN_DATE()).getTime());
			if (DateUtils.judge(begin, end, current)) {
				list.add(o);
			}
		}
		return list;
	}

	public void sliceTask(List<String> taskList, String updateTime) throws InterruptedException {
		// 每500条数据开启一条线程
		int threadSize = Integer.valueOf(initProperties.getThreadSize());
		// 总数据条数
		int dataSize = taskList.size();
		// 线程数
		int threadNum = dataSize / threadSize + 1;
		// 定义标记,过滤threadNum为整数
		boolean special = dataSize % threadSize == 0;
		// 创建一个线程池
		int theadPoolSize = Integer.valueOf(initProperties.getThreadPoolSize());
		ExecutorService exec = Executors.newFixedThreadPool(theadPoolSize);
		// 定义一个任务集合
		List<Callable<List<OverDueIndex>>> tasks = new ArrayList<Callable<List<OverDueIndex>>>();
		Callable<List<OverDueIndex>> task = null;
		List<String> cutList = null;
		logger.info("taskSize:{},线程数：{},单个线程处理记录数量:{}", taskList.size(), theadPoolSize, threadSize);
		// 确定每条线程的数据
		for (int i = 0; i < threadNum; i++) {
			if (i == threadNum - 1) {
				if (special) {
					break;
				}
				cutList = taskList.subList(threadSize * i, dataSize);
			} else {
				cutList = taskList.subList(threadSize * i, threadSize * (i + 1));
			}
			final List<String> listStr = cutList;
			final String udpateTimes = updateTime;
			final Map<String, String[]> returnCodeDic = initProperties.getReturnCodeDic();
			final Map<String, Integer> overDueMouth = initProperties.getOverDueMonth();
			task = new Callable<List<OverDueIndex>>() {
				@Override
				public List<OverDueIndex> call() throws Exception {
					List<OverDueIndex> lst = new ArrayList<OverDueIndex>();
					for (int i = 0; i < listStr.size(); i++) {
						String certNo = listStr.get(i);
						OverDueIndex odi = new OverDueIndex();
						odi.setCERT_NO(certNo);
						// 如果是人的所有记录
						Map<Integer, List<TradeDetailDO>> tradeMap = fatherList(certNo, udpateTimes);
						for (int month : overDueMouth.values()) {
							// 指标结果集
							List<TradeDetailDO> list = tradeMap.get(month);
							if (list.size() != 0) {
								overDueMouth(list, odi, month, returnCodeDic);
							}
						}
						// 客户申请行为统计
						Map<Integer, List<BindCardLog>> bindCardLogMap = getBindCardLog(certNo, udpateTimes);
						if (bindCardLogMap.size() != 0) {
							for (int month : overDueMouth.values()) {
								// 指标结果集
								List<BindCardLog> list = bindCardLogMap.get(month);
								bindCardMouth(list, odi, month, returnCodeDic, udpateTimes);
							}
						}
						logger.info("odi:{}", JSON.toJSONString(odi));
					
						// 信用分计算
						BigDecimal v1 = CreditScoreUtil.getCreditScoreByCertScoreType(CreditScoreUtil.yl_s_rcd_yebz_j3m_pct, odi.getHK030());
						BigDecimal v2 = CreditScoreUtil.getCreditScoreByCertScoreType(CreditScoreUtil.yl_s_amt_success_j2m_pct, odi.getHK033());
						BigDecimal v3 = CreditScoreUtil.getCreditScoreByCertScoreType(CreditScoreUtil.yl_s_latesttn_fail_xj, new BigDecimal(odi.getHK046()));
						BigDecimal v4 = CreditScoreUtil.getCreditScoreByCertScoreType(CreditScoreUtil.overdue3_1d_amt_sum_j3m, odi.getYQ033());
						BigDecimal v5 = CreditScoreUtil.getCreditScoreByCertScoreType(CreditScoreUtil.sf_s_rcd_suces_dk_cnt_j6m, new BigDecimal(odi.getHK012()));
						BigDecimal v6 = CreditScoreUtil.getCreditScoreByCertScoreType(CreditScoreUtil.yl_f_mer_success_j12m_dk_cnt, new BigDecimal(odi.getFK002()));
						BigDecimal v7 = CreditScoreUtil.getCreditScoreByCertScoreType(CreditScoreUtil.yl_card_audit_all_rcd_disntcd_all_j1m, odi.getSQ035());
						BigDecimal v8 = CreditScoreUtil.getCreditScoreByCertScoreType(CreditScoreUtil.yl_s_rcd_fail_j12m_pct, odi.getHK008());
						BigDecimal v9 = CreditScoreUtil.getCreditScoreByCertScoreType(CreditScoreUtil.yl_card_audit_dbt_rcd_nearist_days, new BigDecimal(odi.getSQ045()));
						BigDecimal v10 =CreditScoreUtil.getCreditScoreByCertScoreType(CreditScoreUtil.yl_card_audit_dbt_rcd_disntcd_all_j6m_min, new BigDecimal(odi.getSQ015()));
						
						int creditScore = MathUtil.plus(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);
						odi.setSCORE(creditScore);
						
						lst.add(odi);
					}
					return lst;
				}
			};
			// 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
			tasks.add(task);
		}

		List<Future<List<OverDueIndex>>> results = exec.invokeAll(tasks);
		// StringBuffer sb = new StringBuffer();
		long sysBeginTime = System.nanoTime();
		for (Future<List<OverDueIndex>> future : results) {
			try {
				// 遍历所有人list

				List<OverDueIndex> lst = future.get();
				logger.info("-------------------------------lst.size:{}", lst.size());
				// sb.append("size:").append(lst.size()).append(",");
				// batchInsert(lst);
				// for (OverDueIndex overDueIndex : lst) {
				// logger.info("certNo:{},index:{}",
				// overDueIndex.getCertNo(),JSON.toJSONString(overDueIndex));
				// }
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String useTime = String.valueOf((System.nanoTime() - sysBeginTime) / Math.pow(10, 9));
		logger.info("sb:{},写入记录useTime:{}秒", null, useTime);
		// 关闭线程池
		exec.shutdown();
	}

	/**
	 * 获取绑卡日志集合，以商户id为主键的map
	 * 
	 * @param list
	 * @param orgType
	 *            目前只要贷款和银行类机构，如果机构为空则是全部
	 * @return
	 */
	public Map<String, List<BindCardLog>> bindCardMerMap(List<BindCardLog> list, String orgType) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		Map<String, List<BindCardLog>> map = new HashMap<String, List<BindCardLog>>();
		for (BindCardLog o : list) {
			String merNo = o.getAPP_SYS_ID();
			if (null != orgType) {
				List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
				if (orgTypeList.contains(String.valueOf(o.getMER_TYPE()))) {
					if (map.containsKey(merNo)) {
						map.get(merNo).add(o);
					} else {
						List<BindCardLog> bclLst = new ArrayList<BindCardLog>();
						bclLst.add(o);
						map.put(merNo, bclLst);
					}
				}
			} else {
				if (map.containsKey(merNo)) {
					map.get(merNo).add(o);
				} else {
					List<BindCardLog> bclLst = new ArrayList<BindCardLog>();
					bclLst.add(o);
					map.put(merNo, bclLst);
				}
			}
		}
		return map;
	}

	/**
	 * 客户行为统计
	 * 
	 * @param list
	 * @param odi
	 * @param month
	 * @param returnCodeDic
	 */
	private void bindCardMouth(List<BindCardLog> list, OverDueIndex odi, int month, Map<String, String[]> returnCodeDic,
			String updateTime) {
		/* 15天 */
		if (month == 15) {
			odi.setSQ036(bindCardMerMap(list, null).size());// 申请认证机构数
			odi.setSQ037(bindCardMerMap(list, "dk").size());// 申请认证的贷款类机构数
			odi.setSQ038(bindCardMerMap(list, "yh").size());// 申请认证的银行类机构数
			odi.setSQ039(list.size());// 申请记录数
		}
		/* 1个月 */
		if (month == 1) {
			odi.setSQ031(bindCardMerMap(list, null).size());// 申请认证机构数
			odi.setSQ032(bindCardMerMap(list, "dk").size());// 申请认证的贷款类机构数
			odi.setSQ033(bindCardMerMap(list, "yh").size());// 申请认证的银行类机构数
			odi.setSQ034(list.size());// 申请记录数
			odi.setSQ035(averageBindCardRecord(list, null, "0", "0000"));// 卡均申请记录数:每张借记卡申请认证成功的平均记录数
		}
		/* 2个月 */
		if (month == 2) {
			odi.setSQ027(bindCardMerMap(list, null).size());// 申请认证机构数
			odi.setSQ028(bindCardMerMap(list, "dk").size());// 申请认证的贷款类机构数
			odi.setSQ029(bindCardMerMap(list, "yh").size());// 申请认证的银行类机构数
			odi.setSQ030(list.size());// 申请记录数
		}
		/* 3个月 */
		if (month == 3) {
			odi.setSQ019(bindCardMerMap(list, null).size());// 申请认证的不同机构数
			odi.setSQ020(bindCardMerMap(list, "dk").size());// 申请认证的贷款类机构数
			odi.setSQ021(bindCardMerMap(list, "yh").size());// 申请认证的银行类机构数
			odi.setSQ022(bindCardNoMap(list, null).size());// 用户用于申请认证的银行卡数
			odi.setSQ023(MathUtil.divide(getBindCardByDcType(list, null, "0"), odi.getSQ022()));// 平均每张借记卡申请记录数
			odi.setSQ024(MathUtil.divide(getBindCardByDcType(list, null, "1"), odi.getSQ022()));// 平均每张贷记卡申请记录数
			odi.setSQ025(MathUtil.divide(getBindCardByDcType(list, "dk", null), odi.getSQ022()));// 平均每张卡在贷款类机构申请认证的记录数
			odi.setSQ026(MathUtil.divide(getBindCardByDcType(list, "yh", null), odi.getSQ022()));// 平均每张卡在贷款类机构申请认证的记录数
			odi.setSQ051(MathUtil.divide(list.size(),odi.getSQ022()));//近3个月平均每张卡申请记录数
		}
		/* 6个月 */
		if (month == 6) {
			odi.setSQ009(bindCardMerMap(list, null).size());// 申请认证的不同机构数
			odi.setSQ010(bindCardMerMap(list, "dk").size());// 申请认证的贷款类机构数
			odi.setSQ011(bindCardMerMap(list, "yh").size());// 申请认证的银行类机构数
			odi.setSQ012(bindCardNoMap(list, null).size());// 用户用于申请认证的银行卡数
			odi.setSQ013(MathUtil.divide(getBindCardByDcType(list, null, "0"), odi.getSQ012()));// 平均每张借记卡申请记录数
			odi.setSQ014(MathUtil.divide(getBindCardByDcType(list, null,"1"), odi.getSQ012()));// 平均每张贷记卡申请记录数
			odi.setSQ015(getBindCardMinRecordsByDcType(list, "0", "max"));// 每张借记卡申请最小记录数
			odi.setSQ016(getBindCardMinRecordsByDcType(list, "1","min"));//每张贷记卡申请最小记录数
			odi.setSQ017(MathUtil.divide(getBindCardByDcType(list, "dk", null), odi.getSQ012()));// 平均每张卡在贷款类机构申请认证的记录数
			odi.setSQ018(MathUtil.divide(getBindCardByDcType(list, "yh", null), odi.getSQ012()));// 平均每张卡在贷款类机构申请认证的记录数
			odi.setSQ049(MathUtil.divide(list.size(),odi.getSQ012()));//近6个月平均每张卡申请记录数
			odi.setSQ050(getBindCardMinRecordsByDcType(list, null,"min"));//6个月_全量卡_单卡_申请次数_最小
		}
		/* 12个月 */
		if (month == 12) {
			odi.setSQ001(bindCardMerMap(list, null).size());// 申请认证的不同机构数
			odi.setSQ002(bindCardMerMap(list, "dk").size());// 申请认证的贷款类机构数
			odi.setSQ003(bindCardMerMap(list, "yh").size());// 申请认证的银行类机构数
			odi.setSQ004(bindCardNoMap(list, null).size());// 用户用于申请认证的银行卡数
			odi.setSQ005(MathUtil.divide(getBindCardByDcType(list, null, "0"), odi.getSQ004()));// 平均每张借记卡申请记录数
			odi.setSQ006(MathUtil.divide(getBindCardByDcType(list, null, "1"), odi.getSQ004()));// 平均每张贷记卡申请记录数
			odi.setSQ007(MathUtil.divide(getBindCardByDcType(list, "dk", null), odi.getSQ004()));// 平均每张卡在贷款类机构申请认证的记录数
			odi.setSQ008(MathUtil.divide(getBindCardByDcType(list, "yh", null), odi.getSQ004()));// 平均每张卡在银行类机构申请认证的记录数
			odi.setSQ048(MathUtil.divide(list.size(),odi.getSQ004()));//近12个月平均每张卡申请记录数
			odi.setSQ040(getCreditCardBindLogs(list, "1", "recently"));//最近一次使用信用卡认证申请距今的时间
			Date dateEnd = DateUtils.yyyyMMddToDate(updateTime);
			if(null!=odi.getSQ040()){
				odi.setSQ041(DateUtils.getIntervalDayAmount(
						DateUtils.yyyyMMddToDate(odi.getSQ040()), 
						dateEnd));//最近一次使用信用卡认证申请距今的天数
			}
			odi.setSQ042(getCreditCardBindLogs(list, "1", "early"));//最早一次使用信用卡认证申请时间
			if(null!=odi.getSQ042()){
				odi.setSQ043(DateUtils.getIntervalDayAmount(
						DateUtils.yyyyMMddToDate(odi.getSQ042()), 
						dateEnd));//最早一次使用信用卡认证申请距今的天数
			}
			odi.setSQ044(getCreditCardBindLogs(list, "0", "recently"));//最近一次_借记卡_申请认证时间
			if(null!=odi.getSQ044()){
				odi.setSQ045(DateUtils.getIntervalDayAmount(
						DateUtils.yyyyMMddToDate(odi.getSQ044()), 
						dateEnd));//最近一次_借记卡_申请认证_距今天数
			}
			odi.setSQ046(getCreditCardBindLogs(list, "1", "early"));//最早一次_借记卡_申请认证时间
			if(null!=odi.getSQ046()){
				odi.setSQ047(DateUtils.getIntervalDayAmount(
						DateUtils.yyyyMMddToDate(odi.getSQ046()), 
						dateEnd));//最早一次_借记卡_申请认证_距今天数
			}
		}
	}
	
	/**
	 * 获取最近或者最早信用卡时间 dcType:借贷类型(0：借记卡，1：贷记卡)
	 * @param list
	 * @param dcType
	 * @return
	 */
	public String getCreditCardBindLogs(List<BindCardLog> list, String dcType,String status){
		List<BindCardLog> tmpList = new ArrayList<BindCardLog>();
		for (BindCardLog o : list) {
			if(dcType.equals(o.getDC_TYPE())){
				tmpList.add(o);
			}
		}
		if(tmpList.size()>0){
			Collections.sort(tmpList);
			if("early".equals(status)){//最早
				return tmpList.get(0).getTXN_DATE();
			}else if("recently".equals(status)){//最近
				return tmpList.get(tmpList.size()-1).getTXN_DATE();
			}
		}
		return null;
	}

	/**
	 * 每张借记卡申请最小记录数 dcType:借贷类型(0：借记卡，1：贷记卡)
	 * 
	 * @param list
	 * @param dcType
	 * @param flag
	 *            max/min
	 * @return
	 */
	private int getBindCardMinRecordsByDcType(List<BindCardLog> list, String dcType, String flag) {
		List<Integer> countList = new ArrayList<Integer>();
		Collection<List<BindCardLog>> mapList = bindCardNoMap(list, dcType).values();
		for (List<BindCardLog> o : mapList) {
			countList.add(o.size());
		}
		if (flag.equals("max")) {
			return Collections.max(countList);
		} else if (flag.equals("min")) {
			return Collections.min(countList);
		}
		return 0;
	}

	/**
	 * 获取以银行卡号为主键的所有记录
	 * 
	 * @param list
	 * @param dcType
	 * @return
	 */
	private Map<String, List<BindCardLog>> bindCardNoMap(List<BindCardLog> list, String dcType) {
		Map<String, List<BindCardLog>> map = new HashMap<String, List<BindCardLog>>();
		for (BindCardLog o : list) {
			String bankNo = o.getENC();// 银行卡号
			if (dcType == null) {
				if (map.containsKey(bankNo)) {
					map.get(bankNo).add(o);
				} else {
					List<BindCardLog> bclLst = new ArrayList<BindCardLog>();
					bclLst.add(o);
					map.put(bankNo, bclLst);
				}
			} else if (dcType.equals(o.getDC_TYPE())) {
				if (map.containsKey(bankNo)) {
					map.get(bankNo).add(o);
				} else {
					List<BindCardLog> bclLst = new ArrayList<BindCardLog>();
					bclLst.add(o);
					map.put(bankNo, bclLst);
				}
			}
		}
		return map;
	}

	/**
	 * 
	 * @param list
	 * @param orgType
	 *            商户类型
	 * @param dcType
	 *            借贷类型(0：借记卡，1：贷记卡)
	 * @param success
	 *            认证状态
	 * @return
	 */
	private BigDecimal averageBindCardRecord(List<BindCardLog> list, String orgType, String dcType, String success) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		int totalRecords = 0;
		int successRecords = 0;
		for (BindCardLog o : list) {
			if (null == orgType) {// 所有机构
				// 每张借记卡申请认证成功的平均记录数
				if (null != dcType) {
					if (dcType.equals(o.getDC_TYPE())) {// 借贷类型
						totalRecords++;
						if (null != success) {// 认证状态
							if (success.equals(o.getVALID_STAT())) {// 认证成功
								successRecords++;
							}
						} else {// 认证所有状态
							successRecords++;
						}
					}
				} else {// 指定机构

				}
			}
		}
		if (totalRecords == 0) {
			return new BigDecimal(0.00);
		} else {

		}
		return MathUtil.divide(totalRecords, successRecords);
	}

	/**
	 * 获取dcType卡记录数 平均每张借记卡申请记录数 dcType:借贷类型(0：借记卡，1：贷记卡)
	 * 
	 * @param list
	 * @param orgType
	 * @param dcType:
	 * @return
	 */
	public int getBindCardByDcType(List<BindCardLog> list, String orgType, String dcType) {
		int i = 0;
		for (BindCardLog o : list) {
			if (null == orgType) {
				if (null != dcType) {
					if (o.getDC_TYPE().equals(dcType)) {
						i++;
					}
				}
			} else {// 所有的机构
				Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
				List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
				if (orgTypeList.contains(String.valueOf(o.getMER_TYPE()))) {
					i++;
				}
			}
		}
		return i;
	}

	public List<TradeDetailDO> getListByMonth(List<TradeDetailDO> fatherList, int month, String udpateTimes) {
		List<TradeDetailDO> list = new ArrayList<TradeDetailDO>();
		Timestamp end = new Timestamp(DateUtils.yyyyMMddToDate(udpateTimes).getTime());
		Timestamp begin = null;
		if (month == 15 || month == 7) {// 按天计算日期
			begin = DateUtils.getDateOfXDaysAgo(end, month);
		} else {
			begin = DateUtils.getDateOfXMonthsAgo(end, month);
		}
		for (TradeDetailDO o : fatherList) {
			if (DateUtils.judge(begin, end, o.getCREATE_TIME())) {
				list.add(o);
			}
		}
		return list;
	}

	public void deal1(String updateTime, String flag) {
		try {
			List<String> taskList = getTaskList(updateTime, flag);
			sliceTask(taskList, updateTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onePerson(String certNo, String updateTime) {
		Map<String, String[]> returnCodeDic = initProperties.getReturnCodeDic();
		Map<String, Integer> overDueMouth = initProperties.getOverDueMonth();
		OverDueIndex odi = new OverDueIndex();
		odi.setCERT_NO(certNo);
		// 如果是人的所有记录
		Map<Integer, List<TradeDetailDO>> tradeMap = fatherList(certNo, updateTime);
		for (int month : overDueMouth.values()) {
			// 指标结果集
			List<TradeDetailDO> list = tradeMap.get(month);
			overDueMouth(list, odi, month, returnCodeDic);
		}
		logger.info("odi:{}", JSON.toJSONString(odi));
	}

	/**
	 * 判断指定days逾期次数
	 * 
	 * @param list
	 * @param p
	 * @param days
	 * @return
	 */
	public int overDueDayTimes(List<TradeDetailDO> list, List<String> ywbzLst, int days) {
		// list.size>=2
		int overTimes = 0;
		for (int i = 0; i < list.size(); i++) {
			if(list.size()==(i+1))continue;
			if (!ywbzLst.contains(list.get(i).getRETURN_CODE())
					|| ( list.get(i).getAMOUNT().equals(list.get(i + 1).getAMOUNT())
							&& ywbzLst.contains(list.get(i).getRETURN_CODE()) && i != 0
							&& ywbzLst.contains(list.get(i + 1).getRETURN_CODE()))) {
				continue;
			} else {
				TradeDetailDO o = list.get(i);
				List<TradeDetailDO> tmp = list.subList(i+1, list.size() - 1);
				TradeDetailDO end = getNextRecordOfList(tmp, o);
				if (null == end)
					continue;
				if (days != 0) {// 如果有指定天数就按照指定天数统计
					int overDueBeginDayTemp = DateUtils.getIntervalDayAmount(o.getCREATE_TIME(), end.getCREATE_TIME());
					if (overDueBeginDayTemp == initProperties.getOverDueDayDic().get(days + "d")) {
						overTimes++;
					}
				} else {// 没有指定天数就统计逾期次数
					overTimes++;
				}
			}
		}
		return overTimes;
	}
	
	/**
	 * 判断指定days逾期天数
	 * 在指定机构下面的所有机构的逾期天数总和
	 * @param list
	 * @param p
	 * @param days
	 * @return
	 */
	public int overDueDays(List<TradeDetailDO> list, List<String> ywbzLst) {
		// list.size>=2
		int overDueDays = 0;
		for (int i = 0; i < list.size(); i++) {
			if(list.size()==(i+1))continue;
			if (!ywbzLst.contains(list.get(i).getRETURN_CODE())
					|| ( list.get(i).getAMOUNT().equals(list.get(i + 1).getAMOUNT())
							&& ywbzLst.contains(list.get(i).getRETURN_CODE()) && i != 0
							&& ywbzLst.contains(list.get(i + 1).getRETURN_CODE()))) {
				continue;
			} else {
				TradeDetailDO o = list.get(i);
				List<TradeDetailDO> tmp = list.subList(i+1, list.size() - 1);
				TradeDetailDO end = getNextRecordOfList(tmp, o);
				if (null == end)
					continue;
				overDueDays = overDueDays + DateUtils.getIntervalDayAmount(o.getCREATE_TIME(), end.getCREATE_TIME());
			}
		}
		return overDueDays;
	}
	
	
	/**
	 * 只取逾期记录金额相加
	 * 在指定机构下面的所有机构的逾期金额总和
	 * @param list
	 * @param p
	 * @param days
	 * @return
	 */
	public BigDecimal overDueSumMoney(List<TradeDetailDO> list, List<String> ywbzLst,int days) {
		// list.size>=2
		BigDecimal overDueDays = new BigDecimal(0.00);
		for (int i = 0; i < list.size(); i++) {
			if(list.size()==(i+1))continue;
			if (!ywbzLst.contains(list.get(i).getRETURN_CODE())
					|| ( list.get(i).getAMOUNT().equals(list.get(i + 1).getAMOUNT())
							&& ywbzLst.contains(list.get(i).getRETURN_CODE()) && i != 0
							&& ywbzLst.contains(list.get(i + 1).getRETURN_CODE()))) {
				continue;
			} else {
				TradeDetailDO o = list.get(i);
				List<TradeDetailDO> tmp = list.subList(i+1, list.size() - 1);
				TradeDetailDO end = getNextRecordOfList(tmp, o);
				if (null == end)
					continue;
				int overDueBeginDayTemp = DateUtils.getIntervalDayAmount(o.getCREATE_TIME(), end.getCREATE_TIME());
				if (overDueBeginDayTemp == initProperties.getOverDueDayDic().get(days + "d")) {
					overDueDays = overDueDays.add(o.getAMOUNT());
				}
			}
		}
		return overDueDays;
	}

	/**
	 * 口径3标准
	 * @param tmp
	 * @param o
	 * @return
	 */
	public TradeDetailDO getNextRecordOfList(List<TradeDetailDO> tmp, TradeDetailDO o) {
		for (TradeDetailDO to : tmp) {
			if ("0000".equals(to.getRETURN_CODE())) {
				return to;
			}
		}
		return null;
	}

	private void overDueMouth(List<TradeDetailDO> list, OverDueIndex odi, int month,
			Map<String, String[]> returnCodeDic) {

		if (month == 7) {// 七天
			/******************************* 逾期类变量 ***************************************/
			/**** 最大逾期天数 *****/
			odi.setYQ062(overDueInOrgsMaxDays(list,returnCodeDic,"xd"));//小贷_单机构_最大逾期天数
			odi.setYQ063(overDueInOrgsMaxDays(list,returnCodeDic,"xj"));//消金_单机构_最大逾期天数
			odi.setYQ064(overDueInOrgsMaxDays(list,returnCodeDic,"yh"));//银行_单机构_最大逾期天数
			odi.setYQ065(overDueInOrgsMaxDays(list,returnCodeDic,"dk"));//贷款_单机构_最大逾期天数
			/**** 逾期机构数 *****/
			odi.setYQ074(overDueMoreThanOneDayOfOrgSum(list,returnCodeDic,"dk",1));//全卡_贷款_逾期1天以上_机构数
			odi.setYQ075(overDueMoreThanOneDayOfOrgSum(list,returnCodeDic,"dk",7));//全卡_贷款_逾期7天以上_机构数
		    /******************************* 风险类变量 ***************************************/
            odi.setFX026(yebzProportion(list, returnCodeDic));
            
            int xjYebzAmount = yebzCount(list, "xj", returnCodeDic);
            int dkYebzAmount = yebzCount(list, "dk", returnCodeDic);
            int yhYebzAmount = yebzCount(list, "yh", returnCodeDic);
            int xdYebzAmount = yebzCount(list, "xd", returnCodeDic);
            
            odi.setFX027(xjYebzAmount);
            odi.setFX028(dkYebzAmount);
            odi.setFX029(yhYebzAmount);
            odi.setFX030(xdYebzAmount);

		} else if (month == 15) {// 十五天
			/******************************* 逾期类变量 ***************************************/
			/**** 最大逾期天数 *****/
			odi.setYQ058(overDueInOrgsMaxDays(list,returnCodeDic,"xd"));//小贷_单机构_最大逾期天数
			odi.setYQ059(overDueInOrgsMaxDays(list,returnCodeDic,"xj"));//消金_单机构_最大逾期天数
			odi.setYQ060(overDueInOrgsMaxDays(list,returnCodeDic,"yh"));//银行_单机构_最大逾期天数
			odi.setYQ061(overDueInOrgsMaxDays(list,returnCodeDic,"dk"));//贷款_单机构_最大逾期天数
			/**** 逾期机构数 *****/
			odi.setYQ072(overDueMoreThanOneDayOfOrgSum(list,returnCodeDic,"dk",1));//全卡_贷款_逾期1天以上_机构数
			odi.setYQ073(overDueMoreThanOneDayOfOrgSum(list,returnCodeDic,"dk",7));//全卡_贷款_逾期7天以上_机构数
		    /******************************* 风险类变量 ***************************************/
            odi.setFX031(yebzProportion(list, returnCodeDic));
            
            int xjYebzAmount = yebzCount(list, "xj", returnCodeDic);
            int dkYebzAmount = yebzCount(list, "dk", returnCodeDic);
            int yhYebzAmount = yebzCount(list, "yh", returnCodeDic);
            int xdYebzAmount = yebzCount(list, "xd", returnCodeDic);
            
            odi.setFX032(xjYebzAmount);
            odi.setFX033(dkYebzAmount);
            odi.setFX034(yhYebzAmount);
            odi.setFX035(xdYebzAmount);
		    
		} else if (month == 1) {// 一个月
			/******************************* 逾期类变量 ***************************************/
			/**** 最大逾期天数 *****/
			odi.setYQ054(overDueInOrgsMaxDays(list,returnCodeDic,"xd"));//小贷_单机构_最大逾期天数
			odi.setYQ055(overDueInOrgsMaxDays(list,returnCodeDic,"xj"));//消金_单机构_最大逾期天数
			odi.setYQ056(overDueInOrgsMaxDays(list,returnCodeDic,"yh"));//银行_单机构_最大逾期天数
			odi.setYQ057(overDueInOrgsMaxDays(list,returnCodeDic,"dk"));//贷款_单机构_最大逾期天数
		    /******************************* 还款类变量 ***************************************/
		    int dkRepaymentSuccessAmount = repaymentSuccessCount(list, "dk", returnCodeDic);
		    int xjRepaymentSuccessAmount = repaymentSuccessCount(list, "xj", returnCodeDic);
		    int yhRepaymentSuccessAmount = repaymentSuccessCount(list, "yh", returnCodeDic);
		    int xdRepaymentSuccessAmount = repaymentSuccessCount(list, "xd", returnCodeDic);
		    
		    int sumRepaymentSuccessAmount = dkRepaymentSuccessAmount + xjRepaymentSuccessAmount
		                                  + yhRepaymentSuccessAmount + xdRepaymentSuccessAmount;
		   
		    odi.setHK034(sumRepaymentSuccessAmount);
            odi.setHK035(repaymentSuccessProportion(list, returnCodeDic));
		    
		} else if (month == 2) {// 两个月
		    
		    /******************************* 还款类变量 ***************************************/
            odi.setHK032(repaymentSuccessProportion(list, returnCodeDic));
            odi.setHK033(repaymentSuccessMoneyProportion(list, returnCodeDic));
		    
	    } else if (month == 3) {
			/******************************* 逾期类变量 ***************************************/
	    	/**** 逾期1天以上次数 *****/
			odi.setYQ013(loanOrgOverDueOneDay(list, "dk", returnCodeDic,1));//全卡_贷款_逾期1天以上_总次数
			odi.setYQ014(loanOrgOverDueOneDay(list, "xj", returnCodeDic,1));//全卡_消金_逾期1天以上_总次数
			odi.setYQ015(loanOrgOverDueOneDay(list, "yh", returnCodeDic,1));//全卡_银行_逾期1天以上_总次数
			odi.setYQ016(loanOrgOverDueOneDay(list, "xd", returnCodeDic,1));//全卡_小贷_逾期1天以上_总次数
			/**** 逾期机构数 *****/
			odi.setYQ017(overDueMoreThanOneDayOfOrgSum(list, returnCodeDic,"dk",0));//全卡_贷款_逾期_机构数
			odi.setYQ018(overDueMoreThanOneDayOfOrgSum(list, returnCodeDic,"xj",0));//全卡_消金_逾期_机构数
			odi.setYQ019(overDueMoreThanOneDayOfOrgSum(list, returnCodeDic,"yh",0));//全卡_银行_逾期_机构数
			odi.setYQ020(overDueMoreThanOneDayOfOrgSum(list, returnCodeDic,"xd",0));//全卡_小贷_逾期_机构数
			/**** 平均逾期次数 *****/
			odi.setYQ022(MathUtil.divide(loanOrgOverDueOneDay(list, "dk", returnCodeDic,0), odi.getYQ017()));//全卡_消金_机构均_逾期次数
			odi.setYQ023(MathUtil.divide(avgOrgOverDueCount(list, "xj", returnCodeDic), odi.getYQ018()));//全卡_银行_机构均_逾期次数
			odi.setYQ024(MathUtil.divide(avgOrgOverDueCount(list, "yh", returnCodeDic), odi.getYQ019()));//全卡_小贷_机构均_逾期次数
			odi.setYQ025(MathUtil.divide(avgOrgOverDueCount(list, "xd", returnCodeDic), odi.getYQ020()));//全卡_贷款_机构均_逾期次数
			/**** 逾期天数总和 *****/
			odi.setYQ027(overDueDaysSum(list, "dk", returnCodeDic));
			/**** 逾期金额总和 *****/
			odi.setYQ031(overDueTotalMoneySum(list, "dk",returnCodeDic, 30));
			odi.setYQ032(overDueTotalMoneySum(list, "dk",returnCodeDic, 7));
			odi.setYQ033(overDueTotalMoneySum(list, "dk",returnCodeDic, 1));
			/**** 最大逾期天数 *****/
			odi.setYQ050(overDueInOrgsMaxDays(list,returnCodeDic,"xd"));//小贷_单机构_最大逾期天数
			odi.setYQ051(overDueInOrgsMaxDays(list,returnCodeDic,"xj"));//消金_单机构_最大逾期天数
			odi.setYQ052(overDueInOrgsMaxDays(list,returnCodeDic,"yh"));//银行_单机构_最大逾期天数
			odi.setYQ053(overDueInOrgsMaxDays(list,returnCodeDic,"dk"));//贷款_单机构_最大逾期天数
			/**** 逾期机构数 *****/
			odi.setYQ069(overDueMoreThanOneDayOfOrgSum(list,returnCodeDic,"dk",1));//全卡_贷款_逾期1天以上_机构数
			odi.setYQ070(overDueMoreThanOneDayOfOrgSum(list,returnCodeDic,"dk",7));//全卡_贷款_逾期7天以上_机构数
			odi.setYQ071(overDueMoreThanOneDayOfOrgSum(list,returnCodeDic,"dk",30));//全卡_贷款_逾期30天以上_机构数
			
			/******************************* 授信类变量 ***************************************/
			odi.setSX009(totalCreditLine(list, "dk", returnCodeDic));//全卡_贷款_授信_总额
			odi.setSX010(totalCreditLine(list, "yh", returnCodeDic));//全卡_银行_授信_总额
			odi.setSX011(maxCreditLine(list, "xd", returnCodeDic));//全卡_小贷_单机构_授信额_最大
			odi.setSX012(maxCreditLine(list, "xj", returnCodeDic));//全卡_消金_单机构_授信额_最大

			/******************************* 风险类变量 *****************************/
			int dkAcctfAmount = acctfCount(list, "dk", returnCodeDic);
			int xjAcctfAmount = acctfCount(list, "xj", returnCodeDic);
			int yhAcctfAmount = acctfCount(list, "yh", returnCodeDic);
			int xdAcctfAmount = acctfCount(list, "xd", returnCodeDic);

			odi.setFX017(dkAcctfAmount);
			odi.setFX018(xjAcctfAmount);
			odi.setFX019(yhAcctfAmount);
			odi.setFX020(xdAcctfAmount);
			// odi.setFX021(dkAcctfAmount + xjAcctfAmount + yhAcctfAmount +
			// xdAcctfAmount);

			odi.setFX022(acctfProportion(list, returnCodeDic));
			odi.setFX023(acctfMoneyProportion(list, returnCodeDic));
			odi.setFX024(otlmtMoneyProportion(list, returnCodeDic));
			odi.setFX025(otlmtProportion(list, returnCodeDic));
			
			/******************************* 还款类变量 ***************************************/
            odi.setHK022(repaymentSuccessCount(list, "dk", returnCodeDic));
            odi.setHK023(repaymentSuccessProportion(list, returnCodeDic));
            odi.setHK024(repamentYebzCount(list, returnCodeDic));
            odi.setHK025(repamentFailcProportion(list, returnCodeDic));
            
            odi.setHK026(repaymentSuccessOrgCount(list, "dk", returnCodeDic));
            odi.setHK027(repaymentSuccessOrgCount(list, "yh", returnCodeDic));
            odi.setHK028(repaymentSuccessOrgCount(list, "xj", returnCodeDic));
            odi.setHK029(repaymentSuccessOrgCount(list, "xd", returnCodeDic));
            
            odi.setHK030(repamentYebzProportion(list, returnCodeDic));
            odi.setHK031(repamentYebzMoneyProportion(list, returnCodeDic));

		} else if (month == 6) {
			/******************************* 逾期类变量 ***************************************/
			/**** 逾期1天以上次数 *****/
			odi.setYQ001(loanOrgOverDueOneDay(list, "dk", returnCodeDic,1));//全卡_贷款_逾期1天以上_总次数
			odi.setYQ002(loanOrgOverDueOneDay(list, "xj", returnCodeDic,1));//全卡_消金_逾期1天以上_总次数
			odi.setYQ003(loanOrgOverDueOneDay(list, "yh", returnCodeDic,1));//全卡_银行_逾期1天以上_总次数
			odi.setYQ004(loanOrgOverDueOneDay(list, "xd", returnCodeDic,1));//全卡_小贷_逾期1天以上_总次数
			/**** 平均逾期次数 *****/
			int dkOverDueOrgTimes= overDueMoreThanOneDayOfOrgSum(list, returnCodeDic,"dk",0);//全卡_贷款_逾期_机构数
			int xjOverDueOrgTimes = overDueMoreThanOneDayOfOrgSum(list, returnCodeDic,"xj",0);//全卡_消金_逾期_机构数
			int yhOverDueOrgTimes = overDueMoreThanOneDayOfOrgSum(list, returnCodeDic,"yh",0);//全卡_银行_逾期_机构数
			int xdOverDueOrgTimes = overDueMoreThanOneDayOfOrgSum(list, returnCodeDic,"xd",0);//全卡_小贷_逾期_机构数
			odi.setYQ005(MathUtil.divide(loanOrgOverDueOneDay(list, "dk", returnCodeDic,0), dkOverDueOrgTimes));//全卡_贷款_机构均_逾期次数
			odi.setYQ006(MathUtil.divide(loanOrgOverDueOneDay(list, "xj", returnCodeDic,0), xjOverDueOrgTimes));//全卡_消金_机构均_逾期次数
			odi.setYQ007(MathUtil.divide(loanOrgOverDueOneDay(list, "yh", returnCodeDic,0), yhOverDueOrgTimes));//全卡_银行_机构均_逾期次数
			odi.setYQ008(MathUtil.divide(loanOrgOverDueOneDay(list, "xd", returnCodeDic,0), xdOverDueOrgTimes));//全卡_小贷_机构均_逾期次数
			/**** 逾期机构数 *****/
			odi.setYQ009(dkOverDueOrgTimes);//全卡_贷款_逾期_机构数
			odi.setYQ010(xjOverDueOrgTimes);//全卡_消金_逾期_机构数
			odi.setYQ011(yhOverDueOrgTimes);//全卡_银行_逾期_机构数
			odi.setYQ012(xdOverDueOrgTimes);//全卡_小贷_逾期_机构数
			/**** 逾期天数总和 *****/
			odi.setYQ026(overDueDaysSum(list, "dk", returnCodeDic));
			/**** 逾期金额总和 *****/
			odi.setYQ028(overDueTotalMoneySum(list, "dk",returnCodeDic, 30));//全卡_贷款_逾期30天_金额_总和
			odi.setYQ029(overDueTotalMoneySum(list, "dk",returnCodeDic, 7));//全卡_贷款_逾期7天_金额_总和
			odi.setYQ030(overDueTotalMoneySum(list, "dk",returnCodeDic, 1));//全卡_贷款_逾期1天_金额_总和
			/**** 最大逾期天数 *****/
			odi.setYQ046(overDueInOrgsMaxDays(list,returnCodeDic,"xd"));//小贷_单机构_最大逾期天数
			odi.setYQ047(overDueInOrgsMaxDays(list,returnCodeDic,"xj"));//消金_单机构_最大逾期天数
			odi.setYQ048(overDueInOrgsMaxDays(list,returnCodeDic,"yh"));//银行_单机构_最大逾期天数
			odi.setYQ049(overDueInOrgsMaxDays(list,returnCodeDic,"dk"));//贷款_单机构_最大逾期天数
			/**** 逾期机构数 *****/
			odi.setYQ066(overDueMoreThanOneDayOfOrgSum(list,returnCodeDic,"dk",1));//全卡_贷款_逾期1天以上_机构数
			odi.setYQ067(overDueMoreThanOneDayOfOrgSum(list,returnCodeDic,"dk",7));//全卡_贷款_逾期7天以上_机构数
			odi.setYQ068(overDueMoreThanOneDayOfOrgSum(list,returnCodeDic,"dk",30));//全卡_贷款_逾期30天以上_机构数
			
			/******************************* 放款类变量 ***************************************/
			odi.setFK008(loanOrgLoanSuccessTimes(list, "dk", returnCodeDic));// 成功放款的记录数
			odi.setFK009(loanOrgLoanSuccessOrg(list, "dk", returnCodeDic));// 成功放款的不同机构数
			odi.setFK010(loanOrgLoanSumMoney(list, "yh", returnCodeDic));// 在银行类机构放款的总金额
			odi.setFK011(loanOrgLoanSumMoney(list, "xj", returnCodeDic));// 在消费金融类机构放款的总金额
			odi.setFK012(loanOrgLoanSumMoney(list, "xd", returnCodeDic));// 在小额贷款类机构放款的总金额
			odi.setFK013(loanOrgLoanSumMoney(list, "dk", returnCodeDic));// 在贷款类机构放款的总金额

			/******************************* 授信类变量 ***************************************/
			odi.setSX005(totalCreditLine(list, "dk", returnCodeDic));//全卡_贷款_授信_总额
			odi.setSX006(totalCreditLine(list, "yh", returnCodeDic));//全卡_银行_授信_总额
			odi.setSX007(maxCreditLine(list, "xd", returnCodeDic));//全卡_小贷_单机构_授信额_最大
			odi.setSX004(maxCreditLine(list, "xj", returnCodeDic));//全卡_消金_单机构_授信额_最大

            /******************************* 还款类变量 ***************************************/
            odi.setHK054(repaymentSuccessMoneyCount(list, "yh", returnCodeDic));
            odi.setHK055(repaymentSuccessMoneyCount(list, "xj", returnCodeDic));
            odi.setHK056(repaymentSuccessMoneyCount(list, "xd", returnCodeDic));
            odi.setHK057(repaymentSuccessMoneyCount(list, "dk", returnCodeDic));
            
            /******************************* 风险类变量 *****************************/
			int dkAcctfAmount = acctfCount(list, "dk", returnCodeDic);
			int xjAcctfAmount = acctfCount(list, "xj", returnCodeDic);
			int yhAcctfAmount = acctfCount(list, "yh", returnCodeDic);
			int xdAcctfAmount = acctfCount(list, "xd", returnCodeDic);
			odi.setFX008(dkAcctfAmount);
			odi.setFX009(xjAcctfAmount);
			odi.setFX010(yhAcctfAmount);
			odi.setFX011(xdAcctfAmount);
			// odi.setFX012(dkAcctfAmount + xjAcctfAmount + yhAcctfAmount +
			// xdAcctfAmount);
			odi.setFX013(acctfProportion(list, returnCodeDic));
			odi.setFX014(acctfMoneyProportion(list, returnCodeDic));
			odi.setFX015(otlmtMoneyProportion(list, returnCodeDic));
			odi.setFX016(otlmtProportion(list, returnCodeDic));
			
//			odi.setHK054(repaymentSuccessMoneyCount(list, "yh", returnCodeDic));
//          odi.setHK055(repaymentSuccessMoneyCount(list, "xj", returnCodeDic));
//          odi.setHK056(repaymentSuccessMoneyCount(list, "xd", returnCodeDic));
//          odi.setHK057(repaymentSuccessMoneyCount(list, "dk", returnCodeDic));
            
            odi.setHK012(repaymentSuccessCount(list, "dk", returnCodeDic));
            odi.setHK013(repaymentSuccessProportion(list, returnCodeDic));
            odi.setHK014(repaymentSuccessOrgCount(list, "dk", returnCodeDic));
            odi.setHK015(repaymentSuccessOrgCount(list, "yh", returnCodeDic));
            odi.setHK016(repaymentSuccessOrgCount(list, "xj", returnCodeDic));
            odi.setHK017(repaymentSuccessOrgCount(list, "xd", returnCodeDic));
            
            odi.setHK018(repamentYebzCount(list, returnCodeDic));
            odi.setHK019(repamentFailcProportion(list, returnCodeDic));
            odi.setHK020(repamentYebzProportion(list, returnCodeDic));
            odi.setHK021(repamentYebzMoneyProportion(list, returnCodeDic));

		} else if (month == 12) {
			
			/******************************* 逾期机构数 ***************************************/
			/**** 逾期机构数 *****/
			odi.setYQ034(overDueMoreThanOneDayOfOrgSum(list, returnCodeDic,"dk",0));//全卡_贷款_逾期_机构数
			odi.setYQ035(overDueMoreThanOneDayOfOrgSum(list, returnCodeDic,"yh",0));//全卡_消金_逾期_机构数
			odi.setYQ036(overDueMoreThanOneDayOfOrgSum(list, returnCodeDic,"xj",0));//全卡_银行_逾期_机构数
			odi.setYQ037(overDueMoreThanOneDayOfOrgSum(list, returnCodeDic,"xd",0));//全卡_小贷_逾期_机构数
			/**** 逾期一天以上次数 *****/
			odi.setYQ038(loanOrgOverDueOneDay(list, "dk", returnCodeDic,1));//全卡_贷款_逾期1天以上_总次数
			odi.setYQ039(loanOrgOverDueOneDay(list, "xj", returnCodeDic,1));//全卡_消金_逾期1天以上_总次数
			odi.setYQ040(loanOrgOverDueOneDay(list, "yh", returnCodeDic,1));//全卡_银行_逾期1天以上_总次数
			odi.setYQ041(loanOrgOverDueOneDay(list, "xd", returnCodeDic,1));//全卡_小贷_逾期1天以上_总次数
			/****  最大逾期次数 *****/
			odi.setYQ042(everyOrgOverDueMaxTimes(list, "dk", returnCodeDic));//消金_单机构_逾期次数_最大
			odi.setYQ043(everyOrgOverDueMaxTimes(list, "xj", returnCodeDic));//贷款_单机构_逾期次数_最大
			odi.setYQ044(everyOrgOverDueMaxTimes(list, "yh", returnCodeDic));//银行_单机构_逾期次数_最大
			odi.setYQ045(everyOrgOverDueMaxTimes(list, "xd", returnCodeDic));//小贷_单机构_逾期次数_最大

			/******************************* 放款类变量 ***************************************/
			odi.setFK001(loanOrgLoanSuccessTimes(list, "dk", returnCodeDic));// 成功放款的记录数
			odi.setFK002(loanOrgLoanSuccessOrg(list, "dk", returnCodeDic));// 成功放款的不同机构数
			// odi.setFK003(odi.getFK003());// fk002 和 fk003 是一样的；
			odi.setFK004(loanOrgLoanSumMoney(list, "yh", returnCodeDic));// 在银行类机构放款的总金额
			odi.setFK005(loanOrgLoanSumMoney(list, "xj", returnCodeDic));// 在消费金融类机构放款的总金额
			odi.setFK006(loanOrgLoanSumMoney(list, "xd", returnCodeDic));// 在小额贷款类机构放款的总金额
			odi.setFK007(loanOrgLoanSumMoney(list, "dk", returnCodeDic));// 在贷款类机构放款的总金额
			/* 最近一次 */
			odi.setFK014(loanOrgRecentLoanDate(list, "dk", returnCodeDic, "2"));
			if (odi.getFK014() != null) {
				Date dateBegin = DateUtils.yyyyMMddToDate(odi.getFK014());
				odi.setFK015(DateUtils.getIntervalDayAmount(dateBegin, new Date()));
			}
			/* 最早一次 */
			odi.setFK016(loanOrgRecentLoanDate(list, "dk", returnCodeDic, "1"));
			if (odi.getFK016() != null) {
				Date dateBegin = DateUtils.yyyyMMddToDate(odi.getFK016());
				odi.setFK017(DateUtils.getIntervalDayAmount(dateBegin, new Date()));
			}

			/******************************* 授信类变量 ***************************************/
			odi.setSX001(maxCreditLine(list, "dk", returnCodeDic));//全卡_贷款_单机构_授信额_最大
			odi.setSX002(maxCreditLine(list, "yh", returnCodeDic));//全卡_银行_单机构_授信额_最大
			odi.setSX003(maxCreditLine(list, "xd", returnCodeDic));//全卡_小贷_单机构_授信额_最大
			odi.setSX004(maxCreditLine(list, "xj", returnCodeDic));//全卡_消金_单机构_授信额_最大

			/******************************* 风险类变量 ***************************************/
			int dkAcctfAmount = acctfCount(list, "dk", returnCodeDic);
			int xjAcctfAmount = acctfCount(list, "xj", returnCodeDic);
			int yhAcctfAmount = acctfCount(list, "yh", returnCodeDic);
			int xdAcctfAmount = acctfCount(list, "xd", returnCodeDic);

			odi.setFX001(dkAcctfAmount);
			odi.setFX002(xjAcctfAmount);
			odi.setFX003(yhAcctfAmount);
			odi.setFX004(xdAcctfAmount);
			// odi.setFX005(dkAcctfAmount + xjAcctfAmount + yhAcctfAmount +
			// xdAcctfAmount);

			odi.setFX006(acctfProportion(list, returnCodeDic));
			odi.setFX007(acctfMoneyProportion(list, returnCodeDic));
            
            /******************************* 还款类变量 ***************************************/
            odi.setHK050(repaymentSuccessMoneyCount(list, "yh", returnCodeDic));
            odi.setHK051(repaymentSuccessMoneyCount(list, "xj", returnCodeDic));
            odi.setHK052(repaymentSuccessMoneyCount(list, "xd", returnCodeDic));
            odi.setHK053(repaymentSuccessMoneyCount(list, "dk", returnCodeDic));
            
            odi.setHK001(repaymentSuccessCount(list, "dk", returnCodeDic));
            odi.setHK002(repaymentSuccessProportion(list, returnCodeDic));
            odi.setHK003(repaymentSuccessOrgCount(list, "dk", returnCodeDic));
            odi.setHK004(repaymentSuccessOrgCount(list, "yh", returnCodeDic));
            odi.setHK005(repaymentSuccessOrgCount(list, "xj", returnCodeDic));
            odi.setHK006(repaymentSuccessOrgCount(list, "xd", returnCodeDic));
            
            odi.setHK007(repamentYebzCount(list, returnCodeDic));
            odi.setHK008(repamentFailcProportion(list, returnCodeDic));
            odi.setHK009(repamentYebzProportion(list, returnCodeDic));
            odi.setHK010(repamentYebzMoneyProportion(list, returnCodeDic));
            
            /**
             * 还款类变量：时间指标
             */
            //最早一次
            Map<String, Object> hkEarliestCountResultMap = hkEarlistDateAndDays(list, "dk");
            odi.setHK036((String)hkEarliestCountResultMap.get("hkDate"));
            odi.setHK037((Integer)hkEarliestCountResultMap.get("hkDays"));
            
            //最近一次
            Map<String, Object> hkLatestCountResultMap = hkLatestDateAndDays(list, "dk");
            odi.setHK038((String)hkLatestCountResultMap.get("hkDate"));
            odi.setHK039((Integer)hkLatestCountResultMap.get("hkDays"));
            
            Map<String, Object> hkDkLatestOfOrgSuccessCountResultMap = hkLatestOfOrgDateAndDays(list, "dk", returnCodeDic, "success");
            odi.setHK040((String)hkDkLatestOfOrgSuccessCountResultMap.get("hkDate"));
            odi.setHK041((Integer)hkDkLatestOfOrgSuccessCountResultMap.get("hkDays"));
            
            Map<String, Object> hkYhLatestOfOrgSuccessCountResultMap = hkLatestOfOrgDateAndDays(list, "yh", returnCodeDic, "success");
            odi.setHK042((String)hkYhLatestOfOrgSuccessCountResultMap.get("hkDate"));
            odi.setHK043((Integer)hkYhLatestOfOrgSuccessCountResultMap.get("hkDays"));
            
            Map<String, Object> hkDkLatestOfOrgFailCountResultMap = hkLatestOfOrgDateAndDays(list, "dk", returnCodeDic, "fail");
            odi.setHK044((String)hkDkLatestOfOrgFailCountResultMap.get("hkDate"));
            odi.setHK045((Integer)hkDkLatestOfOrgFailCountResultMap.get("hkDays"));
            
            Map<String, Object> hkYhLatestOfOrgFailCountResultMap = hkLatestOfOrgDateAndDays(list, "yh", returnCodeDic, "fail");
            odi.setHK046((String)hkYhLatestOfOrgFailCountResultMap.get("hkDate"));
            odi.setHK047((Integer)hkYhLatestOfOrgFailCountResultMap.get("hkDays"));
            
            Map<String, Object> hkXdLatestOfOrgFailCountResultMap = hkLatestOfOrgDateAndDays(list, "xd", returnCodeDic, "fail");
            odi.setHK048((String)hkXdLatestOfOrgFailCountResultMap.get("hkDate"));
            odi.setHK049((Integer)hkXdLatestOfOrgFailCountResultMap.get("hkDays"));
		
		}
	}

	/**
	 * 在指定机构下逾期指定天数以上的机构数统计
	 * @param list
	 * @param returnCodeDic
	 * @param orgType
	 * @param i
	 * @return
	 */
	private int overDueMoreThanOneDayOfOrgSum(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic,
			String orgType, int i) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));// 余额不足失败返回码
		// 定义一个用户的银行卡在不同机构下拥有的消费记录集合
		int orgs = 0;
    	Map<String, List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
    	for (Map.Entry<String, List<TradeDetailDO>> entry : map.entrySet()) {
    		List<TradeDetailDO> tmp = entry.getValue();
			if (tmp.size() <= 1)
				continue;
			if(i <= overDueDayTimes(tmp, ywbzLst, i)){
				orgs++;
			}
    	}
		return orgs;
	}

	/**
	 * 在某机构中最大的逾期天数
	 * @param list
	 * @param returnCodeDic
	 * @param orgType
	 * @return
	 */
    private int overDueInOrgsMaxDays(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic, String orgType) {
    	Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));// 余额不足失败返回码
		List<Integer> maxDays = new ArrayList<Integer>();//最大逾期天数
    	// 定义一个用户的银行卡在不同机构下拥有的消费记录集合
    	Map<String, List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
    	for (Map.Entry<String, List<TradeDetailDO>> entry : map.entrySet()) {
			List<TradeDetailDO> tmp = entry.getValue();
			if (tmp.size() <= 1)
				continue;
			maxDays.add(overDueDays(tmp, ywbzLst));//不指定逾期天数
		}
    	if(maxDays.size()==0)return 0;
		return Collections.max(maxDays);
	}

	/**
	 * 每个商户的所有记录
	 * 
	 * @param list
	 * @param orgTypeList
	 * @return
	 */
	public Map<String, List<TradeDetailDO>> merTypeMap(List<TradeDetailDO> list, List<String> orgTypeList) {
		Map<String, List<TradeDetailDO>> map = new HashMap<String, List<TradeDetailDO>>();
		List<TradeDetailDO> records = null;
		for (TradeDetailDO o : list) {
			String merId = o.getSOURCE_MERNO();// 银行卡
			// 非指定机构不参与逾期统计
			if (!orgTypeList.contains(o.getMER_TYPE().toString()))
				continue;
			if (!map.containsKey(merId)) {
				records = new ArrayList<TradeDetailDO>();
				records.add(o);
			} else {
				records = map.get(merId);
				records.add(o);
			}
			map.put(merId, records);
		}
		return map;
	}

	/**
	 * 逾期金额总和 : 近x个月逾期x天以上金额总和 逾期类型3 3.在同一家公司划扣因余额不足失败，直至划扣成功为止
	 * 全卡_贷款_逾期30天_金额_总和
	 * @param list
	 * @param orgType
	 * @param returnCodeDic
	 * @param days
	 */
	public BigDecimal overDueTotalMoneySum(List<TradeDetailDO> list,String orgType,  Map<String, String[]> returnCodeDic, int days) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));// 余额不足失败返回码
		// 逾期金额总和
		BigDecimal overDueSumMoney = new BigDecimal("0.00");
		Map<String, List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
		for (Map.Entry<String, List<TradeDetailDO>> entry : map.entrySet()) {
			List<TradeDetailDO> tmp = entry.getValue();
			if (tmp.size() <= 1)
				continue;
			overDueSumMoney = overDueSumMoney.add(overDueSumMoney(tmp, ywbzLst, days));
		}
		
		return overDueSumMoney;
	}

	/**
	 * 逾期天数总和 : 近x个月逾期x天以上天数总和 逾期类型3 3.在同一家公司划扣因余额不足失败，直至划扣成功为止
	 * 在指定机构类型下面的所有机构逾期的天数总和
	 * @param list
	 * @param indexMap
	 * @param orgType
	 * @param returnCodeDic
	 */
	public int overDueDaysSum(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
		// 余额不足失败返回码
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
		// 逾期天数值
		int overDueOneDays = 0;
		Map<String, List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
		for (Map.Entry<String, List<TradeDetailDO>> entry : map.entrySet()) {
			List<TradeDetailDO> tmp = entry.getValue();
			if(tmp.size()<2)continue;
			overDueOneDays = overDueOneDays + overDueDays(tmp, ywbzLst);
		}
		return overDueOneDays;
	}

	/**
	 * 近12个月在xx机构逾期的最大每家机构逾期次数 逾期类型3 3.在同一家公司划扣因余额不足失败，直至划扣成功为止
	 * 
	 * @param list
	 * @param indexMap
	 * @param orgType
	 * @param returnCodeDic
	 */
	public int everyOrgOverDueMaxTimes(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));// 余额不足失败返回码
		List<Integer> maxOverDueTimesList = new ArrayList<Integer>();
		Map<String, List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
		for (Map.Entry<String, List<TradeDetailDO>> entry : map.entrySet()) {
			List<TradeDetailDO> tmp = entry.getValue();
			if(tmp.size()<2)continue;
			maxOverDueTimesList.add(overDueDayTimes(list, ywbzLst, 0));
		}
		if(maxOverDueTimesList.size()==0)return 0;
		return Collections.max(maxOverDueTimesList);
	}

	/**
	 * 在贷款类机构逾期days天以上次数 逾期类型3 3.在同一家公司划扣因余额不足失败，直至划扣成功为止
	 * 
	 * @param list
	 * @param indexMap
	 * @param orgType
	 * @param returnCodeDic
	 * @param days
	 */
	public int loanOrgOverDueOneDay(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic,int days) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));// 余额不足失败返回码
		// 定义一个用户的银行卡在不同机构下拥有的消费记录集合
		Map<String, List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
		int overTimes = 0;
		for (Map.Entry<String, List<TradeDetailDO>> entry : map.entrySet()) {
			List<TradeDetailDO> tmp = entry.getValue();
			if (tmp.size() <= 1)
				continue;
			overTimes= overTimes+overDueDayTimes(list, ywbzLst, 1);
		}
		return overTimes;
	}

	/**
	 * 逾期机构数 逾期类型3 3.在同一家公司划扣因余额不足失败，可视为一次在该机构下的逾期
	 * 
	 * @param list
	 * @param indexMap
	 * @param orgType
	 * @param returnCodeDic
	 */
	public int overDueOrgCount(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));// 余额不足失败返回码

		// 逾期天数值
		int overDueOneOrgCount = 0;
		// 定义一个用户的银行卡在不同机构下拥有的消费记录集合
		Map<String, List<TradeDetailDO>> map = new HashMap<String, List<TradeDetailDO>>();
		List<TradeDetailDO> records = null;

		for (TradeDetailDO o : list) {
			// 商户号
			String merId = o.getSOURCE_MERNO();
			// 非指定机构不参与逾期统计
			if (!orgTypeList.contains(o.getMER_TYPE().toString()))
				continue;
			if (!map.containsKey(merId)) {
				records = new ArrayList<TradeDetailDO>();
				records.add(o);
			} else {
				records = map.get(merId);
				records.add(o);
			}
			map.put(merId, records);
		}

		// 计算逾期
		for (Map.Entry<String, List<TradeDetailDO>> entry : map.entrySet()) {
			List<TradeDetailDO> cardNolist = entry.getValue();
			for (TradeDetailDO o : cardNolist) {
				// 余额不足,划扣失败,看做该机构下有逾期，逾期机构数加1
				if (ywbzLst.contains(o.getRETURN_CODE())) {
					overDueOneOrgCount++;
					// 跳出当前循环，继续往下寻找
					break;
				}
			}
		}
		return overDueOneOrgCount;
	}

	/**
	 * 在消费金融机构逾期1天以上次数
	 * 
	 * @param list
	 * @param indexMap
	 * @param orgType
	 * @param returnCodeDic
	 */
	public void consumerFinanceOrgOverDueOneDay(List<TradeDetailDO> list, Map<String, Integer> indexMap, String orgType,
			Map<String, String[]> returnCodeDic) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 机构类型
		String extKey = "_" + orgType;
		// int days = initProperties.getOverDueDayDic().get("1d");
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
		List<String> success = Arrays.asList(returnCodeDic.get("success"));
		// 定义一个用户在不同机构下拥有的消费记录集合
		Map<String, List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
		// key orgKey,value:逾期次数（不同机构的逾期次数）
		Map<String, Integer> averageOrgOverDue = new HashMap<String, Integer>();
		// key orgKey,value:逾期天数（不同机构的逾期天数）
		Map<String, Integer> averageOrgOverDay = new HashMap<String, Integer>();
		// 排序和计算逾期
		for (Map.Entry<String, List<TradeDetailDO>> entry : map.entrySet()) {
			String cardNo = entry.getKey();
			List<TradeDetailDO> cardNolist = entry.getValue();
			if (cardNolist.size() <= 1)
				continue;
			// 对集合按照日期进行排序
			// Collections.sort(cardNolist);
			double amout = 0;
			// 逾期日期值
			Timestamp overDueBeginDate = null;
			// 逾期天数值
			int overDueBeginDay = 0;
			for (TradeDetailDO o : cardNolist) {
				if (cardNolist.size() <= 1)
					continue;
				// 余额不足,划扣失败
				if (ywbzLst.contains(o.getRETURN_CODE())) {
					// 记录失败金额
					amout = o.getAMOUNT().doubleValue();
					// 逾期失败日期
					overDueBeginDate = o.getCREATE_TIME();
					continue;
				} else if (success.contains(o.getRETURN_CODE())) {
					String merId = o.getSOURCE_MERNO() + extKey;// 机构平均值主键
					if (!averageOrgOverDue.containsKey(merId)) {
						int i = averageOrgOverDue.get(merId);
						// 逾期次数
						averageOrgOverDue.put(merId, ++i);
					} else {
						averageOrgOverDue.put(merId, 1);
					}
					// 划扣成功,最终划扣成功且划扣成功金额=失败金额
					if (amout == o.getAMOUNT().doubleValue()) {
						// 计算逾期天数
						int overDueBeginDayTemp = DateUtils.differentDaysByMillisecond(overDueBeginDate,
								o.getCREATE_TIME());
						averageOrgOverDay.put(merId, overDueBeginDayTemp + overDueBeginDay);
					}
				}
			}
		}
	}

	/**
	 * 在银行类机构逾期1天以上次数
	 * 
	 * @param list
	 * @param indexMap
	 * @param orgType
	 * @param returnCodeDic
	 */
	public void bankOrgOverDueOneDay(List<TradeDetailDO> list, Map<String, Integer> indexMap, String orgType,
			Map<String, String[]> returnCodeDic) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 机构类型
		String extKey = "_" + orgType;
		// int days = initProperties.getOverDueDayDic().get("1d");
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
		List<String> success = Arrays.asList(returnCodeDic.get("success"));
		// 定义一个用户在不同机构下拥有的消费记录集合
		Map<String, List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
		// key orgKey,value:逾期次数（不同机构的逾期次数）
		Map<String, Integer> averageOrgOverDue = new HashMap<String, Integer>();
		// key orgKey,value:逾期天数（不同机构的逾期天数）
		Map<String, Integer> averageOrgOverDay = new HashMap<String, Integer>();
		// 排序和计算逾期
		for (Map.Entry<String, List<TradeDetailDO>> entry : map.entrySet()) {
			String cardNo = entry.getKey();
			List<TradeDetailDO> cardNolist = entry.getValue();
			if (cardNolist.size() <= 1)
				continue;
			// 对集合按照日期进行排序
			// Collections.sort(cardNolist);
			double amout = 0;
			// 逾期日期值
			Timestamp overDueBeginDate = null;
			// 逾期天数值
			int overDueBeginDay = 0;
			for (TradeDetailDO o : cardNolist) {
				if (cardNolist.size() <= 1)
					continue;
				// 余额不足,划扣失败
				if (ywbzLst.contains(o.getRETURN_CODE())) {
					// 记录失败金额
					amout = o.getAMOUNT().doubleValue();
					// 逾期失败日期
					overDueBeginDate = o.getCREATE_TIME();
					continue;
				} else if (success.contains(o.getRETURN_CODE())) {
					String merId = o.getSOURCE_MERNO() + extKey;// 机构平均值主键
					if (!averageOrgOverDue.containsKey(merId)) {
						int i = averageOrgOverDue.get(merId);
						// 逾期次数
						averageOrgOverDue.put(merId, ++i);
					} else {
						averageOrgOverDue.put(merId, 1);
					}
					// 划扣成功,最终划扣成功且划扣成功金额=失败金额
					if (amout == o.getAMOUNT().doubleValue()) {
						// 计算逾期天数
						int overDueBeginDayTemp = DateUtils.differentDaysByMillisecond(overDueBeginDate,
								o.getCREATE_TIME());
						averageOrgOverDay.put(merId, overDueBeginDayTemp + overDueBeginDay);
					}
				}
			}
		}
	}

	/**
	 * 在小贷款类机构逾期1天以上次数
	 * 
	 * @param list
	 * @param indexMap
	 * @param orgType
	 * @param returnCodeDic
	 */
	public void smallLoanOrgOverDueOneDay(List<TradeDetailDO> list, Map<String, Integer> indexMap, String orgType,
			Map<String, String[]> returnCodeDic) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 机构类型
		String extKey = "_" + orgType;
		// int days = initProperties.getOverDueDayDic().get("1d");
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
		List<String> success = Arrays.asList(returnCodeDic.get("success"));
		// 定义一个用户在不同机构下拥有的消费记录集合
		Map<String, List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
		// key orgKey,value:逾期次数（不同机构的逾期次数）
		Map<String, Integer> averageOrgOverDue = new HashMap<String, Integer>();
		// key orgKey,value:逾期天数（不同机构的逾期天数）
		Map<String, Integer> averageOrgOverDay = new HashMap<String, Integer>();
		// 排序和计算逾期
		for (Map.Entry<String, List<TradeDetailDO>> entry : map.entrySet()) {
			String cardNo = entry.getKey();
			List<TradeDetailDO> cardNolist = entry.getValue();
			if (cardNolist.size() <= 1)
				continue;
			// 对集合按照日期进行排序
			// Collections.sort(cardNolist);
			double amout = 0;
			// 逾期日期值
			Timestamp overDueBeginDate = null;
			// 逾期天数值
			int overDueBeginDay = 0;
			for (TradeDetailDO o : cardNolist) {
				if (cardNolist.size() <= 1)
					continue;
				// 余额不足,划扣失败
				if (ywbzLst.contains(o.getRETURN_CODE())) {
					// 记录失败金额
					amout = o.getAMOUNT().doubleValue();
					// 逾期失败日期
					overDueBeginDate = o.getCREATE_TIME();
					continue;
				} else if (success.contains(o.getRETURN_CODE())) {
					String merId = o.getSOURCE_MERNO() + extKey;// 机构平均值主键
					if (!averageOrgOverDue.containsKey(merId)) {
						int i = averageOrgOverDue.get(merId);
						// 逾期次数
						averageOrgOverDue.put(merId, ++i);
					} else {
						averageOrgOverDue.put(merId, 1);
					}
					// 划扣成功,最终划扣成功且划扣成功金额=失败金额
					if (amout == o.getAMOUNT().doubleValue()) {
						// 计算逾期天数
						int overDueBeginDayTemp = DateUtils.differentDaysByMillisecond(overDueBeginDate,
								o.getCREATE_TIME());
						averageOrgOverDay.put(merId, overDueBeginDayTemp + overDueBeginDay);
					}
				}
			}
		}
	}

	/**
	 * 逾期一天以上计算
	 * 
	 * @param list
	 * @param indexMap
	 * @param orgType
	 * @param returnCodeDic
	 */
	public void calculateOverDue(List<TradeDetailDO> list, Map<String, Integer> indexMap, String orgType,
			Map<String, String[]> returnCodeDic) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 机构类型
		String extKey = "_" + orgType;
		// int days = initProperties.getOverDueDayDic().get("1d");
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
		List<String> success = Arrays.asList(returnCodeDic.get("success"));
		// 定义一个用户在不同机构下拥有的消费记录集合
		Map<String, List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
		// key orgKey,value:逾期次数（不同机构的逾期次数）
		Map<String, Integer> averageOrgOverDue = new HashMap<String, Integer>();
		// key orgKey,value:逾期天数（不同机构的逾期天数）
		Map<String, Integer> averageOrgOverDay = new HashMap<String, Integer>();
		// 排序和计算逾期
		for (Map.Entry<String, List<TradeDetailDO>> entry : map.entrySet()) {
			String cardNo = entry.getKey();
			List<TradeDetailDO> cardNolist = entry.getValue();
			if (cardNolist.size() <= 1)
				continue;
			// 对集合按照日期进行排序
			// Collections.sort(cardNolist);
			double amout = 0;
			// 逾期日期值
			Timestamp overDueBeginDate = null;
			// 逾期天数值
			int overDueBeginDay = 0;
			for (TradeDetailDO o : cardNolist) {
				if (cardNolist.size() <= 1)
					continue;
				// 余额不足,划扣失败
				if (ywbzLst.contains(o.getRETURN_CODE())) {
					// 记录失败金额
					amout = o.getAMOUNT().doubleValue();
					// 逾期失败日期
					overDueBeginDate = o.getCREATE_TIME();
					continue;
				} else if (success.contains(o.getRETURN_CODE())) {
					String merId = o.getSOURCE_MERNO() + extKey;// 机构平均值主键
					if (!averageOrgOverDue.containsKey(merId)) {
						int i = averageOrgOverDue.get(merId);
						// 逾期次数
						averageOrgOverDue.put(merId, ++i);
					} else {
						averageOrgOverDue.put(merId, 1);
					}
					// 划扣成功,最终划扣成功且划扣成功金额=失败金额
					if (amout == o.getAMOUNT().doubleValue()) {
						// 计算逾期天数
						int overDueBeginDayTemp = DateUtils.differentDaysByMillisecond(overDueBeginDate,
								o.getCREATE_TIME());
						averageOrgOverDay.put(merId, overDueBeginDayTemp + overDueBeginDay);
					}
				}
			}
		}
	}

	/**
	 * @Description: 计算某类机构的逾期总次数
	 * @param list
	 * @param string
	 * @param returnCodeDic
	 * @return
	 * @author LZG
	 * @date 2018年07月10日
	 */
	private int avgOrgOverDueCount(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic) {

		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));// 余额不足失败返回码

		// 某类机构的逾期逾期总次数
		int averageOrgOverDueTime = 0;

		// 定义一个用户的银行卡在不同机构下拥有的消费记录集合
		Map<String, List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);

		// 排序和计算逾期
		for (Map.Entry<String, List<TradeDetailDO>> entry : map.entrySet()) {
			List<TradeDetailDO> cardNolist = entry.getValue();
			for (TradeDetailDO o : cardNolist) {
				if (ywbzLst.contains(o.getRETURN_CODE())) {
					averageOrgOverDueTime++;
				}
			}
		}

		return averageOrgOverDueTime;

	}

	/**
	 * 在贷款类机构x月成功放款次数 放款类变量 3.近x个月成功放款的记录数
	 * 
	 * @param list
	 * @param indexMap
	 * @param orgType:
	 *            dk
	 * @param returnCodeDic
	 */
	public int loanOrgLoanSuccessTimes(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
		List<String> success = Arrays.asList(returnCodeDic.get("success"));
		// 放款的记录数
		int records = 0;
		// 近x个月成功放款的记录数
		for (TradeDetailDO o : list) {
			// 放款成功
			if (orgTypeList.contains(o.getMER_TYPE().toString()) && o.getSF_TYPE().toString().equals("F")
					&& success.contains(o.getRETURN_CODE())) {
				records++;
			}
		}
		return records;
	}

	/**
	 * 在贷款类机构放款成功的机构数 放款类变量 3.近x个月在贷款类机构放款成功的机构数
	 * 
	 * @param list
	 * @param indexMap
	 * @param orgType:
	 *            dk
	 * @param returnCodeDic
	 */
	public int loanOrgLoanSuccessOrg(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
		List<String> success = Arrays.asList(returnCodeDic.get("success"));
		// 逾期天数值
		int records = 0;
		// 定义一个用户的银行卡在不同机构下拥有的消费记录集合
		Map<String, List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);

		// 不同结构下成功放款的机构统计
		for (Map.Entry<String, List<TradeDetailDO>> entry : map.entrySet()) {
			List<TradeDetailDO> cardNolist = entry.getValue();
			for (TradeDetailDO o : cardNolist) {
				if (o.getSF_TYPE().toString().equals("F") && success.contains(o.getRETURN_CODE())) {
					records++;
				}
			}
		}
		return records;
	}

	/**
	 * 放款类变量。x个月。总金额。x机构
	 * 
	 * @param list
	 * @param indexMap
	 * @param orgType:
	 *            dk
	 * @param returnCodeDic
	 */
	public BigDecimal loanOrgLoanSumMoney(List<TradeDetailDO> list, String orgType,
			Map<String, String[]> returnCodeDic) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
		// List<String> success = Arrays.asList(returnCodeDic.get("success"));
		// 逾期天数值
		BigDecimal records = new BigDecimal(0);
		// 近x个月成功放款的记录数
		for (TradeDetailDO o : list) {
			// 放款成功
			if (orgTypeList.contains(o.getMER_TYPE())) {
				if (o.getSF_TYPE().toString().equals("F")) {
					records = records.add(o.getAMOUNT());
				}
			}
		}
		if (records.intValue() != 0) {
			records.setScale(2, BigDecimal.ROUND_UP);
		}
		return records;
	}

	/**
	 * 获取最近放款最近的记录
	 * 
	 * @param list
	 * @param orgTypeList
	 * @param flg:最早:1
	 *            最近:2
	 * @return
	 */
	public TradeDetailDO recentOrFirstLoanRecord(List<TradeDetailDO> list, List<String> orgTypeList, String flg) {
		List<TradeDetailDO> lst = new ArrayList<TradeDetailDO>();
		for (TradeDetailDO o : list) {
			if (orgTypeList.contains(o.getMER_TYPE())) {
				lst.add(o);
			}
		}
		if (lst.size() != 0) {
			if (flg.equals("1")) {
				return lst.get(0);
			} else if (flg.equals("2")) {
				return lst.get(lst.size() - 1);
			}
		}
		return null;
	}

	/**
	 * 放款类变量。最近(最早)一次。时间指标.最近一次在贷款机构放款的日期 flg:最早:1 最近:2
	 * 
	 * @param list
	 * @param indexMap
	 * @param orgType:
	 * @param returnCodeDic
	 * @param flg:最早:1
	 *            最近:2
	 */
	public String loanOrgRecentLoanDate(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic,
			String flg) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
		// List<String> success = Arrays.asList(returnCodeDic.get("success"));
		// 逾期天数值
		String records = null;
		// 近x个月成功放款的记录数
		TradeDetailDO o = recentOrFirstLoanRecord(list, orgTypeList, flg);
		if (null != o) {
			Date date = new Date();
			date.setTime(o.getCREATE_TIME().getTime());
			records = DateUtils.yyyyMMddToString(date);
		}
		return records;
	}

	/**
	 * 授信类变量.最大额度
	 * 
	 * @param list
	 * @param orgType
	 * @param returnCodeDic
	 * @return
	 */
	public BigDecimal maxCreditLine(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
		List<BigDecimal> maxLst = new ArrayList<BigDecimal>();
		for (TradeDetailDO o : list) {
			if (orgTypeList.contains(o.getMER_TYPE().toString()) && o.getSF_TYPE().toString().equals("S")) {
				maxLst.add(o.getAMOUNT());
			}
		}
		if (maxLst.size() == 0) {
			return new BigDecimal(0);
		}
		return Collections.max(maxLst);
	}

	/**
	 * 授信类变量.总额度
	 * 
	 * @param list
	 * @param orgType
	 * @param returnCodeDic
	 * @return
	 */
	public BigDecimal totalCreditLine(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic) {
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();// 商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));// 具体机构类
		BigDecimal total = new BigDecimal(0);
		for (TradeDetailDO o : list) {
			if (orgTypeList.contains(o.getMER_TYPE().toString()) && o.getSF_TYPE().toString().equals("S")) {
				total = total.add(o.getAMOUNT());
			}
		}
		if (total.intValue() != 0) {
			total.setScale(2, BigDecimal.ROUND_UP);
		}
		return total;
	}

	/**
	 * @Description: 风险类变量。因账户原因还款失败的次数。
	 * @param list
	 * @param string
	 * @param returnCodeDic
	 * @return
	 * @author LZG
	 * @date 2018年07月17日
	 */
	private int acctfCount(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic) {

		// 商户类型归属分类字典
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
		// 具体机构类
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));
		// 帐户问题还款失败的返回码
		List<String> acctfList = Arrays.asList(returnCodeDic.get("acctf"));

		List<TradeDetailDO> merTypeTradeDetailDOList = new ArrayList<TradeDetailDO>();

		for (TradeDetailDO o : list) {
			if (orgTypeList.contains(o.getMER_TYPE().toString())) {
				merTypeTradeDetailDOList.add(o);
			}
		}

		int acctfCountResult = 0;
		for (TradeDetailDO o : merTypeTradeDetailDOList) {
			if (acctfList.contains(o.getRETURN_CODE())) {
				acctfCountResult++;
			}
		}

		return acctfCountResult;
	}
	
    /**
     * @Description: 风险类变量。因余额不足还款失败的次数。
     * @param list
     * @param string
     * @param returnCodeDic
     * @return 
     * @author LZG
     * @date 2018年07月18日
     */
    private int yebzCount(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic) {
     // 商户类型归属分类字典
        Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
        // 具体机构类
        List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));
        // 帐户问题还款失败的返回码
        List<String> yebzList = Arrays.asList(returnCodeDic.get("yebz"));

        List<TradeDetailDO> merTypeTradeDetailDOList = new ArrayList<TradeDetailDO>();

        for (TradeDetailDO o : list) {
            if (orgTypeList.contains(o.getMER_TYPE().toString())) {
                merTypeTradeDetailDOList.add(o);
            }
        }

        int yebzCountResult = 0;
        for (TradeDetailDO o : merTypeTradeDetailDOList) {
            if (yebzList.contains(o.getRETURN_CODE())) {
                yebzCountResult++;
            }
        }

        return yebzCountResult;
    }

	/**
	 * @Description: 风险类变量。因账户原因还款失败的记录数占比。
	 * @param list
	 * @param returnCodeDic
	 * @return
	 * @author LZG
	 * @date 2018年07月17日
	 */
	private BigDecimal acctfProportion(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {

		// 帐户问题还款失败的返回码
		List<String> acctfList = Arrays.asList(returnCodeDic.get("acctf"));

		int oltmtAmount = 0;
		for (TradeDetailDO o : list) {
			if (acctfList.contains(o.getRETURN_CODE())) {
				oltmtAmount++;
			}
		}

		return MathUtil.divide(oltmtAmount, list.size());
	}
	
    /**
     * @Description: 风险类变量。余额不足记录数占比。
     * @param list
     * @param returnCodeDic
     * @return 
     * @author LZG
     * @date 2018年07月18日
     */
    private BigDecimal yebzProportion(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {
        // 余额不足的返回码
        List<String> yebzList = Arrays.asList(returnCodeDic.get("yebz"));

        int yebzAmount = 0;
        for (TradeDetailDO o : list) {
            if (yebzList.contains(o.getRETURN_CODE())) {
                yebzAmount++;
            }
        }

        return MathUtil.divide(yebzAmount, yebzList.size());
    }

	/**
	 * @Description: 风险类变量。因账户原因还款失败的金额占比。
	 * @param list
	 * @param returnCodeDic
	 * @return
	 * @author LZG
	 * @date 2018年07月17日
	 */
	private BigDecimal acctfMoneyProportion(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {

		// 帐户问题还款失败的返回码
		List<String> acctfList = Arrays.asList(returnCodeDic.get("acctf"));

		BigDecimal sumMoney = new BigDecimal("0");
		BigDecimal acctfMoney = new BigDecimal("0");

		for (TradeDetailDO o : list) {
			sumMoney = sumMoney.add(o.getAMOUNT());
			if (acctfList.contains(o.getRETURN_CODE())) {
				acctfMoney = acctfMoney.add(o.getAMOUNT());
			}
		}

		return MathUtil.divide(acctfMoney, sumMoney);
	}

	/**
	 * @Description: 风险类变量。超出限额还款失败的金额占比。
	 * @param list
	 * @param returnCodeDic
	 * @return
	 * @author LZG
	 * @date 2018年07月17日
	 */
	private BigDecimal otlmtMoneyProportion(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {

		// 超出限额还款失败的返回码
		List<String> otlmtList = Arrays.asList(returnCodeDic.get("otlmt"));

		BigDecimal sumMoney = new BigDecimal("0");
		BigDecimal otlmtMoney = new BigDecimal("0");

		for (TradeDetailDO o : list) {
			sumMoney = sumMoney.add(o.getAMOUNT());
			if (otlmtList.contains(o.getRETURN_CODE())) {
				otlmtMoney = otlmtMoney.add(o.getAMOUNT());
			}
		}

		return MathUtil.divide(otlmtMoney, sumMoney);
	}

	/**
	 * @Description: 风险类变量。超出限额还款失败的记录数占比。
	 * @param list
	 * @param returnCodeDic
	 * @return
	 * @author LZG
	 * @date 2018年07月17日
	 */
	private BigDecimal otlmtProportion(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {

		// 帐户问题还款失败的返回码
		List<String> otlmtList = Arrays.asList(returnCodeDic.get("otlmt"));

		int oltmtAmount = 0;
		for (TradeDetailDO o : list) {
			if (otlmtList.contains(o.getRETURN_CODE())) {
				oltmtAmount++;
			}
		}

		return MathUtil.divide(oltmtAmount, otlmtList.size());
	}

	/**
	 * @Description: 放款类变量。放款成功次数。
	 * @param list
	 * @param returnCodeDic
	 * @return
	 * @author LZG
	 * @date 2018年07月17日
	 */
	private int fkSuccessCount(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {

		List<String> successList = Arrays.asList(returnCodeDic.get("success"));

		int fkSuccessCountResult = 0;
		for (TradeDetailDO o : list) {
			if ('F' == o.getSF_TYPE()) {
				if (successList.contains(o.getRETURN_CODE())) {
					fkSuccessCountResult++;
				}
			}
		}
		return fkSuccessCountResult;
	}

	/**
	 * @Description: 放款类变量。放款成功的总机构数。
	 * @param list
	 * @param returnCodeDic
	 * @return
	 * @author LZG
	 * @date 2018年07月17日
	 */
	private int fkSuccessOrgCount(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {

		List<String> successList = Arrays.asList(returnCodeDic.get("success"));

		Set<String> fkSuccessOrgCountSet = new HashSet<String>();
		for (TradeDetailDO o : list) {
			if ('F' == o.getSF_TYPE()) {
				if (successList.contains(o.getRETURN_CODE())) {
					fkSuccessOrgCountSet.add(o.getSOURCE_MERNO());
				}
			}
		}
		return fkSuccessOrgCountSet.size();
	}

	/**
	 * @Description: 放款类变量。不同机构放款成功的机构数。
	 * @param list
	 * @param string
	 * @param returnCodeDic
	 * @return
	 * @author LZG
	 * @date 2018年07月17日
	 */
	private int fksuccessOrgCount(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic) {

		List<String> successList = Arrays.asList(returnCodeDic.get("success"));
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));

		// 根据机构类别筛选数据
		List<TradeDetailDO> merTypeTradeDetailDOList = new ArrayList<TradeDetailDO>();

		for (TradeDetailDO o : list) {
			if (orgTypeList.contains(o.getMER_TYPE().toString())) {
				merTypeTradeDetailDOList.add(o);
			}
		}

		Set<String> fkSuccessOrgCountSet = new HashSet<String>();
		for (TradeDetailDO o : merTypeTradeDetailDOList) {
			if ('F' == o.getSF_TYPE()) {
				if (successList.contains(o.getRETURN_CODE())) {
					fkSuccessOrgCountSet.add(o.getSOURCE_MERNO());
				}
			}
		}
		return fkSuccessOrgCountSet.size();
	}

	/**
	 * @Description: 放款类变量。放款总金额。
	 * @param list
	 * @param string
	 * @param returnCodeDic
	 * @return
	 * @author LZG
	 * @date 2018年07月17日
	 */
	private BigDecimal fkSuccessMoneyCount(List<TradeDetailDO> list, String orgType,
			Map<String, String[]> returnCodeDic) {

		List<String> successList = Arrays.asList(returnCodeDic.get("success"));
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));

		// 根据机构类别筛选数据
		List<TradeDetailDO> merTypeTradeDetailDOList = new ArrayList<TradeDetailDO>();
		for (TradeDetailDO o : list) {
			if (orgTypeList.contains(o.getMER_TYPE().toString())) {
				merTypeTradeDetailDOList.add(o);
			}
		}

		BigDecimal fkSuccessMoney = new BigDecimal("0");
		for (TradeDetailDO o : merTypeTradeDetailDOList) {
			if ('F' == o.getSF_TYPE()) {
				if (successList.contains(o.getRETURN_CODE())) {
					fkSuccessMoney.add(o.getAMOUNT());
				}
			}
		}

		return fkSuccessMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @Description: 放款类变量。最近一次。
	 * @param list
	 * @param returnCodeDic
	 * @return Map<String, String> key:fkDate ||fkDays
	 * @author LZG
	 * @date 2018年07月17日
	 */
	private Map<String, Object> fkLastestTimeCount(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> successList = Arrays.asList(returnCodeDic.get("success"));

		int listSize = list.size();
		for (int i = listSize - 1; i >= 0; i--) {
			TradeDetailDO o = list.get(i);
			if ('F' == o.getSF_TYPE()) {
				if (successList.contains(o.getRETURN_CODE())) {
					resultMap.put("fkDate", DateUtils.yyyyMMddToString(o.getCREATE_TIME()));
					// 第2个参数是今天
					int fkDays = DateUtils.getIntervalDayAmount(o.getCREATE_TIME(), o.getCREATE_TIME());
					resultMap.put("fkDays", fkDays);
				}
			}
		}

		return resultMap;
	}

	/**
	 * @Description: 放款类变量。最近一次。
	 * @param list
	 * @param returnCodeDic
	 * @return Map<String, String> key:fkDate ||fkDays
	 * @author LZG
	 * @date 2018年07月17日
	 */
	private Map<String, Object> fkEarliestTimeCount(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> successList = Arrays.asList(returnCodeDic.get("success"));

		for (TradeDetailDO o : list) {
			if ('F' == o.getSF_TYPE()) {
				if (successList.contains(o.getRETURN_CODE())) {
					resultMap.put("fkDate", DateUtils.yyyyMMddToString(o.getCREATE_TIME()));
					// 第2个参数是今天
					int fkDays = DateUtils.getIntervalDayAmount(o.getCREATE_TIME(), o.getCREATE_TIME());
					resultMap.put("fkDays", fkDays);
				}
			}
		}

		return resultMap;
	}

    /**
     * @Description: 还款类变量。在不同类型机构下的还款成功金额sum
     * @param list
     * @param string
     * @param returnCodeDic
     * @return 
     * @author LZG
     * @date 2018年07月18日
     */
    private BigDecimal repaymentSuccessMoneyCount(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic) {
        
        List<String> successList = Arrays.asList(returnCodeDic.get("success"));
        
        Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
        List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));
        
        List<TradeDetailDO> merTypeTradeDetailDOList = new ArrayList<TradeDetailDO>();
        for(TradeDetailDO o : list) {
            if (orgTypeList.contains(o.getMER_TYPE().toString())) {
                merTypeTradeDetailDOList.add(o);
            }
        }
        
        BigDecimal sumMoney = new BigDecimal("0"); 
        for(TradeDetailDO o : merTypeTradeDetailDOList) {
            if('S' == o.getSF_TYPE() && successList.contains(o.getRETURN_CODE())) {
                sumMoney = sumMoney.add(o.getAMOUNT());
            }
        }
        
        return sumMoney;
    }
    
    /**
     * @Description: 还款类变量。还款成功记录数。
     * @param list
     * @param string
     * @param returnCodeDic
     * @return 
     * @author LZG
     * @date 2018年07月18日
     */
    private int repaymentSuccessCount(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic) {
        
        // 商户类型归属分类字典
        Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
        // 具体机构类
        List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));
        // 还款成功的返回码
        List<String> successReturnCodeList = Arrays.asList(returnCodeDic.get("success"));

        List<TradeDetailDO> merTypeTradeDetailDOList = new ArrayList<TradeDetailDO>();

        for (TradeDetailDO o : list) {
            if (orgTypeList.contains(o.getMER_TYPE().toString())) {
                merTypeTradeDetailDOList.add(o);
            }
        }

        int successCountResult = 0;
        for (TradeDetailDO o : merTypeTradeDetailDOList) {
            if (successReturnCodeList.contains(o.getRETURN_CODE())) {
                successCountResult++;
            }
        }

        return successCountResult;
    }
    
    /**
     * @Description: 还款类变量。还款成功记录数占比。
     * @param list
     * @param returnCodeDic
     * @return 
     * @author LZG
     * @date 2018年07月18日
     */
    private BigDecimal repaymentSuccessProportion(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {
        List<String> successReturnCodeList = Arrays.asList(returnCodeDic.get("success"));

        int successAmount = 0;
        for (TradeDetailDO o : list) {
            if (successReturnCodeList.contains(o.getRETURN_CODE())) {
                successAmount++;
            }
        }

        return MathUtil.divide(successAmount, list.size());
    }
    
    /**
     * @Description: 还款类变量。还款成功的机构数
     * @param list
     * @param string
     * @param returnCodeDic
     * @return 
     * @author LZG
     * @date 2018年07月18日
     */
    private int repaymentSuccessOrgCount(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic) {
        
        // 商户类型归属分类字典
        Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
        // 具体机构类
        List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));
        // 还款成功的返回码
        List<String> successReturnCodeList = Arrays.asList(returnCodeDic.get("success"));

        List<TradeDetailDO> merTypeTradeDetailDOList = new ArrayList<TradeDetailDO>();

        for (TradeDetailDO o : list) {
            if (orgTypeList.contains(o.getMER_TYPE().toString())) {
                merTypeTradeDetailDOList.add(o);
            }
        }
        
        Set<String> repaymentSuccessOrgCountSet = new HashSet<String>();
        for (TradeDetailDO o : list) {
            if ('S' == o.getSF_TYPE() && successReturnCodeList.contains(o.getRETURN_CODE())) {
                repaymentSuccessOrgCountSet.add(o.getSOURCE_MERNO());
            }
        }
        
        return repaymentSuccessOrgCountSet.size();
    }
    
    
    /**
     * @Description: 还款类变量。 还款余额不足的记录数。
     * @param list
     * @param returnCodeDic
     * @return 
     * @author LZG
     * @date 2018年07月18日
     */
    private int repamentYebzCount(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {
        
        List<String> yebzReturnCodeList = Arrays.asList(returnCodeDic.get("yebz"));

        int yebzAmount = 0;
        for (TradeDetailDO o : list) {
            if ('S' == o.getSF_TYPE() && yebzReturnCodeList.contains(o.getRETURN_CODE())) {
                yebzAmount++;
            }
        }

        return yebzAmount;
    }
    
    /**
     * @Description: 还款类变量。失败还款记录的占比。
     * @param list
     * @param returnCodeDic
     * @return 
     * @author LZG
     * @date 2018年07月18日
     */
    private BigDecimal repamentFailcProportion(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {
       
        List<String> failcReturnCodeList = Arrays.asList(returnCodeDic.get("failc"));

        int failcAmount = 0;
        for (TradeDetailDO o : list) {
            if ('S' == o.getSF_TYPE() && failcReturnCodeList.contains(o.getRETURN_CODE())) {
                failcAmount++;
            }
        }

        return MathUtil.divide(failcAmount, list.size());
    }
    
    /**
     * @Description: 还款类变量。余额不足的记录数占比。
     * @param list
     * @param returnCodeDic
     * @return 
     * @author LZG
     * @date 2018年07月18日
     */
    private BigDecimal repamentYebzProportion(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {
        
        List<String> yebzReturnCodeList = Arrays.asList(returnCodeDic.get("yebz"));

        int yebzAmount = 0;
        for (TradeDetailDO o : list) {
            if ('S' == o.getSF_TYPE() && yebzReturnCodeList.contains(o.getRETURN_CODE())) {
                yebzAmount++;
            }
        }

        return MathUtil.divide(yebzAmount, list.size());
    }
    
    /**
     * @Description: 还款类变量。余额不足的金额数占比。
     * @param list
     * @param returnCodeDic
     * @return 
     * @author LZG
     * @date 2018年07月18日
     */
    private BigDecimal repamentYebzMoneyProportion(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {
        
        List<String> yebzReturnCodeList = Arrays.asList(returnCodeDic.get("yebz"));
        
        BigDecimal sumMoney = new BigDecimal("0"); 
        BigDecimal yebzMoney = new BigDecimal("0");
        for(TradeDetailDO o : list) {
            sumMoney = sumMoney.add(o.getAMOUNT());
            if('S' == o.getSF_TYPE() && yebzReturnCodeList.contains(o.getRETURN_CODE())) {
                yebzMoney = yebzMoney.add(o.getAMOUNT());
            }
        }
        
        return MathUtil.divide(yebzMoney, sumMoney);
    }
    
    /**
     * @Description: 还款类变量。还款成功金额占比。
     * @param list
     * @param returnCodeDic
     * @return 
     * @author LZG
     * @date 2018年07月18日
     */
    private BigDecimal repaymentSuccessMoneyProportion(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic) {
        
        List<String> successReturnCodeList = Arrays.asList(returnCodeDic.get("success"));
        
        BigDecimal sumMoney = new BigDecimal("0"); 
        BigDecimal successMoney = new BigDecimal("0");
        for(TradeDetailDO o : list) {
            sumMoney = sumMoney.add(o.getAMOUNT());
            if('S' == o.getSF_TYPE() && successReturnCodeList.contains(o.getRETURN_CODE())) {
                successMoney = successMoney.add(o.getAMOUNT());
            }
        }
        
        return MathUtil.divide(successMoney, sumMoney);
    }
    
    /**
     * @Description: 还款类变量。最早一次还款时间和距离今天的天数。
     * @param list
     * @param string
     * @param returnCodeDic
     * @return HashMap key: hkDate || hkDays
     * @author LZG
     * @date 2018年07月19日
     */
    private Map<String, Object> hkEarlistDateAndDays(List<TradeDetailDO> list, String orgType) {
        
        // 商户类型归属分类字典
        Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
        // 具体机构类
        List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));

        List<TradeDetailDO> merTypeTradeDetailDOList = new ArrayList<TradeDetailDO>();

        for (TradeDetailDO o : list) {
            if (orgTypeList.contains(o.getMER_TYPE().toString())) {
                merTypeTradeDetailDOList.add(o);
            }
        }

        int merTypeTradeDetailDOListSzie = merTypeTradeDetailDOList.size();
        String earlistDateStr = "";
        int intervalDays = 0;
        for(int i = merTypeTradeDetailDOListSzie - 1; i >= 0; i--) {
            if('S' == merTypeTradeDetailDOList.get(i).getSF_TYPE()) {
                Date earlistDate = DateUtils.formatTimeStamp(merTypeTradeDetailDOList.get(i).getCREATE_TIME().toString());
                earlistDateStr = DateUtils.yyyyMMddToString(earlistDate);
                intervalDays = DateUtils.getIntervalDayAmount(earlistDate, new Date());
                break;
            }
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("hkDate", earlistDateStr);
        resultMap.put("hkDays", intervalDays);
        
        return resultMap;
    }
    
    /**
     * @Description: 还款类变量。最近一次还款时间和距离今天的天数。
     * @param list
     * @param string
     * @param returnCodeDic
     * @return HashMap key: hkDate || hkDays
     * @author LZG
     * @date 2018年07月19日
     */
    private Map<String, Object> hkLatestDateAndDays(List<TradeDetailDO> list, String orgType) {
        
        // 商户类型归属分类字典
        Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
        // 具体机构类
        List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));

        List<TradeDetailDO> merTypeTradeDetailDOList = new ArrayList<TradeDetailDO>();

        for (TradeDetailDO o : list) {
            if (orgTypeList.contains(o.getMER_TYPE().toString())) {
                merTypeTradeDetailDOList.add(o);
            }
        }

        String earlistDateStr = "";
        int intervalDays = 0;
        for(TradeDetailDO o : merTypeTradeDetailDOList) {
            if('S' == o.getSF_TYPE()) {
                Date earlistDate = DateUtils.formatTimeStamp(o.getCREATE_TIME().toString());
                earlistDateStr = DateUtils.yyyyMMddToString(earlistDate);
                intervalDays = DateUtils.getIntervalDayAmount(earlistDate, new Date());
                break;
            }
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("hkDate", earlistDateStr);
        resultMap.put("hkDays", intervalDays);
        
        return resultMap;
    }
    
    /**
     * @Description: 还款类变量。最近一次在不同类型机构(1)下的还款是否成功(2)的日期和距离今天天数
     * @param list
     * @param string
     * @param returnCodeDic
     * @param flag 标识：success(成功)  fail(失败)
     * @return 
     * @author LZG
     * @date 2018年07月19日
     */
    private Map<String, Object> hkLatestOfOrgDateAndDays(List<TradeDetailDO> list, String orgType, Map<String, String[]> returnCodeDic, String flag) {
        // 商户类型归属分类字典
        Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
        // 具体机构类
        List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));
        
        //筛选数据
        List<TradeDetailDO> merTypeTradeDetailDOList = new ArrayList<TradeDetailDO>();
        for (TradeDetailDO o : list) {
            if (orgTypeList.contains(o.getMER_TYPE().toString())) {
                merTypeTradeDetailDOList.add(o);
            }
        }
        
        List<String> returnCodeList = new ArrayList<String>();
        if(flag.equals("successs")) {
            returnCodeList = Arrays.asList(returnCodeDic.get("success"));
        } else if(flag.equals("fail")){
            returnCodeList = Arrays.asList(returnCodeDic.get("failc"));
        }
        
        
        String earlistDateStr = "";
        int intervalDays = 0;
        for(TradeDetailDO o : merTypeTradeDetailDOList) {
            if('S' == o.getSF_TYPE() && returnCodeList.contains(o.getRETURN_CODE())) {
                Date earlistDate = DateUtils.formatTimeStamp(o.getCREATE_TIME().toString());
                earlistDateStr = DateUtils.yyyyMMddToString(earlistDate);
                intervalDays = DateUtils.getIntervalDayAmount(earlistDate, new Date());
                break;
            }
        }
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("hkDate", earlistDateStr);
        resultMap.put("hkDays", intervalDays);
        
        return resultMap;
    }
    
}
