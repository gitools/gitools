package es.imim.bg.ztools.model;

public class Figure extends Artifact {

    /** Peu de figura **/
    private String figureText;
    
    public Figure(String id, String artifactType) {
	this(id, artifactType, null, null, null);
    }

    public Figure(String id, String artifactType, String title, String description,
	    String figureText) {
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
