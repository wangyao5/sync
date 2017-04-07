package com.leecco.sync.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leecco.sync.bean.LeOrg;
import com.leecco.sync.bean.SyncOrgResult;
import org.apache.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class OrgSyncService {
    @Autowired KingdeeApiService kingdeeApiService;
    @Autowired LeApiService leApiService;

    /**
     * @description 启动时间段同步
     * @param startTime 起始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime   截止时间 yyyy-MM-dd HH:mm:ss
     */
    public SyncOrgResult syncOrg(String startTime, String endTime) {
        SyncOrgResult result = new SyncOrgResult();
        String addOrgsKingdeeMessage = "未添加组织";
        String delOrgsKingdeeMessage = "未删除组织";

        JSONArray delOrgs = new JSONArray();
        JSONArray addOrgs = new JSONArray();

        //kingdee内的组织架构信息
        Map<String, JSONObject> kingdeeOrgs = kingdeeApiService.getKindeeOrgs();
        LeOrg leOrg = leApiService.getAllLeOrgs(startTime, endTime);

        //对比kingdee的组织信息跟Leeco的数据
        Map<String, Boolean> fullPathOrgs = leOrg.getLeOrgsWithFullPathAndStatus();
        Set<String> orgFullNames = kingdeeOrgs.keySet();
        for (String orgName : orgFullNames) {
            if (fullPathOrgs.get(orgName) == null) {
                System.out.println("乐视不存在该组织：" + orgName + "将执行删除操作");
                delOrgs.add(orgName);
            } else {
                boolean effect = fullPathOrgs.get(orgName);
                if (!effect) {
                    delOrgs.add(orgName);
                }
                fullPathOrgs.remove(orgName);
            }
        }
        addOrgs.addAll(fullPathOrgs.keySet());
        if (addOrgs.size() > 0) {
            try {
                addOrgsKingdeeMessage = kingdeeApiService.addDepartment(addOrgs);
            } catch (IOException e) {
                addOrgsKingdeeMessage = e.getMessage();
                e.printStackTrace();
            } catch (ParseException e) {
                addOrgsKingdeeMessage = e.getMessage();
                e.printStackTrace();
            }
        }

        if (delOrgs.size() > 0) {
            try {
                delOrgsKingdeeMessage = kingdeeApiService.delDepartment(delOrgs);
            } catch (IOException e) {
                delOrgsKingdeeMessage = e.getMessage();
                e.printStackTrace();
            } catch (ParseException e) {
                delOrgsKingdeeMessage = e.getMessage();
                e.printStackTrace();
            }
        }

        result.setLeOrg(leOrg);
        result.setAddOrgKingdeeMessage(addOrgsKingdeeMessage);
        result.setDelOrgKingdeeMessage(delOrgsKingdeeMessage);
        return result;
    }
}
