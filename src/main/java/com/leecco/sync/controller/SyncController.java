package com.leecco.sync.controller;

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

        SyncOrgResult syncOrgResult = syncOrg();
        LeOrg leOrg = syncOrgResult.getLeOrg();
        userSyncService.fullSyncUser(leOrg);
        return null;
    }

    /**
     * @description 指定时间范围更新
     */
    @RequestMapping(value = "/sync/{startTime}/{endTime}", method = RequestMethod.POST)
    @ResponseBody
    private StatusVo sync(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        SyncOrgResult syncOrgResult = syncOrg();
        LeOrg leOrg = syncOrgResult.getLeOrg();
        userSyncService.synUser(leOrg, startTime, endTime);
        return null;
    }

    private SyncOrgResult syncOrg() {
        String orgStartTime = "2012-01-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String orgEndTime = sdf.format(new Date());
        return orgSyncService.syncOrg(orgStartTime, orgEndTime);
    }
}
