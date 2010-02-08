package org.gitools.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Figure extends Artifact {

	private static final long serialVersionUID = -5908048128953551645L;

	/** Footer **/
	private String footer;

	public Figure() {
		super();
	}

	public Figure(Figure figure) {
		super(figure);
		
		this.footer = figure.getFooter();
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}
}