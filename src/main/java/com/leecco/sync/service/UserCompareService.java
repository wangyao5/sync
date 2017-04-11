package com.leecco.sync.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leecco.sync.ApplicationProperties;
import com.leecco.sync.bean.KingdeePerson;
import com.leecco.sync.bean.KingdeePersonKeyValue;
import com.leecco.sync.bean.LeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class UserCompareService {
    @Autowired
    private ApplicationProperties applicationProperties;
    private JSONArray updatePersonInfoJSONArray = new JSONArray();//更新用户信息人员
    private JSONArray updatePersonDepartJSONArray = new JSONArray();//更新组织信息人员
    private JSONArray updatePersonStatusJSONArray = new JSONArray();//更新在职，离职状态人员
    private JSONArray unUsePersonJSONArray = new JSONArray();//禁用人员
    private JSONArray addLeUserList = new JSONArray();//新增人员

    /**
     * @param allPerson kingdee中所有的用户信息
     * @param leUpdatePerson 乐视中变更的用户信息
     * */
    public void compare(Map<String, KingdeePerson> allPerson, Map<String, LeUser> leUpdatePerson){
        //深度拷贝leecco中人员信息
        Map<String, LeUser> leNewUserList = new HashMap<>();
        leNewUserList.putAll(leUpdatePerson);

        Set<String> leUpdatePersonkeys = leUpdatePerson.keySet();
        for (String leUpdatePersonkey : leUpdatePersonkeys) {

            KingdeePersonKeyValue personKeyValue = getPerson(allPerson, leUpdatePersonkey);
            if (null != personKeyValue) {
                LeUser leUser = leUpdatePerson.get(leUpdatePersonkey);
                //非在职人员直接不做任何更新处理
                if (!leUser.getStatus().equals("N")) {
                    leNewUserList.remove(leUpdatePersonkey);
                    allPerson.remove(personKeyValue.getKey());
                    continue;
                }

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

        unUsePersonJSONArray.addAll(convertToUnUse(allPerson.values()));
        addLeUserList.addAll(convert(leNewUserList.values()));
    }

    public JSONArray getUpdatePersonInfoJSONArray() {
        return updatePersonInfoJSONArray;
    }

    public void setUpdatePersonInfoJSONArray(JSONArray updatePersonInfoJSONArray) {
        this.updatePersonInfoJSONArray = updatePersonInfoJSONArray;
    }

    public JSONArray getUpdatePersonDepartJSONArray() {
        return updatePersonDepartJSONArray;
    }

    public void setUpdatePersonDepartJSONArray(JSONArray updatePersonDepartJSONArray) {
        this.updatePersonDepartJSONArray = updatePersonDepartJSONArray;
    }

    public JSONArray getUpdatePersonStatusJSONArray() {
        return updatePersonStatusJSONArray;
    }

    public void setUpdatePersonStatusJSONArray(JSONArray updatePersonStatusJSONArray) {
        this.updatePersonStatusJSONArray = updatePersonStatusJSONArray;
    }

    public JSONArray getUnUsePersonJSONArray() {
        return unUsePersonJSONArray;
    }

    public void setUnUsePersonJSONArray(JSONArray unUsePersonJSONArray) {
        this.unUsePersonJSONArray = unUsePersonJSONArray;
    }

    public JSONArray getAddLeUserList() {
        return addLeUserList;
    }

    public void setAddLeUserList(JSONArray addLeUserList) {
        this.addLeUserList = addLeUserList;
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
                } else {
                    System.out.println("不含@账号：" + key);
                }
            }
        }

        if (null == kingdeePerson) {
            System.out.println("kingdee导入失败人员：" + email);
            return null;
        }

        return new KingdeePersonKeyValue(key, kingdeePerson);
    }

    private KingdeePerson departmentChanged(KingdeePerson kingdeePerson, LeUser leUser) {
        if (kingdeePerson.getDepartment().equals(leUser.getDepartment()) || !leUser.getStatus().equals("N")) {
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

        //只允许修改职员工信息
        if (flag && leUser.getStatus().equals("N")) {
            person.setOpenId(kingdeePerson.getOpenId());
            return person;
        } else {
            return null;
        }
    }

    private KingdeePerson statusChanged(KingdeePerson kingdeePerson, LeUser leUser) {
        boolean flag = false;
        String type = "";
        if (leUser.getStatus().equals("N")) {
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
        } else {
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
            if (user.getStatus().equals("N")) {
                array.add(JSONObject.toJSON(convert(user)));
            }
        }
        return array;
    }

    private JSONArray convertToUnUse(Collection<KingdeePerson> users) {
        JSONArray array = new JSONArray();
        for (KingdeePerson person: users) {
            if (null == person.getStatus()) {
                //正常-->禁用
                if (!person.getType().equals("3")) {
                    KingdeePerson p = new KingdeePerson();
                    p.setOpenId(person.getOpenId());
                    p.setType("3");
                    array.add(JSONObject.toJSON(p));
                    continue;
                }
            }

            if (person.getType() == null) {
                //正常-->禁用
                if (person.getStatus().equals("1")) {
                    KingdeePerson p = new KingdeePerson();
                    p.setOpenId(person.getOpenId());
                    p.setType("3");
                    array.add(JSONObject.toJSON(p));
                    continue;
                }
            }
        }
        return array;
    }
}
