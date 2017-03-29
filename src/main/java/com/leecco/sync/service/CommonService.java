package com.leecco.sync.service;

import com.kingdee.letv.sync.util.RSAUtils;
import com.leecco.sync.ApplicationProperties;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.security.MessageDigest;
import java.security.PrivateKey;

@Service
public class CommonService {
    @Autowired private ApplicationProperties applicationProperties;
    public HttpEntity execPost(String url, UrlEncodedFormEntity entity) {
        HttpEntity resultEntity = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            long t1 = System.currentTimeMillis();
            CloseableHttpResponse response = client.execute(post);
            long t2 = System.currentTimeMillis();
            System.out.println("请求  " + url + ":耗时：" + (t2 - t1) + "ms");
            resultEntity = response.getEntity();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return resultEntity;
    }

    public HttpEntity execPost(String url) {
        HttpEntity resultEntity = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            long t1 = System.currentTimeMillis();
            CloseableHttpResponse response = client.execute(post);
            long t2 = System.currentTimeMillis();
            System.out.println("请求  " + url + ":耗时：" + (t2 - t1) + "ms");
            resultEntity = response.getEntity();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return resultEntity;
    }

    public HttpEntity execGet(String url, UrlEncodedFormEntity entity) {
        HttpEntity resultEntity = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);

            long t1 = System.currentTimeMillis();
            CloseableHttpResponse response = client.execute(get);
            response.setEntity(entity);
            long t2 = System.currentTimeMillis();
            System.out.println("耗时：" + (t2 - t1) + "ms");
            resultEntity = response.getEntity();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return resultEntity;
    }

    public String encrypt(String data) {
        String entryData = null;
        File keyFile = new File(applicationProperties.getKingdeeKeyFilePath());
        try {
            byte[] b = FileUtils.readFileToByteArray(keyFile);
            PrivateKey restorePublicKey = RSAUtils.restorePrivateKey(b);
            byte[] bytes = Base64.encodeBase64(RSAUtils.encryptLarger(data.getBytes(), restorePublicKey));
            entryData = new String(bytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entryData;
    }

    public String getMD5(String key) {
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
