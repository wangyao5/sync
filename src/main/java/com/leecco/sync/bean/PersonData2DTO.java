package com.leecco.sync.bean;

import java.util.ArrayList;
import java.util.List;

public class PersonData2DTO {
    private String eid;

    private List<KingdeePerson> persons = new ArrayList<KingdeePerson>();

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public List<KingdeePerson> getPersons() {
        return persons;
    }

    public void setPersons(List<KingdeePerson> persons) {
        this.persons = persons;
    }

}
