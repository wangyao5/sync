package com.leecco.sync.bean;

import java.util.*;

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
    private boolean deepFindParent(Map<String, LeDeptNode> source ,LeDeptNode currentNode){
        LeDeptNode node = source.get(currentNode.getpNum());
        if (null == node) {
            Collection<LeDeptNode> sourceLeDeptNode = source.values();
            for(LeDeptNode sourceChild : sourceLeDeptNode) {
                if(deepFindParent(sourceChild.getNodes(), currentNode)) {
                    sourceChild.getNodes().put(currentNode.getOrgNum(), currentNode);
                }
            }
            return false;
        } else {
            node.getNodes().put(currentNode.getOrgNum(), currentNode);
            return true;
        }
    }

    public void combin() {
        Collection<LeDeptNode> allOrgLeDeptNode = allLeOrgs.values();
        for (LeDeptNode currentNode : allOrgLeDeptNode) {
            allLeOrgs.remove(currentNode.getOrgNum());
            LeDeptNode node = currentNode;
            while(true) {
                LeDeptNode parentNode = allLeOrgs.get(node.getpNum());
                if (null != parentNode) {
                    parentNode.getNodes().put(node.getOrgNum(), node);
                    node = parentNode;
                    allLeOrgs.remove(node.getOrgNum());
                } else {
                    //1、是否存在于allLeOrgs的子节点中
                    if (deepFindParent(allLeOrgs, node)) {
                        break;
                    }

                    //2、是否存在于treeMap中
                    if (deepFindParent(treeMap, node)) {
                        break;
                    }

                    //3、该节点存在于delTreeMap中
                    if (deepFindParent(delTreeMap, node)) {
                        break;
                    }

                    //4、出现错误，存在游离的节点
                    System.out.println("org_num:" + node.getOrgNum() + ";org_name:" + node.getName());
                    break;
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
