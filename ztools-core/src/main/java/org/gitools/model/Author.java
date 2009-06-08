package org.gitools.model;

import java.io.Serializable;

public class Author
		implements Serializable {

	private static final long serialVersionUID = -1132892392275362936L;

	private String name;
	private String surnames;
	private Affiliation affiliation;

	public Author() {

	}

	public Author(String name) {
		this(name, null, null);
	}

	public Author(String name, String surnames) {
		this(name, surnames, null);
	}

	public Author(String name, String surnames, Affiliation affiliation) {
		this.name = name;
		this.surnames = surnames;
		this.affiliation = affiliation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurnames() {
		return surnames;
	}

	public void setSurnames(String surnames) {
		this.surnames = surnames;
	}

	public void setAffiliation(Affiliation affiliation) {
		this.affiliation = affiliation;
	}

	public Affiliation getAffiliation() {
		return affiliation;
	}

}
