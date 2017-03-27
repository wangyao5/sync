package com.kingdee.letv.sync.util;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class RescouseUtil {
	private static final String BUNDLE_NAME = "com.kingdee.letv.sync.util";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	public static String getString(String key)
	{
		try
		{
			return RESOURCE_BUNDLE.getString(key); 
		} catch (MissingResourceException e) {
		}
		return '!' + key + 
				'!';
	}
}
