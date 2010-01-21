/*
 *  Copyright 2010 cperez.
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

import java.beans.PropertyChangeListener;
import java.util.List;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;

public class HeatmapMatrixViewAdapter implements IMatrixView {

	private final Heatmap hm;
	private final IMatrixView mv;

	public HeatmapMatrixViewAdapter(Heatmap hm) {
		this.hm = hm;
		this.mv = hm.getMatrixView();
	}

	@Override
	public IMatrix getContents() {
		return mv.getContents();
	}

	@Override
	public int[] getVisibleRows() {
		return mv.getVisibleRows();
	}

	@Override
	public void setVisibleRows(int[] indices) {
		mv.setVisibleRows(indices);
	}

	@Override
	public int[] getVisibleColumns() {
		return mv.getVisibleColumns();
	}

	@Override
	public void setVisibleColumns(int[] indices) {
		mv.setVisibleColumns(indices);
	}

	@Override
	public void moveRowsUp(int[] indices) {
		mv.moveRowsUp(indices);
	}

	@Override
	public void moveRowsDown(int[] indices) {
		mv.moveRowsDown(indices);
	}

	@Override
	public void moveColumnsLeft(int[] indices) {
		mv.moveColumnsLeft(indices);
	}

	@Override
	public void moveColumnsRight(int[] indices) {
		mv.moveColumnsRight(indices);
	}

	@Override
	public int[] getSelectedRows() {
		return mv.getSelectedRows();
	}

	@Override
	public void setSelectedRows(int[] indices) {
		mv.setSelectedRows(indices);
	}

	@Override
	public boolean isRowSelected(int index) {
		return mv.isRowSelected(index);
	}

	@Override
	public int[] getSelectedColumns() {
		return mv.getSelectedColumns();
	}

	@Override
	public void setSelectedColumns(int[] indices) {
		mv.setSelectedColumns(indices);
	}

	@Override
	public boolean isColumnSelected(int index) {
		return mv.isColumnSelected(index);
	}

	@Override
	public void selectAll() {
		mv.selectAll();
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
		return mv.getLeadSelectionRow();
	}

	@Override
	public int getLeadSelectionColumn() {
		return mv.getLeadSelectionColumn();
	}

	@Override
	public void setLeadSelection(int row, int column) {
		mv.setLeadSelection(row, column);
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
		mv.removePropertyChangeListener(listener);
	}

	@Override
	public int getRowCount() {
		return mv.getRowCount();
	}

	@Override
	public String getRowLabel(int index) {
		return hm.getRowLabel(index);
	}

	@Override
	public int getColumnCount() {
		return mv.getColumnCount();
	}

	@Override
	public String getColumnLabel(int index) {
		return hm.getColumnLabel(index);
	}

	@Override
	public Object getCell(int row, int column) {
		return mv.getCell(row, column);
	}

	@Override
	public Object getCellValue(int row, int column, int index) {
		return mv.getCellValue(row, column, index);
	}

	@Override
	public Object getCellValue(int row, int column, String id) {
		return mv.getCellValue(row, column, id);
	}

	@Override
	public void setCellValue(int row, int column, int index, Object value) {
		mv.setCellValue(row, column, index, value);
	}

	@Override
	public void setCellValue(int row, int column, String id, Object value) {
		mv.setCellValue(row, column, id, value);
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
