package com.leecco.sync.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leecco.sync.bean.LeOrg;
import org.apache.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class OrgSyncService {
    @Autowired KingdeeApiService kingdeeApiService;
    @Autowired LeApiService leApiService;

    JSONArray delOrgs = new JSONArray();//需要删除的组织
    JSONArray addOrgs = new JSONArray();//需要添加的组织

    /**
     * @description 启动时间段同步
     * @param startTime 起始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime   截止时间 yyyy-MM-dd HH:mm:ss
     */
    public LeOrg initOrgsInfo(String startTime, String endTime) {
        //kingdee内的组织架构信息
        Map<String, JSONObject> kingdeeOrgs = kingdeeApiService.getKindeeOrgs();
        LeOrg leOrg = leApiService.getAllLeOrgs(startTime, endTime);

        //对比kingdee的组织信息跟Leeco的数据
        Map<String, Boolean> fullPathOrgs = leOrg.getLeOrgsWithFullPathAndStatus();
        Set<String> orgFullNames = kingdeeOrgs.keySet();
        for (String orgName : orgFullNames) {
            if (fullPathOrgs.get(orgName) == null) {
                System.out.println("乐视不存在该组织：<" + orgName + ">将执行删除操作");
                delOrgs.add(orgName);
            } else {
                boolean effect = fullPathOrgs.get(orgName);
                if (!effect) {
                    delOrgs.add(orgName);
                }
                fullPathOrgs.remove(orgName);
            }
        }

        //找出需要添加的组织
        for (String org : fullPathOrgs.keySet()) {
            if(fullPathOrgs.get(org)) {
                addOrgs.add(org);
            }
        }
        return leOrg;
    }

    public String syncAddOrg() {
        String addOrgsKingdeeMessage = "未添加组织";
        if (addOrgs.size() > 0) {
            try {
                addOrgsKingdeeMessage = kingdeeApiService.addDepartment(addOrgs).toJSONString();
            } catch (IOException e) {
                addOrgsKingdeeMessage = e.getMessage();
                e.printStackTrace();
            } catch (ParseException e) {
                addOrgsKingdeeMessage = e.getMessage();
                e.printStackTrace();
            }
        }

        return addOrgsKingdeeMessage;
    }

    public String syncDelOrg() {
        String delOrgsKingdeeMessage = "未删除组织";
        if (delOrgs.size() > 0) {
            try {
                delOrgsKingdeeMessage = kingdeeApiService.delDepartment(delOrgs).toJSONString();
            } catch (IOException e) {
                delOrgsKingdeeMessage = e.getMessage();
                e.printStackTrace();
            } catch (ParseException e) {
                delOrgsKingdeeMessage = e.getMessage();
                e.printStackTrace();
            }
        }
        return delOrgsKingdeeMessage;
    }
}
