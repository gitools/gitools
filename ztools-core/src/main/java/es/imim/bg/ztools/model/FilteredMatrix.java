package es.imim.bg.ztools.model;

import java.util.Map;

public class FilteredMatrix extends TableFigure {
	/**
	 * Represents a matrix filtered, original data or final resuts of an
	 * analysis
	 */
	protected TableArtifact artifactTable;

	/** Id of the Decorator to use **/
	protected String decoratorName;

	/** Specific set up for the Decorator **/
	protected Map<String, String> decProperties;

	/** Positions of the columns to show **/
	protected int[] columnsToShow;

	public FilteredMatrix() {
		super();
	}

	public int[] getColumnsToShow() {
		return columnsToShow;
	}

	public void setColumnsToShow(int[] columnsToShow) {
		this.columnsToShow = columnsToShow;
	}

	public String getDecoratorName() {
		return decoratorName;
	}

	public void setDecoratorName(String decoratorName) {
		this.decoratorName = decoratorName;
	}

}
