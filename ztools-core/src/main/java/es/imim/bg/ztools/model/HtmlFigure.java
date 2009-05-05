package es.imim.bg.ztools.model;

import java.io.Serializable;

public class HtmlFigure extends Figure implements Serializable {

	private String htmlCode;

	public HtmlFigure(String id, String artifactType) {
		super(id, artifactType);

	}

	public HtmlFigure(String id, String artifactType, String title) {
		super(id, artifactType, title);

	}

	public HtmlFigure(String id, String artifactType, String title,
			String description) {
		super(id, artifactType, title, description);

	}

	public String getHtmlCode() {
		return htmlCode;
	}

	public void setHtmlCode(String htmlCode) {
		this.htmlCode = htmlCode;
	}
}
