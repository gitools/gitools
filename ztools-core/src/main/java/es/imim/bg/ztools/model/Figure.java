package es.imim.bg.ztools.model;

public class Figure extends Artifact {

	/** Peu de figura **/
	private String figureText;

	public Figure() {

	}

	public Figure(String id, String artifactType) {
		super(id, artifactType);
	}

	public Figure(String id, String artifactType, String title) {
		super(id, artifactType, title);
	}

	public Figure(String id, String artifactType, String title,
			String description) {
		super(id, artifactType, title, description);
	}

	public Figure(String id, String artifactType, String title,
			String description, String figureText) {
		super(id, artifactType, title, description);
		this.figureText = figureText;
	}

	public String getFigureText() {
		return figureText;
	}

	public void setFigureText(String figureText) {
		this.figureText = figureText;
	}
}
