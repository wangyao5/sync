package com.leecco.sync.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kingdee.letv.sync.been.DeptDTO;
import com.kingdee.letv.sync.been.Person;
import com.kingdee.letv.sync.been.PersonAllInfo;
import com.leecco.sync.ApplicationProperties;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class KingdeeApiService {
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private CommonService commonService;


    public String getKingdeeKey() {
        return applicationProperties.getKingdeeKey();
    }

    /**
     * @description 获取kingdee中的部门信息
     */
    public Map<String, JSONObject> getKindeeOrgs() {
        String getOrgUrl = applicationProperties.getKingdeeHost() + "openaccess/input/dept/getall";

        Map<String, JSONObject> orgs = new HashMap<String, JSONObject>();
        DeptDTO dat = new DeptDTO();
        dat.setEid(applicationProperties.getKingdeeKey());
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("eid", applicationProperties.getKingdeeKey()));
        nvps.add(new BasicNameValuePair("data", commonService.encrypt(JSON.toJSONString(dat))));
        UrlEncodedFormEntity reqEntity = null;
        try {
            reqEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpEntity resEntity = commonService.execPost(getOrgUrl, reqEntity);
        String reponse = null;
        try {
            reponse = EntityUtils.toString(resEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (reponse != null) {
            JSONObject responseJSONObject = JSON.parseObject(reponse);
            JSONArray departmentJSONArray = responseJSONObject.getJSONArray("data");
            for (int index = 0; index < departmentJSONArray.size(); index++) {
                JSONObject departmentJSONObject = departmentJSONArray.getJSONObject(index);
                if (departmentJSONObject.get("name") != null && departmentJSONObject.getString("department") != null) {
                    orgs.put(departmentJSONObject.get("department").toString(), departmentJSONObject);
                }
            }
        }
        return orgs;
    }

    public boolean addDepartment() {
        String addOrgUrl = applicationProperties.getKingdeeHost() + "openaccess/input/dept/add";

        return true;
    }

    public boolean delDepartment() {
        String delOrgUrl = applicationProperties.getKingdeeHost() + "openaccess/input/dept/delete";
        return true;
    }

    public boolean updateDepartment() {
        String updateOrgUrl = applicationProperties.getKingdeeHost() + "openaccess/input/dept/update";
        return true;
    }

    public Map<String, Person> getAllperson() {
        Map<String, Person> allPerson = new HashMap<String, Person>();
        int index = 0;
        int pageSize = 1000;

        JSONArray personJSONArray = null;
        //循环迭代获取人员信息
        do {
            personJSONArray = getPersonByIndexAndSize(index, pageSize);
            for (int arrIndex = 0; arrIndex < personJSONArray.size(); arrIndex++) {
                JSONObject obj = personJSONArray.getJSONObject(arrIndex);
                Person person = new Person();
                person.setEmail(obj.getString("email"));
                person.setName(obj.getString("name"));
                person.setOpenId(obj.getString("openId"));
                person.setJobTitle(obj.getString("jobTitle"));
                person.setDepartment(obj.getString("department"));
                person.setStatus(obj.getString("status"));
                String phone = obj.getString("phone");
                person.setPhone(phone);
                if (phone != null) {
                    allPerson.put(phone, person);
                }
            }
            index = index + pageSize;
        } while (personJSONArray.size() == pageSize);

        return allPerson;
    }

    private JSONArray getPersonByIndexAndSize(int index, int size) {
        String getPersonUrl = applicationProperties.getKingdeeHost() + "/openaccess/input/person/getall";
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));
        nvps.add(new BasicNameValuePair("eid", applicationProperties.getKingdeeKey()));
        PersonAllInfo info = new PersonAllInfo();
        info.setEid(applicationProperties.getKingdeeKey());
        info.setBegin(index);
        info.setCount(index + size);
        nvps.add(new BasicNameValuePair("data", JSON.toJSONString(info)));
        UrlEncodedFormEntity reqEntity = null;
        try {
            reqEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpEntity entity = commonService.execPost(getPersonUrl, reqEntity);

        String responseString = null;
        try {
            responseString = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject psersionJSONObject = JSON.parseObject(responseString);

        return psersionJSONObject.getJSONArray("data");
    }

}