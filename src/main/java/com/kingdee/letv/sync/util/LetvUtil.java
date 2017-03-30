package com.kingdee.letv.sync.util;

public class LetvUtil {
	// public static String host =
	// "http://10.154.30.189/";//sso.api.test.leshiren.cn
	public static String host = ConfigUtil.getValue("openHost");
	// public static String host = "http://mcloud.kingdee.com/";
	public static String POST_URL_Invoke_Org = ConfigUtil.getValue("sync_orgUrl");
	public static String POST_URL_Invoke_USER = ConfigUtil.getValue("sync_userUrl");
	public static String KEY = ConfigUtil.getValue("key");

	public static String post(String url, Object o) {
	   return null;
        }
	public static String enyte(String data) {
		return null;
	}

	public static String getMD5(String key) {
	     return null;
        }

	public static void writeLog(String json, String result, String op) {
		// 写日志
	}
}
