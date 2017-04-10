package com.leecco.sync.bean;

import java.util.*;

public class LeOrg {
    private Map<String, LeDeptNode> treeMap = new HashMap<>();
    private Map<String, LeDeptNode> allLeOrgs = new HashMap<>();
    private Map<String, LeDeptNode> leOrgsBack = new HashMap<>();

    //Leecco中的脏数据
    private Map<String, LeDeptNode> dirtyOrgs = new HashMap<>();

    public void add(LeDeptNode node) {

        //如果节点值为一个空格，就是根组织
        if (" ".equals(node.getpNum())) {
            treeMap.put(node.getOrgNum(), node);
            leOrgsBack.put(node.getOrgNum(), node);
        } else {
            allLeOrgs.put(node.getOrgNum(), node);
            leOrgsBack.put(node.getOrgNum(), node);
        }
    }

    private void bindNode(LeDeptNode parent, LeDeptNode node) {
        allLeOrgs.remove(node.getOrgNum());
        Map<String, LeDeptNode> nodes = parent.getNodes();
        if (null == nodes) {
            nodes = new HashMap<>();
        }
        nodes.put(node.getOrgNum(), node);
        parent.setNodes(nodes);
    }

    /**
     * @description 深度遍历查找父节点，并绑定子节点到父节点
     */
    private void deepFindParent(Map<String, LeDeptNode> source, LeDeptNode currentNode) {
        LeDeptNode node = source.get(currentNode.getpNum());
        if (null == node) {
            Collection<LeDeptNode> sourceLeDeptNode = source.values();
            for (LeDeptNode sourceChild : sourceLeDeptNode) {
                if (null != sourceChild.getNodes()) {
                    deepFindParent(sourceChild.getNodes(), currentNode);
                }
            }
        } else {
            bindNode(node, currentNode);
        }
    }

    public void combinToTree() {
        while (allLeOrgs.size() > 0) {
            Collection<LeDeptNode> co = allLeOrgs.values();
            LeDeptNode node = co.iterator().next();

            while (true) {
                LeDeptNode parentNode = allLeOrgs.get(node.getpNum());
                if (null != parentNode) {
                    bindNode(parentNode, node);
                    node = parentNode;
                } else {
                    //1、是否存在于allLeOrgs的子节点中
                    deepFindParent(allLeOrgs, node);

                    //2、是否存在于treeMap中
                    deepFindParent(treeMap, node);

                    //3、出现错误，存在游离的节点
                    if (allLeOrgs.get(node.getOrgNum()) != null) {
                        System.out.println("org_num:" + node.getOrgNum() + ";org_name:" + node.getName());

                        if (leOrgsBack.get(node.getpNum()) != null) {
                            System.out.println("not found this data!");
                            dirtyOrgs.put(node.getOrgNum(), node);
                        }
                        allLeOrgs.remove(node.getOrgNum());
                    }
                    break;
                }

            }
        }
    }

    public Map<String, Boolean> getLeOrgsWithFullPathAndStatus() {
        Map<String, Boolean> fullPathAndStatusMap = new HashMap<>();
        deepFindFullPath(fullPathAndStatusMap, treeMap, "");
        return fullPathAndStatusMap;
    }

    private void deepFindFullPath(Map<String, Boolean> fullPathAndStatusMap, Map<String, LeDeptNode> node, String parentName) {
        if (node != null) {
            Collection<LeDeptNode> childCollection = node.values();
            for (LeDeptNode childNode : childCollection) {
                String path = "";
                if (parentName.equals("")) {
                    path = childNode.getName();
                } else {
                    path = parentName + "\\" + childNode.getName();
                }
                fullPathAndStatusMap.put(path, childNode.getEffect() == 1);
                if (null != childNode.getNodes()) {
                    deepFindFullPath(fullPathAndStatusMap, childNode.getNodes(), path);
                }
            }
        }
    }

    public Map<String, LeDeptNode> getTreeMap() {
        return treeMap;
    }

    public Map<String, LeDeptNode> getLeOrgsBack() {
        return leOrgsBack;
    }
}
