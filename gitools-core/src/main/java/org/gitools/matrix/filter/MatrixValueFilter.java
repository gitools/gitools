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

package org.gitools.matrix.filter;

import java.util.List;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;

public class MatrixValueFilter {

	public static void filterRows(
			IMatrixView matrixView,
			int[] selection,
			List<ValueFilterCriteria> criteriaList,
			boolean includeHidden,
			boolean allCriteriaPerCell,		// For a given cell all criteria should match
			boolean allCells				// All cells in a row/column should match
			) {

		final IMatrix matrix = matrixView.getContents();

		
		/*int[] indices = null;

		if (includeHidden) {
			if (selection == null || selection.length == 0) {
				int[] sel = new int[selection.length];
				int[] visible = matrixView.getVisibleColumns();
				for (int i = 0; i < selection.length; i++)
					sel[i] = visible[selection[i]];
				selection = sel;
			}
		}
		else {

		}

		if (selection == null || selection.length == 0) {
			int numColumns = includeHidden ? matrix.getColumnCount() : matrixView.getColumnCount();
			int[] sel = new int[numColumns];
			for (int i = 0; i < sel.length; i++)
				sel[i] = i;
		}
		else if (includeHidden) {
			int[] sel = new int[selection.length];
			int[] visible = matrixView.getVisibleColumns();
			for (int i = 0; i < selection.length; i++)
				sel[i] = visible[selection[i]];
			selection = sel;
		}*/

		for (int row = 0; row < matrix.getRowCount(); row++) {
			boolean filter = false;

			//TODO

			if (filter) {
				// TODO filter out
			}
		}
	}
}
