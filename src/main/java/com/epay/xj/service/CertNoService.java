package com.epay.xj.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csvreader.CsvReader;
import com.epay.xj.domain.CertNoDO;
import com.epay.xj.domain.Variables;
import com.epay.xj.repository.CertNoRepository;
import com.epay.xj.utils.DateUtils;
import com.epay.xj.utils.MD5Util;

/**
 * Created by hx on 2017/11/4.
 */
@Service
public class CertNoService {

	Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private CertNoRepository userRepository;

    @Autowired
    private VariablesService variablesService;
    
    @Autowired
    private DutyService dutyService;
    
    @Transactional
    public void insertTwo(){

//        User userA = new User();
//        userA.setGrade("A");
//        userA.setName("aa");
//
//        userRepository.save(userA);
//
//        User userB = new User();
//        userB.setGrade("B");
//        userB.setName("bb");
//
//        userRepository.save(userB);
    }

    public List<String> getAllCertNo(String updateTime){
    	return userRepository.getIds(updateTime);
    }
    /**
     * 根据年龄查找用户信息
     * @param id
     * @throws Exception
     */
    public void getAge(Integer id) throws Exception{
//        User user = userRepository.findOne(id);
//        Integer age = user.getAge();
//        if(age < 21){
//
//            throw new UserException(ResultEnum.COLLEGE_STUDENT);
//
//        }else if(age > 21 && age <25 ){
//
//            throw new UserException(ResultEnum.GRADUATE_STUDENT);
//        }
    }

    public boolean exists(CertNoDO o){
    	Example<CertNoDO> example = Example.of(o);
        return userRepository.exists(example);
    }
    
    /**
     *  第一阶段：
     *  	录入身份证号，
     *  	标识需要参与计算的指标，
     *  	写入该身份证号下的文件或者数据库的表中
     *  	单条记录数操作函数
     * @Title: updateOrInsert 
     * @author yanghf
     * @throws IOException 
     * @Date 2018年7月2日 下午2:48:06
     */
    @Transactional
    public void updateOrInsert(CsvReader reader) throws Exception{
    	//参与字表计算的数据
    	String certNo = reader.get(reader.getHeaders()[2]);
    	CertNoDO c = new CertNoDO();
    	c.setId(MD5Util.encrypt(certNo));
    	//参与指标计算标识，以供参与计算
    	c.setUpdateTime(DateUtils.yyyyMMddToString(new Date()));
    	//如果库中存在
    	if(exists(c)){
    		//更新记录
    		userRepository.update(c.getUpdateTime(), c.getId());
    		//写入数据写入该身份证号下的追加文件或者数据库的表中
    		dutyService.appendTradeDetail(c.getId(), reader.getValues());
    	}else{
    		//新增的身份证号下的日志记录，保存到身份证号表中
    		c.setCertNo(certNo);
    		userRepository.saveAndFlush(c);
    		//将数据写入该身份证号下的文件或者数据库的表中
    		dutyService.appendTradeDetail(c.getId(), reader.getValues());
    		//在指标表中新增记录
    		Variables v = new Variables();
    		v.setCertNo(certNo);
    		v.setMd5CertNo(c.getId());	
    		variablesService.insert(v);
    	}
    }
    
    
    /**
     *  第一阶段：
     *  	录入身份证号，
     *  	标识需要参与计算的指标，
     *  	写入该身份证号下的文件或者数据库的表中
     *  	单条记录数操作函数
     * @Title: updateOrInsert 
     * @author yanghf
     * @throws IOException 
     * @Date 2018年7月2日 下午2:48:06
     */
    @Transactional
    public void updateOrInsertRecord(String[] records) throws Exception{
    	//参与字表计算的数据
    	String certNo = records[3];
    	CertNoDO c = new CertNoDO();
    	c.setId(MD5Util.encrypt(certNo));
    	//参与指标计算标识，以供参与计算
    	c.setUpdateTime(DateUtils.yyyyMMddToString(new Date()));
    	//如果库中存在
    	if(exists(c)){
    		//更新记录
    		userRepository.update(c.getUpdateTime(), c.getId());
    		//写入数据写入该身份证号下的追加文件或者数据库的表中
    		dutyService.appendTradeDetail(c.getId(), records);
    	}else{
    		//新增的身份证号下的日志记录，保存到身份证号表中
    		c.setCertNo(certNo);
    		userRepository.saveAndFlush(c);
    		//将数据写入该身份证号下的文件或者数据库的表中
    		dutyService.appendTradeDetail(c.getId(), records);
    		//在指标表中新增记录
    		Variables v = new Variables();
    		v.setCertNo(certNo);
    		v.setMd5CertNo(c.getId());	
    		variablesService.insert(v);
    	}
    }


}
