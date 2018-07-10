package com.epay.xj.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.epay.xj.domain.TradeDetailDO;
import com.epay.xj.properties.InitProperties;
import com.epay.xj.utils.DateUtils;

@Service
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

	public List<TradeDetailDO> getTradeDetail(String certNo, String beginTime, String endTime) {
		List<TradeDetailDO> tradeDetailList = new ArrayList<TradeDetailDO>();
		String sql = "select * from CP_ODS.P1055_TRA_TRADE_DETAIL_PARA where IDCARD=" + certNo+ " and CREATE_TIME between TIMESTAMP_FORMAT('"+beginTime+
				"','yyyy-mm-dd') and TIMESTAMP_FORMAT('"+ endTime+"','yyyy-mm-dd') order by CREATE_TIME asc";
		List list = entityManager.createNativeQuery(sql).getResultList();
		for (Object object : list) {
			Object[] arr = (Object[]) object;
			TradeDetailDO t = new TradeDetailDO();
			Timestamp timeStamp = (Timestamp) arr[0];
			t.setCREATE_TIME(timeStamp);
			t.setID((BigDecimal)arr[1]);
			t.setIDCARD((String)arr[2]);
			t.setACCOUNT_NO((String)arr[3]);
			t.setSOURCE_MERNO((String)arr[4]);
			t.setMER_TYPE((Integer)arr[5]);
			t.setAMOUNT((BigDecimal)arr[6]);
			t.setSF_TYPE(arr[7].toString());
			t.setRETURN_CODE(arr[8].toString());
			tradeDetailList.add(t);
		}
		return tradeDetailList;
	}

	public List<TradeDetailDO> fatherList(String certNo) {
		List<TradeDetailDO> tradeDetailList = new ArrayList<TradeDetailDO>();
		String sql = "select * from CP_ODS.P1055_TRA_TRADE_DETAIL_PARA where IDCARD=" + certNo+ " order by CREATE_TIME asc";
		List list = entityManager.createNativeQuery(sql).getResultList();
		for (Object object : list) {
			Object[] arr = (Object[]) object;
			TradeDetailDO t = new TradeDetailDO();
			Timestamp timeStamp = (Timestamp) arr[0];
			t.setCREATE_TIME(timeStamp);
			t.setID((BigDecimal)arr[1]);
			t.setIDCARD((String)arr[2]);
			t.setACCOUNT_NO((String)arr[3]);
			t.setSOURCE_MERNO((String)arr[4]);
			t.setMER_TYPE((Integer)arr[5]);
			t.setAMOUNT((BigDecimal)arr[6]);
			t.setSF_TYPE(arr[7].toString());
			t.setRETURN_CODE(arr[8].toString());
			tradeDetailList.add(t);
		}
		return tradeDetailList;
	}
	
	public void sliceTask(List<String> taskList,String updateTime) throws InterruptedException{
		 // 每500条数据开启一条线程
        int threadSize = 5000;
        // 总数据条数
        int dataSize = taskList.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % threadSize == 0;
        // 创建一个线程池
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        // 定义一个任务集合
        List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
        Callable<Integer> task = null;
        List<String> cutList = null;
        logger.info("线程数：{}", threadNum);
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
            task = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                	Map<String, Integer> overDueMouth = initProperties.getOverDueMonth();
                    logger.info("{}程数：集合数量：{}", Thread.currentThread().getName(),listStr.size());
                    for (int i = 0; i < listStr.size(); i++) {
            			long sysBeginTime = System.nanoTime();
            			//如果是人的所有记录
            			List<TradeDetailDO> fatherList = fatherList(listStr.get(i));
            			Map<Integer,List<TradeDetailDO>> tradeMap = new HashMap<Integer,List<TradeDetailDO>>();
            			for (int month : overDueMouth.values()) {
            				tradeMap.put(month, getListByMonth(fatherList, month, udpateTimes));
            			}
            			for (int month : overDueMouth.values()) {
//            				String beginTime = DateUtils.getDateOfXMonthsAgo(udpateTimes, month);
            				Map<String, String> indexMap = new HashMap<String, String>();
            				Map<String, String[]> returnCodeDic = initProperties.getReturnCodeDic();
//            				List<TradeDetailDO> list = getTradeDetail(listStr.get(i), beginTime, udpateTimes);
            				List<TradeDetailDO> list = tradeMap.get(month);
            				logger.info("certNo:{},month:{},集合数量:{}",listStr.get(i),month,list.size());
            				overDueMouth(list, indexMap, month, returnCodeDic);
//            				for (TradeDetailDO tradeDetail : list) {
//            					overDueMouth(list, indexMap, month, returnCodeDic);
//            				}
            				//每个人指定月份下所有预期类指标
            				for(Map.Entry<String,String> entry : indexMap.entrySet()){
            					System.out.println("p:"+entry.getKey()+",v:"+entry.getValue());
            				}
            			}
            			String useTime = String.valueOf((System.nanoTime() - sysBeginTime)/Math.pow(10, 9));
            			logger.info("certNo：{},useTime:{}秒",listStr.get(i),useTime);
            			// 天数统计
            		}
                    return 1;
                }
            };
            // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
            tasks.add(task);
        }
        List<Future<Integer>> results = exec.invokeAll(tasks);
        for (Future<Integer> future : results) {
            try {
				System.out.println(future.get());
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
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
		Map<String, Integer> overDueMouth = initProperties.getOverDueMonth();
		List<String> taskList = getTaskList(updateTime, flag);
		try {
			sliceTask(taskList, updateTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deal(String updateTime, String flag) {
		Map<String, Integer> overDueMouth = initProperties.getOverDueMonth();
		List<String> taskList = getTaskList(updateTime, flag);
		for (int i = 0; i < taskList.size(); i++) {
			long sysBeginTime = System.nanoTime();
			for (int month : overDueMouth.values()) {
				String beginTime = DateUtils.getDateOfXMonthsAgo(updateTime, month);
				Map<String, String> indexMap = new HashMap<String, String>();
				Map<String, String[]> returnCodeDic = initProperties.getReturnCodeDic();
				List<TradeDetailDO> list = getTradeDetail(taskList.get(i), beginTime, updateTime);
				for (TradeDetailDO tradeDetail : list) {
					overDueMouth(list, indexMap, month, returnCodeDic);
				}
				for(Map.Entry<String,String> entry : indexMap.entrySet()){
					System.out.println("p:"+entry.getKey()+",v:"+entry.getValue());
				}
			}
			String useTime = String.valueOf((System.nanoTime() - sysBeginTime)/Math.pow(10, 9));
			logger.info("useTime:{}秒",useTime);
			// 天数统计
		}
	}

	private void overDueMouth(List<TradeDetailDO> list, Map<String, String> indexMap, int month,
			Map<String, String[]> returnCodeDic) {
		if (month == 3) {
			/******************************* 逾期一天以上次数 ***************************************/
			indexMap.put("YQ013", loanOrgOverDueOneDay(list, "dk", returnCodeDic));
			indexMap.put("YQ014", loanOrgOverDueOneDay(list, "xj", returnCodeDic));
			indexMap.put("YQ015", loanOrgOverDueOneDay(list, "yh", returnCodeDic));
			indexMap.put("YQ016", loanOrgOverDueOneDay(list, "xd", returnCodeDic));
			/******************************* 逾期机构数 ***************************************/
			indexMap.put("YQ017", overDueOrgCount(list, "dk", returnCodeDic));
			indexMap.put("YQ018", overDueOrgCount(list, "xj", returnCodeDic));
			indexMap.put("YQ019", overDueOrgCount(list, "yh", returnCodeDic));
			indexMap.put("YQ020", overDueOrgCount(list, "xd", returnCodeDic));
			/******************************* 逾期天数总和 ***************************************/
			indexMap.put("YQ027", overDueDaysSum(list, returnCodeDic));
			/******************************* 逾期金额总和 ***************************************/
			indexMap.put("YQ033", overDueTotalMoneySum(list, returnCodeDic, "1d"));
			indexMap.put("YQ032", overDueTotalMoneySum(list, returnCodeDic, "7d"));
			indexMap.put("YQ031", overDueTotalMoneySum(list, returnCodeDic, "30d"));

		} else if (month == 6) {
			/******************************* 逾期一天以上次数 ***************************************/
			indexMap.put("YQ001", loanOrgOverDueOneDay(list, "dk", returnCodeDic));
			indexMap.put("YQ002", loanOrgOverDueOneDay(list, "xj", returnCodeDic));
			indexMap.put("YQ003", loanOrgOverDueOneDay(list, "yh", returnCodeDic));
			indexMap.put("YQ004", loanOrgOverDueOneDay(list, "xd", returnCodeDic));
			/******************************* 逾期机构数 ***************************************/
			indexMap.put("YQ009", overDueOrgCount(list, "dk", returnCodeDic));
			indexMap.put("YQ010", overDueOrgCount(list, "xj", returnCodeDic));
			indexMap.put("YQ011", overDueOrgCount(list, "yh", returnCodeDic));

			/******************************* 逾期天数总和 ***************************************/

			indexMap.put("YQ026", overDueDaysSum(list, returnCodeDic));

			/******************************* 逾期金额总和 ***************************************/
			indexMap.put("YQ030", overDueTotalMoneySum(list, returnCodeDic, "1d"));
			indexMap.put("YQ029", overDueTotalMoneySum(list, returnCodeDic, "7d"));
			indexMap.put("YQ028", overDueTotalMoneySum(list, returnCodeDic, "30d"));
		} else if (month == 12) {
			/******************************* 逾期一天以上次数 ***************************************/
			indexMap.put("YQ038", loanOrgOverDueOneDay(list, "dk", returnCodeDic));
			indexMap.put("YQ039", loanOrgOverDueOneDay(list, "xj", returnCodeDic));
			indexMap.put("YQ040", loanOrgOverDueOneDay(list, "yh", returnCodeDic));
			indexMap.put("YQ041", loanOrgOverDueOneDay(list, "xd", returnCodeDic));
			/******************************* 逾期机构数 ***************************************/
			indexMap.put("YQ034", overDueOrgCount(list, "dk", returnCodeDic));
			indexMap.put("YQ035", overDueOrgCount(list, "xj", returnCodeDic));
			indexMap.put("YQ036", overDueOrgCount(list, "yh", returnCodeDic));
			indexMap.put("YQ037", overDueOrgCount(list, "xd", returnCodeDic));
			/*******************************
			 * 12个月 最大逾期次数
			 ***************************************/
			indexMap.put("YQ042", everyOrgOverDueMaxTimes(list, "dk", returnCodeDic));
			indexMap.put("YQ043", everyOrgOverDueMaxTimes(list, "xj", returnCodeDic));
			indexMap.put("YQ044", everyOrgOverDueMaxTimes(list, "yh", returnCodeDic));
			indexMap.put("YQ045", everyOrgOverDueMaxTimes(list, "xd", returnCodeDic));
		}
	}

		/**
	 * 逾期金额总和 : 近x个月逾期x天以上金额总和
	 * 逾期类型3  3.在同一家公司划扣因余额不足失败，直至划扣成功为止
	 * @param list
	 * @param indexMap
	 * @param orgType
	 * @param returnCodeDic
	 */
	public String overDueTotalMoneySum(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic,String days){
//		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();//商户类型归属分类字典
//		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//具体机构类
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));//因余额不足失败
//		List<String> success = Arrays.asList(returnCodeDic.get("success"));//划扣成功
		//逾期天数值
		BigDecimal overDueSumMoney = null;
		//逾期日期值
		Timestamp overDueBeginDate = null ;
		//定义一个用户的银行卡在不同机构下拥有的消费记录集合
		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
		List<TradeDetailDO> records = null;
		for (TradeDetailDO o : list) {
			String merId = o.getSOURCE_MERNO();//银行卡
			//非指定机构不参与逾期统计
//			if(!orgTypeList.contains(o.getMER_TYPE()))continue;
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
			//对集合按照日期进行排序
			//Collections.sort(cardNolist);
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
					int overDueBeginDayTemp = DateUtils.differentDaysByMillisecond(overDueBeginDate, o.getCREATE_TIME());
					//逾期days天以上
					if(overDueBeginDayTemp>initProperties.getOverDueDayDic().get(days)){
						overDueSumMoney = overDueSumMoney.add(o.getAMOUNT());
						overDueBeginDate = null;
					}
				}
			}
			//还原标记第一次划扣失败时间
			if(!StringUtils.isEmpty(overDueBeginDate)){
				overDueBeginDate = null;
			}
		}
		
		return overDueSumMoney==null?"0":overDueSumMoney.toString();
	}
	
	
	/**
	 * 逾期天数总和 : 近x个月逾期x天以上天数总和
	 * 逾期类型3  3.在同一家公司划扣因余额不足失败，直至划扣成功为止
	 * @param list
	 * @param indexMap
	 * @param orgType
	 * @param returnCodeDic
	 */
	public String overDueDaysSum(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic){
//		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();//商户类型归属分类字典
//		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//具体机构类
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));//因余额不足失败
//		List<String> success = Arrays.asList(returnCodeDic.get("success"));//划扣成功
		//逾期天数值
		int overDueOneDays = 0;
		//逾期日期值
		Timestamp overDueBeginDate = null ;
		//定义一个用户的银行卡在不同机构下拥有的消费记录集合
		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
		List<TradeDetailDO> records = null;
		for (TradeDetailDO o : list) {
			String merId = o.getSOURCE_MERNO();//银行卡
			//非指定机构不参与逾期统计
//			if(!orgTypeList.contains(o.getMER_TYPE()))continue;
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
			//对集合按照日期进行排序
			//Collections.sort(cardNolist);
			//逾期天数值
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
		return String.valueOf(overDueOneDays);
	}
	/**
	 * 近12个月在xx机构逾期的最大每家机构逾期次数
	 * 逾期类型3  3.在同一家公司划扣因余额不足失败，直至划扣成功为止
	 * @param list
	 * @param indexMap
	 * @param orgType
	 * @param returnCodeDic
	 */
	public String everyOrgOverDueMaxTimes(List<TradeDetailDO> list,String orgType , Map<String, String[]> returnCodeDic){
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();//商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//具体机构类
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));//因余额不足失败
		List<String> success = Arrays.asList(returnCodeDic.get("success"));//划扣成功
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
		//排序和计算逾期
		for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
			List<TradeDetailDO> cardNolist = entry.getValue();
			if(cardNolist.size()<=1)continue;//如果记录小于等于一条就不参与逾期统计
			//对集合按照日期进行排序
			//Collections.sort(cardNolist);
			//逾期天数值
			for (TradeDetailDO o : cardNolist) {
				//余额不足,划扣失败
				if(ywbzLst.contains(o.getCREATE_TIME())){
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
						overDueOneDayTimes = overDueOneDayTimes +1;
						overDueBeginDate = null;
					}
				}
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
		return String.valueOf(max);
	}
	
	/**
	 * 在贷款类机构逾期1天以上次数
	 * 逾期类型3  3.在同一家公司划扣因余额不足失败，直至划扣成功为止
	 * @param list
	 * @param indexMap
	 * @param orgType
	 * @param returnCodeDic
	 */
	public String loanOrgOverDueOneDay(List<TradeDetailDO> list,String orgType , Map<String, String[]> returnCodeDic){
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();//商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//具体机构类
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));//因余额不足失败
//		List<String> success = Arrays.asList(returnCodeDic.get("success"));//划扣成功
		//逾期天数值
		int overDueOneDayTimes = 0;
		//逾期日期值
		Timestamp overDueBeginDate = null ;
		//定义一个用户的银行卡在不同机构下拥有的消费记录集合
		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
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
		//排序和计算逾期
		for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
			List<TradeDetailDO> cardNolist = entry.getValue();
			if(cardNolist.size()<=1)continue;//如果记录小于等于一条就不参与逾期统计
			//对集合按照日期进行排序
			//Collections.sort(cardNolist);
			//逾期天数值
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
						overDueOneDayTimes = overDueOneDayTimes +1;
					}
				}
			}
			//还原标记第一次划扣失败时间
			if(!StringUtils.isEmpty(overDueBeginDate)){
				overDueBeginDate = null;
			}
		}
		return String.valueOf(overDueOneDayTimes);
	}
	
	
	/**
	 * 逾期机构数
	 * 逾期类型3  3.在同一家公司划扣因余额不足失败，可视为一次在该机构下的逾期
	 * @param list
	 * @param indexMap
	 * @param orgType
	 * @param returnCodeDic
	 */
	public String overDueOrgCount(List<TradeDetailDO> list,String orgType , Map<String, String[]> returnCodeDic){
		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();//商户类型归属分类字典
		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//具体机构类
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));//因余额不足失败
//		List<String> success = Arrays.asList(returnCodeDic.get("success"));//划扣成功
		//逾期天数值
		int overDueOneOrgCount = 0;
		//定义一个用户的银行卡在不同机构下拥有的消费记录集合
		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
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
		//排序和计算逾期
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
		return String.valueOf(overDueOneOrgCount);
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
//		int days = initProperties.getOverDueDayDic().get("1d");
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
		List<String> success = Arrays.asList(returnCodeDic.get("success"));
		//定义一个用户在不同机构下拥有的消费记录集合
		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
		List<TradeDetailDO> records = null;
		
		for (TradeDetailDO o : list) {
			String cordNo = o.getSOURCE_MERNO()+extKey;
			//非指定机构不参与逾期统计
			if(!orgTypeList.contains(o.getMER_TYPE()))continue;
			if(!map.containsKey(cordNo)){
				records = new ArrayList<TradeDetailDO>();	
				records.add(o);
			}else{
				records = map.get(cordNo);
				records.add(o);
			}
			map.put(cordNo, records);
		}
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
		
		//逾期天数计算
		for(Map.Entry<String,Integer> entry : averageOrgOverDay.entrySet()){
			
		}
		//3333
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
//		int days = initProperties.getOverDueDayDic().get("1d");
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
		List<String> success = Arrays.asList(returnCodeDic.get("success"));
		//定义一个用户在不同机构下拥有的消费记录集合
		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
		List<TradeDetailDO> records = null;
		
		for (TradeDetailDO o : list) {
			String cordNo = o.getSOURCE_MERNO()+extKey;
			//非指定机构不参与逾期统计
			if(!orgTypeList.contains(o.getMER_TYPE()))continue;
			if(!map.containsKey(cordNo)){
				records = new ArrayList<TradeDetailDO>();	
				records.add(o);
			}else{
				records = map.get(cordNo);
				records.add(o);
			}
			map.put(cordNo, records);
		}
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
		
		//逾期天数计算
		for(Map.Entry<String,Integer> entry : averageOrgOverDay.entrySet()){
			
		}
		//3333
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
//		int days = initProperties.getOverDueDayDic().get("1d");
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
		List<String> success = Arrays.asList(returnCodeDic.get("success"));
		//定义一个用户在不同机构下拥有的消费记录集合
		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
		List<TradeDetailDO> records = null;
		
		for (TradeDetailDO o : list) {
			String cordNo = o.getSOURCE_MERNO()+extKey;
			//非指定机构不参与逾期统计
			if(!orgTypeList.contains(o.getMER_TYPE()))continue;
			if(!map.containsKey(cordNo)){
				records = new ArrayList<TradeDetailDO>();	
				records.add(o);
			}else{
				records = map.get(cordNo);
				records.add(o);
			}
			map.put(cordNo, records);
		}
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
		
		//逾期天数计算
		for(Map.Entry<String,Integer> entry : averageOrgOverDay.entrySet()){
			
		}
		//3333
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
//		int days = initProperties.getOverDueDayDic().get("1d");
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
		List<String> success = Arrays.asList(returnCodeDic.get("success"));
		//定义一个用户在不同机构下拥有的消费记录集合
		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
		List<TradeDetailDO> records = null;
		
		for (TradeDetailDO o : list) {
			String cordNo = o.getSOURCE_MERNO()+extKey;
			//非指定机构不参与逾期统计
			if(!orgTypeList.contains(o.getMER_TYPE()))continue;
			if(!map.containsKey(cordNo)){
				records = new ArrayList<TradeDetailDO>();	
				records.add(o);
			}else{
				records = map.get(cordNo);
				records.add(o);
			}
			map.put(cordNo, records);
		}
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
		
		//逾期天数计算
		for(Map.Entry<String,Integer> entry : averageOrgOverDay.entrySet()){
			
		}
		//3333
	}
}
