package org.gitools.model;

import java.io.Serializable;

public class Affiliation implements Serializable {

	private static final long serialVersionUID = -9120118831048874109L;

	private String department;

	private String university;

	private String address;

	public Affiliation() {

	}
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

}
