package es.imim.bg.ztools.model;

import java.io.Serializable;

public class HtmlFigure extends Figure implements Serializable{

    public HtmlFigure(String id, String artifactType) {
	super(id, artifactType);
	
    }

    public HtmlFigure(String id, String artifactType, String title, String description) {
	super(id, artifactType);
	
    }

}
