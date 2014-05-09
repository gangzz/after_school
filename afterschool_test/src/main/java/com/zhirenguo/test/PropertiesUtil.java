package com.zhirenguo.test;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	public static Properties loadFromClasspath(String path){
		Properties properties = new Properties();
		InputStream inStream = null;
		try{
			ClassLoader loader = PropertiesUtil.class.getClassLoader();
			inStream = loader.getResourceAsStream(path);
			properties.load(inStream);
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(inStream != null)
			{
				try {
					inStream.close();
				} catch (Exception e){}
			}
		}
		
		return properties;
	}
}
