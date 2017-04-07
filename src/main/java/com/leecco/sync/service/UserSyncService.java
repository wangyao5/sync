package com.leecco.sync.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

    public String synUser(LeOrg leOrg, String startTime, String endTime) {
        JSONArray resultJSONArray = new JSONArray();
        JSONArray updatePersonInfoJSONArray = new JSONArray();//更新用户信息人员
        JSONArray updatePersonDepartJSONArray = new JSONArray();//更新组织信息人员
        JSONArray updatePersonStatusJSONArray = new JSONArray();//更新在职，离职状态人员
        JSONArray addLeUserList = new JSONArray();//新增人员

        //kingdee内的所有人员信息
        Map<String, KingdeePerson> allPerson = kingdeeApiService.getAllperson();

        //leeco中所有人员信息
        Map<String, LeUser> leUpdatePerson = leApiService.getAllLeUsers(leOrg, startTime, endTime);

        //深度拷贝leecco中人员信息
        Map<String, LeUser> leNewUserList = new HashMap<>();
        leNewUserList.putAll(leNewUserList);

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
                    updatePersonInfoJSONArray.add(JSONObject.toJSON(infoChangedPerson));
                }

                if (null != departmentChangedPerson) {
                    updatePersonDepartJSONArray.add(JSONObject.toJSON(departmentChangedPerson));
                }

                if (null != statusChangedPerson) {
                    updatePersonStatusJSONArray.add(JSONObject.toJSON(statusChangedPerson));
                }

                leNewUserList.remove(leUpdatePersonkey);
                allPerson.remove(personKeyValue.getKey());
            }
        }

        addLeUserList.addAll(convert(leNewUserList.values()));
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

        JSONArray updatePersonInfoJSONArray = new JSONArray();//更新用户信息人员
        JSONArray updatePersonDepartJSONArray = new JSONArray();//更新组织信息人员
        JSONArray updatePersonStatusJSONArray = new JSONArray();//更新在职，离职状态人员
        JSONArray unUsePersonJSONArray = new JSONArray();//禁用人员
        JSONArray addLeUserList = new JSONArray();//新增人员

        //kingdee内的所有人员信息
        Map<String, KingdeePerson> allPerson = kingdeeApiService.getAllperson();

        //leeco中所有人员信息
        Map<String, LeUser> leUpdatePerson = leApiService.getAllLeUsers(leOrg, startTime, endTime);

        //深度拷贝leecco中人员信息
        Map<String, LeUser> leNewUserList = new HashMap<>();
        leNewUserList.putAll(leNewUserList);

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
                    updatePersonInfoJSONArray.add(JSONObject.toJSON(infoChangedPerson));
                    if (updatePersonInfoJSONArray.size() >= 1000) {
                        JSONArray result = kingdeeApiService.updateUserInfo(updatePersonInfoJSONArray);
                        resultJSONArray.addAll(result);
                        updatePersonInfoJSONArray.clear();
                    }
                }

                if (null != departmentChangedPerson) {
                    updatePersonDepartJSONArray.add(JSONObject.toJSON(departmentChangedPerson));
                    if (updatePersonDepartJSONArray.size() >= 1000) {
                        JSONArray result = kingdeeApiService.updateUserDepartment(updatePersonDepartJSONArray);
                        resultJSONArray.addAll(result);
                        updatePersonDepartJSONArray.clear();
                    }
                }

                if (null != statusChangedPerson) {
                    updatePersonStatusJSONArray.add(JSONObject.toJSON(statusChangedPerson));
                    if (updatePersonStatusJSONArray.size() >= 1000) {
                        JSONArray result = kingdeeApiService.updateUserStatus(updatePersonStatusJSONArray);
                        resultJSONArray.addAll(result);
                        updatePersonStatusJSONArray.clear();
                    }
                }

                leNewUserList.remove(leUpdatePersonkey);
                allPerson.remove(personKeyValue.getKey());
            }
        }

        unUsePersonJSONArray.addAll(convertToUnUse(allPerson.values()));
        if (unUsePersonJSONArray.size() > 0) {
            JSONArray result = kingdeeApiService.updateUserStatus(updatePersonStatusJSONArray);
            resultJSONArray.addAll(result);
        }

        addLeUserList.addAll(convert(leNewUserList.values()));
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

    private KingdeePersonKeyValue getPerson(Map<String, KingdeePerson> allPerson, String email) {
        String key = email;
        KingdeePerson kingdeePerson = allPerson.get(key);
        if (applicationProperties.getLeeccoIsrepair()) {
            if (kingdeePerson == null) {
                System.out.println("kingdee账号未用与账号人员：" + key);
                int index = key.indexOf("@");
                if (index > -1) {
                    key = key.substring(0, index);
                    kingdeePerson = allPerson.get(key);
                    if (kingdeePerson == null) {
                        System.out.println("kingdee导入失败人员：" + email);
                    }
                } else {
                    System.out.println("不含@账号：" + key);
                }
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

        if (null != kingdeePerson.getJobTitle() && !kingdeePerson.getJobTitle().equals(leUser.getJobTitle())) {
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

    private KingdeePerson convert(LeUser user) {
        KingdeePerson person = new KingdeePerson();
        person.setPhone(user.getUserName());
        person.setEmail(user.getUserName());
        person.setJobTitle(user.getJobTitle());
        person.setDepartment(user.getDepartment());
        return person;
    }

    private JSONArray convert(Collection<LeUser> users) {
        JSONArray array = new JSONArray();
        for (LeUser user: users) {
            array.add(JSONObject.toJSON(convert(user)));
        }
        return array;
    }

    private JSONArray convertToUnUse(Collection<KingdeePerson> users) {
        JSONArray array = new JSONArray();
        for (KingdeePerson person: users) {
            //正常-->禁用
            if (person.getStatus().equals("1")) {
                KingdeePerson p = new KingdeePerson();
                p.setOpenId(person.getOpenId());
                p.setType("3");
                array.add(JSONObject.toJSON(p));
            }
        }
        return array;
    }
}
