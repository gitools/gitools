package org.gitools.model.figure;

import java.io.Serializable;

public class HtmlFigure 
		extends Figure 
		implements Serializable {

	private static final long serialVersionUID = 2985803897669366351L;

	private String htmlCode;

	public HtmlFigure() {
	}

	public String getHtmlCode() {
		return htmlCode;
	}

	public void setHtmlCode(String htmlCode) {
		this.htmlCode = htmlCode;
	}
}
