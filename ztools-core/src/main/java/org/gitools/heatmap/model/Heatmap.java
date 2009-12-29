package org.gitools.heatmap.model;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementProperty;
import org.gitools.model.Figure;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.model.xml.adapter.ColorXmlAdapter;
import org.gitools.model.xml.adapter.ElementDecoratorXmlAdapter;

@XmlType( propOrder={
		"cellDecorator",
		"rowHeader",
		"columnHeader",
		"matrixView",
		"showGrid", 
		"gridColor",
		"cellHeight",
		"cellWidth",
		"rowHeaderSize",
		"columnHeaderSize"} )

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "heatmap")
public class Heatmap
		extends Figure
		implements Serializable {

	private static final long serialVersionUID = 325437934312047512L;

	public static final String CELL_DECORATOR_CHANGED = "cellDecorator";
	public static final String ROW_DECORATOR_CHANGED = "rowDecorator";
	public static final String COLUMN_DECORATOR_CHANGED = "columnDecorator";
	public static final String MATRIX_VIEW_CHANGED = "matrixView";
	public static final String GRID_PROPERTY_CHANGED = "gridProperty";
	public static final String CELL_SIZE_CHANGED = "cellSize";
	public static final String ROW_HEADER_SIZE_CHANGED = "rowHeaderSize";
	public static final String COLUMN_HEADER_SIZE_CHANGED = "columnHeaderSize";
	
	
	@XmlJavaTypeAdapter(ElementDecoratorXmlAdapter.class)
	private ElementDecorator cellDecorator;
	
	private HeatmapHeader rowHeader;
	private HeatmapHeader columnHeader;
	
	@XmlElement(type=MatrixView.class)
	private IMatrixView matrixView;

	private boolean showGrid;
	
	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	private Color gridColor;
	
	private int cellWidth;
	private int cellHeight;
		
	private int rowHeaderSize;

	private int columnHeaderSize;

	@XmlTransient
	private boolean showBorders;
	
	public Heatmap() {
		this(
				null, null, //FIXME should it be null ?
				new HeatmapHeader(),
				new HeatmapHeader());
	}
	
	public Heatmap(IMatrixView matrixView) {
		this(
				matrixView,
				cellDecoratorFromMatrix(matrixView),
				new HeatmapHeader(),
				new HeatmapHeader());
	}
	
	public Heatmap(
			IMatrixView matrixView,
			ElementDecorator cellDecorator,
			HeatmapHeader rowsDecorator,
			HeatmapHeader columnsDecorator) {
		
		this.matrixView = matrixView;
		this.cellDecorator = cellDecorator;
		this.rowHeader = rowsDecorator;
		this.columnHeader = columnsDecorator;
		this.showGrid = true;
		this.gridColor = Color.WHITE;
		this.cellWidth = 18;
		this.cellHeight = 18;
		this.rowHeaderSize = 300;
		this.columnHeaderSize = 200;
		this.showBorders = false;
	}
	
	private static ElementDecorator cellDecoratorFromMatrix(
			IMatrixView matrixView) {
		
		ElementDecorator decorator = null;
		
		IElementAdapter adapter = matrixView.getCellAdapter();
		List<IElementProperty> attributes = matrixView.getCellAttributes();
		
		int attrIndex = matrixView.getSelectedPropertyIndex();
		if (attrIndex >= 0 && attrIndex < attributes.size()) {
			Class<?> elementClass = attributes.get(attrIndex).getValueClass();
			
			//FIXME No funciona
			if (Double.class.isInstance(elementClass)
					|| double.class.isInstance(elementClass))
				decorator = ElementDecoratorFactory.create(
						ElementDecoratorNames.LINEAR_TWO_SIDED, adapter);
		}
		
		if (decorator == null)
			decorator = ElementDecoratorFactory.create(
					ElementDecoratorNames.LINEAR_TWO_SIDED, adapter);
		
		return decorator;
	}

	public final ElementDecorator getCellDecorator() {
		return cellDecorator;
	}
	
	public final void setCellDecorator(ElementDecorator decorator) {
		final ElementDecorator old = this.cellDecorator;	
		this.cellDecorator = decorator;
		firePropertyChange(CELL_DECORATOR_CHANGED, old, decorator);
	}
	
	public HeatmapHeader getRowHeader() {
		return rowHeader;
	}
	
	public void setRowDecorator(HeatmapHeader rowDecorator) {
		final HeatmapHeader old = this.rowHeader;
		this.rowHeader = rowDecorator;
		firePropertyChange(ROW_DECORATOR_CHANGED, old, rowDecorator);
	}
	
	public HeatmapHeader getColumnHeader() {
		return columnHeader;
	}
	
	public void setColumnDecorator(HeatmapHeader columnDecorator) {
		final HeatmapHeader old = this.columnHeader;
		this.columnHeader = columnDecorator;
		firePropertyChange(COLUMN_DECORATOR_CHANGED, old, columnDecorator);
	}
	
	public final IMatrixView getMatrixView() {
		return matrixView;
	}
	
	public final void setMatrixView(IMatrixView matrixView) {
		final IMatrixView old = this.matrixView;
		this.matrixView = matrixView;
		firePropertyChange(MATRIX_VIEW_CHANGED, old, matrixView);
	}
	
	public boolean isShowGrid() {
		return showGrid;
	}
	
	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
		firePropertyChange(GRID_PROPERTY_CHANGED);
	}
	
	public Color getGridColor() {
		return gridColor;
	}
	
	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
		firePropertyChange(GRID_PROPERTY_CHANGED);
	}

	public int getCellWidth() {
		return cellWidth;
	}
	
	public void setCellWidth(int cellWidth) {
		this.cellWidth = cellWidth;
		firePropertyChange(CELL_SIZE_CHANGED);
	}

	public int getCellHeight() {
		return cellHeight;
	}
	
	public void setCellHeight(int cellHeight) {
		this.cellHeight = cellHeight;
		firePropertyChange(CELL_SIZE_CHANGED);
	}
	
	public int getRowHeaderSize() {
		return rowHeaderSize;
	}
	
	public void setRowHeaderSize(int rowSize) {
		this.rowHeaderSize = rowSize;
		firePropertyChange(ROW_HEADER_SIZE_CHANGED);
	}

	public int getColumnHeaderSize() {
		return columnHeaderSize;
	}
	
	public void setColumnHeaderSize(int columnSize) {
		this.columnHeaderSize = columnSize;
		firePropertyChange(COLUMN_HEADER_SIZE_CHANGED);
	}

	public boolean isShowBorders() {
		return showBorders;
	}

	public void setShowBorders(boolean showBorders) {
		this.showBorders = showBorders;
	}

	public String getColumnLabel(int index) {
		String header = matrixView.getColumnLabel(index);
		HeatmapHeaderDecoration decoration = new HeatmapHeaderDecoration();
		columnHeader.decorate(decoration, header);
		return decoration.getText();
	}

	public String getRowLabel(int index) {
		String header = matrixView.getRowLabel(index);
		HeatmapHeaderDecoration decoration = new HeatmapHeaderDecoration();
		rowHeader.decorate(decoration, header);
		return decoration.getText();
	}

	public String getColumnLinkUrl(int index) {
		String header = matrixView.getColumnLabel(index);
		HeatmapHeaderDecoration decoration = new HeatmapHeaderDecoration();
		columnHeader.decorate(decoration, header);
		return decoration.getUrl();
	}

	public String getRowLinkUrl(int index) {
		String header = matrixView.getRowLabel(index);
		HeatmapHeaderDecoration decoration = new HeatmapHeaderDecoration();
		rowHeader.decorate(decoration, header);
		return decoration.getUrl();
	}
}
