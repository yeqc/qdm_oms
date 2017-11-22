package com.work.shop.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * PropertieFileReader.java
 * @author qulm
 * @since 1.0
 * @version 1.0
 */
public class PropertieFileReader {
	
	private static Properties config = new Properties(); //单例

	// 类加载时通过类加载器读取类路径下的配置文件
	static {
		try 
		{
			InputStream in = PropertieFileReader.class.getClassLoader()
							.getResourceAsStream("resource.properties");// 通过类加载器获得类路径下该属性文件的一个输入流
			config.load(in);// 从输入流中读取属性列表
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getString(String key) {
		return config.getProperty(key);
	}
	
	public static String getString(String key, String defaultValue) {
		return config.getProperty(key, defaultValue);
	}

	public static Properties getInstance() {
		return config;
	}

}
