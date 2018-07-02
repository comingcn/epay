/*******************************************************************
 * $Header$
 * $Revision: 1.0$
 * $Date: May 29, 2018$
 * Copyright (c) 2017-2018 北京中数合一科技有限公司.
 * All rights reserved. 
 *******************************************************************/
package com.zshy.datacleaning.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties工具类 (main为测试方法)
 * 
 * @author LZG
 */
public class PropertiesUtil {

	/**
	 * 功能说明：根据绝对路径文件名获取Properties对象
	 * @param fileName
	 * @return Properties对象
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
	 * 根据相对路径文件名获取Properties对象
	 * 
	 * @param fileName
	 * @return Properties
	 */
	public static Properties read(String fileName) {

		if (null == fileName || "".equals(fileName))
			//throw new RuntimeException("fileName不能为空");
			return null;

		InputStream in = null;
		try {
			Properties prop = new Properties();
			in = PropertiesUtil.class.getResourceAsStream(fileName);

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
	 * 根据键名获取值(也可以直接使用官方提供的properties.getProperty("XXX"))
	 * 
	 * @param prop
	 * @param key
	 * @return String
	 */
	public static String readKeyValue(Properties prop, String key) {
		if (prop != null)
			return prop.getProperty(key);

		return null;
	}

	// 测试
	public static void main(String[] args) {
		Properties prop = PropertiesUtil.read("config/mail.properties");

		// 使用本工具类的readKeyValue方法
		System.out.println(PropertiesUtil.readKeyValue(prop, "mail.subject.register"));
		System.out.println(PropertiesUtil.readKeyValue(prop, "hello")); // properties文件没有的，返回null

		// 使用官方的getProperty方法
		System.out.println(prop.getProperty("mail.username"));
		System.out.println(prop.getProperty("world")); // properties文件没有的，返回null
	}

}
