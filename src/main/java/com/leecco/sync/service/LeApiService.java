package com.leecco.sync.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leecco.sync.ApplicationProperties;
import com.leecco.sync.bean.LeDeptNode;
import com.leecco.sync.bean.LeOrg;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class LeApiService {
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private CommonService commonService;

    /**
     * @param startTime 起始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime   截止时间 yyyy-MM-dd HH:mm:ss
     * @description 获取时间范围内，更新的组织架构
     */
    public HttpEntity getLeOrgs(String startTime, String endTime, int page, int pageSize) {
        String syncOrgUrl = applicationProperties.getLeeccoSyncOrgUrl();
        String time = (System.currentTimeMillis() + "").substring(0, 10);
        String sign = commonService.getMD5("endtime="+endTime+"&p="+page+"&ps=" +pageSize+"&site=app&starttime="+startTime+"&time="+time+"8956dmqwqddsxwfvt977");
        String postUrl = null;
        try {
            postUrl = syncOrgUrl + "p="+page + "&ps=" +pageSize+"&time="+time+"&endtime="+URLEncoder.encode(endTime, "utf-8")+"&starttime="+URLEncoder.encode(startTime, "utf-8") +"&sign="+sign;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return commonService.execPost(postUrl);
    }

    /**
     * @param startTime 起始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime   截止时间 yyyy-MM-dd HH:mm:ss
     * @description 获取le下所有的组织架构
     */
    public LeOrg getAllLeOrgs(String startTime, String endTime) {
        LeOrg leOrg = new LeOrg();
        int page = 0;
        int pageSize = 100;
        int maxPage = 0;
        do {
            page++;
            HttpEntity responseEntity = getLeOrgs(startTime, endTime, page, pageSize);
            String response = null;
            try {
                response = EntityUtils.toString(responseEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (null != response) {
                JSONObject responseJSONObject = JSON.parseObject(response);
                maxPage = responseJSONObject.getJSONObject("respond").getInteger("total_pages");

                JSONArray orgJSONArray = responseJSONObject.getJSONArray("objects");

                for (int orgIndex = 0; orgIndex < orgJSONArray.size(); orgIndex++) {
                    JSONObject dep = orgJSONArray.getJSONObject(orgIndex);
                    LeDeptNode node = new LeDeptNode();
                    if ("10000073".equals(dep.getString("org_num")) || "LE00310000074".equals(dep.getString("org_num"))) {
                        System.out.println(dep.getString("org_num"));
                    }
                    String orgNum = dep.getString("org_num");
                    node.setOrgNum(orgNum);
                    node.setName(dep.getString("org_name"));
                    node.setpNum(dep.getString("pnum"));
                    node.setEffect(dep.getIntValue("effect"));
                    node.setLevel(dep.getIntValue("level"));
                    leOrg.add(node);
                }
            }
        } while (page < maxPage);
        return leOrg;
    }
}
