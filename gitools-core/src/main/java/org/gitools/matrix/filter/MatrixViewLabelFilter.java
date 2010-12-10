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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.gitools.matrix.TransposedMatrixView;
import org.gitools.matrix.model.IMatrixView;

public class MatrixViewLabelFilter {

	public static void filter(IMatrixView matrixView, List<String> values, boolean useRegex, boolean applyToRows, boolean applyToColumns) {
		if (applyToRows)
			filterRows(matrixView, values, useRegex);

		if (applyToColumns)
			filterColumns(matrixView, values, useRegex);
	}

	public static void filterRows(IMatrixView matrixView, List<String> values, boolean useRegex) {
		List<Integer> rows = new ArrayList<Integer>();
		if (useRegex)
			filterByRegex(matrixView, values, rows);
		else
			filterByNames(matrixView, values, rows);

		// Change visibility of rows in the table
		int[] visibleRows = matrixView.getVisibleRows();
		int[] filterVisibleRows = new int[rows.size()];
		for (int i = 0; i < rows.size(); i++)
			filterVisibleRows[i] = visibleRows[rows.get(i)];
		matrixView.setVisibleRows(filterVisibleRows);
	}

	public static void filterColumns(IMatrixView matrixView, List<String> values, boolean useRegex) {
		IMatrixView mv = new TransposedMatrixView(matrixView);
		filterRows(mv, values, useRegex);
	}

	private static void filterByNames(IMatrixView mv, List<String> values, List<Integer> rows) {

		int rowCount = mv.getRowCount();

		Map<String, Integer> rowIndexMap = new HashMap<String, Integer>();

		for (int i = 0; i < rowCount; i++) {
			final String rowName = mv.getRowLabel(i);
			rowIndexMap.put(rowName, i);
		}

		for (int i = 0; i < values.size(); i++) {
			final String name = values.get(i);
			Integer row = rowIndexMap.get(name);
			if (row != null)
				rows.add(row);
		}
	}

	private static void filterByRegex(IMatrixView mv, List<String> values, List<Integer> rows) {
		// Compile patterns
		List<Pattern> patterns = new ArrayList<Pattern>(values.size());
		for (String name : values)
			patterns.add(Pattern.compile(name));

		int rowCount = mv.getRowCount();

		// Check patterns
		for (int i = 0; i < rowCount; i++) {
			final String rowName = mv.getRowLabel(i);
			for (int j = 0; j < patterns.size(); j++) {
				if (patterns.get(j).matcher(rowName).matches()) {
					rows.add(i);
					break;
				}
			}
		}
	}
}
