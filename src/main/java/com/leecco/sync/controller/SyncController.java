package com.leecco.sync.controller;

import com.leecco.sync.bean.StatusVo;
import com.leecco.sync.service.OrgSyncService;
import com.leecco.sync.service.UserSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/Le")
public class SyncController {

    @Autowired
    private OrgSyncService orgSyncService;
    @Autowired
    private UserSyncService userSyncService;

    /**
     * @description 全量更新
     * */
    @RequestMapping(value = "/sync/full", method = RequestMethod.POST)
    @ResponseBody
    private StatusVo sync() {
        return null;
    }

    /**
     * @description 指定时间范围更新
     * */
    @RequestMapping(value = "/sync/{startTime}/{endTime}", method = RequestMethod.POST)
    @ResponseBody
    private StatusVo sync(long startTime, long endTime) {
        return null;
    }
}
