package com.leecco.sync.service;

import com.leecco.sync.ApplicationProperties;
import com.leecco.sync.bean.LeOrg;
import com.leecco.sync.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserSyncService {
    @Autowired
    private KingdeeApiService kingdeeApiService;
    @Autowired
    private LeApiService leApiService;
    @Autowired
    private ApplicationProperties applicationProperties;

    public boolean synUser(LeOrg leOrg, String startTime, String endTime) {
        List<Person> updatePerson = new ArrayList<>();
        List<Person> delPerson = new ArrayList<>();
        List<Person> addPerson = new ArrayList<>();
        
        //kingdee内的所有人员信息
        Map<String, Person> allPerson = kingdeeApiService.getAllperson();

        //leeco中所有人员信息
        Map<String, Person> leUpdatePerson= leApiService.getAllLeUsers(leOrg, startTime, endTime);
        Set<String> leUpdatePersonkeys = leUpdatePerson.keySet();
        for (String leUpdatePersonkey : leUpdatePersonkeys) {


            Person kingdeePerson = allPerson.get(leUpdatePersonkey);
//            boolean isSame = kingdeePerson.sameWith(leUpdatePerson.get(leUpdatePersonkey));
//            if (isSame) {
//
//            } else {
////                updatePerson.put()
//            }
        }
        return true;
    }

    private Person getPerson(Map<String, Person> allPerson, String phone) {
        Person kingdeePerson = allPerson.get(phone);
        if (applicationProperties.getLeeccoIsrepair() == 1) {
            if (null == kingdeePerson) {
                kingdeePerson = allPerson.get(phone.substring(0, phone.indexOf("@")));
            }
        }
        return kingdeePerson;
    }

    private boolean departmentChanged(){
        

        return true;
    }

    private boolean infoChanged(){
        return true;
    }
}
