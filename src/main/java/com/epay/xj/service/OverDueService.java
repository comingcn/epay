//package com.epay.xj.service;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.math.BigDecimal;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import com.csvreader.CsvReader;
//import com.csvreader.CsvWriter;
//import com.epay.xj.common.BigFileReader;
//import com.epay.xj.common.IHandle;
//import com.epay.xj.domain.TradeDetailDO;
//import com.epay.xj.domain.Variables;
//import com.epay.xj.properties.InitProperties;
//import com.epay.xj.utils.DateUtils;
//import com.epay.xj.utils.FileUtils;
//
//@Service
//public class OverDueService {
//
//	Logger logger = LoggerFactory.getLogger(getClass());
//	@Autowired
//	private InitProperties initProperties;
//	@Autowired
//	private CertNoService certNoService;
//	@Autowired
//	private TradeDetailService tradeDetailService;
//
//	public void readerMerTradeDetailTable() throws Exception{
//		StringBuffer sb = new StringBuffer();
//		sb.append(initProperties.getfPathInput()).append(initProperties.getTradeDetail()).append("20180102").append(".DEL");
//		BigFileReader.Builder builder = new BigFileReader.Builder(sb.toString(), new IHandle() {
//			
//			public void handle(String line) {
//				try {
//					certNoService.updateOrInsertRecord(line.split(","));
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
////				String s = line.split(",")[3];
////				System.out.println(s);
////				set.add(s);
////				log.info(MD5Util.encrypt(s));
////				System.out.println(set.size());
//			}
//		});
//		builder.withTreahdSize(2).withCharset("ISO-8859-1").withBufferSize(1024 * 1024);
//		BigFileReader bigFileReader = builder.build();
//		bigFileReader.start();
////		File file = new File(sb.toString());
////		CsvReader reader = new CsvReader(new FileReader(file.getAbsolutePath()));
////		String[] headerInfo = initProperties.getInputHeaderMtd().split(",");
////		reader.setHeaders(headerInfo);
////		while (reader.readRecord()) {
////			certNoService.updateOrInsert(reader);
//////			logger.info(MD5Util.encrypt(certNo));
////		}
//	}
//	
//	public void appendTradeDetail(String fileName, String[] record) throws IOException{
//		StringBuffer sb = new StringBuffer();
//		//该目录必须存在
//		sb.append(initProperties.getfPathOutput()).append("certNo//");
//		//文件不存在，创建目录
//		if(!FileUtils.existsDirectory(sb.toString()))FileUtils.forceDirectory(sb.toString());
//		sb.append(fileName).append(".DEL");
//		File certNoFile = new File(sb.toString());// 指定要写入的文件
//		//如果文件不存在，创建文件
//		if(!FileUtils.existsFile(certNoFile.getAbsolutePath()))certNoFile.createNewFile();
//        // 获取该文件的缓冲输出流  
//        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
//				new FileOutputStream(certNoFile.getAbsolutePath(), true), Charset.forName("ISO-8859-1")));
//		CsvWriter csvWriter = new CsvWriter(bufferedWriter, ',');
//		csvWriter.writeRecord(record);
//		bufferedWriter.close();
//		csvWriter.close();
//	}
//	
//	
//	public void addTradeDetail(String updateTime) throws Exception{
//		List<String> list = certNoService.getAllCertNo(updateTime);
//		// 每500条数据开启一条线程
//        int threadSize = 500;
//        // 总数据条数
//        int dataSize = list.size();
//        // 线程数
//        int threadNum = dataSize / threadSize + 1;
//        // 定义标记,过滤threadNum为整数
//        boolean special = dataSize % threadSize == 0;
//        // 创建一个线程池
//        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
//        
//        // 定义一个任务集合
//        List<Callable<List<Variables>>> tasks = new ArrayList<Callable<List<Variables>>>();
//        Callable<List<Variables>> task = null;
//        List<String> cutList = null;
//        // 确定每条线程的数据
//        for (int i = 0; i < threadNum; i++) {
//            if (i == threadNum - 1) {
//                if (special) {
//                    break;
//                }
//                cutList = list.subList(threadSize * i, dataSize);
//            } else {
//                cutList = list.subList(threadSize * i, threadSize * (i + 1));
//            }
//            logger.info("第{}组", i + 1);
//            final List<String> listStr = cutList;
//            task = new Callable<List<Variables>>() {
//                @Override
//                public List<Variables> call() throws Exception {
//                	List<Variables> vlist = new ArrayList<Variables>();
//                	for (int i=0;i<listStr.size();i++) {
//                		Variables v = dealSingleCertNoData(listStr.get(i),i);
//                		vlist.add(v);
//					}
//                	logger.info("当前线程名称:{},处理数据：{}", Thread.currentThread().getName(),listStr);
//                    return vlist;
//                }
//            };
//            // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
//            tasks.add(task);
//        }
//        List<Future<List<Variables>>> results = exec.invokeAll(tasks);
//        for (Future<List<Variables>> future : results) {
//        	List<Variables> lst = future.get();
//        	for (Variables variables : lst) {
//        		System.out.println(variables.toString());
//			}
//        }
//        // 关闭线程池
//        exec.shutdown();
//	}
//
//	/**
//	 * 汇总指定身份号的指标
//	 * @param certNo 身份证号
//	 * @param i 组合表名
//	 * @return
//	 * @throws Exception 
//	 */
//	public Variables dealSingleCertNoData(String certNo, int i) throws Exception {
//		Variables v = new Variables();
//		v.setCertNo(String.valueOf(new Random().nextInt(10)));
//		StringBuffer sb = new StringBuffer();
//		//该目录必须存在
//		sb.append(initProperties.getfPathOutput()).append("certNo//").append(certNo).append(".DEL");
//		File file = new File(sb.toString());
//		CsvReader reader = new CsvReader(new FileReader(file.getAbsolutePath()));
//		String[] headerInfo = initProperties.getInputHeaderMtd().split(",");
//		reader.setHeaders(headerInfo);
//		int rows = FileUtils.readFileLines(file);//读取记录数
//		List<TradeDetailDO> list = new ArrayList<TradeDetailDO>(rows);//初始化记录数
//		while (reader.readRecord()) {
//			TradeDetailDO tdd = new TradeDetailDO();
//			BigDecimal amout = new BigDecimal(reader.get("AMOUNT"));
//			String cardNo = reader.get("CARD_NO");
//			String certNoTemp = reader.get("CERT_NO");
////			double amount = decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
////			tdd.setAmout(amout);
////			tdd.setCardNo(cardNo);
////			tdd.setCertNo(certNoTemp);
////			tdd.setMerId(reader.get("MER_ID"));
////			tdd.setMerType(reader.get("MER_TYPE"));
////			tdd.setSfType(reader.get("SF_TYPE"));
////			tdd.setTxtDate(reader.get("TXN_DATE"));
////			tdd.setTxtSeqId(reader.get("TXN_SEQ_ID"));
////			tdd.setReturnCode(reader.get("RETURN_CODE"));
//			list.add(tdd);
//		}
//		tradeDetailService.batchInsert(list);
//		
//		//计算各个指标值
//		return v;
//	}
//	
//	
//	
//	/**----------------------------------------------------------------------------------------------**/
//	
//	/**
//	 * 逾期所有指标计算统一入口
//	 * 逾期类型1:1.在同一家公司划扣因余额不足失败，到最终划扣成功且划扣成功金额=失败金额为止，设定为逾期天数；
//	 * 逾期类型2:2.在同一家公司划扣因余额不足失败，直至划扣成功金额>=失败金额为止
//     * 逾期类型3:3.在同一家公司划扣因余额不足失败，直至划扣成功为止
//	 */
//	public void overDue(String certNo){
//		//逾期口径1
//		String overDueType = initProperties.getOverDueType();
//		
//		if(overDueType.equals("1")){
//			calculateOverDuePrepare(certNo, "20180704");
//		}else if(overDueType.equals("2")){
//			calculateOverDuePrepare(certNo, "20180704");
//		}else{
//			calculateOverDuePrepare(certNo, "20180704");
//		}
//	}
//	
//	/**
//	 * 目前暂定为trade_detail_log表中身份证下一年的所有记录，不知道放入内存会不会崩溃
//	 * 返回逾期的所有指标计算结果
//	 */
//	public void calculateOverDuePrepare(String certNo, String endTime ){
//		
//		Map<String, String[]> returnCodeDic = initProperties.getReturnCodeDic();
//		Map<String,Integer> overDueMouth = initProperties.getOverDueMonth();
//		//逾期指标结果集
//		Map<String,String> indexMap = new HashMap<String,String>();
//		for (int month : overDueMouth.values()) {
//			String beginTime = DateUtils.getDateOfXMonthsAgo(endTime, month);
//			logger.info("overDueMonth:{},beginTime:{},endTime:{}", month,beginTime,endTime);
//			//获取指定时间段某人的交易记录
//			List<TradeDetailDO> list = tradeDetailService.getTradeDetailsByCertNo(certNo, beginTime, endTime);
//			if(list.size()>1){
//				overDueOneDay(list, indexMap, month, returnCodeDic);
////				//时间区间+
////				indexMap.put(month+"_dk", 0);
////				calculateOverDue(list, indexMap, "dk", returnCodeDic);//贷款类机构
////				indexMap.put(month+"xj", 0);
////				calculateOverDue(list, indexMap, "xj", returnCodeDic);//消费金融机构
////				indexMap.put(month+"yh", 0);
////				calculateOverDue(list, indexMap, "yh", returnCodeDic);//银行类机构
////				indexMap.put(month+"xd", 0);
////				calculateOverDue(list, indexMap, "xd", returnCodeDic);//小贷贷款类机构
//			}
//		}
//		for(Map.Entry<String,String> entry : indexMap.entrySet()){
//			System.out.println("p:"+entry.getKey()+",v:"+entry.getValue());
//		}
//
//	}
//	
//	/**
//	 * 核心指标:逾期一天以上
//	 * @param list
//	 * @param indexMap
//	 * @param orgType
//	 * @param mount
//	 * @param returnCodeDic
//	 */
//	public void overDueOneDay(List<TradeDetailDO> list,Map<String,String> indexMap,int month, Map<String, String[]> returnCodeDic){
//		
//		if(month==3){
//			/*******************************   逾期一天以上次数     ***************************************/
//			indexMap.put("YQ013", loanOrgOverDueOneDay(list,"dk",returnCodeDic));
//			indexMap.put("YQ014", loanOrgOverDueOneDay(list,"xj",returnCodeDic));
//			indexMap.put("YQ015", loanOrgOverDueOneDay(list,"yh",returnCodeDic));
//			indexMap.put("YQ016", loanOrgOverDueOneDay(list,"xd",returnCodeDic));
//			/*******************************   逾期机构数     ***************************************/
//			indexMap.put("YQ017", overDueOrgCount(list, "dk", returnCodeDic));
//			indexMap.put("YQ018", overDueOrgCount(list, "xj", returnCodeDic));
//			indexMap.put("YQ019", overDueOrgCount(list, "yh", returnCodeDic));
//			indexMap.put("YQ020", overDueOrgCount(list, "xd", returnCodeDic));
//			/*******************************   逾期天数总和     ***************************************/
//			indexMap.put("YQ027", overDueDaysSum(list,  returnCodeDic));
//			/*******************************   逾期金额总和     ***************************************/
//			indexMap.put("YQ033", overDueTotalMoneySum(list, returnCodeDic, "1d"));
//			indexMap.put("YQ032", overDueTotalMoneySum(list, returnCodeDic, "7d"));
//			indexMap.put("YQ031", overDueTotalMoneySum(list, returnCodeDic, "30d"));
//			
//		}else if(month==6){
//			/*******************************   逾期一天以上次数     ***************************************/
//			indexMap.put("YQ001", loanOrgOverDueOneDay(list,"dk",returnCodeDic));
//			indexMap.put("YQ002", loanOrgOverDueOneDay(list,"xj",returnCodeDic));
//			indexMap.put("YQ003", loanOrgOverDueOneDay(list,"yh",returnCodeDic));
//			indexMap.put("YQ004", loanOrgOverDueOneDay(list,"xd",returnCodeDic));
//			/*******************************   逾期机构数     ***************************************/
//			indexMap.put("YQ009", overDueOrgCount(list, "dk", returnCodeDic));
//			indexMap.put("YQ010", overDueOrgCount(list, "xj", returnCodeDic));
//			indexMap.put("YQ011", overDueOrgCount(list, "yh", returnCodeDic));
//			
//			/*******************************   逾期天数总和     ***************************************/
//			
//			indexMap.put("YQ026", overDueDaysSum(list,  returnCodeDic));
//			
//			/*******************************   逾期金额总和     ***************************************/
//			indexMap.put("YQ030", overDueTotalMoneySum(list, returnCodeDic, "1d"));
//			indexMap.put("YQ029", overDueTotalMoneySum(list, returnCodeDic, "7d"));
//			indexMap.put("YQ028", overDueTotalMoneySum(list, returnCodeDic, "30d"));
//		}else if(month==12){
//			/*******************************   逾期一天以上次数     ***************************************/
//			indexMap.put("YQ038", loanOrgOverDueOneDay(list,"dk",returnCodeDic));
//			indexMap.put("YQ039", loanOrgOverDueOneDay(list,"xj",returnCodeDic));
//			indexMap.put("YQ040", loanOrgOverDueOneDay(list,"yh",returnCodeDic));
//			indexMap.put("YQ041", loanOrgOverDueOneDay(list,"xd",returnCodeDic));
//			/*******************************   逾期机构数     ***************************************/
//			indexMap.put("YQ034", overDueOrgCount(list, "dk", returnCodeDic));
//			indexMap.put("YQ035", overDueOrgCount(list, "xj", returnCodeDic));
//			indexMap.put("YQ036", overDueOrgCount(list, "yh", returnCodeDic));
//			indexMap.put("YQ037", overDueOrgCount(list, "xd", returnCodeDic));
//			/******************************* 12个月 最大逾期次数     ***************************************/
//			indexMap.put("YQ042", everyOrgOverDueMaxTimes(list, "dk", returnCodeDic));
//			indexMap.put("YQ043", everyOrgOverDueMaxTimes(list, "xj", returnCodeDic));
//			indexMap.put("YQ044", everyOrgOverDueMaxTimes(list, "yh", returnCodeDic));
//			indexMap.put("YQ045", everyOrgOverDueMaxTimes(list, "xd", returnCodeDic));
//		}
//	}
//	
//	/**
//	 * 逾期金额总和 : 近x个月逾期x天以上金额总和
//	 * 逾期类型3  3.在同一家公司划扣因余额不足失败，直至划扣成功为止
//	 * @param list
//	 * @param indexMap
//	 * @param orgType
//	 * @param returnCodeDic
//	 */
//	public String overDueTotalMoneySum(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic,String days){
////		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();//商户类型归属分类字典
////		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//具体机构类
//		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));//因余额不足失败
////		List<String> success = Arrays.asList(returnCodeDic.get("success"));//划扣成功
//		//逾期天数值
//		BigDecimal overDueSumMoney = null;
//		//逾期日期值
//		String overDueBeginDate = null ;
//		//定义一个用户的银行卡在不同机构下拥有的消费记录集合
//		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
//		List<TradeDetailDO> records = null;
//		for (TradeDetailDO o : list) {
//			String merId = o.getMerId();//银行卡
//			//非指定机构不参与逾期统计
////			if(!orgTypeList.contains(o.getMerType()))continue;
//			if(!map.containsKey(merId)){
//				records = new ArrayList<TradeDetailDO>();	
//				records.add(o);
//			}else{
//				records = map.get(merId);
//				records.add(o);
//			}
//			map.put(merId, records);
//		} 
//		//排序和计算逾期
//		for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
//			List<TradeDetailDO> cardNolist = entry.getValue();
//			if(cardNolist.size()<=1)continue;//如果记录小于等于一条就不参与逾期统计
//			//对集合按照日期进行排序
//			Collections.sort(cardNolist);
//			//逾期天数值
//			for (TradeDetailDO o : cardNolist) {
//				//余额不足,划扣失败
//				if(ywbzLst.contains(o.getReturnCode())){
//					if(!StringUtils.isEmpty(overDueBeginDate))continue;//标记第一次划扣失败时间
//					//逾期失败日期
//					overDueBeginDate = o.getTxtDate();
//					overDueSumMoney = o.getAmout();
//					continue;
//				}else if("0000".contains(o.getReturnCode())){
//					if(StringUtils.isEmpty(overDueBeginDate))continue;//标记第一次划扣失败时间
//					//逾期天数
//					Date date1 = DateUtils.yyyyMMddToDate(overDueBeginDate);
//					Date date2 = DateUtils.yyyyMMddToDate(o.getTxtDate());
//					int overDueBeginDayTemp = DateUtils.differentDaysByMillisecond(date1, date2);
//					//逾期days天以上
//					if(overDueBeginDayTemp>initProperties.getOverDueDayDic().get(days)){
//						overDueSumMoney = overDueSumMoney.add(o.getAmout());
//						overDueBeginDate = null;
//					}
//				}
//			}
//			//还原标记第一次划扣失败时间
//			if(!StringUtils.isEmpty(overDueBeginDate)){
//				overDueBeginDate = null;
//			}
//		}
//		return overDueSumMoney.toString();
//	}
//	
//	
//	/**
//	 * 逾期天数总和 : 近x个月逾期x天以上天数总和
//	 * 逾期类型3  3.在同一家公司划扣因余额不足失败，直至划扣成功为止
//	 * @param list
//	 * @param indexMap
//	 * @param orgType
//	 * @param returnCodeDic
//	 */
//	public String overDueDaysSum(List<TradeDetailDO> list, Map<String, String[]> returnCodeDic){
////		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();//商户类型归属分类字典
////		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//具体机构类
//		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));//因余额不足失败
////		List<String> success = Arrays.asList(returnCodeDic.get("success"));//划扣成功
//		//逾期天数值
//		int overDueOneDays = 0;
//		//逾期日期值
//		String overDueBeginDate = null ;
//		//定义一个用户的银行卡在不同机构下拥有的消费记录集合
//		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
//		List<TradeDetailDO> records = null;
//		for (TradeDetailDO o : list) {
//			String merId = o.getMerId();//银行卡
//			//非指定机构不参与逾期统计
////			if(!orgTypeList.contains(o.getMerType()))continue;
//			if(!map.containsKey(merId)){
//				records = new ArrayList<TradeDetailDO>();	
//				records.add(o);
//			}else{
//				records = map.get(merId);
//				records.add(o);
//			}
//			map.put(merId, records);
//		}
//		//排序和计算逾期
//		for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
//			List<TradeDetailDO> cardNolist = entry.getValue();
//			if(cardNolist.size()<=1)continue;//如果记录小于等于一条就不参与逾期统计
//			//对集合按照日期进行排序
//			Collections.sort(cardNolist);
//			//逾期天数值
//			for (TradeDetailDO o : cardNolist) {
//				//余额不足,划扣失败
//				if(ywbzLst.contains(o.getReturnCode())){
//					if(!StringUtils.isEmpty(overDueBeginDate))continue;//标记第一次划扣失败时间
//					//逾期失败日期
//					overDueBeginDate = o.getTxtDate();
//					continue;
//				}else if("0000".contains(o.getReturnCode())){
//					if(StringUtils.isEmpty(overDueBeginDate))continue;//标记第一次划扣失败时间
//					//逾期天数
//					Date date1 = DateUtils.yyyyMMddToDate(overDueBeginDate);
//					Date date2 = DateUtils.yyyyMMddToDate(o.getTxtDate());
//					int overDueBeginDayTemp = DateUtils.differentDaysByMillisecond(date1, date2);
//					//逾期一天以上
//					if(overDueBeginDayTemp>initProperties.getOverDueDayDic().get("1d")){
//						overDueOneDays = overDueBeginDayTemp +overDueOneDays;
//						overDueBeginDate = null;
//					}
//				}
//			}
//			//还原标记第一次划扣失败时间
//			if(!StringUtils.isEmpty(overDueBeginDate)){
//				overDueBeginDate = null;
//			}
//		}
//		return String.valueOf(overDueOneDays);
//	}
//	/**
//	 * 近12个月在xx机构逾期的最大每家机构逾期次数
//	 * 逾期类型3  3.在同一家公司划扣因余额不足失败，直至划扣成功为止
//	 * @param list
//	 * @param indexMap
//	 * @param orgType
//	 * @param returnCodeDic
//	 */
//	public String everyOrgOverDueMaxTimes(List<TradeDetailDO> list,String orgType , Map<String, String[]> returnCodeDic){
//		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();//商户类型归属分类字典
//		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//具体机构类
//		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));//因余额不足失败
//		List<String> success = Arrays.asList(returnCodeDic.get("success"));//划扣成功
//		//逾期天数值
//		int overDueOneDayTimes = 0;
//		//最大逾期次数值
//		int max = 0;
//		//逾期日期值
//		String overDueBeginDate = null ;
//		//定义一个用户的银行卡在不同机构下拥有的消费记录集合
//		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
//		List<TradeDetailDO> records = null;
//		for (TradeDetailDO o : list) {
//			String merId = o.getMerId();//银行卡
//			//非指定机构不参与逾期统计
//			if(!orgTypeList.contains(o.getMerType()))continue;
//			if(!map.containsKey(merId)){
//				records = new ArrayList<TradeDetailDO>();	
//				records.add(o);
//			}else{
//				records = map.get(merId);
//				records.add(o);
//			}
//			map.put(merId, records);
//		}
//		//排序和计算逾期
//		for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
//			List<TradeDetailDO> cardNolist = entry.getValue();
//			if(cardNolist.size()<=1)continue;//如果记录小于等于一条就不参与逾期统计
//			//对集合按照日期进行排序
//			Collections.sort(cardNolist);
//			//逾期天数值
//			for (TradeDetailDO o : cardNolist) {
//				//余额不足,划扣失败
//				if(ywbzLst.contains(o.getReturnCode())){
//					if(!StringUtils.isEmpty(overDueBeginDate))continue;//标记第一次划扣失败时间
//					//逾期失败日期
//					overDueBeginDate = o.getTxtDate();
//					continue;
//				}else if("0000".contains(o.getReturnCode())){
//					if(StringUtils.isEmpty(overDueBeginDate))continue;//标记第一次划扣失败时间
//					//逾期天数
//					Date date1 = DateUtils.yyyyMMddToDate(overDueBeginDate);
//					Date date2 = DateUtils.yyyyMMddToDate(o.getTxtDate());
//					int overDueBeginDayTemp = DateUtils.differentDaysByMillisecond(date1, date2);
//					//逾期一天以上
//					if(overDueBeginDayTemp>initProperties.getOverDueDayDic().get("1d")){
//						overDueOneDayTimes = overDueOneDayTimes +1;
//						overDueBeginDate = null;
//					}
//				}
//			}
//			//比较当前逾期的次数和最大逾期次数值比较
//			if(max<=overDueOneDayTimes){
//				max = overDueOneDayTimes;
//			}
//			//还原标记第一次划扣失败时间
//			if(!StringUtils.isEmpty(overDueBeginDate)){
//				overDueBeginDate = null;
//			}
//		}
//		return String.valueOf(max);
//	}
//	
//	/**
//	 * 在贷款类机构逾期1天以上次数
//	 * 逾期类型3  3.在同一家公司划扣因余额不足失败，直至划扣成功为止
//	 * @param list
//	 * @param indexMap
//	 * @param orgType
//	 * @param returnCodeDic
//	 */
//	public String loanOrgOverDueOneDay(List<TradeDetailDO> list,String orgType , Map<String, String[]> returnCodeDic){
//		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();//商户类型归属分类字典
//		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//具体机构类
//		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));//因余额不足失败
////		List<String> success = Arrays.asList(returnCodeDic.get("success"));//划扣成功
//		//逾期天数值
//		int overDueOneDayTimes = 0;
//		//逾期日期值
//		String overDueBeginDate = null ;
//		//定义一个用户的银行卡在不同机构下拥有的消费记录集合
//		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
//		List<TradeDetailDO> records = null;
//		for (TradeDetailDO o : list) {
//			String merId = o.getMerId();//银行卡
//			//非指定机构不参与逾期统计
//			if(!orgTypeList.contains(o.getMerType()))continue;
//			if(!map.containsKey(merId)){
//				records = new ArrayList<TradeDetailDO>();	
//				records.add(o);
//			}else{
//				records = map.get(merId);
//				records.add(o);
//			}
//			map.put(merId, records);
//		}
//		//排序和计算逾期
//		for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
//			List<TradeDetailDO> cardNolist = entry.getValue();
//			if(cardNolist.size()<=1)continue;//如果记录小于等于一条就不参与逾期统计
//			//对集合按照日期进行排序
//			Collections.sort(cardNolist);
//			//逾期天数值
//			for (TradeDetailDO o : cardNolist) {
//				//余额不足,划扣失败
//				if(ywbzLst.contains(o.getReturnCode())){
//					if(!StringUtils.isEmpty(overDueBeginDate))continue;//标记第一次划扣失败时间
//					//逾期失败日期
//					overDueBeginDate = o.getTxtDate();
//					continue;
//				}else if("0000".contains(o.getReturnCode())){
//					if(StringUtils.isEmpty(overDueBeginDate))continue;//标记第一次划扣失败时间
//					//逾期天数
//					Date date1 = DateUtils.yyyyMMddToDate(overDueBeginDate);
//					Date date2 = DateUtils.yyyyMMddToDate(o.getTxtDate());
//					int overDueBeginDayTemp = DateUtils.differentDaysByMillisecond(date1, date2);
//					//逾期一天以上
//					if(overDueBeginDayTemp>initProperties.getOverDueDayDic().get("1d")){
//						overDueOneDayTimes = overDueOneDayTimes +1;
//					}
//				}
//			}
//			//还原标记第一次划扣失败时间
//			if(!StringUtils.isEmpty(overDueBeginDate)){
//				overDueBeginDate = null;
//			}
//		}
//		return String.valueOf(overDueOneDayTimes);
//	}
//	
//	
//	/**
//	 * 逾期机构数
//	 * 逾期类型3  3.在同一家公司划扣因余额不足失败，可视为一次在该机构下的逾期
//	 * @param list
//	 * @param indexMap
//	 * @param orgType
//	 * @param returnCodeDic
//	 */
//	public String overDueOrgCount(List<TradeDetailDO> list,String orgType , Map<String, String[]> returnCodeDic){
//		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();//商户类型归属分类字典
//		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//具体机构类
//		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));//因余额不足失败
////		List<String> success = Arrays.asList(returnCodeDic.get("success"));//划扣成功
//		//逾期天数值
//		int overDueOneOrgCount = 0;
//		//定义一个用户的银行卡在不同机构下拥有的消费记录集合
//		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
//		List<TradeDetailDO> records = null;
//		for (TradeDetailDO o : list) {
//			String merId = o.getMerId();//银行卡
//			//非指定机构不参与逾期统计
//			if(!orgTypeList.contains(o.getMerType()))continue;
//			if(!map.containsKey(merId)){
//				records = new ArrayList<TradeDetailDO>();	
//				records.add(o);
//			}else{
//				records = map.get(merId);
//				records.add(o);
//			}
//			map.put(merId, records);
//		}
//		//排序和计算逾期
//		for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
//			List<TradeDetailDO> cardNolist = entry.getValue();
//			for (TradeDetailDO o : cardNolist) {
//				//余额不足,划扣失败,看做该机构下有逾期，逾期机构数加1
//				if(ywbzLst.contains(o.getReturnCode())){
//					overDueOneOrgCount++;
//					//跳出当前循环，继续往下寻找
//					break;
//				}
//			}
//		}
//		return String.valueOf(overDueOneOrgCount);
//	}
//	
//	/**
//	 * 在消费金融机构逾期1天以上次数
//	 * @param list
//	 * @param indexMap
//	 * @param orgType
//	 * @param returnCodeDic
//	 */
//	public void consumerFinanceOrgOverDueOneDay(List<TradeDetailDO> list,Map<String,Integer> indexMap ,String orgType , Map<String, String[]> returnCodeDic){
//		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
//		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//机构类型
//		String extKey = "_"+orgType;
////		int days = initProperties.getOverDueDayDic().get("1d");
//		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
//		List<String> success = Arrays.asList(returnCodeDic.get("success"));
//		//定义一个用户在不同机构下拥有的消费记录集合
//		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
//		List<TradeDetailDO> records = null;
//		
//		for (TradeDetailDO o : list) {
//			String cordNo = o.getCardNo()+extKey;
//			//非指定机构不参与逾期统计
//			if(!orgTypeList.contains(o.getMerType()))continue;
//			if(!map.containsKey(cordNo)){
//				records = new ArrayList<TradeDetailDO>();	
//				records.add(o);
//			}else{
//				records = map.get(cordNo);
//				records.add(o);
//			}
//			map.put(cordNo, records);
//		}
//		//key orgKey,value:逾期次数（不同机构的逾期次数）
//		Map<String,Integer> averageOrgOverDue = new HashMap<String,Integer>();
//		//key orgKey,value:逾期天数（不同机构的逾期天数）
//		Map<String,Integer> averageOrgOverDay = new HashMap<String,Integer>();
//		//排序和计算逾期
//		for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
//			String cardNo = entry.getKey();
//			List<TradeDetailDO> cardNolist = entry.getValue();
//			if(cardNolist.size()<=1)continue;
//			//对集合按照日期进行排序
//			Collections.sort(cardNolist);
//			double amout = 0;
//			//逾期日期值
//			int overDueBeginDate = 0 ;
//			//逾期天数值
//			int overDueBeginDay = 0;
//			for (TradeDetailDO o : cardNolist) {
//				if(cardNolist.size()<=1)continue;
//				//余额不足,划扣失败
//				if(ywbzLst.contains(o.getReturnCode())){
//					//记录失败金额
//					amout = o.getAmout().doubleValue();
//					//逾期失败日期
//					overDueBeginDate = Integer.valueOf(o.getTxtDate());
//					continue;
//				}else if(success.contains(o.getReturnCode())){
//					String merId = o.getMerId()+extKey;//机构平均值主键
//					if(!averageOrgOverDue.containsKey(merId)){
//						int i = averageOrgOverDue.get(merId);
//						//逾期次数
//						averageOrgOverDue.put(merId, ++i);
//					}else{
//						averageOrgOverDue.put(merId, 1);
//					}
//					//划扣成功,最终划扣成功且划扣成功金额=失败金额
//					if(amout==o.getAmout().doubleValue()){
//						//计算逾期天数
//						int overDueBeginDayTemp = Integer.valueOf(o.getTxtDate())-overDueBeginDate;
//						averageOrgOverDay.put(merId, overDueBeginDayTemp+ overDueBeginDay);
//					}
//				}
//			}
//		}
//		
//		//逾期天数计算
//		for(Map.Entry<String,Integer> entry : averageOrgOverDay.entrySet()){
//			
//		}
//		//3333
//	}
//	
//	/**
//	 * 在银行类机构逾期1天以上次数
//	 * @param list
//	 * @param indexMap
//	 * @param orgType
//	 * @param returnCodeDic
//	 */
//	public void bankOrgOverDueOneDay(List<TradeDetailDO> list,Map<String,Integer> indexMap ,String orgType , Map<String, String[]> returnCodeDic){
//		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
//		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//机构类型
//		String extKey = "_"+orgType;
////		int days = initProperties.getOverDueDayDic().get("1d");
//		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
//		List<String> success = Arrays.asList(returnCodeDic.get("success"));
//		//定义一个用户在不同机构下拥有的消费记录集合
//		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
//		List<TradeDetailDO> records = null;
//		
//		for (TradeDetailDO o : list) {
//			String cordNo = o.getCardNo()+extKey;
//			//非指定机构不参与逾期统计
//			if(!orgTypeList.contains(o.getMerType()))continue;
//			if(!map.containsKey(cordNo)){
//				records = new ArrayList<TradeDetailDO>();	
//				records.add(o);
//			}else{
//				records = map.get(cordNo);
//				records.add(o);
//			}
//			map.put(cordNo, records);
//		}
//		//key orgKey,value:逾期次数（不同机构的逾期次数）
//		Map<String,Integer> averageOrgOverDue = new HashMap<String,Integer>();
//		//key orgKey,value:逾期天数（不同机构的逾期天数）
//		Map<String,Integer> averageOrgOverDay = new HashMap<String,Integer>();
//		//排序和计算逾期
//		for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
//			String cardNo = entry.getKey();
//			List<TradeDetailDO> cardNolist = entry.getValue();
//			if(cardNolist.size()<=1)continue;
//			//对集合按照日期进行排序
//			Collections.sort(cardNolist);
//			double amout = 0;
//			//逾期日期值
//			int overDueBeginDate = 0 ;
//			//逾期天数值
//			int overDueBeginDay = 0;
//			for (TradeDetailDO o : cardNolist) {
//				if(cardNolist.size()<=1)continue;
//				//余额不足,划扣失败
//				if(ywbzLst.contains(o.getReturnCode())){
//					//记录失败金额
//					amout = o.getAmout().doubleValue();
//					//逾期失败日期
//					overDueBeginDate = Integer.valueOf(o.getTxtDate());
//					continue;
//				}else if(success.contains(o.getReturnCode())){
//					String merId = o.getMerId()+extKey;//机构平均值主键
//					if(!averageOrgOverDue.containsKey(merId)){
//						int i = averageOrgOverDue.get(merId);
//						//逾期次数
//						averageOrgOverDue.put(merId, ++i);
//					}else{
//						averageOrgOverDue.put(merId, 1);
//					}
//					//划扣成功,最终划扣成功且划扣成功金额=失败金额
//					if(amout==o.getAmout().doubleValue()){
//						//计算逾期天数
//						int overDueBeginDayTemp = Integer.valueOf(o.getTxtDate())-overDueBeginDate;
//						averageOrgOverDay.put(merId, overDueBeginDayTemp+ overDueBeginDay);
//					}
//				}
//			}
//		}
//		
//		//逾期天数计算
//		for(Map.Entry<String,Integer> entry : averageOrgOverDay.entrySet()){
//			
//		}
//		//3333
//	}
//	
//	
//	/**
//	 * 在小贷款类机构逾期1天以上次数
//	 * @param list
//	 * @param indexMap
//	 * @param orgType
//	 * @param returnCodeDic
//	 */
//	public void smallLoanOrgOverDueOneDay(List<TradeDetailDO> list,Map<String,Integer> indexMap ,String orgType , Map<String, String[]> returnCodeDic){
//		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
//		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//机构类型
//		String extKey = "_"+orgType;
////		int days = initProperties.getOverDueDayDic().get("1d");
//		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
//		List<String> success = Arrays.asList(returnCodeDic.get("success"));
//		//定义一个用户在不同机构下拥有的消费记录集合
//		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
//		List<TradeDetailDO> records = null;
//		
//		for (TradeDetailDO o : list) {
//			String cordNo = o.getCardNo()+extKey;
//			//非指定机构不参与逾期统计
//			if(!orgTypeList.contains(o.getMerType()))continue;
//			if(!map.containsKey(cordNo)){
//				records = new ArrayList<TradeDetailDO>();	
//				records.add(o);
//			}else{
//				records = map.get(cordNo);
//				records.add(o);
//			}
//			map.put(cordNo, records);
//		}
//		//key orgKey,value:逾期次数（不同机构的逾期次数）
//		Map<String,Integer> averageOrgOverDue = new HashMap<String,Integer>();
//		//key orgKey,value:逾期天数（不同机构的逾期天数）
//		Map<String,Integer> averageOrgOverDay = new HashMap<String,Integer>();
//		//排序和计算逾期
//		for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
//			String cardNo = entry.getKey();
//			List<TradeDetailDO> cardNolist = entry.getValue();
//			if(cardNolist.size()<=1)continue;
//			//对集合按照日期进行排序
//			Collections.sort(cardNolist);
//			double amout = 0;
//			//逾期日期值
//			int overDueBeginDate = 0 ;
//			//逾期天数值
//			int overDueBeginDay = 0;
//			for (TradeDetailDO o : cardNolist) {
//				if(cardNolist.size()<=1)continue;
//				//余额不足,划扣失败
//				if(ywbzLst.contains(o.getReturnCode())){
//					//记录失败金额
//					amout = o.getAmout().doubleValue();
//					//逾期失败日期
//					overDueBeginDate = Integer.valueOf(o.getTxtDate());
//					continue;
//				}else if(success.contains(o.getReturnCode())){
//					String merId = o.getMerId()+extKey;//机构平均值主键
//					if(!averageOrgOverDue.containsKey(merId)){
//						int i = averageOrgOverDue.get(merId);
//						//逾期次数
//						averageOrgOverDue.put(merId, ++i);
//					}else{
//						averageOrgOverDue.put(merId, 1);
//					}
//					//划扣成功,最终划扣成功且划扣成功金额=失败金额
//					if(amout==o.getAmout().doubleValue()){
//						//计算逾期天数
//						int overDueBeginDayTemp = Integer.valueOf(o.getTxtDate())-overDueBeginDate;
//						averageOrgOverDay.put(merId, overDueBeginDayTemp+ overDueBeginDay);
//					}
//				}
//			}
//		}
//		
//		//逾期天数计算
//		for(Map.Entry<String,Integer> entry : averageOrgOverDay.entrySet()){
//			
//		}
//		//3333
//	}
//	/**
//	 * 逾期一天以上计算
//	 * @param list
//	 * @param indexMap
//	 * @param orgType
//	 * @param returnCodeDic
//	 */
//	public void calculateOverDue(List<TradeDetailDO> list,Map<String,Integer> indexMap ,String orgType , Map<String, String[]> returnCodeDic){
//		Map<String, String[]> merTypeDic = initProperties.getMerTypeDic();
//		List<String> orgTypeList = Arrays.asList(merTypeDic.get(orgType));//机构类型
//		String extKey = "_"+orgType;
////		int days = initProperties.getOverDueDayDic().get("1d");
//		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
//		List<String> success = Arrays.asList(returnCodeDic.get("success"));
//		//定义一个用户在不同机构下拥有的消费记录集合
//		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
//		List<TradeDetailDO> records = null;
//		
//		for (TradeDetailDO o : list) {
//			String cordNo = o.getCardNo()+extKey;
//			//非指定机构不参与逾期统计
//			if(!orgTypeList.contains(o.getMerType()))continue;
//			if(!map.containsKey(cordNo)){
//				records = new ArrayList<TradeDetailDO>();	
//				records.add(o);
//			}else{
//				records = map.get(cordNo);
//				records.add(o);
//			}
//			map.put(cordNo, records);
//		}
//		//key orgKey,value:逾期次数（不同机构的逾期次数）
//		Map<String,Integer> averageOrgOverDue = new HashMap<String,Integer>();
//		//key orgKey,value:逾期天数（不同机构的逾期天数）
//		Map<String,Integer> averageOrgOverDay = new HashMap<String,Integer>();
//		//排序和计算逾期
//		for (Map.Entry<String,List<TradeDetailDO>> entry : map.entrySet()) {
//			String cardNo = entry.getKey();
//			List<TradeDetailDO> cardNolist = entry.getValue();
//			if(cardNolist.size()<=1)continue;
//			//对集合按照日期进行排序
//			Collections.sort(cardNolist);
//			double amout = 0;
//			//逾期日期值
//			int overDueBeginDate = 0 ;
//			//逾期天数值
//			int overDueBeginDay = 0;
//			for (TradeDetailDO o : cardNolist) {
//				if(cardNolist.size()<=1)continue;
//				//余额不足,划扣失败
//				if(ywbzLst.contains(o.getReturnCode())){
//					//记录失败金额
//					amout = o.getAmout().doubleValue();
//					//逾期失败日期
//					overDueBeginDate = Integer.valueOf(o.getTxtDate());
//					continue;
//				}else if(success.contains(o.getReturnCode())){
//					String merId = o.getMerId()+extKey;//机构平均值主键
//					if(!averageOrgOverDue.containsKey(merId)){
//						int i = averageOrgOverDue.get(merId);
//						//逾期次数
//						averageOrgOverDue.put(merId, ++i);
//					}else{
//						averageOrgOverDue.put(merId, 1);
//					}
//					//划扣成功,最终划扣成功且划扣成功金额=失败金额
//					if(amout==o.getAmout().doubleValue()){
//						//计算逾期天数
//						int overDueBeginDayTemp = Integer.valueOf(o.getTxtDate())-overDueBeginDate;
//						averageOrgOverDay.put(merId, overDueBeginDayTemp+ overDueBeginDay);
//					}
//				}
//			}
//		}
//		
//		//逾期天数计算
//		for(Map.Entry<String,Integer> entry : averageOrgOverDay.entrySet()){
//			
//		}
//		//3333
//	}
//}
