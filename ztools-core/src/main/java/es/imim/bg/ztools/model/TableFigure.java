package es.imim.bg.ztools.model;

public class TableFigure extends Figure {
	// TODO: pendent.
	protected TableArtifact artifactTable;
	//protected Property proptoShow;
	// Items to show?
	// should i use items???
	
	public TableFigure(String id, String artifactType, String figureText,
			TableArtifact artifactTable) {
		super(id, artifactType);
		this.artifactTable = artifactTable;
	}

}
