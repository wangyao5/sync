package com.leecco.sync.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leecco.sync.bean.KingdeePerson;
import com.leecco.sync.ApplicationProperties;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
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

    /**
     * @description 获取kingdee中的部门信息
     */
    public Map<String, JSONObject> getKindeeOrgs() {
        String getOrgUrl = applicationProperties.getKingdeeHost() + "openaccess/input/dept/getall";

        Map<String, JSONObject> orgs = new HashMap<String, JSONObject>();
        JSONObject queryJSONObject = new JSONObject();
        queryJSONObject.put("eid", applicationProperties.getKingdeeKey());
        String queryInfo = queryJSONObject.toJSONString();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));
        nvps.add(new BasicNameValuePair("eid", applicationProperties.getKingdeeKey()));
        nvps.add(new BasicNameValuePair("data", commonService.encrypt(queryInfo)));
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

    public String addDepartment(JSONArray departments) throws IOException, ParseException {
        JSONObject o = new JSONObject();
        o.put("eid", applicationProperties.getKingdeeKey());
        o.put("departments", departments);

        String addOrgUrl = applicationProperties.getKingdeeHost() + "openaccess/input/dept/add";
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));
        nvps.add(new BasicNameValuePair("eid", applicationProperties.getKingdeeKey()));
        nvps.add(new BasicNameValuePair("data", commonService.encrypt(JSON.toJSONString(o))));
        UrlEncodedFormEntity reqEntity = null;
        try {
            reqEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpEntity resEntity = commonService.execPost(addOrgUrl, reqEntity);
        return EntityUtils.toString(resEntity);
    }

    public String delDepartment(JSONArray departments) throws IOException, ParseException {
        JSONObject o = new JSONObject();
        o.put("eid", applicationProperties.getKingdeeKey());
        o.put("departments", departments);

        String delOrgUrl = applicationProperties.getKingdeeHost() + "openaccess/input/dept/delete";
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));
        nvps.add(new BasicNameValuePair("eid", applicationProperties.getKingdeeKey()));
        nvps.add(new BasicNameValuePair("data", commonService.encrypt(JSONArray.toJSONString(o))));
        UrlEncodedFormEntity reqEntity = null;
        try {
            reqEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpEntity resEntity = commonService.execPost(delOrgUrl, reqEntity);
        return EntityUtils.toString(resEntity);
    }

    public Map<String, KingdeePerson> getAllperson() {
        Map<String, KingdeePerson> allPerson = new HashMap<String, KingdeePerson>();
        int index = 0;
        int pageSize = 1000;

        JSONArray personJSONArray = null;
        //循环迭代获取人员信息
        do {
            personJSONArray = getPersonByIndexAndSize(index, pageSize);
            for (int arrIndex = 0; arrIndex < personJSONArray.size(); arrIndex++) {
                JSONObject obj = personJSONArray.getJSONObject(arrIndex);
                KingdeePerson person = new KingdeePerson();
                String email = obj.getString("email");
                person.setEmail(email);
                person.setName(obj.getString("name"));
                person.setOpenId(obj.getString("openId"));
                person.setJobTitle(obj.getString("jobTitle"));
                person.setDepartment(obj.getString("department"));
                person.setStatus(obj.getString("status"));
                String phone = obj.getString("phone");
                person.setPhone(phone);

                if (null != phone) {
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

        JSONObject queryJSONObject = new JSONObject();
        queryJSONObject.put("eid", applicationProperties.getKingdeeKey());
        queryJSONObject.put("begin", index);
        queryJSONObject.put("count", size);
        String queryInfo = queryJSONObject.toJSONString();
        nvps.add(new BasicNameValuePair("data", commonService.encrypt(queryInfo)));
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

    public JSONArray updateUserInfo(JSONArray info) {
        String updateInfoUrl = applicationProperties.getKingdeeHost() + "/openaccess/input/person/updateInfo";
        return callUserApiWithUrlAndData(updateInfoUrl, info);
    }

    public JSONArray updateUserDepartment(JSONArray departments) {
        String updateDeptUrl = applicationProperties.getKingdeeHost() + "/openaccess/input/person/updateDept";
        return callUserApiWithUrlAndData(updateDeptUrl, departments);
    }

    public JSONArray updateUserStatus(JSONArray status) {
        String updateStatusUrl = applicationProperties.getKingdeeHost() + "/openaccess/input/person/updateStatus";
        return callUserApiWithUrlAndData(updateStatusUrl, status);
    }
    /**test*/
    public JSONArray addUsers(JSONArray users) {
        String updateStatusUrl = applicationProperties.getKingdeeHost() + "/openaccess/input/person/add";
        return callUserApiWithUrlAndData(updateStatusUrl, users);
    }

    /**kingdee错误接口,调用删除跟禁用一个效果*/
    public JSONArray delUsers(JSONArray users) {
        JSONArray testData = new JSONArray();
        testData.add("0e7ef854-91c6-11e6-9ff4-5cb9018cfd68");
        return callDelUsers(testData);
    }

    private JSONArray callDelUsers(JSONArray users) {
        JSONArray resultArray = new JSONArray();
        int pageCount = (int) Math.ceil(((double) users.size()) / 1000);
        int page = 0;
        int size = 1000;
        while (page < pageCount) {
            JSONObject object = new JSONObject();
            object.put("eid", applicationProperties.getKingdeeKey());
            JSONArray openIdJSONArray = new JSONArray();
            if ((page + 1) * size > users.size()) {
                openIdJSONArray.addAll(users.subList(page * size, users.size()));
            } else {
                openIdJSONArray.addAll(users.subList(page * size, (page + 1) * size));
            }
            object.put("openIds", openIdJSONArray);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));
            nvps.add(new BasicNameValuePair("eid", applicationProperties.getKingdeeKey()));
            nvps.add(new BasicNameValuePair("data", commonService.encrypt(object.toJSONString())));
            UrlEncodedFormEntity reqEntity = null;
            try {
                reqEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String url = applicationProperties.getKingdeeHost() + "/openaccess/input/person/delete";
            HttpEntity resEntity = commonService.execPost(url, reqEntity);
            String responseString = null;
            try {
                responseString = EntityUtils.toString(resEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            resultArray.add(responseString);
            page++;
        }
        return resultArray;
    }


    private JSONArray callUserApiWithUrlAndData(String url, JSONArray users) {
        JSONArray resultArray = new JSONArray();
        int pageCount = (int) Math.ceil(((double) users.size()) / 1000);
        int page = 0;
        int size = 1000;
        while (page < pageCount) {
            JSONObject object = new JSONObject();
            object.put("eid", applicationProperties.getKingdeeKey());
            JSONArray updateJSONArray = new JSONArray();
            if ((page + 1) * size > users.size()) {
                updateJSONArray.addAll(users.subList(page * size, users.size()));
            } else {
                updateJSONArray.addAll(users.subList(page * size, (page + 1) * size));
            }
            object.put("persons", updateJSONArray);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));
            nvps.add(new BasicNameValuePair("eid", applicationProperties.getKingdeeKey()));
            nvps.add(new BasicNameValuePair("data", commonService.encrypt(object.toJSONString())));
            UrlEncodedFormEntity reqEntity = null;
            try {
                reqEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            HttpEntity resEntity = commonService.execPost(url, reqEntity);
            String responseString = null;
            try {
                responseString = EntityUtils.toString(resEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            resultArray.add(responseString);
            page++;
        }
        return resultArray;
    }
}
