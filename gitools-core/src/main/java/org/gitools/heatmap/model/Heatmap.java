/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.heatmap.model;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
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

/*TODO: Heatmap should implement IMatrixView
 * and handle movement and visibility synchronized
 * between annotations, clusters and so on.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement()
public class Heatmap
		extends Figure
		implements Serializable {

	private static final long serialVersionUID = 325437934312047512L;

	public static final String CELL_DECORATOR_CHANGED = "cellDecorator";
	public static final String MATRIX_VIEW_CHANGED = "matrixView";
	public static final String CELL_SIZE_CHANGED = "cellSize";
	public static final String ROW_DIMENSION_CHANGED = "rowDim";
	public static final String COLUMN_DIMENSION_CHANGED = "columnDim";
	
	@Deprecated public static final String ROW_LABELS_HEADER_CHANGED = "rowDecorator";
	@Deprecated public static final String COLUMN_LABELS_HEADER_CHANGED = "columnDecorator";
	@Deprecated public static final String GRID_PROPERTY_CHANGED = "gridProperty";
	//@Deprecated public static final String ROW_HEADER_SIZE_CHANGED = "rowHeaderSize";
	//@Deprecated public static final String COLUMN_HEADER_SIZE_CHANGED = "columnHeaderSize";
	@Deprecated public static final String ROW_CLUSTER_SETS_CHANGED = "rowClusterSets";
	@Deprecated public static final String COLUMN_CLUSTER_SETS_CHANGED = "columnClusterSets";

	@XmlJavaTypeAdapter(HeatmapMatrixViewXmlAdapter.class)
	private IMatrixView matrixView;

	// Cells

	private ElementDecorator cellDecorator;
	private int cellWidth;
	private int cellHeight;

	private HeatmapDim rowDim;

	private HeatmapDim columnDim;

	// Other

	@XmlTransient
	private boolean showBorders;

	PropertyChangeListener propertyListener;
	
	public Heatmap() {
		this(
				null, null, //FIXME should it be null ?
				new HeatmapLabelsHeader(),
				new HeatmapLabelsHeader());
	}

	public Heatmap(IMatrixView matrixView) {
		this(
				matrixView,
				cellDecoratorFromMatrix(matrixView),
				new HeatmapLabelsHeader(),
				new HeatmapLabelsHeader());
	}
	
	public Heatmap(
			IMatrixView matrixView,
			ElementDecorator cellDecorator,
			HeatmapLabelsHeader rowsLabelsHeader,
			HeatmapLabelsHeader columnLabelsHeader) {

		propertyListener = new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChange(evt); } };

		this.matrixView = matrixView;
		this.matrixView.addPropertyChangeListener(propertyListener);
		
		this.cellDecorator = cellDecorator;
		this.cellDecorator.addPropertyChangeListener(propertyListener);

		this.cellWidth = 14;
		this.cellHeight = 14;
		this.showBorders = false;
				
		this.rowDim = new HeatmapDim();
		rowsLabelsHeader.setSize(220);
		this.rowDim.setLabelsHeader(rowsLabelsHeader);
		this.rowDim.addPropertyChangeListener(propertyListener);

		this.columnDim = new HeatmapDim();
		columnLabelsHeader.setSize(130);
		this.columnDim.setLabelsHeader(columnLabelsHeader);
		this.columnDim.addPropertyChangeListener(propertyListener);
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
		this.matrixView.removePropertyChangeListener(propertyListener);
		matrixView.addPropertyChangeListener(propertyListener);
		final IMatrixView old = this.matrixView;
		this.matrixView = matrixView;
		firePropertyChange(MATRIX_VIEW_CHANGED, old, matrixView);
	}

	// Cells

	public final ElementDecorator getCellDecorator() {
		return cellDecorator;
	}
	
	public final void setCellDecorator(ElementDecorator decorator) {
		this.cellDecorator.removePropertyChangeListener(propertyListener);
		decorator.addPropertyChangeListener(propertyListener);
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

	// Dimensions

	public HeatmapDim getRowDim() {
		return rowDim;
	}

	public void setRowDim(HeatmapDim rowDim) {
		this.rowDim.removePropertyChangeListener(propertyListener);
		rowDim.addPropertyChangeListener(propertyListener);
		HeatmapDim old = this.rowDim;
		this.rowDim = rowDim;
		firePropertyChange(ROW_DIMENSION_CHANGED, old, rowDim);
	}

	public HeatmapDim getColumnDim() {
		return columnDim;
	}

	public void setColumnDim(HeatmapDim columnDim) {
		this.columnDim.removePropertyChangeListener(propertyListener);
		columnDim.addPropertyChangeListener(propertyListener);
		HeatmapDim old = this.columnDim;
		this.columnDim = columnDim;
		firePropertyChange(COLUMN_DIMENSION_CHANGED, old, columnDim);
	}

	// Headers

	@Deprecated
	public int getRowHeaderSize() {
		return rowDim.getHeaderSize();
	}

	/*@Deprecated
	public void setRowHeaderSize(int rowHeaderSize) {
		int old = this.rowDim.getHeaderSize();
		this.rowDim.setHeaderSize(rowHeaderSize);
		firePropertyChange(ROW_HEADER_SIZE_CHANGED, old, rowHeaderSize);
	}

	@Deprecated
	public int getColumnHeaderSize() {
		return columnDim.getHeaderSize();
	}

	@Deprecated
	public void setColumnHeaderSize(int columnHeaderSize) {
		int old = this.columnDim.getHeaderSize();
		this.columnDim.setHeaderSize(columnHeaderSize);
		firePropertyChange(COLUMN_HEADER_SIZE_CHANGED, old, columnHeaderSize);
	}*/

	// Labels

	@Deprecated
	public HeatmapLabelsHeader getRowLabelsHeader() {
		return rowDim.getLabelsHeader();
	}

	@Deprecated
	public void setRowLabelsHeader(HeatmapLabelsHeader rowDecorator) {
		final HeatmapLabelsHeader old = this.rowDim.getLabelsHeader();
		this.rowDim.setLabelsHeader(rowDecorator);
		firePropertyChange(ROW_LABELS_HEADER_CHANGED, old, rowDecorator);
	}

	@Deprecated
	public HeatmapLabelsHeader getColumnLabelsHeader() {
		return columnDim.getLabelsHeader();
	}

	@Deprecated
	public void setColumnLabelsHeader(HeatmapLabelsHeader columnDecorator) {
		final HeatmapLabelsHeader old = this.columnDim.getLabelsHeader();
		this.columnDim.setLabelsHeader(columnDecorator);
		firePropertyChange(COLUMN_LABELS_HEADER_CHANGED, old, columnDecorator);
	}

	// Grid

	/** Whether to show or not a grid between rows */
	@Deprecated
	public boolean isRowsGridEnabled() {
		return rowDim.isGridEnabled();
	}

	/** Whether to show or not a grid between rows */
	@Deprecated
	public void setRowsGridEnabled(boolean enabled) {
		boolean old = this.rowDim.isGridEnabled();
		this.rowDim.setGridEnabled(enabled);
		firePropertyChange(GRID_PROPERTY_CHANGED, old, enabled);
	}

	@Deprecated
	public int getRowsGridSize() {
		return rowDim.getGridSize();
	}

	@Deprecated
	public void setRowsGridSize(int size) {
		int old = this.rowDim.getGridSize();
		this.rowDim.setGridSize(size);
		firePropertyChange(GRID_PROPERTY_CHANGED, old, size);
	}

	/** Get color to use for rows and columns grid */
	@Deprecated
	public Color getRowsGridColor() {
		return rowDim.getGridColor();
	}

	/** Set color to use for rows and columns grid */
	@Deprecated
	public void setRowsGridColor(Color gridColor) {
		Color old = this.rowDim.getGridColor();
		this.rowDim.setGridColor(gridColor);
		firePropertyChange(GRID_PROPERTY_CHANGED, old, gridColor);
	}

	/** Whether to show or not a grid between columns */
	@Deprecated
	public boolean isColumnsGridEnabled() {
		return columnDim.isGridEnabled();
	}

	/** Whether to show or not a grid between columns */
	@Deprecated
	public void setColumnsGridEnabled(boolean enabled) {
		boolean old = this.columnDim.isGridEnabled();
		this.columnDim.setGridEnabled(enabled);
		firePropertyChange(GRID_PROPERTY_CHANGED, old, enabled);
	}

	@Deprecated
	public int getColumnsGridSize() {
		return columnDim.getGridSize();
	}

	@Deprecated
	public void setColumnsGridSize(int size) {
		int old = this.columnDim.getGridSize();
		this.columnDim.setGridSize(size);
		firePropertyChange(GRID_PROPERTY_CHANGED, old, size);
	}

	/** Get color to use for rows and columns grid */
	@Deprecated
	public Color getColumnsGridColor() {
		return columnDim.getGridColor();
	}

	/** Set color to use for rows and columns grid */
	@Deprecated
	public void setColumnsGridColor(Color gridColor) {
		Color old = this.columnDim.getGridColor();
		this.columnDim.setGridColor(gridColor);
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
		HeatmapLabelsDecoration decoration = new HeatmapLabelsDecoration();
		columnDim.getLabelsHeader().decorate(decoration, header);
		return decoration.getText();
	}

	public String getRowLabel(int index) {
		String header = matrixView.getRowLabel(index);
		HeatmapLabelsDecoration decoration = new HeatmapLabelsDecoration();
		rowDim.getLabelsHeader().decorate(decoration, header);
		return decoration.getText();
	}

	public String getColumnLinkUrl(int index) {
		String header = matrixView.getColumnLabel(index);
		HeatmapLabelsDecoration decoration = new HeatmapLabelsDecoration();
		columnDim.getLabelsHeader().decorate(decoration, header);
		return decoration.getUrl();
	}

	public String getRowLinkUrl(int index) {
		String header = matrixView.getRowLabel(index);
		HeatmapLabelsDecoration decoration = new HeatmapLabelsDecoration();
		rowDim.getLabelsHeader().decorate(decoration, header);
		return decoration.getUrl();
	}
}
