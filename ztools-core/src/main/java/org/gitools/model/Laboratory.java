package org.gitools.model;

import java.io.Serializable;

public class Laboratory 
	implements Serializable {

	/** Name of the laboratory **/
	private String laboratoryName;

	/** URL of the laboratory **/
	private String url;

	public Laboratory() {

	}

	public String getLaboratoryName() {
		return laboratoryName;
	}

	public void setLaboratoryName(String laboratoryName) {
		this.laboratoryName = laboratoryName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
