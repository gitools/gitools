/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.matrix.model;

import org.gitools.model.IModel;

public interface IMatrixView extends IModel, IMatrix
{
    // events
    String VISIBLE_CHANGED = "visibleChanged";
    String SELECTED_LEAD_CHANGED = "selectionLead";
    String SELECTION_CHANGED = "selectionChanged";
    String CELL_VALUE_CHANGED = "cellValueChanged";
    String CELL_DECORATION_CONTEXT_CHANGED = "cellDecorationContextChanged";

    // contents
    IMatrixViewDimension getRows();
    IMatrixViewDimension getColumns();
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
