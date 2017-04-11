package com.leecco.sync.service;

import com.alibaba.fastjson.JSONArray;
import com.leecco.sync.bean.*;
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
    private UserCompareService userCompareService;

    public String synUser(LeOrg leOrg, String startTime, String endTime) {
        JSONArray resultJSONArray = new JSONArray();
        //kingdee内的所有人员信息
        Map<String, KingdeePerson> allPerson = kingdeeApiService.getAllperson();

        //leeco中所有人员信息
        Map<String, LeUser> leUpdatePerson = leApiService.getAllLeUsers(leOrg, startTime, endTime);
        userCompareService.compare(allPerson, leUpdatePerson);
        JSONArray updatePersonInfoJSONArray = userCompareService.getUpdatePersonInfoJSONArray();//更新用户信息人员
        JSONArray updatePersonDepartJSONArray = userCompareService.getUpdatePersonDepartJSONArray();//更新组织信息人员
        JSONArray updatePersonStatusJSONArray = userCompareService.getUpdatePersonStatusJSONArray();//更新在职，离职状态人员
        JSONArray addLeUserList = userCompareService.getAddLeUserList();//新增人员

        if (updatePersonInfoJSONArray.size() > 0) {
            JSONArray result = kingdeeApiService.updateUserInfo(updatePersonInfoJSONArray);
            resultJSONArray.addAll(result);
        }

        if (updatePersonDepartJSONArray.size() > 0) {
            JSONArray result = kingdeeApiService.updateUserDepartment(updatePersonDepartJSONArray);
            resultJSONArray.addAll(result);
        }

        if (updatePersonStatusJSONArray.size() > 0) {
            JSONArray result = kingdeeApiService.updateUserStatus(updatePersonStatusJSONArray);
            resultJSONArray.addAll(result);
        }

        if (addLeUserList.size() > 0) {
            JSONArray result = kingdeeApiService.addUsers(addLeUserList);
            resultJSONArray.addAll(result);
        }

        return resultJSONArray.toJSONString();
    }

    public String fullSyncUser(LeOrg leOrg) {
        JSONArray resultJSONArray = new JSONArray();
        String startTime = "2012-01-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = sdf.format(new Date());

        //kingdee内的所有人员信息
        Map<String, KingdeePerson> allPerson = kingdeeApiService.getAllperson();

        //leeco中所有人员信息
        Map<String, LeUser> leUpdatePerson = leApiService.getAllLeUsers(leOrg, startTime, endTime);

        userCompareService.compare(allPerson, leUpdatePerson);
        JSONArray updatePersonInfoJSONArray = userCompareService.getUpdatePersonInfoJSONArray();//更新用户信息人员
        JSONArray updatePersonDepartJSONArray = userCompareService.getUpdatePersonDepartJSONArray();//更新组织信息人员
        JSONArray updatePersonStatusJSONArray = userCompareService.getUpdatePersonStatusJSONArray();//更新在职，离职状态人员
        JSONArray unUsePersonJSONArray = userCompareService.getUnUsePersonJSONArray();//禁用人员
        JSONArray addLeUserList = userCompareService.getAddLeUserList();//新增人员

        if (unUsePersonJSONArray.size() > 0) {
            JSONArray result = kingdeeApiService.updateUserStatus(unUsePersonJSONArray);
            resultJSONArray.addAll(result);
        }

        if (updatePersonStatusJSONArray.size() > 0) {
            JSONArray result = kingdeeApiService.updateUserStatus(updatePersonStatusJSONArray);
            resultJSONArray.addAll(result);
        }

        if (updatePersonInfoJSONArray.size() > 0) {
            JSONArray result = kingdeeApiService.updateUserInfo(updatePersonInfoJSONArray);
            resultJSONArray.addAll(result);
        }

        if (updatePersonDepartJSONArray.size() > 0) {
            JSONArray result = kingdeeApiService.updateUserDepartment(updatePersonDepartJSONArray);
            resultJSONArray.addAll(result);
        }

        if (addLeUserList.size() > 0) {
            JSONArray result = kingdeeApiService.addUsers(addLeUserList);
            resultJSONArray.addAll(result);
        }

        return resultJSONArray.toJSONString();
    }
}
