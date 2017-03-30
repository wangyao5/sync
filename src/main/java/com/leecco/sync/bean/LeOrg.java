package com.leecco.sync.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LeOrg {
    private Map<String, LeDeptNode> treeMap = new HashMap<>();
    private Map<String, LeDeptNode> delTreeMap = new HashMap<>();
    private Map<String, LeDeptNode> allLeOrgs = new HashMap<>();

    public void add(LeDeptNode node) {
        if (node.getEffect() != 1) {
            delTreeMap.put(node.getOrgNum(), node);
            return;
        }

        //如果节点值为一个空格，就是根组织
        if (" ".equals(node.getpNum())) {
            treeMap.put(node.getOrgNum(), node);
        } else {
            allLeOrgs.put(node.getOrgNum(), node);
        }
    }

    /**
     * @description 深度遍历查找父节点，并绑定子节点到父节点
     * */
    private boolean deepFindParent(LeDeptNode treeNode, LeDeptNode currentNode){
        if () {
            
        }
        return false;
    }

    public void combin() {
        Set<String> allOrgNumkeys = allLeOrgs.keySet();
        for (String allOrgKey : allOrgNumkeys) {
            LeDeptNode node = allLeOrgs.get(allOrgKey);
            allLeOrgs.remove(allOrgKey);
            while (true) {
                LeDeptNode parentNode = allLeOrgs.get(node.getpNum());
                //上级节点是否存在于allLeOrgs中
                if (null == parentNode) {
                    //1、是否存在于allLeOrgs的子节点中

                    //存在就，修改节点信息，然后break;

                    //2、是否存在于treeMap中

                    //存在就修改节点信息，然后break;

                } else {
                    parentNode.getNodes().put(parentNode.getOrgNum(), node);
                    node = parentNode;
                    allLeOrgs.put(node.getOrgNum(), node);
                }
            }
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
