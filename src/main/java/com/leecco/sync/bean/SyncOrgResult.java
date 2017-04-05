package com.leecco.sync.bean;

public class SyncOrgResult {
    LeOrg leOrg;
    String addOrgKingdeeMessage;
    String delOrgKingdeeMessage;

    public LeOrg getLeOrg() {
        return leOrg;
    }

    public void setLeOrg(LeOrg leOrg) {
        this.leOrg = leOrg;
    }

    public String getAddOrgKingdeeMessage() {
        return addOrgKingdeeMessage;
    }

    public void setAddOrgKingdeeMessage(String addOrgKingdeeMessage) {
        this.addOrgKingdeeMessage = addOrgKingdeeMessage;
    }

    public String getDelOrgKingdeeMessage() {
        return delOrgKingdeeMessage;
    }

    public void setDelOrgKingdeeMessage(String delOrgKingdeeMessage) {
        this.delOrgKingdeeMessage = delOrgKingdeeMessage;
    }
}
