package com.leecco.sync.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leecco.sync.ApplicationProperties;
import com.leecco.sync.bean.LeDeptNode;
import com.leecco.sync.bean.LeOrg;
import com.leecco.sync.bean.LeUser;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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
    private HttpEntity getLeOrgs(String startTime, String endTime, int page, int pageSize) {
        String syncOrgUrl = applicationProperties.getLeeccoSyncOrgUrl();
        String time = (System.currentTimeMillis() + "").substring(0, 10);
        String sign = commonService.getMD5("endtime=" + endTime + "&p=" + page + "&ps=" + pageSize + "&site=app&starttime=" + startTime + "&time=" + time + "8956dmqwqddsxwfvt977");
        String postUrl = null;
        try {
            postUrl = syncOrgUrl + "p=" + page + "&ps=" + pageSize + "&time=" + time + "&endtime=" + URLEncoder.encode(endTime, "utf-8") + "&starttime=" + URLEncoder.encode(startTime, "utf-8") + "&sign=" + sign;
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
        leOrg.combinToTree();
        return leOrg;
    }

    /**
     * @param startTime 起始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime   截止时间 yyyy-MM-dd HH:mm:ss
     * @description 获取时间范围内，更新的人员信息
     */
    private HttpEntity getLeUser(String startTime, String endTime, int page, int pageSize) {
        String syncUserUrl = applicationProperties.getLeeccoSyncUserUrl();
        String time = (System.currentTimeMillis() + "").substring(0, 10);
        String sign = commonService.getMD5("endtime=" + endTime + "&p=" + page + "&ps=" + pageSize + "&site=app&starttime=" + startTime + "&time=" + time + "8956dmqwqddsxwfvt977");
        String postUrl = null;
        try {
            postUrl = syncUserUrl + "p=" + page + "&ps=" + pageSize + "&time=" + time + "&endtime=" + URLEncoder.encode(endTime, "utf-8") + "&starttime=" + URLEncoder.encode(startTime, "utf-8") + "&sign=" + sign;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return commonService.execPost(postUrl);
    }

    /**
     * @param startTime 起始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime   截止时间 yyyy-MM-dd HH:mm:ss
     * @description 获取le下所有的人员信息
     */
    public Map<String, LeUser> getAllLeUsers(LeOrg leOrg, String startTime, String endTime) {
        Map<String, LeUser> leUserMap = new HashMap<>();
        int page = 0;
        int pageSize = 100;
        int maxPage = 0;
        do {
            page++;
            HttpEntity responseEntity = getLeUser(startTime, endTime, page, pageSize);
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
                    JSONObject personJSONObject = orgJSONArray.getJSONObject(orgIndex);
                    LeUser user = new LeUser();
                    user.setUserName(personJSONObject.getString("username").trim());
                    user.setNickName(personJSONObject.getString("nickname"));
                    user.setJobTitle(personJSONObject.getString("job_title"));
                    String orgNum = personJSONObject.getString("org_num");
                    user.setOrgNum(orgNum);
                    String department = "";

                    Map<String, LeDeptNode> orgs = leOrg.getLeOrgsBack();
                    while (!" ".equals(orgNum) && null != orgs.get(orgNum)) {
                        LeDeptNode node = orgs.get(orgNum);
                        if (department == "") {
                            department = node.getName();
                        } else {
                            department = node.getName() + "\\" + department;
                        }
                        orgNum = node.getpNum();
                    }
                    user.setDepartment(department);
                    user.setPhoneNum(personJSONObject.getString("phone"));
                    user.setStatus(personJSONObject.getString("status"));

                    if ("".equals(department)) {
                        System.out.println(JSONObject.toJSONString(user) + "------用户为游离状态");
                    } else {
                        user.setDepartment(department);
                        leUserMap.put(user.getUserName(), user);
                    }
                }
            }
        } while (page < maxPage);
        return leUserMap;
    }


}
