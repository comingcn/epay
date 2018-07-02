/**   
 * @Title: PropertiesUtils.java 
 * @Package: com.epay.xj.utils 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018 北京中数合一科技有限公司
 */
package com.epay.xj.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Description:
 * @author LZG
 * @date 2018年07月02日
 */
public class PropertiesUtils {

	/**
	 * @Description: 功能说明：根据绝对路径文件名获取Properties对象
	 * @param fileName
	 * @return
	 * @author LZG
	 * @date 2018年07月02日
	 */
	public static Properties readFromAbsolutePath(String fileName) {
		if (null == fileName || "".equals(fileName))
			return null;

		Properties prop = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			if (in == null)
				return null;
			prop.load(in);
			return prop;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	/**
	 * @Description: 根据相对路径文件名获取Properties对象
	 * @param fileName
	 * @return
	 * @author LZG
	 * @date 2018年07月02日
	 */
	public static Properties read(String fileName) {

		if (null == fileName || "".equals(fileName))
			// throw new RuntimeException("fileName不能为空");
			return null;

		InputStream in = null;
		try {
			Properties prop = new Properties();
			in = PropertiesUtils.class.getResourceAsStream(fileName);

			if (in == null)
				return null;
			prop.load(in);

			return prop;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * @Description: 根据键名获取值(也可以直接使用官方提供的properties.getProperty("XXX"))
	 * @param prop
	 * @param key
	 * @return
	 * @author LZG
	 * @date 2018年07月02日
	 */
	public static String readKeyValue(Properties prop, String key) {
		if (prop != null)
			return prop.getProperty(key);

		return null;
	}
}
