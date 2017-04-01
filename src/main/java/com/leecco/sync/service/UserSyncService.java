package com.leecco.sync.service;

import com.kingdee.letv.sync.been.Person;
import com.leecco.sync.ApplicationProperties;
import com.leecco.sync.bean.LeOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserSyncService {
    @Autowired
    private KingdeeApiService kingdeeApiService;

    public boolean synUser(LeOrg leOrg, String startTime, String endTime) {
        //kingdee内的所有人员信息
        Map<String, Person> allPerson = kingdeeApiService.getAllperson();

        return true;
    }
}
