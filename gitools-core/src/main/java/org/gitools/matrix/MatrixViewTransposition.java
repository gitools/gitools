/*
 *  Copyright 2010 chris.
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

package org.gitools.matrix;

import java.beans.PropertyChangeListener;
import java.util.List;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;

public class MatrixViewTransposition implements IMatrixView {

	private IMatrixView mv;

	public MatrixViewTransposition() {
	}

	public MatrixViewTransposition(IMatrixView mv) {
		this.mv = mv;
	}

	/*public IMatrixView getMatrixView() {
		return mv;
	}

	public void setMatrixView(IMatrixView mv) {
		this.mv = mv;
	}*/

	public void setMatrix(IMatrix matrix) {
		this.mv = matrix instanceof IMatrixView ?
			(IMatrixView) matrix : new MatrixView(matrix);
	}
	
	@Override
	public IMatrix getContents() {
		return mv.getContents();
	}

	@Override
	public int[] getVisibleRows() {
		return mv.getVisibleColumns();
	}

	@Override
	public void setVisibleRows(int[] indices) {
		mv.setVisibleColumns(indices);
	}

	@Override
	public int[] getVisibleColumns() {
		return mv.getVisibleRows();
	}

	@Override
	public void setVisibleColumns(int[] indices) {
		mv.setVisibleRows(indices);
	}

	@Override
	public void moveRowsUp(int[] indices) {
		mv.moveColumnsLeft(indices);
	}

	@Override
	public void moveRowsDown(int[] indices) {
		mv.moveColumnsRight(indices);
	}

	@Override
	public void moveColumnsLeft(int[] indices) {
		mv.moveRowsUp(indices);
	}

	@Override
	public void moveColumnsRight(int[] indices) {
		mv.moveRowsDown(indices);
	}

	@Override
	public int[] getSelectedRows() {
		return mv.getSelectedColumns();
	}

	@Override
	public void setSelectedRows(int[] indices) {
		mv.setSelectedColumns(indices);
	}

	@Override
	public boolean isRowSelected(int index) {
		return mv.isColumnSelected(index);
	}

	@Override
	public int[] getSelectedColumns() {
		return mv.getSelectedRows();
	}

	@Override
	public void setSelectedColumns(int[] indices) {
		mv.setSelectedRows(indices);
	}

	@Override
	public boolean isColumnSelected(int index) {
		return mv.isRowSelected(index);
	}

	@Override
	public void selectAll() {
		mv.selectAll(); //FIXME by columns
	}

	@Override
	public void invertSelection() {
		mv.invertSelection();
	}

	@Override
	public void clearSelection() {
		mv.clearSelection();
	}

	@Override
	public int getLeadSelectionRow() {
		return mv.getLeadSelectionColumn();
	}

	@Override
	public int getLeadSelectionColumn() {
		return mv.getLeadSelectionRow();
	}

	@Override
	public void setLeadSelection(int row, int column) {
		mv.setLeadSelection(column, row);
	}

	@Override
	public int getSelectedPropertyIndex() {
		return mv.getSelectedPropertyIndex();
	}

	@Override
	public void setSelectedPropertyIndex(int index) {
		mv.setSelectedPropertyIndex(index);
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		mv.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		removePropertyChangeListener(listener);
	}

	@Override
	public int getRowCount() {
		return mv.getColumnCount();
	}

	@Override
	public int getColumnCount() {
		return mv.getRowCount();
	}

	@Override
	public String getRowLabel(int index) {
		return mv.getColumnLabel(index);
	}

	@Override
	public String getColumnLabel(int index) {
		return mv.getRowLabel(index);
	}

	@Override
	public Object getCell(int row, int column) {
		return mv.getCell(column, row);
	}

	@Override
	public Object getCellValue(int row, int column, int index) {
		return mv.getCellValue(column, row, index);
	}

	@Override
	public Object getCellValue(int row, int column, String id) {
		return mv.getCellValue(column, row, id);
	}

	@Override
	public void setCellValue(int row, int column, int index, Object value) {
		mv.setCellValue(column, row, index, value);
	}

	@Override
	public void setCellValue(int row, int column, String id, Object value) {
		mv.setCellValue(column, row, id, value);
	}

	@Override
	public IElementAdapter getCellAdapter() {
		return mv.getCellAdapter();
	}

	@Override
	public List<IElementAttribute> getCellAttributes() {
		return mv.getCellAttributes();
	}

}
