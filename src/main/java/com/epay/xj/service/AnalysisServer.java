package com.epay.xj.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.epay.xj.common.BigFileReader;
import com.epay.xj.common.IHandle;
import com.epay.xj.dao.ICertDao;
import com.epay.xj.dao.ITradeDetailDao;
import com.epay.xj.domain.CertNo;
import com.epay.xj.domain.TradeDetail;
import com.epay.xj.properties.InitProperties;

@Service
public class AnalysisServer implements IAnalysisServer{

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private InitProperties initProperties;
	
	@Resource
	private ICertDao certDao;
	
	@Resource
	private ITradeDetailDao tradeDetailDao;
//	
//	@PersistenceContext
//	private EntityManager em;
	public static Set<String> readDataFromFile(String file) {
		BigFileReader.Builder builder = new BigFileReader.Builder(file, new IHandle() {
			@Override
			public void handle(String line) {
				String[] lines = line.split("-");
				String idNo = lines[0];
				System.out.println(lines);
			}
		});
//		Runtime.getRuntime().availableProcessors()
		builder.withTreahdSize(Runtime.getRuntime().availableProcessors()*3-1).withCharset("utf-8")
				.withBufferSize(1024*1024 * 1024); // 设置读取缓冲区大小
		BigFileReader bigFileReader = builder.build();
		bigFileReader.start();
		return null;
	}
	
	public  void readLineAndSave(String fp){
		saveTradeDetailFromTxtFile(fp);
		
//		BufferedWriter out = null;
//		try {
////			String filePath = "F:/yhfw/data/a.txt";
//			String outPath = "E:/yhfw/data/cc.txt";//F:/yhfw/data/a.txt
//			File file = new File(fp);
//			
//			BufferedReader br = new BufferedReader(new FileReader(file));
//			String line = null;
//			try {
//				Set<String> set = new HashSet<>();
//				int i = 0;
//				while((line = br.readLine())!=null){
//					
////					i = StrictMath.max(i, line.getBytes().length);
////					logger.info(line);
//					System.out.println(line);
////					String[] lines = line.split("-");
////					System.out.println(lines[0]);
////					System.out.println(lines[1]);
////					Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
////					Matcher m = p.matcher(lines[1]);
////					out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPath, true)));
////					while(m.find()){
////						String str =m.group().substring(1, m.group().length()-1);
////						System.out.println(m.group().substring(1, m.group().length()-1));
////						if(str.trim().length()>0){
////							out.write(str+"\r\n");
////						}
////						list.add(m.group().substring(1, m.group().length()-1));
////					}
////					String[] tmp = lines[1].split("]]");
////					int ii = tmp.length;
////					logger.info(lines[1]);
//					
////					CertNo cn = new CertNo();
////					String idNo = lines[0];
////					System.out.println(idNo);
////					cn.setId(getUUID32());
////					cn.setIdNo(idNo);
////					insert(cn);
////					em.persist(cn);
////					em.flush();
//				}
//				System.out.println("----------------set.size()------------:"+set.size());
//				br.close();
//				System.out.println(set.size());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
	}
	
	public void saveTradeDetailFromTxtFile(String fp){
		File file = new File(fp);
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			try {
				while((line=br.readLine())!=null){
					line = line.replace("[", "").replace("]", "").replace("\"", "");
					System.out.println(line);
					TradeDetail td;
					try {
						Object[] v = line.split(",");
						if(v.length!=9){
							continue;
						}
						String txnDate = (String) v[0];
						if(txnDate.length()!=13){
							continue;
						}
						td = new TradeDetail();
						td.setTxnSeqId((String)v[1]);
						String emp = (String) v[3];
						td.setCardNo(DigestUtils.md5DigestAsHex(emp.getBytes()));

						td.setAmount(new BigDecimal(Double.valueOf((String)v[6])).setScale(2, BigDecimal.ROUND_UP));
						System.out.println(td.getAmount().toString());
						td.setTxnDate(new Timestamp(Long.parseLong(txnDate)));
						td.setMerType(Integer.valueOf((String)v[5]));
						td.setCertNo((String) v[2]);
						td.setReturnCode((String) v[8]);
						td.setSfType((String) v[7]);
						td.setMerId((String) v[4]);
						insert(td);
					} catch (NumberFormatException e) {
						System.out.println(line);
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
	}
	public static String getUUID32(){
	    String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
	    return uuid;
	}

	public void getList() {
		List<CertNo> list = certDao.findAll();
//		String sql = "select * from cert_no";
//		List<CertNo> list = em.createNativeQuery(sql, CertNo.class).getResultList();
		System.out.println(list.size());
	}

    public static void method3(String fileName, String content) {   
        try {   
            // 打开一个随机访问文件流，按读写方式   
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");   
            // 文件长度，字节数   
            long fileLength = randomFile.length();   
            // 将写文件指针移到文件尾。   
            randomFile.seek(fileLength);  
//           String s2=new String(content.getBytes("u"),"iso8859-1"); 
            randomFile.writeBytes("\r\n");
            randomFile.close();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
    } 
    
	public static void main(String[] args) {
		String filePath = "E:/yhfw/data/cc.txt";//F:/yhfw/data/a.txt
		String outPath = "E:/yhfw/data/bb.txt";//F:/yhfw/data/a.txt
		
//		method3(filePath, "");
		AnalysisServer as = new AnalysisServer();
//		File file = new File(filePath);
//		System.out.println(FileUtils.readFileLines(file));
		as.readLineAndSave(filePath);
//		File file = new File(filePath);
//		Set<String> set = readDataFromFile(filePath);
	}

	@Override
	public void insert(CertNo o) {
		certDao.insert(o);
	}

	@Override
	public void insert(TradeDetail o) {
		tradeDetailDao.insert(o);
	}
}
