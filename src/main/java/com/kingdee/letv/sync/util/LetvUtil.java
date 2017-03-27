package com.kingdee.letv.sync.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kingdee.letv.sync.model.LogModel;
import com.kingdee.letv.sync.model.SynTimeModel;
import com.kingdee.letv.sync.service.LogService;

public class LetvUtil {
	// public static String host =
	// "http://10.154.30.189/";//sso.api.test.leshiren.cn
	public static String host = ConfigUtil.getValue("openHost");
	// public static String host = "http://mcloud.kingdee.com/";
	public static String POST_URL_Invoke_Org = ConfigUtil.getValue("sync_orgUrl");
	public static String POST_URL_Invoke_USER = ConfigUtil.getValue("sync_userUrl");
	public static String KEY = ConfigUtil.getValue("key");

	public static String post(String url, List<NameValuePair> params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		String responseBody = null;
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			responseBody = httpClient.execute(post, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return responseBody;
	}

	public static String enyte(String data) {
		try {
			//String path = OrgSyncUtil.class.getResource("").getPath();
		//	System.out.println("文件路径：" + path);
			byte[] b = FileUtils.readFileToByteArray(new File(ConfigUtil.getValue("keyPath") + KEY + ".key"));
			PrivateKey restorePublicKey = RSAUtils.restorePrivateKey(b);
			byte[] bytes = Base64.encodeBase64(RSAUtils.encryptLarger(data.getBytes(), restorePublicKey));
			return new String(bytes, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getMD5(String key) {
		StringBuffer buffer = new StringBuffer();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(key.getBytes("UTF-8"));
			for (byte b : digest.digest()) {
				buffer.append(String.format("%02x", b & 0xff));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public static void writeLog(String json, String result, String op) {
		// 写日志
		LogService logService = new LogService();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SynTimeModel synTimeModel = logService.getSynTimeModel(op);
		LogModel model = new LogModel();
		model.setIntNo(op);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		model.setSynTime(sdf.format(new Date()));
		model.setMidParams(json);
		model.setParams(json);
		JSONObject jsonDep = JSON.parseObject(result);
		String success = jsonDep.getString("success");
		if (success != null && success.equals("true")) {
			model.setResult("同步成功");
		} else if (success != null && success.equals("false")) {
			model.setResult(jsonDep.getString("error"));
		} else {
			model.setResult(result);
		}
		logService.saveLogModel(model);
		synTimeModel.setIntNo(op);
		synTimeModel.setSynTime(new Timestamp(cal.getTimeInMillis()));
		logService.saveSynTimeModel(synTimeModel);

	}
}
