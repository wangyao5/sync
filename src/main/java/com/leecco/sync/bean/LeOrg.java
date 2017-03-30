package com.leecco.sync.bean;

import org.apache.commons.lang.StringUtils;
import java.util.HashMap;
import java.util.Map;

public class LeOrg {
    private Map<String, LeDeptNode> treeMap = new HashMap<>();
    private Map<String, LeDeptNode> delTreeMap = new HashMap<>();
    private Map<String, LeDeptNode> allLeOrgs = new HashMap<>();

    public void add(LeDeptNode node) {
        if (node.getEffect() != 1) {
            delTreeMap.put(node.getOrgNum(), node);
            return;
        }

        //如果没有父节点,该节点为根节点
        if (StringUtils.isEmpty(node.getpNum())) {
            treeMap.put(node.getOrgNum(), node);
        } else {
            allLeOrgs.put(node.getOrgNum(), node);
        }
    }

    public void combin() {
        for (int index = 0; index < allLeOrgs.size(); index++) {

        }
    }

    public Map<String, LeDeptNode> getTreeMap() {
        return treeMap;
    }

    public Map<String, LeDeptNode> getDelTreeMap() {
        return delTreeMap;
    }

    public Map<String, LeDeptNode> getAllLeOrgs() {
        return allLeOrgs;
    }
}
