package com.epay.xj.service;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csvreader.CsvReader;
import com.epay.xj.domain.CertNoDO;
import com.epay.xj.repository.CertNoRepository;
import com.epay.xj.utils.DateUtils;
import com.epay.xj.utils.MD5Util;

/**
 * Created by hx on 2017/11/4.
 */
@Service
public class CertNoService {

    @Autowired
    private CertNoRepository userRepository;

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
    	String certNo = MD5Util.encrypt(reader.get(reader.getHeaders()[2]));
    	CertNoDO c = new CertNoDO();
    	c.setId(certNo);
    	//如果库中存在
    	if(exists(c)){
    		//参与指标计算标识，以供参与计算
    		c.setUpdateTime(DateUtils.yyyyMMddToString(new Date()));
    		//写入数据写入该身份证号下的追加文件或者数据库的表中
    		//更新记录
    		userRepository.saveAndFlush(c);
    	}else{
    		//新增的身份证号下的日志记录，保存到身份证号表中
    		//参与指标计算标识，以供参与计算
    		//写入数据写入该身份证号下的文件或者数据库的表中
    		//在指标表中新增记录
    	}
    }


}
