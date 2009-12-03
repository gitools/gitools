package org.gitools.model.figure;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.model.decorator.HeaderDecorator;
import org.gitools.model.decorator.impl.AnnotationHeaderDecorator;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.MatrixView;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.matrix.element.IElementProperty;
import org.gitools.model.xml.adapter.ColorXmlAdapter;
import org.gitools.model.xml.adapter.ElementDecoratorXmlAdapter;
import org.gitools.model.xml.adapter.HeaderDecoratorXmlAdapter;

@XmlType( propOrder={
		"cellDecorator", 
		"rowDecorator", 
		"columnDecorator", 
		"matrixView",
		"showGrid", 
		"gridColor",
		"cellHeight",
		"cellWidth",
		"rowHeaderSize",
		"columnHeaderSize"} )

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "matrixFigure")
public class MatrixFigure 
		extends Figure
		implements Serializable {

	private static final long serialVersionUID = 325437934312047512L;

	public static final String CELL_DECORATOR_CHANGED = "cellDecorator";
	public static final String ROW_DECORATOR_CHANGED = "rowDecorator";
	public static final String COLUMN_DECORATOR_CHANGED = "columnDecorator";
	public static final String TABLE_CHANGED = "table";
	
	
	@XmlJavaTypeAdapter(ElementDecoratorXmlAdapter.class)
	private ElementDecorator cellDecorator;
	
	@XmlJavaTypeAdapter(HeaderDecoratorXmlAdapter.class)
	private HeaderDecorator rowDecorator;
	
	@XmlJavaTypeAdapter(HeaderDecoratorXmlAdapter.class)
	private HeaderDecorator columnDecorator;
	
	@XmlElement(type=MatrixView.class)
	private IMatrixView matrixView;

	private boolean showGrid;
	
	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	private Color gridColor;
	
	//private int cellSize;
	private int cellWidth;
	private int cellHeight;
		
	private int rowHeaderSize;

	private int columnHeaderSize;
	
	public MatrixFigure() {
		this(
				null, null, //FIXME should it be null ?
				new AnnotationHeaderDecorator(),
				new AnnotationHeaderDecorator());
	}
	
	public MatrixFigure(IMatrixView matrixView) {
		this(
				matrixView,
				cellDecoratorFromMatrix(matrixView),
				new AnnotationHeaderDecorator(),
				new AnnotationHeaderDecorator());
	}
	
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
		//this.cellSize = 18;
		this.cellWidth = 18;
		this.cellHeight = 18;
		this.rowHeaderSize = 400;
		this.columnHeaderSize = 200;
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
	
	/*public int getCellSize() {
		return cellSize;
	}
	
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
		firePropertyChange(PROPERTY_CHANGED);
	}*/
	
	public int getCellWidth() {
		return cellWidth;
	}
	
	public void setCellWidth(int cellWidth) {
		this.cellWidth = cellWidth;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public int getCellHeight() {
		return cellHeight;
	}
	
	public void setCellHeight(int cellHeight) {
		this.cellHeight = cellHeight;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public int getRowHeaderSize() {
		return rowHeaderSize;
	}
	
	public void setRowHeaderSize(int rowSize) {
		this.rowHeaderSize = rowSize;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public int getColumnHeaderSize() {
		return columnHeaderSize;
	}
	
	public void setColumnHeaderSize(int columnSize) {
		this.columnHeaderSize = columnSize;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
}
