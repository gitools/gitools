package org.gitools.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType( propOrder= {"name", "surnames", "contribution", "affiliation" } )
public class Author
		implements Serializable {

	private static final long serialVersionUID = -1132892392275362936L;

	private String name;
	
	/** Composed surnames should be considered as one **/
	private String surnames;
	
	/** The affiliation this author belongs to **/
	private Affiliation affiliation;

	/** True if this author should be considered as a corresponding author**/
	boolean contribution;
	
	
	public Author() {

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
