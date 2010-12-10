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

package org.gitools.matrix.filter;

import java.util.ArrayList;
import java.util.List;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.TransposedMatrixView;
import org.gitools.matrix.model.IMatrixView;

public class MatrixViewValueFilter {

	public static void filter(
			IMatrixView matrixView,
			List<ValueFilterCriteria> criteriaList,
			boolean allCriteria,		// For a given cell all criteria should match
			boolean allElements,		// All cells in a row/column should match
			boolean invertCriteria,
			boolean applyToRows,
			boolean applyToColumns
			) {

		if (applyToRows)
			filterRows(matrixView, matrixView.getSelectedColumns(),
					criteriaList, allCriteria, allElements, invertCriteria);

		if (applyToColumns)
			filterColumns(matrixView, matrixView.getSelectedRows(),
					criteriaList, allCriteria, allElements, invertCriteria);
	}

	public static void filterRows(
			IMatrixView matrixView,
			int[] selection,
			List<ValueFilterCriteria> criteriaList,
			boolean allCriteria,		// For a given cell all criteria should match
			boolean allElements,		// All cells in a row/column should match
			boolean invertCriteria
			) {

		if (selection == null || selection.length == 0) {
			int numColumns = matrixView.getColumnCount();
			int[] sel = new int[numColumns];
			for (int i = 0; i < sel.length; i++)
				sel[i] = i;
			selection = sel;
		}

		List<Integer> filterin = new ArrayList<Integer>();

		for (int row = 0; row < matrixView.getRowCount(); row++) {
			boolean cellsAnd = true;
			boolean cellsOr = false;
			for (int col = 0; col < selection.length; col++) {
				boolean critAnd = true;
				boolean critOr = false;
				for (int critIndex = 0; critIndex < criteriaList.size(); critIndex++) {
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
				cellsFilterIn = !cellsFilterIn;

			if (cellsFilterIn)
				filterin.add(row);
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
			List<ValueFilterCriteria> criteriaList,
			boolean allCriteria,		// For a given cell all criteria should match
			boolean allElements,		// All cells in a row/column should match
			boolean invertCriteria
			) {

		final IMatrixView mv = new TransposedMatrixView(matrixView);
		filterRows(mv, selection, criteriaList, allCriteria, allElements, invertCriteria);
	}
}
