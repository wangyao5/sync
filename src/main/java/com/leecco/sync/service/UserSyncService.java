package com.leecco.sync.service;

import com.leecco.sync.ApplicationProperties;
import com.leecco.sync.bean.LeOrg;
import com.leecco.sync.bean.LeUser;
import com.leecco.sync.bean.KingdeePerson;
import com.leecco.sync.bean.KingdeePersonKeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
        List<KingdeePerson> updatePerson = new ArrayList<>();
        List<KingdeePerson> delPerson = new ArrayList<>();
        List<LeUser> addLeUser = new ArrayList<>();

        //kingdee内的所有人员信息
        Map<String, KingdeePerson> allPerson = kingdeeApiService.getAllperson();

        //leeco中所有人员信息
        Map<String, LeUser> leUpdatePerson = leApiService.getAllLeUsers(leOrg, startTime, endTime);
        Set<String> leUpdatePersonkeys = leUpdatePerson.keySet();
        for (String leUpdatePersonkey : leUpdatePersonkeys) {
            KingdeePersonKeyValue personKeyValue = getPerson(allPerson, leUpdatePersonkey);
            if (null != personKeyValue) {
                LeUser leUser = leUpdatePerson.get(leUpdatePersonkey);


                leUpdatePerson.remove(leUpdatePersonkey);
            }
        }
        addLeUser.addAll(leUpdatePerson.values());
        return true;
    }

    public String fullSyncUser(LeOrg leOrg) {
        String startTime = "2012-01-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = sdf.format(new Date());

        List<KingdeePerson> updatePersonInfoList = new ArrayList<>();//更新用户信息人员
        List<KingdeePerson> updatePersonDepartList = new ArrayList<>();//更新组织信息人员
        List<KingdeePerson> updatePersonStatusList = new ArrayList<>();//更新在职，离职状态人员
        List<KingdeePerson> unUsePersonList = new ArrayList<>();//禁用人员
        List<LeUser> addLeUserList = new ArrayList<>();//新增人员

        //kingdee内的所有人员信息
        Map<String, KingdeePerson> allPerson = kingdeeApiService.getAllperson();

        //leeco中所有人员信息
        Map<String, LeUser> leUpdatePerson = leApiService.getAllLeUsers(leOrg, startTime, endTime);
        Set<String> leUpdatePersonkeys = leUpdatePerson.keySet();
        for (String leUpdatePersonkey : leUpdatePersonkeys) {
            KingdeePersonKeyValue personKeyValue = getPerson(allPerson, leUpdatePersonkey);
            if (null != personKeyValue) {
                LeUser leUser = leUpdatePerson.get(leUpdatePersonkey);

                KingdeePerson kingdeePerson = personKeyValue.getValue();
                KingdeePerson infoChangedPerson = infoChanged(kingdeePerson, leUser);
                KingdeePerson departmentChangedPerson = departmentChanged(kingdeePerson, leUser);
                KingdeePerson statusChangedPerson = statusChanged(kingdeePerson, leUser);

                if (null != infoChangedPerson) {
                    updatePersonInfoList.add(infoChangedPerson);
                }

                if (null != departmentChangedPerson) {
                    updatePersonDepartList.add(departmentChangedPerson);
                }

                if (null != statusChangedPerson) {
                    updatePersonStatusList.add(statusChangedPerson);
                }

                leUpdatePerson.remove(leUpdatePersonkey);
                allPerson.remove(personKeyValue.getKey());
            }
        }
        addLeUserList.addAll(leUpdatePerson.values());
        unUsePersonList.addAll(allPerson.values());
        return null;
    }

    private KingdeePersonKeyValue getPerson(Map<String, KingdeePerson> allPerson, String email) {
        String key = email;
        KingdeePerson kingdeePerson = allPerson.get(key);
        if (applicationProperties.getLeeccoIsrepair()) {
            if (kingdeePerson == null) {
                System.out.println(key);
                key = key.substring(0, key.indexOf("@"));
                kingdeePerson = allPerson.get(key);
            }
        }

        if (null == kingdeePerson) {
            return null;
        }

        return new KingdeePersonKeyValue(key, kingdeePerson);
    }

    private KingdeePerson departmentChanged(KingdeePerson kingdeePerson, LeUser leUser) {
        if (kingdeePerson.getDepartment().equals(leUser.getDepartment())) {
            return null;
        }

        KingdeePerson person = new KingdeePerson();
        person.setOpenId(kingdeePerson.getOpenId());
        person.setDepartment(leUser.getDepartment());
        return person;
    }

    private KingdeePerson infoChanged(KingdeePerson kingdeePerson, LeUser leUser) {
        boolean flag = false;
        KingdeePerson person = new KingdeePerson();
        if (!kingdeePerson.getEmail().equals(leUser.getUserName())) {
            person.setEmail(leUser.getUserName());
            flag = true;
        }

        if (!kingdeePerson.getJobTitle().equals(leUser.getJobTitle())) {
            person.setJobTitle(leUser.getJobTitle());
            flag = true;
        }

        if (flag) {
            person.setOpenId(kingdeePerson.getOpenId());
            return person;
        } else {
            return null;
        }
    }

    private KingdeePerson statusChanged(KingdeePerson kingdeePerson, LeUser leUser) {
        boolean flag = false;
        String type = "";
        if (leUser.getStatus().equals("T")) {
            //注销-->正常
            if (kingdeePerson.getStatus().equals("0")) {
                flag = true;
                type = "2";
            }
            //禁用-->正常
            if (kingdeePerson.getStatus().equals("2")) {
                flag = true;
                type = "4";
            }
        } else if (leUser.getStatus().equals("N")) {
            //正常-->禁用
            if (kingdeePerson.getStatus().equals("1")) {
                flag = true;
                type = "3";
            }
        }

        if (flag) {
            KingdeePerson person = new KingdeePerson();
            person.setOpenId(kingdeePerson.getOpenId());
            person.setType(type);
            return person;
        } else {
            return null;
        }

    }
}
