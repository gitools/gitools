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

package org.gitools.matrix.model;

import org.gitools.model.IModel;

public interface IMatrixView extends IModel, IMatrix {
	
	// events
	
	String VISIBLE_COLUMNS_CHANGED = "visibleColsChanged";
	String VISIBLE_ROWS_CHANGED = "visibleRowsChanged";
	
	String SELECTED_LEAD_CHANGED = "selectionLead";
	String SELECTION_CHANGED = "selectionChanged";
	//String SELECTED_COLUMNS_CHANGED = "selectionColumns";
	//String SELECTED_ROWS_CHANGED = "selectionRows";
	
	String CELL_VALUE_CHANGED = "cellValueChanged";
	
	String CELL_DECORATION_CONTEXT_CHANGED = "cellDecorationContextChanged";

	// contents
	
	IMatrix getContents();
	
	// visibility
	
	int[] getVisibleRows();
	void setVisibleRows(int[] indices);
	
	int[] getVisibleColumns();
	void setVisibleColumns(int[] indices);
	
	void moveRowsUp(int[] indices);
	void moveRowsDown(int[] indices);
	void moveColumnsLeft(int[] indices);
	void moveColumnsRight(int[] indices);

	void hideRows(int[] indices);
	void hideColumns(int[] indices);
	
	// selection
	
	int[] getSelectedRows();
	void setSelectedRows(int[] indices);
	boolean isRowSelected(int index);

	int[] getSelectedColumns();
	void setSelectedColumns(int[] indices);
	boolean isColumnSelected(int index);
	
	void selectAll();
	void invertSelection();
	void clearSelection();
	
	int getLeadSelectionRow();
	int getLeadSelectionColumn();
	void setLeadSelection(int row, int column);
	
	// properties
	
	int getSelectedPropertyIndex();
	void setSelectedPropertyIndex(int index);
}
