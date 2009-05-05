package es.imim.bg.ztools.model;

public class Affiliation {

	private String labName;
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
		super();
		this.labName = labName;
		this.university = university;
		this.address = address;
	}

	public String getLabName() {
		return labName;
	}

	public void setLabName(String labName) {
		this.labName = labName;
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
