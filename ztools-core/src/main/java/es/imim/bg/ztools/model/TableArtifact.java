package es.imim.bg.ztools.model;

import java.util.HashMap;
import java.util.List;

import es.imim.bg.ztools.table.ITableContents;
import es.imim.bg.ztools.table.element.IElementProperty;

public class TableArtifact extends Artifact {

	/** The table associated to this artifact. */
	protected ITableContents tableContents;

	/** The group of the rows **/
	protected Group rowsGroup;

	/** The group of the columns **/
	protected Group columnsGroup;

	/** The item list of the table */
	protected HashMap<Long, Item> items;

	/** Analysis to which the table belongs to **/
	protected AnalysisArt analysis;

	/**
	 * Properties of each cell of the table. As we describe cells as POJO
	 * elements, we need some information about the contents of the POJO
	 * elements
	 **/
	protected List<IElementProperty> cellProperties;

	public TableArtifact(String id, String artifactType) {
		super(id, artifactType);
	}

	public TableArtifact(String id, String artifactType, String title) {
		super(id, artifactType, title);

	}

	public TableArtifact(String id, String artifactType, String title,
			String description) {
		super(id, artifactType, title, description);

	}

	public TableArtifact(String id, String artifactType, String title,
			String description, ITableContents table, Group rowsGroup,
			Group columnsGroup) {
		super(id, artifactType, title, description);
		tableContents = table;
		this.rowsGroup = rowsGroup;
		this.columnsGroup = columnsGroup;
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

	public HashMap<Long, Item> getItems() {
		return items;
	}

	public void setItems(HashMap<Long, Item> items) {
		this.items = items;
	}

	public List<IElementProperty> getCellProperties() {
		return cellProperties;
	}

	public void setCellProperties(List<IElementProperty> cellProperties) {
		this.cellProperties = cellProperties;
	}

	public AnalysisArt getAnalysis() {
		return analysis;
	}

	public void setAnalysis(AnalysisArt analysis) {
		this.analysis = analysis;
	}

}
