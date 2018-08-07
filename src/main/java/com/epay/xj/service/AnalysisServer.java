package com.epay.xj.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epay.xj.common.BigFileReader;
import com.epay.xj.common.IHandle;
import com.epay.xj.dao.ICertDao;
import com.epay.xj.domain.CertNo;
import com.epay.xj.properties.InitProperties;

@Service
@Transactional
public class AnalysisServer implements IAnalysisServer{

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private InitProperties initProperties;
	
	@Resource
	private ICertDao certDao;
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
	
	@Transactional
	public  void readLineAndSave(String fp){
		BufferedWriter out = null;
		try {
//			String filePath = "F:/yhfw/data/a.txt";
			String outPath = "F:/yhfw/data/cc.txt";//F:/yhfw/data/a.txt
			File file = new File(fp);
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			try {
				Set<String> set = new HashSet<>();
				int i = 0;
				while((line = br.readLine())!=null){
					i++;
					System.out.println(i);
					
//					out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPath, true)));
//					out.write(line);
//					if(i==1000000){
////						String[] lines = line.split(",");
////						System.out.println(lines[1]);
////						set.add(lines[0]);
//						break;
//					}
//					CertNo cn = new CertNo();
//					String idNo = lines[0];
//					System.out.println(idNo);
//					cn.setId(getUUID32());
//					cn.setIdNo(idNo);
//					insert(cn);
//					em.persist(cn);
//					em.flush();
				}
				br.close();
				System.out.println(set.size());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
		String filePath = "F:/yhfw/data/a.txt";//F:/yhfw/data/a.txt
		String outPath = "F:/yhfw/data/bb.txt";//F:/yhfw/data/a.txt
		
//		method3(filePath, "");
		AnalysisServer as = new AnalysisServer();
//		File file = new File(outPath);
//		System.out.println(FileUtils.readFileLines(file));
		as.readLineAndSave(filePath);
//		File file = new File(filePath);
//		Set<String> set = readDataFromFile(filePath);
	}

	@Override
	public void insert(CertNo o) {
		certDao.insert(o);
	}
}
