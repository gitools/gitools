package es.imim.bg.ztools.model;

import es.imim.bg.ztools.table.ITableContents;

public class TableArtifact extends Artifact {

    /** The table associated to this artifact. */
    protected ITableContents tableContents;

    /** The group row ids belongs to. */
    protected Group group;

    public TableArtifact(String id, String artifactType) {
	this(id, artifactType, null, null, null, null);
    }

    public TableArtifact(String id, String artifactType, String title, String description, ITableContents table, Group group) {
	super(id, artifactType, title, description);
	tableContents = table;
	this.group = group;
    }

    protected ITableContents getTableContents() {
	return tableContents;
    }

    protected void setTableContents(ITableContents tableContents) {
	this.tableContents = tableContents;
    }

    protected Group getGroup() {
        return group;
    }

    protected void setGroup(Group group) {
        this.group = group;
    }
    
}
