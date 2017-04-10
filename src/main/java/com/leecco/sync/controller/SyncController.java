package com.leecco.sync.controller;

import com.alibaba.fastjson.JSONObject;
import com.leecco.sync.bean.LeOrg;
import com.leecco.sync.bean.StatusVo;
import com.leecco.sync.bean.SyncOrgResult;
import com.leecco.sync.service.OrgSyncService;
import com.leecco.sync.service.UserSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(path = "/Le")
public class SyncController {

    @Autowired
    private OrgSyncService orgSyncService;
    @Autowired
    private UserSyncService userSyncService;

    /**
     * @description 全量更新
     */
    @RequestMapping(value = "/sync/full", method = RequestMethod.GET)
    @ResponseBody
    private StatusVo sync() {
        LeOrg leOrg = syncOrg();
        String syncAddOrgMessage = orgSyncService.syncAddOrg();
        String userSyncMessage = userSyncService.fullSyncUser(leOrg);
        String syncDelOrgMessage = orgSyncService.syncDelOrg();
        
        StatusVo vo = new StatusVo();
        JSONObject syncMessageObj = new JSONObject();
        syncMessageObj.put("addOrgMessage", syncAddOrgMessage);
        syncMessageObj.put("delOrgMessage", syncDelOrgMessage);
        syncMessageObj.put("syncUserMessage", userSyncMessage);
        vo.setMessage(syncMessageObj.toJSONString());
        vo.setCode(0);
        return vo;
    }

    /**
     * @description 指定时间范围更新
     */
    @RequestMapping(value = "/sync/{startTime}/{endTime}", method = RequestMethod.POST)
    @ResponseBody
    private StatusVo sync(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        LeOrg leOrg = syncOrg();
        String syncAddOrgMessage = orgSyncService.syncAddOrg();
        String userSyncMessage = userSyncService.synUser(leOrg, startTime, endTime);
        String syncDelOrgMessage = orgSyncService.syncDelOrg();
        StatusVo vo = new StatusVo();
        JSONObject syncMessageObj = new JSONObject();
        syncMessageObj.put("addOrgMessage", syncAddOrgMessage);
        syncMessageObj.put("delOrgMessage", syncDelOrgMessage);
        syncMessageObj.put("syncUserMessage", userSyncMessage);
        vo.setMessage(syncMessageObj.toJSONString());
        vo.setCode(0);
        return vo;
    }

    private LeOrg syncOrg() {
        String orgStartTime = "2012-01-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String orgEndTime = sdf.format(new Date());
        return orgSyncService.initOrgsInfo(orgStartTime, orgEndTime);
    }
}
