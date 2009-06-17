package org.gitools.model;

/** Annotation for items, adds a dynamic attribute to the desired item **/
public class Attribute {

	/** Name **/
	private String name;

	/** Value **/
	private String value;

	public Attribute() {
	}

	public Attribute(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
