package com.leecco.sync.service;

import com.alibaba.fastjson.JSONObject;
import com.kingdee.letv.sync.been.DeptDTO;
import com.kingdee.letv.sync.been.Person;
import org.apache.http.HttpEntity;
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
    public void syncOrg(String startTime, String endTime) {
        //kingdee内的组织架构信息
        Map<String, JSONObject> kingdeeOrgs = kingdeeApiService.getKindeeOrgs();


        DeptDTO depts = new DeptDTO();
        depts.setEid(kingdeeApiService.getKingdeeKey());
        int page = 1;
        int pageSize = 50;
        HttpEntity entity = leApiService.getLeOrgs(startTime, endTime, page, pageSize);

    }


}
