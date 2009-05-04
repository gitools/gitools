package es.imim.bg.ztools.model;

import es.imim.bg.ztools.table.ITableContents;

public class TableArtifact extends Artifact {

    /** The table associated to this artifact. */
    protected ITableContents tableContents;

    /** The group rows and columns ids belongs to. */
    protected Group rowsGroup;
    protected Group columnsGroup;
     
    public TableArtifact(String id, String artifactType) {
	this(id, artifactType, null, null, null, null);
    }

    public TableArtifact(String id, String artifactType, String title, String description, Group group) {
	super(id, artifactType, title, description);
	tableContents = null;
	this.rowsGroup = group;
    }
    
    public TableArtifact(String id, String artifactType, String title, String description, ITableContents table, Group group) {
	super(id, artifactType, title, description);
	tableContents = table;
	this.rowsGroup = group;
    }

    public ITableContents getTableContents() {
	return tableContents;
    }

    public void setTableContents(ITableContents tableContents) {
	this.tableContents = tableContents;
    }

    public Group getRowsGroup() {
        return rowsGroup;
    }

    public void setRowsGroup(Group rowsGroup) {
        this.rowsGroup = rowsGroup;
    }

    public Group getColumnsGroup() {
        return columnsGroup;
    }

    public void setColumnsGroup(Group columnsGroup) {
        this.columnsGroup = columnsGroup;
    }

}
