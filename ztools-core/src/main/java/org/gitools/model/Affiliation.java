package org.gitools.model;

import java.io.Serializable;

public class Affiliation 
		implements Serializable {

	private static final long serialVersionUID = -9120118831048874109L;
	
	private String laboratory;
	private String university;
	private String address;

	public Affiliation() {

	}

	public Affiliation(String labName) {
		this(labName, null, null);

	}

	public Affiliation(String labName, String university) {
		this(labName, university, null);
	}

	public Affiliation(String labName, String university, String address) {
		this.laboratory = labName;
		this.university = university;
		this.address = address;
	}

	public String getLaboratory() {
		return laboratory;
	}

	public void setLaboratory(String labName) {
		this.laboratory = labName;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
