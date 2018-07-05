package com.epay.xj.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.epay.xj.common.BigFileReader;
import com.epay.xj.common.IHandle;
import com.epay.xj.domain.TradeDetailDO;
import com.epay.xj.domain.Variables;
import com.epay.xj.properties.InitProperties;
import com.epay.xj.utils.DateUtils;
import com.epay.xj.utils.FileUtils;

@Service
public class DutyService {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private InitProperties initProperties;
	@Autowired
	private CertNoService certNoService;
	@Autowired
	private TradeDetailService tradeDetailService;

	public void readerMerTradeDetailTable() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append(initProperties.getfPathInput()).append(initProperties.getTradeDetail()).append("20180102").append(".DEL");
		BigFileReader.Builder builder = new BigFileReader.Builder(sb.toString(), new IHandle() {
			
			public void handle(String line) {
				try {
					certNoService.updateOrInsertRecord(line.split(","));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				String s = line.split(",")[3];
//				System.out.println(s);
//				set.add(s);
//				log.info(MD5Util.encrypt(s));
//				System.out.println(set.size());
			}
		});
		builder.withTreahdSize(2).withCharset("ISO-8859-1").withBufferSize(1024 * 1024);
		BigFileReader bigFileReader = builder.build();
		bigFileReader.start();
//		File file = new File(sb.toString());
//		CsvReader reader = new CsvReader(new FileReader(file.getAbsolutePath()));
//		String[] headerInfo = initProperties.getInputHeaderMtd().split(",");
//		reader.setHeaders(headerInfo);
//		while (reader.readRecord()) {
//			certNoService.updateOrInsert(reader);
////			logger.info(MD5Util.encrypt(certNo));
//		}
	}
	
	public void appendTradeDetail(String fileName, String[] record) throws IOException{
		StringBuffer sb = new StringBuffer();
		//该目录必须存在
		sb.append(initProperties.getfPathOutput()).append("certNo//");
		//文件不存在，创建目录
		if(!FileUtils.existsDirectory(sb.toString()))FileUtils.forceDirectory(sb.toString());
		sb.append(fileName).append(".DEL");
		File certNoFile = new File(sb.toString());// 指定要写入的文件
		//如果文件不存在，创建文件
		if(!FileUtils.existsFile(certNoFile.getAbsolutePath()))certNoFile.createNewFile();
        // 获取该文件的缓冲输出流  
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(certNoFile.getAbsolutePath(), true), Charset.forName("ISO-8859-1")));
		CsvWriter csvWriter = new CsvWriter(bufferedWriter, ',');
		csvWriter.writeRecord(record);
		bufferedWriter.close();
		csvWriter.close();
	}
	
	
	public void addTradeDetail(String updateTime) throws Exception{
		List<String> list = certNoService.getAllCertNo(updateTime);
		// 每500条数据开启一条线程
        int threadSize = 500;
        // 总数据条数
        int dataSize = list.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % threadSize == 0;
        // 创建一个线程池
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        
        // 定义一个任务集合
        List<Callable<List<Variables>>> tasks = new ArrayList<Callable<List<Variables>>>();
        Callable<List<Variables>> task = null;
        List<String> cutList = null;
        // 确定每条线程的数据
        for (int i = 0; i < threadNum; i++) {
            if (i == threadNum - 1) {
                if (special) {
                    break;
                }
                cutList = list.subList(threadSize * i, dataSize);
            } else {
                cutList = list.subList(threadSize * i, threadSize * (i + 1));
            }
            logger.info("第{}组", i + 1);
            final List<String> listStr = cutList;
            task = new Callable<List<Variables>>() {
                @Override
                public List<Variables> call() throws Exception {
                	List<Variables> vlist = new ArrayList<Variables>();
                	for (int i=0;i<listStr.size();i++) {
                		Variables v = dealSingleCertNoData(listStr.get(i),i);
                		vlist.add(v);
					}
                	logger.info("当前线程名称:{},处理数据：{}", Thread.currentThread().getName(),listStr);
                    return vlist;
                }
            };
            // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
            tasks.add(task);
        }
        List<Future<List<Variables>>> results = exec.invokeAll(tasks);
        for (Future<List<Variables>> future : results) {
        	List<Variables> lst = future.get();
        	for (Variables variables : lst) {
        		System.out.println(variables.toString());
			}
        }
        // 关闭线程池
        exec.shutdown();
	}

	/**
	 * 汇总指定身份号的指标
	 * @param certNo 身份证号
	 * @param i 组合表名
	 * @return
	 * @throws Exception 
	 */
	public Variables dealSingleCertNoData(String certNo, int i) throws Exception {
		Variables v = new Variables();
		v.setCertNo(String.valueOf(new Random().nextInt(10)));
		StringBuffer sb = new StringBuffer();
		//该目录必须存在
		sb.append(initProperties.getfPathOutput()).append("certNo//").append(certNo).append(".DEL");
		File file = new File(sb.toString());
		CsvReader reader = new CsvReader(new FileReader(file.getAbsolutePath()));
		String[] headerInfo = initProperties.getInputHeaderMtd().split(",");
		reader.setHeaders(headerInfo);
		int rows = FileUtils.readFileLines(file);//读取记录数
		List<TradeDetailDO> list = new ArrayList<TradeDetailDO>(rows);//初始化记录数
		while (reader.readRecord()) {
			TradeDetailDO tdd = new TradeDetailDO();
			BigDecimal amout = new BigDecimal(reader.get("AMOUNT"));
			String cardNo = reader.get("CARD_NO");
			String certNoTemp = reader.get("CERT_NO");
//			double amount = decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			tdd.setAmout(amout);
			tdd.setCardNo(cardNo);
			tdd.setCertNo(certNoTemp);
			tdd.setMerId(reader.get("MER_ID"));
			tdd.setMerType(reader.get("MER_TYPE"));
			tdd.setSfType(reader.get("SF_TYPE"));
			tdd.setTxtDate(reader.get("TXN_DATE"));
			tdd.setTxtSeqId(reader.get("TXN_SEQ_ID"));
			tdd.setReturnCode(reader.get("RETURN_CODE"));
			list.add(tdd);
		}
		tradeDetailService.batchInsert(list);
		
		//计算各个指标值
		return v;
	}
	
	
	
	/**----------------------------------------------------------------------------------------------**/
	
	/**
	 * 逾期所有指标计算统一入口
	 * 逾期类型1:1.在同一家公司划扣因余额不足失败，到最终划扣成功且划扣成功金额=失败金额为止，设定为逾期天数；
	 * 逾期类型2:2.在同一家公司划扣因余额不足失败，直至划扣成功金额>=失败金额为止
     * 逾期类型3:3.在同一家公司划扣因余额不足失败，直至划扣成功为止
	 */
	public void overDue(String certNo){
		//逾期口径1
		String overDueType = initProperties.getOverDueType();
		
		if(overDueType.equals("1")){
			calculateOverDuePrepare(certNo, "20180704");
		}else if(overDueType.equals("2")){
			calculateOverDuePrepare(certNo, "20180704");
		}else{
			calculateOverDuePrepare(certNo, "20180704");
		}
	}
	
	/**
	 * 目前暂定为trade_detail_log表中身份证下一年的所有记录，不知道放入内存会不会崩溃
	 * 返回逾期的所有指标计算结果
	 */
	public void calculateOverDuePrepare(String certNo, String endTime ){
		
		Map<String, String[]> returnCodeDic = initProperties.getReturnCodeDic();
		Map<String,Integer> overDueMouth = initProperties.getOverDueMonth();
		//逾期指标结果集
		Map<String,Integer> indexMap = new HashMap<String,Integer>();
		for (int month : overDueMouth.values()) {
			String beginTime = DateUtils.getDateOfXMonthsAgo(endTime, month);
			logger.info("overDueMonth:{},beginTime:{},endTime:{}", month,beginTime,endTime);
			//获取指定时间段某人的交易记录
			List<TradeDetailDO> list = tradeDetailService.getTradeDetailsByCertNo(certNo, beginTime, endTime);
			if(list.size()>1){
				calculateOverDue(list, indexMap, "dk", returnCodeDic);//贷款类机构
				calculateOverDue(list, indexMap, "xj", returnCodeDic);//消费金融机构
				calculateOverDue(list, indexMap, "yh", returnCodeDic);//银行类机构
				calculateOverDue(list, indexMap, "xd", returnCodeDic);//小贷贷款类机构
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
//		int days = initProperties.getOverDueDayDic().get("1d");
		List<String> ywbzLst = Arrays.asList(returnCodeDic.get("yebz"));
		List<String> success = Arrays.asList(returnCodeDic.get("success"));
		//定义一个用户在不同机构下拥有的消费记录集合
		Map<String,List<TradeDetailDO>> map = new HashMap<String,List<TradeDetailDO>>();
		List<TradeDetailDO> records = null;
		
		for (TradeDetailDO o : list) {
			String cordNo = o.getCardNo()+extKey;
			//非指定机构不参与逾期统计
			if(!orgTypeList.contains(o.getMerType()))continue;
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
			Collections.sort(cardNolist);
			double amout = 0;
			//逾期日期值
			int overDueBeginDate = 0 ;
			//逾期天数值
			int overDueBeginDay = 0;
			for (TradeDetailDO o : cardNolist) {
				if(cardNolist.size()<=1)continue;
				//余额不足,划扣失败
				if(ywbzLst.contains(o.getReturnCode())){
					//记录失败金额
					amout = o.getAmout().doubleValue();
					//逾期失败日期
					overDueBeginDate = Integer.valueOf(o.getTxtDate());
					continue;
				}else if(success.contains(o.getReturnCode())){
					String merId = o.getMerId()+extKey;//机构平均值主键
					if(!averageOrgOverDue.containsKey(merId)){
						int i = averageOrgOverDue.get(merId);
						//逾期次数
						averageOrgOverDue.put(merId, ++i);
					}else{
						averageOrgOverDue.put(merId, 1);
					}
					//划扣成功,最终划扣成功且划扣成功金额=失败金额
					if(amout==o.getAmout().doubleValue()){
						//计算逾期天数
						int overDueBeginDayTemp = Integer.valueOf(o.getTxtDate())-overDueBeginDate;
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
