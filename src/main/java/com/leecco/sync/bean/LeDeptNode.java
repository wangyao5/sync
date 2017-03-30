package com.leecco.sync.bean;

import java.util.Map;

public class LeDeptNode {
    private String orgNum;
    private String name;
    private String pNum;
    private int level;
    private int effect;
    private Map<String, LeDeptNode> nodes;

    public String getOrgNum() {
        return orgNum;
    }

    public void setOrgNum(String orgNum) {
        this.orgNum = orgNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getpNum() {
        return pNum;
    }

    public void setpNum(String pNum) {
        this.pNum = pNum;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getEffect() {
        return effect;
    }

    public void setEffect(int effect) {
        this.effect = effect;
    }

    public Map<String, LeDeptNode> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, LeDeptNode> nodes) {
        this.nodes = nodes;
    }
}
