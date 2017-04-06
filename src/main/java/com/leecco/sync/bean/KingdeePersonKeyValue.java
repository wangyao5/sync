package com.leecco.sync.bean;


public class KingdeePersonKeyValue {

    private String key;
    private KingdeePerson value;

    public KingdeePersonKeyValue(String key, KingdeePerson value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public KingdeePerson getValue() {
        return value;
    }

    public void setValue(KingdeePerson value) {
        this.value = value;
    }
}
