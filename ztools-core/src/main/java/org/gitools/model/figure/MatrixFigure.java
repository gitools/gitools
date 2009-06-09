package org.gitools.model.figure;

import java.awt.Color;
import java.io.Serializable;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.HeaderDecorator;
import org.gitools.model.decorator.impl.SimpleHeaderDecorator;
import org.gitools.model.matrix.IMatrixView;

public class MatrixFigure 
		extends Figure
		implements Serializable {

	private static final long serialVersionUID = 325437934312047512L;

	public static final String CELL_DECORATOR_CHANGED = "cellDecorator";
	public static final String ROW_DECORATOR_CHANGED = "rowDecorator";
	public static final String COLUMN_DECORATOR_CHANGED = "columnDecorator";
	public static final String TABLE_CHANGED = "table";
	
	private ElementDecorator cellDecorator;
	private HeaderDecorator rowDecorator;
	private HeaderDecorator columnDecorator;
	
	private IMatrixView matrixView;
	
	private boolean showGrid;
	private Color gridColor;
	private int cellSize;
	
	public MatrixFigure(
			IMatrixView matrixView,
			ElementDecorator cellDecorator,
			HeaderDecorator rowsDecorator,
			HeaderDecorator columnsDecorator) {
		
		this.matrixView = matrixView;
		this.cellDecorator = cellDecorator;
		this.rowDecorator = rowsDecorator;
		this.columnDecorator = columnsDecorator;
		this.showGrid = true;
		this.gridColor = Color.WHITE;
		this.cellSize = 18;
	}
	
	public final ElementDecorator getCellDecorator() {
		return cellDecorator;
	}
	
	public final void setCellDecorator(ElementDecorator decorator) {
		final ElementDecorator old = this.cellDecorator;	
		this.cellDecorator = decorator;
		firePropertyChange(CELL_DECORATOR_CHANGED, old, decorator);
	}
	
	public HeaderDecorator getRowDecorator() {
		return rowDecorator;
	}
	
	public void setRowDecorator(HeaderDecorator rowDecorator) {
		final HeaderDecorator old = this.rowDecorator;
		this.rowDecorator = rowDecorator;
		firePropertyChange(ROW_DECORATOR_CHANGED, old, rowDecorator);
	}
	
	public HeaderDecorator getColumnDecorator() {
		return columnDecorator;
	}
	
	public void setColumnDecorator(HeaderDecorator columnDecorator) {
		final HeaderDecorator old = this.columnDecorator;
		this.columnDecorator = columnDecorator;
		firePropertyChange(COLUMN_DECORATOR_CHANGED, old, columnDecorator);
	}
	
	public final IMatrixView getMatrixView() {
		return matrixView;
	}
	
	public final void setMatrixView(IMatrixView matrixView) {
		this.matrixView = matrixView;
		firePropertyChange(TABLE_CHANGED);
	}
	
	public boolean isShowGrid() {
		return showGrid;
	}
	
	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getGridColor() {
		return gridColor;
	}
	
	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public int getCellSize() {
		return cellSize;
	}
	
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
		firePropertyChange(PROPERTY_CHANGED);
	}
}
