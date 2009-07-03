package org.gitools.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType( propOrder={"name", "url" } )
public class Laboratory 
	implements Serializable {

	private static final long serialVersionUID = 6128418993564199357L;

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
