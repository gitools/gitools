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
package org.gitools.matrix.filter;

import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.TransposedMatrixView;
import org.gitools.matrix.model.IMatrixView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MatrixViewValueFilter
{

    public static void filter(
            @NotNull IMatrixView matrixView,
            @NotNull List<ValueFilterCriteria> criteriaList,
            boolean allCriteria,        // For a given cell all criteria should match
            boolean allElements,        // All cells in a row/column should match
            boolean invertCriteria,
            boolean applyToRows,
            boolean applyToColumns
    )
    {

        if (applyToRows)
        {
            filterRows(matrixView, matrixView.getSelectedColumns(),
                    criteriaList, allCriteria, allElements, invertCriteria);
        }

        if (applyToColumns)
        {
            filterColumns(matrixView, matrixView.getSelectedRows(),
                    criteriaList, allCriteria, allElements, invertCriteria);
        }
    }

    public static void filterRows(
            @NotNull IMatrixView matrixView,
            @Nullable int[] selection,
            @NotNull List<ValueFilterCriteria> criteriaList,
            boolean allCriteria,        // For a given cell all criteria should match
            boolean allElements,        // All cells in a row/column should match
            boolean invertCriteria
    )
    {

        if (selection == null || selection.length == 0)
        {
            int numColumns = matrixView.getColumnCount();
            int[] sel = new int[numColumns];
            for (int i = 0; i < sel.length; i++)
                sel[i] = i;
            selection = sel;
        }

        List<Integer> filterin = new ArrayList<Integer>();

        for (int row = 0; row < matrixView.getRowCount(); row++)
        {
            boolean cellsAnd = true;
            boolean cellsOr = false;
            for (int col = 0; col < selection.length; col++)
            {
                boolean critAnd = true;
                boolean critOr = false;
                for (int critIndex = 0; critIndex < criteriaList.size(); critIndex++)
                {
                    ValueFilterCriteria criteria = criteriaList.get(critIndex);
                    double value = MatrixUtils.doubleValue(
                            matrixView.getCellValue(row, selection[col], criteria.getAttributeIndex()));
                    boolean critRes = criteria.getComparator().compare(value, criteria.getValue());
                    critAnd &= critRes;
                    critOr |= critRes;
                }
                boolean critFilterIn = allCriteria ? critAnd : critOr;
                cellsAnd &= critFilterIn;
                cellsOr |= critFilterIn;
            }
            boolean cellsFilterIn = allElements ? cellsAnd : cellsOr;
            if (invertCriteria)
            {
                cellsFilterIn = !cellsFilterIn;
            }

            if (cellsFilterIn)
            {
                filterin.add(row);
            }
        }

        int[] visibleRows = matrixView.getVisibleRows();
        int[] filterRows = new int[filterin.size()];
        for (int i = 0; i < filterin.size(); i++)
            filterRows[i] = visibleRows[filterin.get(i)];

        matrixView.setVisibleRows(filterRows);
    }

    public static void filterColumns(
            IMatrixView matrixView,
            int[] selection,
            @NotNull List<ValueFilterCriteria> criteriaList,
            boolean allCriteria,        // For a given cell all criteria should match
            boolean allElements,        // All cells in a row/column should match
            boolean invertCriteria
    )
    {

        final IMatrixView mv = new TransposedMatrixView(matrixView);
        filterRows(mv, selection, criteriaList, allCriteria, allElements, invertCriteria);
    }
}
