package com.leecco.sync.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leecco.sync.bean.LeOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public LeOrg syncOrg(String startTime, String endTime) {
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
                System.out.println("乐视不存在该组织：" + orgName);
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
            kingdeeApiService.addDepartment(addOrgs);
        }

        if (delOrgs.size() > 0) {
            kingdeeApiService.delDepartment(delOrgs);
        }
        return leOrg;
    }
}
