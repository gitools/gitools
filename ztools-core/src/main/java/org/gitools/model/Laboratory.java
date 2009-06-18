package org.gitools.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "name", "url" })
public class Laboratory implements Serializable {

	/** Name of the laboratory **/
	private String name;

	/** URL of the laboratory **/
	private String url;

	/*
	 * Maybe we can also store to wich affiliation this lab belongs to and the
	 * laboratory address 
	 * private String address; 
	 * private Affiliation affiliation;
	 */
	public Laboratory() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
