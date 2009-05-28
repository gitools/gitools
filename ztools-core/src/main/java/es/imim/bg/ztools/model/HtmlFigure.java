package es.imim.bg.ztools.model;

import java.io.Serializable;

public class HtmlFigure extends Figure implements Serializable {

	private String htmlCode;

	public HtmlFigure() {
		super();
	}

	public String getHtmlCode() {
		return htmlCode;
	}

	public void setHtmlCode(String htmlCode) {
		this.htmlCode = htmlCode;
	}
}
