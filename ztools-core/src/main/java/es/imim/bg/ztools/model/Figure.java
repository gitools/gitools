package es.imim.bg.ztools.model;

public class Figure extends Artifact {

    /** Peu de figura **/
    private String FigureText;
    
    public Figure(String id, String artifactType) {
	this(id, artifactType, null);
    }
    
    public Figure(String id, String artifactType, String figureText) {
	super(id, artifactType);
	this.FigureText = figureText; 
    }

    protected String getFigureText() {
        return FigureText;
    }
    
    protected void setFigureText(String figureText) {
        FigureText = figureText;
    }
    
}
