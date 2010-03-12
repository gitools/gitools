package org.gitools.heatmap.model;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.heatmap.xml.HeatmapMatrixViewXmlAdapter;

import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.Figure;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import edu.upf.bg.xml.adapter.ColorXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement()
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

	@XmlJavaTypeAdapter(HeatmapMatrixViewXmlAdapter.class)
	private IMatrixView matrixView;

	// Cells

	private ElementDecorator cellDecorator;
	private int cellWidth;
	private int cellHeight;

	// Row headers

	private HeatmapHeader rowHeader;
	private int rowHeaderSize;

	// Column headers

	private HeatmapHeader columnHeader;
	private int columnHeaderSize;

	// Rows grid

	private boolean rowsGridEnabled;
	private int rowsGridSize;

	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	private Color rowsGridColor;

	// Columns grid

	private boolean columnsGridEnabled;
	private int columnsGridSize;

	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	private Color columnsGridColor;

	// Other

	@XmlTransient
	private boolean showBorders;
	
	public Heatmap() {
		this(
				null, null, //FIXME should it be null ?
				new HeatmapHeader(),
				new HeatmapHeader());
	}

	/*public Heatmap(Heatmap hm) {
		super(hm);

		//this.cellDecorator = hm.getCellDecorator().clone();


	}*/

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
		this.rowsGridEnabled = true;
		this.rowsGridSize = 1;
		this.rowsGridColor = Color.WHITE;
		this.columnsGridEnabled = true;
		this.columnsGridSize = 1;
		this.columnsGridColor = Color.WHITE;
		this.cellWidth = 14;
		this.cellHeight = 14;
		this.rowHeaderSize = 300;
		this.columnHeaderSize = 200;
		this.showBorders = false;
	}
	
	private static ElementDecorator cellDecoratorFromMatrix(
			IMatrixView matrixView) {
		
		ElementDecorator decorator = null;
		
		IElementAdapter adapter = matrixView.getCellAdapter();
		List<IElementAttribute> attributes = matrixView.getCellAttributes();
		
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

	// Matrix View

	public final IMatrixView getMatrixView() {
		return matrixView;
	}

	public final void setMatrixView(IMatrixView matrixView) {
		final IMatrixView old = this.matrixView;
		this.matrixView = matrixView;
		firePropertyChange(MATRIX_VIEW_CHANGED, old, matrixView);
	}

	// Cells

	public final ElementDecorator getCellDecorator() {
		return cellDecorator;
	}
	
	public final void setCellDecorator(ElementDecorator decorator) {
		final ElementDecorator old = this.cellDecorator;	
		this.cellDecorator = decorator;
		firePropertyChange(CELL_DECORATOR_CHANGED, old, decorator);
	}

	public int getCellWidth() {
		return cellWidth;
	}

	public void setCellWidth(int cellWidth) {
		int old = this.cellWidth;
		this.cellWidth = cellWidth;
		firePropertyChange(CELL_SIZE_CHANGED, old, cellWidth);
	}

	public int getCellHeight() {
		return cellHeight;
	}

	public void setCellHeight(int cellHeight) {
		int old = this.cellHeight;
		this.cellHeight = cellHeight;
		firePropertyChange(CELL_SIZE_CHANGED, old, cellHeight);
	}

	// Headers

	public HeatmapHeader getRowHeader() {
		return rowHeader;
	}
	
	public void setRowHeader(HeatmapHeader rowDecorator) {
		final HeatmapHeader old = this.rowHeader;
		this.rowHeader = rowDecorator;
		firePropertyChange(ROW_DECORATOR_CHANGED, old, rowDecorator);
	}

	public int getRowHeaderSize() {
		return rowHeaderSize;
	}

	public void setRowHeaderSize(int rowHeaderSize) {
		int old = this.rowHeaderSize;
		this.rowHeaderSize = rowHeaderSize;
		firePropertyChange(ROW_HEADER_SIZE_CHANGED, old, rowHeaderSize);
	}

	public HeatmapHeader getColumnHeader() {
		return columnHeader;
	}
	
	public void setColumnHeader(HeatmapHeader columnDecorator) {
		final HeatmapHeader old = this.columnHeader;
		this.columnHeader = columnDecorator;
		firePropertyChange(COLUMN_DECORATOR_CHANGED, old, columnDecorator);
	}

	public int getColumnHeaderSize() {
		return columnHeaderSize;
	}

	public void setColumnHeaderSize(int columnHeaderSize) {
		int old = this.columnHeaderSize;
		this.columnHeaderSize = columnHeaderSize;
		firePropertyChange(COLUMN_HEADER_SIZE_CHANGED, old, columnHeaderSize);
	}

	// Grid

	/** Whether to show or not a grid between rows */
	public boolean isRowsGridEnabled() {
		return rowsGridEnabled;
	}

	/** Whether to show or not a grid between rows */
	public void setRowsGridEnabled(boolean enabled) {
		boolean old = this.rowsGridEnabled;
		this.rowsGridEnabled = enabled;
		firePropertyChange(GRID_PROPERTY_CHANGED, old, enabled);
	}

	public int getRowsGridSize() {
		return rowsGridSize;
	}

	public void setRowsGridSize(int size) {
		int old = this.rowsGridSize;
		this.rowsGridSize = size;
		firePropertyChange(GRID_PROPERTY_CHANGED, old, size);
	}

	/** Get color to use for rows and columns grid */
	public Color getRowsGridColor() {
		return rowsGridColor;
	}

	/** Set color to use for rows and columns grid */
	public void setRowsGridColor(Color gridColor) {
		Color old = this.rowsGridColor;
		this.rowsGridColor = gridColor;
		firePropertyChange(GRID_PROPERTY_CHANGED, old, gridColor);
	}

	/** Whether to show or not a grid between columns */
	public boolean isColumnsGridEnabled() {
		return columnsGridEnabled;
	}

	/** Whether to show or not a grid between columns */
	public void setColumnsGridEnabled(boolean enabled) {
		boolean old = this.columnsGridEnabled;
		this.columnsGridEnabled = enabled;
		firePropertyChange(GRID_PROPERTY_CHANGED, old, enabled);
	}

	public int getColumnsGridSize() {
		return columnsGridSize;
	}

	public void setColumnsGridSize(int size) {
		int old = this.columnsGridSize;
		this.columnsGridSize = size;
		firePropertyChange(GRID_PROPERTY_CHANGED, old, size);
	}

	/** Get color to use for rows and columns grid */
	public Color getColumnsGridColor() {
		return columnsGridColor;
	}

	/** Set color to use for rows and columns grid */
	public void setColumnsGridColor(Color gridColor) {
		Color old = this.columnsGridColor;
		this.columnsGridColor = gridColor;
		firePropertyChange(GRID_PROPERTY_CHANGED, old, gridColor);
	}

	// Other

	public boolean isShowBorders() {
		return showBorders;
	}

	public void setShowBorders(boolean showBorders) {
		this.showBorders = showBorders;
	}

	// Generated values
	
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
