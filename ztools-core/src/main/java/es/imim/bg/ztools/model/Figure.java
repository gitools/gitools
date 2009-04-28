package es.imim.bg.ztools.model;

public class Figure extends Artifact {

    /** Peu de figura **/
    private String FigureText;
    
    public Figure(String id, String artifactType) {
	this(id, artifactType, null, null, null);
    }
    
    public Figure(String id, String artifactType, String title, String description, String figureText) {
	super(id, artifactType, title, description);
	this.FigureText = figureText; 
    }

    protected String getFigureText() {
        return FigureText;
    }
    
    protected void setFigureText(String figureText) {
        FigureText = figureText;
    }
    
}
