package es.imim.bg.ztools.model;


 public class TableFigure extends Figure{

     protected TableArtifact artifactTable;
     
    public TableFigure(String id, String artifactType, String figureText, TableArtifact artifactTable) {
	super(id, artifactType);
	this.artifactTable = artifactTable;
    }

}
