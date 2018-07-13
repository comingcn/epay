package com.epay.xj.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.epay.xj.domain.OverDueIndex;
import com.epay.xj.domain.TradeDetailDO;
import com.epay.xj.properties.InitProperties;
import com.epay.xj.utils.DateUtils;

@Service
@Transactional
public class TaskServer {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private InitProperties initProperties;

	@PersistenceContext
	private EntityManager entityManager;

	public List<String> getTaskList(String updateTime, String flag) {
		String sql = "select CERT_NO from CP_ODS.P1055_CERT_LIST";
		return entityManager.createNativeQuery(sql).getResultList();
	}
	
	/**
	 * batchInsert
	 * @param list
	 */
	public void batchInsert(List<OverDueIndex> list){
		int size =  list.size();
        for (int i = 0; i < size; i++) {
        	OverDueIndex dd =  list.get(i);
        	entityManager.persist(dd);
            if (i % 10000 == 0 || i==(size-1)) { // 每1000条数据执行一次，或者最后不足1000条时执行
            	entityManager.flush();
            	entityManager.clear();
            }
        }
	}
	/**
	 * 获取certNo下的不同月份下的所有
	 * @param certNo
	 * @param updateTime
	 * @return
	 */
	public Map<Integer,List<TradeDetailDO>> fatherList(String certNo,String updateTime) {
//		List<TradeDetailDO> tradeDetailList = new ArrayList<TradeDetailDO>();
		Map<Integer,List<TradeDetailDO>> tradeMap = new HashMap<Integer,List<TradeDetailDO>>();
		String sql = "select * from CP_ODS.P1055_TRA_TRADE_DETAIL_PARA where IDCARD='" + certNo+ "'";
		try {
			List<TradeDetailDO> tradeDetailList = entityManager.createNativeQuery(sql,TradeDetailDO.class).getResultList();
			Collections.sort(tradeDetailList);
			Map<String, Integer> overDueMouth = initProperties.getOverDueMonth();
			for (int month : overDueMouth.values()) {
				tradeMap.put(month, getListByMonth(tradeDetailList, month, updateTime));
			}
		} catch (Exception e) {
			logger.error("执行sql:{},error:{}",sql,e.getMessage());
			e.printStackTrace();
		}
		return tradeMap;
	}
	
	public void sliceTask(List<String> taskList,String updateTime) throws InterruptedException{
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
       logger.info("taskSize:{},线程数：{},单个线程处理记录数量:{}",taskList.size(), theadPoolSize,threadSize);
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
//                   logger.info("{}程数：集合数量：{}", Thread.currentThread().getName(),listStr.size());
                   for (int i = 0; i < listStr.size(); i++) {
           			String certNo = listStr.get(i);
           			OverDueIndex odi = new OverDueIndex();
           			odi.setCERT_NO(certNo);
           			Map<String,List<Map<String,String>>> everyPersonMap = new HashMap<String,List<Map<String,String>>>();
           			List<Map<String,String>> indexList = new ArrayList<Map<String,String>>();
           			//如果是人的所有记录
           			Map<Integer,List<TradeDetailDO>> tradeMap = fatherList(certNo,udpateTimes);
           			for (int month : overDueMouth.values()) {
           				//指标结果集
           				Map<String, String> indexMap = new HashMap<String, String>();
           				List<TradeDetailDO> list = tradeMap.get(month);
           				overDueMouth(list, odi, month, returnCodeDic);
           				if(everyPersonMap.get(certNo)==null || everyPersonMap.get(certNo).isEmpty()){
           					indexList.add(indexMap);
           					everyPersonMap.put(certNo, indexList);
           				}else{
           					everyPersonMap.get(certNo).add(indexMap);
           				}
           			}
           			lst.add(odi);
           		}
                   return lst;
               }
           };
           // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
           tasks.add(task);
       }
       
       List<Future<List<OverDueIndex>>> results = exec.invokeAll(tasks);
       StringBuffer sb = new StringBuffer();
       long sysBeginTime = System.nanoTime();
       for (Future<List<OverDueIndex>> future : results) {
			try {
				//遍历所有人list
				
				List<OverDueIndex> lst = future.get();
				sb.append("size:").append(lst.size());
				batchInsert(lst);
//				for (OverDueIndex overDueIndex : lst) {
//					logger.info("certNo:{},index:{}", overDueIndex.getCertNo(),JSON.toJSONString(overDueIndex));
//				}
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       }
       String useTime = String.valueOf((System.nanoTime() - sysBeginTime)/Math.pow(10, 9));
       logger.info("sb:{},写入记录useTime:{}秒",sb.toString(),useTime);
       // 关闭线程池
       exec.shutdown();
	}
	
	public List<TradeDetailDO> getListByMonth(List<TradeDetailDO> fatherList,int month,String udpateTimes){
		List<TradeDetailDO> list = new ArrayList<TradeDetailDO>();
		Timestamp end = new Timestamp(DateUtils.yyyyMMddToDate(udpateTimes).getTime()) ;
        Timestamp begin = DateUtils.getDateOfXMonthsAgo(end, month);
		for (TradeDetailDO o : fatherList) {
			if(DateUtils.judge(begin, end, o.getCREATE_TIME())){
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

//	public void deal(String updateTime, String flag) {
//		Map<String, Integer> overDueMouth = initProperties.getOverDueMonth();
//		List<String> taskList = getTaskList(updateTime, flag);
//		for (int i = 0; i < taskList.size(); i++) {
//			long sysBeginTime = System.nanoTime();
//			for (int month : overDueMouth.values()) {
//				String beginTime = DateUtils.getDateOfXMonthsAgo(updateTime, month);
//				Map<String, String> indexMap = new HashMap<String, String>();
//				Map<String, String[]> returnCodeDic = initProperties.getReturnCodeDic();
//				List<TradeDetailDO> list = getTradeDetail(taskList.get(i), beginTime, updateTime);
//				overDueMouth(list, indexMap, month, returnCodeDic);
//				for(Map.Entry<String,String> entry : indexMap.entrySet()){
//					System.out.println("p:"+entry.getKey()+",v:"+entry.getValue());
//				}
//			}
//			String useTime = String.valueOf((System.nanoTime() - sysBeginTime)/Math.pow(10, 9));
//			logger.info("useTime:{}秒",useTime);
//			// 天数统计
//		}
//	}

	private void overDueMouth(List<TradeDetailDO> list, OverDueIndex odi, int month,
			Map<String, String[]> returnCodeDic) {
		if (month == 3) {
			/******************************* 逾期一天以上次数 ***************************************/
//			indexMap.put("YQ013", loanOrgOverDueOneDay(list, "dk", returnCodeDic));
			odi.setYQ013(loanOrgOverDueOneDay(list, "dk", returnCodeDic));
//			indexMap.put("YQ014", loanOrgOverDueOneDay(list, "xj", returnCodeDic));
			odi.setYQ014(loanOrgOverDueOneDay(list, "xj", returnCodeDic));
//			indexMap.put("YQ015", loanOrgOverDueOneDay(list, "yh", returnCodeDic));
			odi.setYQ015(loanOrgOverDueOneDay(list, "yh", returnCodeDic));
//			indexMap.put("YQ016", loanOrgOverDueOneDay(list, "xd", returnCodeDic));
			odi.setYQ016(loanOrgOverDueOneDay(list, "xd", returnCodeDic));
			/******************************* 逾期机构数 ***************************************/
//			indexMap.put("YQ017", overDueOrgCount(list, "dk", returnCodeDic));
			odi.setYQ017(overDueOrgCount(list, "dk", returnCodeDic));
//			indexMap.put("YQ018", overDueOrgCount(list, "xj", returnCodeDic));
			odi.setYQ018(overDueOrgCount(list, "xj", returnCodeDic));
//			indexMap.put("YQ019", overDueOrgCount(list, "yh", returnCodeDic));
			odi.setYQ019(overDueOrgCount(list, "yh", returnCodeDic));
//			indexMap.put("YQ020", overDueOrgCount(list, "xd", returnCodeDic));
			odi.setYQ020(overDueOrgCount(list, "xd", returnCodeDic));
			/******************************* 逾期天数总和 ***************************************/
//			indexMap.put("YQ027", overDueDaysSum(list,"dk", returnCodeDic));
			odi.setYQ027(overDueDaysSum(list,"dk", returnCodeDic));
			/******************************* 逾期金额总和 ***************************************/
//			indexMap.put("YQ033", overDueTotalMoneySum(list, returnCodeDic, "1d"));
			odi.setYQ033(overDueTotalMoneySum(list, returnCodeDic, "1d"));
//			indexMap.put("YQ032", overDueTotalMoneySum(list, returnCodeDic, "7d"));
			odi.setYQ032(overDueTotalMoneySum(list, returnCodeDic, "7d"));
//			indexMap.put("YQ031", overDueTotalMoneySum(list, returnCodeDic, "30d"));
			odi.setYQ031(overDueTotalMoneySum(list, returnCodeDic, "30d"));
			
			/******************************* 平均逾期次数 ***************************************/
            BigDecimal dkOverDueOrgAmount = new BigDecimal(overDueOrgCount(list, "dk", returnCodeDic));
            BigDecimal xjOverDueOrgAmount = new BigDecimal(overDueOrgCount(list, "xj", returnCodeDic));
            BigDecimal yhOverDueOrgAmount = new BigDecimal(overDueOrgCount(list, "yh", returnCodeDic));
            BigDecimal xdOverDueOrgAmount = new BigDecimal(overDueOrgCount(list, "xd", returnCodeDic));
            //一个人所有类型机构的逾期机构数
            
            BigDecimal overDueOrgAmount = dkOverDueOrgAmount.add(xjOverDueOrgAmount).add(dkOverDueOrgAmount)
            		.add(yhOverDueOrgAmount).add(xdOverDueOrgAmount);
            
            if(overDueOrgAmount.intValue()==0){
//              indexMap.put("YQ022", MathUtil.divide(avgOrgOverDueCount(list, "xj", returnCodeDic), overDueOrgAmount));
                odi.setYQ022(overDueOrgAmount);
//                indexMap.put("YQ023", MathUtil.divide(avgOrgOverDueCount(list, "yh", returnCodeDic), overDueOrgAmount));
                odi.setYQ023(overDueOrgAmount);
//                indexMap.put("YQ024", MathUtil.divide(avgOrgOverDueCount(list, "xd", returnCodeDic), overDueOrgAmount));
                odi.setYQ024(overDueOrgAmount);
//                indexMap.put("YQ025", MathUtil.divide(avgOrgOverDueCount(list, "dk", returnCodeDic), overDueOrgAmount));
                odi.setYQ025(overDueOrgAmount);
            }else{
//              indexMap.put("YQ022", MathUtil.divide(avgOrgOverDueCount(list, "xj", returnCodeDic), overDueOrgAmount));
                odi.setYQ022(dkOverDueOrgAmount.divide(overDueOrgAmount,2,BigDecimal.ROUND_HALF_UP));
//                indexMap.put("YQ023", MathUtil.divide(avgOrgOverDueCount(list, "yh", returnCodeDic), overDueOrgAmount));
                odi.setYQ023(xjOverDueOrgAmount.divide(overDueOrgAmount,2,BigDecimal.ROUND_HALF_UP));
//                indexMap.put("YQ024", MathUtil.divide(avgOrgOverDueCount(list, "xd", returnCodeDic), overDueOrgAmount));
                odi.setYQ024(yhOverDueOrgAmount.divide(overDueOrgAmount,2,BigDecimal.ROUND_HALF_UP));
//                indexMap.put("YQ025", MathUtil.divide(avgOrgOverDueCount(list, "dk", returnCodeDic), overDueOrgAmount));
                odi.setYQ025(xdOverDueOrgAmount.divide(overDueOrgAmount,2,BigDecimal.ROUND_HALF_UP));
            }
		} else if (month == 6) {
			/******************************* 逾期一天以上次数 ***************************************/
//			indexMap.put("YQ001", loanOrgOverDueOneDay(list, "dk", returnCodeDic));
			odi.setYQ001(loanOrgOverDueOneDay(list, "dk", returnCodeDic));
//			indexMap.put("YQ002", loanOrgOverDueOneDay(list, "xj", returnCodeDic));
			odi.setYQ002(loanOrgOverDueOneDay(list, "xj", returnCodeDic));
//			indexMap.put("YQ003", loanOrgOverDueOneDay(list, "yh", returnCodeDic));
			odi.setYQ003(loanOrgOverDueOneDay(list, "yh", returnCodeDic));
//			indexMap.put("YQ004", loanOrgOverDueOneDay(list, "xd", returnCodeDic));
			odi.setYQ004(loanOrgOverDueOneDay(list, "xd", returnCodeDic));
			/******************************* 逾期机构数 ***************************************/
//			indexMap.put("YQ009", overDueOrgCount(list, "dk", returnCodeDic));
			odi.setYQ009(overDueOrgCount(list, "dk", returnCodeDic));
//			indexMap.put("YQ010", overDueOrgCount(list, "xj", returnCodeDic));
			odi.setYQ010(overDueOrgCount(list, "xj", returnCodeDic));
//			indexMap.put("YQ011", overDueOrgCount(list, "yh", returnCodeDic));
			odi.setYQ011(overDueOrgCount(list, "yh", returnCodeDic));

			/******************************* 逾期天数总和 ***************************************/

//			indexMap.put("YQ026", overDueDaysSum(list,"dk", returnCodeDic));
			odi.setYQ026(overDueDaysSum(list,"dk", returnCodeDic));

			/******************************* 逾期金额总和 ***************************************/
//			indexMap.put("YQ030", overDueTotalMoneySum(list, returnCodeDic, "1d"));
			odi.setYQ030(overDueTotalMoneySum(list, returnCodeDic, "1d"));
//			indexMap.put("YQ029", overDueTotalMoneySum(list, returnCodeDic, "7d"));
			odi.setYQ029(overDueTotalMoneySum(list, returnCodeDic, "7d"));
//			indexMap.put("YQ028", overDueTotalMoneySum(list, returnCodeDic, "30d"));
			odi.setYQ028(overDueTotalMoneySum(list, returnCodeDic, "30d"));
			
			/******************************* 平均逾期次数 ***************************************/
			BigDecimal dkOverDueOrgAmount = new BigDecimal(overDueOrgCount(list, "dk", returnCodeDic));
			BigDecimal xjOverDueOrgAmount = new BigDecimal(overDueOrgCount(list, "xj", returnCodeDic));
			BigDecimal yhOverDueOrgAmount = new BigDecimal(overDueOrgCount(list, "yh", returnCodeDic));
			BigDecimal xdOverDueOrgAmount = new BigDecimal(overDueOrgCount(list, "xd", returnCodeDic));
            //一个人所有类型机构的逾期机构数
			 BigDecimal overDueOrgAmount = dkOverDueOrgAmount.add(xjOverDueOrgAmount).add(dkOverDueOrgAmount)
	            		.add(yhOverDueOrgAmount).add(xdOverDueOrgAmount);
	            if(overDueOrgAmount.intValue()==0){
//	              indexMap.put("YQ005", MathUtil.divide(avgOrgOverDueCount(list, "dk", returnCodeDic), overDueOrgAmount));
	                odi.setYQ005(overDueOrgAmount);
//	                indexMap.put("YQ006", MathUtil.divide(avgOrgOverDueCount(list, "xj", returnCodeDic), overDueOrgAmount));
	                odi.setYQ006(overDueOrgAmount);
//	                indexMap.put("YQ007", MathUtil.divide(avgOrgOverDueCount(list, "yh", returnCodeDic), overDueOrgAmount));
	                odi.setYQ007(overDueOrgAmount);
//	                indexMap.put("YQ008", MathUtil.divide(avgOrgOverDueCount(list, "xd", returnCodeDic), overDueOrgAmount));
	                odi.setYQ008(overDueOrgAmount);
	            }else{
//	              indexMap.put("YQ005", MathUtil.divide(avgOrgOverDueCount(list, "dk", returnCodeDic), overDueOrgAmount));
	                odi.setYQ005(dkOverDueOrgAmount.divide(overDueOrgAmount,2,BigDecimal.ROUND_HALF_UP));
//	                indexMap.put("YQ006", MathUtil.divide(avgOrgOverDueCount(list, "xj", returnCodeDic), overDueOrgAmount));
	                odi.setYQ006(xjOverDueOrgAmount.divide(overDueOrgAmount,2,BigDecimal.ROUND_HALF_UP));
//	                indexMap.put("YQ007", MathUtil.divide(avgOrgOverDueCount(list, "yh", returnCodeDic), overDueOrgAmount));
	                odi.setYQ007(yhOverDueOrgAmount.divide(overDueOrgAmount,2,BigDecimal.ROUND_HALF_UP));
//	                indexMap.put("YQ008", MathUtil.divide(avgOrgOverDueCount(list, "xd", returnCodeDic), overDueOrgAmount));
	                odi.setYQ008(xdOverDueOrgAmount.divide(overDueOrgAmount,2,BigDecimal.ROUND_HALF_UP));
	            }
            
		} else if (month == 12) {
			/******************************* 逾期一天以上次数 ***************************************/
//			indexMap.put("YQ038", loanOrgOverDueOneDay(list, "dk", returnCodeDic));
			odi.setYQ038(loanOrgOverDueOneDay(list, "dk", returnCodeDic));
//			indexMap.put("YQ039", loanOrgOverDueOneDay(list, "xj", returnCodeDic));
			odi.setYQ039(loanOrgOverDueOneDay(list, "xj", returnCodeDic));
//			indexMap.put("YQ040", loanOrgOverDueOneDay(list, "yh", returnCodeDic));
			odi.setYQ040(loanOrgOverDueOneDay(list, "yh", returnCodeDic));
//			indexMap.put("YQ041", loanOrgOverDueOneDay(list, "xd", returnCodeDic));
			odi.setYQ041(loanOrgOverDueOneDay(list, "xd", returnCodeDic));
			/******************************* 逾期机构数 ***************************************/
//			indexMap.put("YQ034", overDueOrgCount(list, "dk", returnCodeDic));
			odi.setYQ034(overDueOrgCount(list, "dk", returnCodeDic));
//			indexMap.put("YQ035", overDueOrgCount(list, "xj", returnCodeDic));
			odi.setYQ035(overDueOrgCount(list, "xj", returnCodeDic));
//			indexMap.put("YQ036", overDueOrgCount(list, "yh", returnCodeDic));
			odi.setYQ036(overDueOrgCount(list, "yh", returnCodeDic));
//			indexMap.put("YQ037", overDueOrgCount(list, "xd", returnCodeDic));
			odi.setYQ037(overDueOrgCount(list, "xd", returnCodeDic));
			/*******************************
			 * 12个月 最大逾期次数
			 ***************************************/
//			indexMap.put("YQ042", everyOrgOverDueMaxTimes(list, "dk", returnCodeDic));
			odi.setYQ042(everyOrgOverDueMaxTimes(list, "dk", returnCodeDic));
//			indexMap.put("YQ043", everyOrgOverDueMaxTimes(list, "xj", returnCodeDic));
			odi.setYQ043(everyOrgOverDueMaxTimes(list, "xj", returnCodeDic));
//			indexMap.put("YQ044", everyOrgOverDueMaxTimes(list, "yh", returnCodeDic));
			odi.setYQ044(everyOrgOverDueMaxTimes(list, "yh", returnCodeDic));
//			indexMap.put("YQ045", everyOrgOverDueMaxTimes(list, "xd", returnCodeDic));
			odi.setYQ045(everyOrgOverDueMaxTimes(list, "xd", returnCodeDic));
		}
	}
	
	/**
	 * 每个商户的所有记录
	 * @param list
	 * @param orgTypeList
	 * @return
	 */
	public Map<String,List<TradeDetailDO>>  merTypeMap(List<TradeDetailDO> list,List<String> orgTypeList){
		Map<String,List<TradeDetailDO>> map =  new HashMap<String,List<TradeDetailDO>>();
		List<TradeDetailDO> records = null;
		for (TradeDetailDO o : list) {
			String merId = o.getSOURCE_MERNO();//银行卡
			//非指定机构不参与逾期统计
			if(!orgTypeList.contains(o.getMER_TYPE()))continue;
			if(!map.containsKey(merId)){
				records = new ArrayList<TradeDetailDO>();	
				records.add(o);
			}else{
				records = map.get(merId);
				records.add(o);
			}
			map.put(merId, records);
		}
		return map;
	}

	/**
     * 逾期金额总和 : 近x个月逾期x天以上金额总和
     * 逾期类型3  3.在同一家公司划扣因余额不足失败，直至划扣成功为止
     * @param list
     * @param indexMap
     * @param orgType
     * @param returnCodeDic
     */
    public BigDecimal overDueTotalMoneySum(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic,String days){
        
        List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
        //逾期金额总和
        BigDecimal overDueSumMoney = null;
        BigDecimal singleOverDueSumMoney = new BigDecimal("0.00");
        //逾期日期值
        Timestamp overDueBeginDate = null ;
        //定义一个用户的银行卡在不同机构下拥有的消费记录集合
        Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
        List<TradeDetailDO> records = null;
        
        for (TradeDetailDO o : list) {
            //商户号
            String merId = o.getSOURCE_MERNO();
            
            if(!map.containsKey(merId)){
                records = new ArrayList<TradeDetailDO>();   
                records.add(o);
            }else{
                records = map.get(merId);
                records.add(o);
            }
            map.put(merId, records);
        }
        //排序和计算逾期
        for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
            List<TradeDetailDO> cardNolist = entry.getValue();
            if(cardNolist.size()<=1)continue;//如果记录小于等于一条就不参与逾期统计
            
            //逾期天数值
            for (TradeDetailDO o : cardNolist) {
                //余额不足,划扣失败
                if(ywbzLst.contains(o.getRETURN_CODE())){
                    if(!StringUtils.isEmpty(overDueBeginDate))continue;//标记第一次划扣失败时间
                    //逾期失败日期
                    overDueBeginDate = o.getCREATE_TIME();
                    overDueSumMoney = o.getAMOUNT();
                    continue;
                }else if("0000".contains(o.getRETURN_CODE())){
                    if(StringUtils.isEmpty(overDueBeginDate))continue;//标记第一次划扣失败时间
                    //逾期天数
                    int overDueBeginDayTemp = DateUtils.getIntervalDayAmount(overDueBeginDate, o.getCREATE_TIME());
                    //逾期days(天)
                    if(overDueBeginDayTemp > initProperties.getOverDueDayDic().get(days)){
                        singleOverDueSumMoney = singleOverDueSumMoney.add(overDueSumMoney);
                    }
                }
                
                //还原标记第一次划扣失败时间
                if(!StringUtils.isEmpty(overDueBeginDate)){
                    overDueBeginDate = null;
                }
            }
        }
        
        if(overDueSumMoney!=null){
            overDueSumMoney =  overDueSumMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return overDueSumMoney;
    }
    
    /**
     * 逾期天数总和 : 近x个月逾期x天以上天数总和
     * 逾期类型3  3.在同一家公司划扣因余额不足失败，直至划扣成功为止
     * @param list
     * @param indexMap
     * @param orgType
     * @param returnCodeDic
     */
    public int overDueDaysSum(List<TradeDetailDO> list,String orgType, Map<String, String[]> returnCodeDic){
        //余额不足失败返回码
        List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
        //逾期天数值
        int overDueOneDays = 0;
        //逾期日期值
        Timestamp overDueBeginDate = null ;
        
        //定义一个用户的银行卡在不同机构下拥有的消费记录集合
        Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
        List<TradeDetailDO> records = null;
        
        for (TradeDetailDO o : list) {
            //商户号
            String merId = o.getSOURCE_MERNO();
            if(!map.containsKey(merId)){
                records = new ArrayList<TradeDetailDO>();
                records.add(o);
            }else{
                records = map.get(merId);
                records.add(o);
            }
            map.put(merId, records);
        }
        
        //计算逾期
        for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
            List<TradeDetailDO> cardNolist = entry.getValue();
            if(cardNolist.size()<=1)continue;//如果记录小于等于一条就不参与逾期统计
            
            for (TradeDetailDO o : cardNolist) {
                //余额不足,划扣失败
                if(ywbzLst.contains(o.getRETURN_CODE())){
                    if(!StringUtils.isEmpty(overDueBeginDate))continue;//标记第一次划扣失败时间
                    //逾期失败日期
                    overDueBeginDate = o.getCREATE_TIME();
                    continue;
                }else if("0000".contains(o.getRETURN_CODE())){
                    if(StringUtils.isEmpty(overDueBeginDate))continue;//标记第一次划扣失败时间
                    //逾期天数
                    int overDueBeginDayTemp = DateUtils.differentDaysByMillisecond(overDueBeginDate, o.getCREATE_TIME());
                    //逾期一天以上
                    if(overDueBeginDayTemp>initProperties.getOverDueDayDic().get("1d")){
                        overDueOneDays = overDueBeginDayTemp +overDueOneDays;
                        overDueBeginDate = null;
                    }
                }
            }
            //还原标记第一次划扣失败时间
            if(!StringUtils.isEmpty(overDueBeginDate)){
                overDueBeginDate = null;
            }
        }
        return overDueOneDays;
    }
    
    /**
     * 近12个月在xx机构逾期的最大每家机构逾期次数
     * 逾期类型3  3.在同一家公司划扣因余额不足失败，直至划扣成功为止
     * @param list
     * @param indexMap
     * @param orgType
     * @param returnCodeDic
     */
    public int everyOrgOverDueMaxTimes(List<TradeDetailDO> list,String orgType , Map<String, String[]> returnCodeDic){
        Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();//商户类型归属分类字典
        List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//具体机构类
        List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));//余额不足失败返回码
        //List<String> success = Arrays.asList(returnCodeDic.get("success"));//划扣成功返回码
        
        //逾期天数值
        int overDueOneDayTimes = 0;
        //最大逾期次数值
        int max = 0;
        //逾期日期值
        Timestamp overDueBeginDate = null ;
        //定义一个用户的银行卡在不同机构下拥有的消费记录集合
        Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
        List<TradeDetailDO> records = null;
        
        for (TradeDetailDO o : list) {
            //商户号
            String merId = o.getSOURCE_MERNO();
            //非指定机构不参与逾期统计
            if(!orgTypeList.contains(o.getMER_TYPE().toString()))
                continue;
            if(!map.containsKey(merId)){
                records = new ArrayList<TradeDetailDO>();   
                records.add(o);
            }else{
                records = map.get(merId);
                records.add(o);
            }
            map.put(merId, records);
        }
        //计算逾期
        for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
            List<TradeDetailDO> cardNolist = entry.getValue();
            if(cardNolist.size()<=1)continue;//如果记录小于等于一条就不参与逾期统计
            
            for (TradeDetailDO o : cardNolist) {
                //余额不足,划扣失败
                if(ywbzLst.contains(o.getRETURN_CODE())){
                    if(!StringUtils.isEmpty(overDueBeginDate))continue;//标记第一次划扣失败时间
                    //逾期失败日期
                    overDueBeginDate = o.getCREATE_TIME();
                    overDueOneDayTimes = overDueOneDayTimes +1;
                }
                
                //比较当前逾期的次数和最大逾期次数值比较
                if(max<=overDueOneDayTimes){
                    max = overDueOneDayTimes;
                }
                //还原标记第一次划扣失败时间
                if(!StringUtils.isEmpty(overDueBeginDate)){
                    overDueBeginDate = null;
                }
            }
        }
        return max;
    }
    
    /**
     * 在贷款类机构逾期1天以上次数
     * 逾期类型3  3.在同一家公司划扣因余额不足失败，直至划扣成功为止
     * @param list
     * @param indexMap
     * @param orgType
     * @param returnCodeDic
     */
    public int loanOrgOverDueOneDay(List<TradeDetailDO> list,String orgType , Map<String, String[]> returnCodeDic){
        Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();//商户类型归属分类字典
        List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//具体机构类
        List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));//余额不足失败返回码
        
        //逾期天数值
        int overDueOneDayTimes = 0;
        //逾期日期值
        Timestamp overDueBeginDate = null ;
        //定义一个用户的银行卡在不同机构下拥有的消费记录集合
        Map<String,List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
        //计算逾期
        for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
            List<TradeDetailDO> cardNolist = entry.getValue();
            if(cardNolist.size()<=1)continue;//如果记录小于等于一条就不参与逾期统计
            
            for (TradeDetailDO o : cardNolist) {
                //余额不足,划扣失败
                if(ywbzLst.contains(o.getRETURN_CODE())){
                    if(!StringUtils.isEmpty(overDueBeginDate)) //非空跳过找成功
                        continue;//标记第一次划扣失败时间
                    //逾期失败日期
                    overDueBeginDate = o.getCREATE_TIME();
                    continue;
                }else if("0000".contains(o.getRETURN_CODE())){
                    if(StringUtils.isEmpty(overDueBeginDate))
                        continue;//标记第一次划扣失败时间
                    //逾期天数
                    int overDueBeginDayTemp = DateUtils.getIntervalDayAmount(overDueBeginDate, o.getCREATE_TIME());
                    //逾期一天以上
                    if(overDueBeginDayTemp > initProperties.getOverDueDayDic().get("1d")){
                        overDueOneDayTimes = overDueOneDayTimes +1;
                    }
                }
                //还原标记第一次划扣失败时间
                if(!StringUtils.isEmpty(overDueBeginDate)){
                    overDueBeginDate = null;
                }
            }
        }
        return overDueOneDayTimes;
    }
    
    
    /**
     * 逾期机构数
     * 逾期类型3  3.在同一家公司划扣因余额不足失败，可视为一次在该机构下的逾期
     * @param list
     * @param indexMap
     * @param orgType
     * @param returnCodeDic
     */
    public int overDueOrgCount(List<TradeDetailDO> list,String orgType , Map<String, String[]> returnCodeDic){
        Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();//商户类型归属分类字典
        List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//具体机构类
        List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));//余额不足失败返回码
        
        //逾期天数值
        int overDueOneOrgCount = 0;
        //定义一个用户的银行卡在不同机构下拥有的消费记录集合
        Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
        List<TradeDetailDO> records = null;
        
        for (TradeDetailDO o : list) {
            //商户号
            String merId = o.getSOURCE_MERNO();
            //非指定机构不参与逾期统计
            if(!orgTypeList.contains(o.getMER_TYPE().toString()))
                continue;
            if(!map.containsKey(merId)){
                records = new ArrayList<TradeDetailDO>();   
                records.add(o);
            }else{
                records = map.get(merId);
                records.add(o);
            }
            map.put(merId, records);
        }
        
        //计算逾期
        for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
            List<TradeDetailDO> cardNolist = entry.getValue();
            for (TradeDetailDO o : cardNolist) {
                //余额不足,划扣失败,看做该机构下有逾期，逾期机构数加1
                if(ywbzLst.contains(o.getRETURN_CODE())){
                    overDueOneOrgCount++;
                    //跳出当前循环，继续往下寻找
                    break;
                }
            }
        }
        return overDueOneOrgCount;
    }
    
    /**
     * 在消费金融机构逾期1天以上次数
     * @param list
     * @param indexMap
     * @param orgType
     * @param returnCodeDic
     */
    public void consumerFinanceOrgOverDueOneDay(List<TradeDetailDO> list,Map<String,Integer> indexMap ,String orgType , Map<String, String[]> returnCodeDic){
        Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
        List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//机构类型
        String extKey = "_"+orgType;
//      int days = initProperties.getOverDueDayDic().get("1d");
        List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
        List<String> success = Arrays.asList(returnCodeDic.get("success"));
        //定义一个用户在不同机构下拥有的消费记录集合
        Map<String,List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
        //key orgKey,value:逾期次数（不同机构的逾期次数）
        Map<String,Integer> averageOrgOverDue = new HashMap<String,Integer>();
        //key orgKey,value:逾期天数（不同机构的逾期天数）
        Map<String,Integer> averageOrgOverDay = new HashMap<String,Integer>();
        //排序和计算逾期
        for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
            String cardNo = entry.getKey();
            List<TradeDetailDO> cardNolist = entry.getValue();
            if(cardNolist.size()<=1)continue;
            //对集合按照日期进行排序
            //Collections.sort(cardNolist);
            double amout = 0;
            //逾期日期值
            Timestamp overDueBeginDate = null ;
            //逾期天数值
            int overDueBeginDay = 0;
            for (TradeDetailDO o : cardNolist) {
                if(cardNolist.size()<=1)continue;
                //余额不足,划扣失败
                if(ywbzLst.contains(o.getRETURN_CODE())){
                    //记录失败金额
                    amout = o.getAMOUNT().doubleValue();
                    //逾期失败日期
                    overDueBeginDate = o.getCREATE_TIME();
                    continue;
                }else if(success.contains(o.getRETURN_CODE())){
                    String merId = o.getSOURCE_MERNO()+extKey;//机构平均值主键
                    if(!averageOrgOverDue.containsKey(merId)){
                        int i = averageOrgOverDue.get(merId);
                        //逾期次数
                        averageOrgOverDue.put(merId, ++i);
                    }else{
                        averageOrgOverDue.put(merId, 1);
                    }
                    //划扣成功,最终划扣成功且划扣成功金额=失败金额
                    if(amout==o.getAMOUNT().doubleValue()){
                        //计算逾期天数
                        int overDueBeginDayTemp = DateUtils.differentDaysByMillisecond(overDueBeginDate, o.getCREATE_TIME());
                        averageOrgOverDay.put(merId, overDueBeginDayTemp+ overDueBeginDay);
                    }
                }
            }
        }
    }
    
    /**
     * 在银行类机构逾期1天以上次数
     * @param list
     * @param indexMap
     * @param orgType
     * @param returnCodeDic
     */
    public void bankOrgOverDueOneDay(List<TradeDetailDO> list,Map<String,Integer> indexMap ,String orgType , Map<String, String[]> returnCodeDic){
        Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
        List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//机构类型
        String extKey = "_"+orgType;
//      int days = initProperties.getOverDueDayDic().get("1d");
        List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
        List<String> success = Arrays.asList(returnCodeDic.get("success"));
        //定义一个用户在不同机构下拥有的消费记录集合
        Map<String,List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
        //key orgKey,value:逾期次数（不同机构的逾期次数）
        Map<String,Integer> averageOrgOverDue = new HashMap<String,Integer>();
        //key orgKey,value:逾期天数（不同机构的逾期天数）
        Map<String,Integer> averageOrgOverDay = new HashMap<String,Integer>();
        //排序和计算逾期
        for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
            String cardNo = entry.getKey();
            List<TradeDetailDO> cardNolist = entry.getValue();
            if(cardNolist.size()<=1)continue;
            //对集合按照日期进行排序
            //Collections.sort(cardNolist);
            double amout = 0;
            //逾期日期值
            Timestamp overDueBeginDate = null ;
            //逾期天数值
            int overDueBeginDay = 0;
            for (TradeDetailDO o : cardNolist) {
                if(cardNolist.size()<=1)continue;
                //余额不足,划扣失败
                if(ywbzLst.contains(o.getRETURN_CODE())){
                    //记录失败金额
                    amout = o.getAMOUNT().doubleValue();
                    //逾期失败日期
                    overDueBeginDate = o.getCREATE_TIME();
                    continue;
                }else if(success.contains(o.getRETURN_CODE())){
                    String merId = o.getSOURCE_MERNO()+extKey;//机构平均值主键
                    if(!averageOrgOverDue.containsKey(merId)){
                        int i = averageOrgOverDue.get(merId);
                        //逾期次数
                        averageOrgOverDue.put(merId, ++i);
                    }else{
                        averageOrgOverDue.put(merId, 1);
                    }
                    //划扣成功,最终划扣成功且划扣成功金额=失败金额
                    if(amout==o.getAMOUNT().doubleValue()){
                        //计算逾期天数
                        int overDueBeginDayTemp = DateUtils.differentDaysByMillisecond(overDueBeginDate, o.getCREATE_TIME());
                        averageOrgOverDay.put(merId, overDueBeginDayTemp+ overDueBeginDay);
                    }
                }
            }
        }
    }
    
    
    /**
     * 在小贷款类机构逾期1天以上次数
     * @param list
     * @param indexMap
     * @param orgType
     * @param returnCodeDic
     */
    public void smallLoanOrgOverDueOneDay(List<TradeDetailDO> list,Map<String,Integer> indexMap ,String orgType , Map<String, String[]> returnCodeDic){
        Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
        List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//机构类型
        String extKey = "_"+orgType;
//      int days = initProperties.getOverDueDayDic().get("1d");
        List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
        List<String> success = Arrays.asList(returnCodeDic.get("success"));
        //定义一个用户在不同机构下拥有的消费记录集合
        Map<String,List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
        //key orgKey,value:逾期次数（不同机构的逾期次数）
        Map<String,Integer> averageOrgOverDue = new HashMap<String,Integer>();
        //key orgKey,value:逾期天数（不同机构的逾期天数）
        Map<String,Integer> averageOrgOverDay = new HashMap<String,Integer>();
        //排序和计算逾期
        for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
            String cardNo = entry.getKey();
            List<TradeDetailDO> cardNolist = entry.getValue();
            if(cardNolist.size()<=1)continue;
            //对集合按照日期进行排序
            //Collections.sort(cardNolist);
            double amout = 0;
            //逾期日期值
            Timestamp overDueBeginDate = null ;
            //逾期天数值
            int overDueBeginDay = 0;
            for (TradeDetailDO o : cardNolist) {
                if(cardNolist.size()<=1)continue;
                //余额不足,划扣失败
                if(ywbzLst.contains(o.getRETURN_CODE())){
                    //记录失败金额
                    amout = o.getAMOUNT().doubleValue();
                    //逾期失败日期
                    overDueBeginDate = o.getCREATE_TIME();
                    continue;
                }else if(success.contains(o.getRETURN_CODE())){
                    String merId = o.getSOURCE_MERNO()+extKey;//机构平均值主键
                    if(!averageOrgOverDue.containsKey(merId)){
                        int i = averageOrgOverDue.get(merId);
                        //逾期次数
                        averageOrgOverDue.put(merId, ++i);
                    }else{
                        averageOrgOverDue.put(merId, 1);
                    }
                    //划扣成功,最终划扣成功且划扣成功金额=失败金额
                    if(amout==o.getAMOUNT().doubleValue()){
                        //计算逾期天数
                        int overDueBeginDayTemp = DateUtils.differentDaysByMillisecond(overDueBeginDate, o.getCREATE_TIME());
                        averageOrgOverDay.put(merId, overDueBeginDayTemp+ overDueBeginDay);
                    }
                }
            }
        }
    }
    /**
     * 逾期一天以上计算
     * @param list
     * @param indexMap
     * @param orgType
     * @param returnCodeDic
     */
    public void calculateOverDue(List<TradeDetailDO> list,Map<String,Integer> indexMap ,String orgType , Map<String, String[]> returnCodeDic){
        Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
        List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//机构类型
        String extKey = "_"+orgType;
//      int days = initProperties.getOverDueDayDic().get("1d");
        List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
        List<String> success = Arrays.asList(returnCodeDic.get("success"));
        //定义一个用户在不同机构下拥有的消费记录集合
        Map<String,List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
        //key orgKey,value:逾期次数（不同机构的逾期次数）
        Map<String,Integer> averageOrgOverDue = new HashMap<String,Integer>();
        //key orgKey,value:逾期天数（不同机构的逾期天数）
        Map<String,Integer> averageOrgOverDay = new HashMap<String,Integer>();
        //排序和计算逾期
        for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
            String cardNo = entry.getKey();
            List<TradeDetailDO> cardNolist = entry.getValue();
            if(cardNolist.size()<=1)continue;
            //对集合按照日期进行排序
            //Collections.sort(cardNolist);
            double amout = 0;
            //逾期日期值
            Timestamp overDueBeginDate = null ;
            //逾期天数值
            int overDueBeginDay = 0;
            for (TradeDetailDO o : cardNolist) {
                if(cardNolist.size()<=1)continue;
                //余额不足,划扣失败
                if(ywbzLst.contains(o.getRETURN_CODE())){
                    //记录失败金额
                    amout = o.getAMOUNT().doubleValue();
                    //逾期失败日期
                    overDueBeginDate = o.getCREATE_TIME();
                    continue;
                }else if(success.contains(o.getRETURN_CODE())){
                    String merId = o.getSOURCE_MERNO()+extKey;//机构平均值主键
                    if(!averageOrgOverDue.containsKey(merId)){
                        int i = averageOrgOverDue.get(merId);
                        //逾期次数
                        averageOrgOverDue.put(merId, ++i);
                    }else{
                        averageOrgOverDue.put(merId, 1);
                    }
                    //划扣成功,最终划扣成功且划扣成功金额=失败金额
                    if(amout==o.getAMOUNT().doubleValue()){
                        //计算逾期天数
                        int overDueBeginDayTemp = DateUtils.differentDaysByMillisecond(overDueBeginDate, o.getCREATE_TIME());
                        averageOrgOverDay.put(merId, overDueBeginDayTemp+ overDueBeginDay);
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
         
         Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();//商户类型归属分类字典
         List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//具体机构类
         List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));//余额不足失败返回码
         
         //某类机构的逾期逾期总次数
         int averageOrgOverDueTime = 0;
         
         //定义一个用户的银行卡在不同机构下拥有的消费记录集合
         Map<String,List<TradeDetailDO>> map = merTypeMap(list, orgTypeList);
         
         //排序和计算逾期
         for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
             List<TradeDetailDO> cardNolist = entry.getValue();
             for (TradeDetailDO o : cardNolist) {
                 if(ywbzLst.contains(o.getRETURN_CODE())){
                     averageOrgOverDueTime++;
                 }
             }
         }
         
         return averageOrgOverDueTime;
         
     }
     
}
