package com.leecco.sync;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class ApplicationProperties {
    private String kingdeeHost;
    private String kingdeeKey;
    private String kingdeeKeyFilePath;
    private String leeccoSyncOrgUrl;
    private String leeccoSyncUserUrl;
    private boolean leeccoIsrepair;

    @Value("${spring.kingdee.openHost}")
    public void setKingdeeHost(String kingdeeHost) {
        this.kingdeeHost = kingdeeHost;
    }

    public String getKingdeeHost() {
        return kingdeeHost;
    }

    public String getKingdeeKey() {
        return kingdeeKey;
    }

    @Value("${spring.kingdee.key}")
    public void setKingdeeKey(String kingdeeKey) {
        this.kingdeeKey = kingdeeKey;
    }

    public String getLeeccoSyncOrgUrl() {
        return leeccoSyncOrgUrl;
    }

    @Value("${srping.leecco.sync_orgUrl}")
    public void setLeeccoSyncOrgUrl(String leeccoSyncOrgUrl) {
        this.leeccoSyncOrgUrl = leeccoSyncOrgUrl;
    }

    public String getLeeccoSyncUserUrl() {
        return leeccoSyncUserUrl;
    }

    @Value("${spring.leecco.sync_userUrl}")
    public void setLeeccoSyncUserUrl(String leeccoSyncUserUrl) {
        this.leeccoSyncUserUrl = leeccoSyncUserUrl;
    }

    public boolean getLeeccoIsrepair() {
        return leeccoIsrepair;
    }

    @Value("${spring.leecco.isrepair}")
    public void setLeeccoIsrepair(boolean leeccoIsrepair) {
        this.leeccoIsrepair = leeccoIsrepair;
    }

    public String getKingdeeKeyFilePath() {
        return kingdeeKeyFilePath;
    }

    @Value("${spring.kingdee.key.file_path}")
    public void setKingdeeKeyFilePath(String kingdeeKeyFilePath) {
        this.kingdeeKeyFilePath = kingdeeKeyFilePath;
    }
}
