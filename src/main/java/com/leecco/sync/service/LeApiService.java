package com.leecco.sync.service;

import com.leecco.sync.ApplicationProperties;
import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class LeApiService {
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private CommonService commonService;

    /**
     * @param startTime 起始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime   截止时间 yyyy-MM-dd HH:mm:ss
     * @description 获取时间范围内，更新的组织架构
     */
    public HttpEntity getLeOrgs(String startTime, String endTime, int page, int pageSize) {
        String syncOrgUrl = applicationProperties.getLeeccoSyncOrgUrl();
        String time = (System.currentTimeMillis() + "").substring(0, 10);
        String sign = commonService.getMD5("endtime=" + endTime + "&p=" + page + "&site=app&starttime=" + startTime + "&time=" + time + "8956dmqwqddsxwfvt977");
        String postUrl = null;
        try {
            postUrl = syncOrgUrl + "p=" + page + "&time=" + time + "&endtime=" + URLEncoder.encode(endTime, "utf-8") + "&starttime=" + URLEncoder.encode(startTime, "utf-8") + "&ps=" + pageSize + "&sign=" + sign;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return commonService.execPost(postUrl);
    }
}
