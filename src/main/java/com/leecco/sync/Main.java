package com.leecco.sync;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

public class Main {
    public static void main(String[] argc) {
        String syncOrgUrl = "http://ucapi.lecommons.com/sync_org.php?site=app&";
        String time = (System.currentTimeMillis() + "").substring(0, 10);
        int page = 1;
        int pageSize = 100;
        String startTime = "2013-10-01 00:00:00";
        String endTime = "2017-10-01 00:00:00";

        String sign = getMD5("endtime="+endTime+"&p="+page+"&ps=" +10+"&site=app&starttime="+startTime+"&time="+time+"8956dmqwqddsxwfvt977");
        String postUrl = null;
        try {
            postUrl = syncOrgUrl + "p="+page + "&ps=" +10+"&time="+time+"&endtime="+URLEncoder.encode(endTime, "utf-8")+"&starttime="+URLEncoder.encode(startTime, "utf-8") +"&sign="+sign;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(postUrl);
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
}
