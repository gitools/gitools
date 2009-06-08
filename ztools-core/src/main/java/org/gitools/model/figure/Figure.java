package org.gitools.model.figure;

import org.gitools.model.Artifact;

public class Figure extends Artifact {

	private static final long serialVersionUID = -5908048128953551645L;

	/** Footer **/
	private String footer;

	public Figure() {
		super();
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}
}