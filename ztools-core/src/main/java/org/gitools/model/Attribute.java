package org.gitools.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/** Annotation for items, adds a dynamic attribute to the desired item **/

@XmlAccessorType(XmlAccessType.FIELD)
public class Attribute 
	implements Serializable{ 

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
