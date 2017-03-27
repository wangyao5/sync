package com.kingdee.letv.sync.been;

import java.util.ArrayList;
import java.util.List;

public class PersonData2DTO {
	private String eid;
	
	private List<Person> persons = new ArrayList<Person>();
	
	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

}
