package com.kingdee.letv.sync.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


public class ConfigUtil {
	private static Map<String, String> pushMapping = new HashMap<String, String>();
	
	


	public  ConfigUtil(String path){
		File file =new File(path);
		Properties properties = new Properties();
        try {
			properties.load(new FileInputStream(file));
			initPushMapping(properties);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public  void initPushMapping(Properties pushProperties){
		Set<Object> keys = pushProperties.keySet();
		for(Object key : keys){
			String val = (String) pushProperties.get(key);
			pushMapping.put((String) key, val);
		}
	}
	
	
	public static String  getValue(String key){
		String value=pushMapping.get(key);
		if(value!=null){
			return value;
					}
		else return "";
	}
	
}
