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
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.gitools.label.AnnotationsPatternProvider;
import org.gitools.label.LabelProvider;
import org.gitools.label.MatrixColumnsLabelProvider;
import org.gitools.label.MatrixRowsLabelProvider;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;

public class MatrixViewLabelFilter {

	public enum FilterDimension {
		ROWS, COLUMNS
	}

	private static interface LabelFilter {
		int matchIndex(String label);
	}

	private static class StringFilter implements LabelFilter {
		private Map<String, Integer> values;
		public StringFilter(List<String> values) {
			this.values = new HashMap<String, Integer>();
			for (int i = 0; i < values.size(); i++) {
				String v = values.get(i).trim();
				if (!v.isEmpty())
					this.values.put(v, i);
			}
		}

		@Override
		public int matchIndex(String label) {
			Integer index = values.get(label);
			return index != null ? index : -1;
		}
	}

	private static class RegexFilter implements LabelFilter {
		private List<Pattern> patterns;
		public RegexFilter(List<String> values) {
			patterns = new ArrayList<Pattern>(values.size());
			for (String value : values)
				if (!value.trim().isEmpty())
					patterns.add(Pattern.compile(value));
		}
		@Override
		public int matchIndex(String label) {
			for (int i = 0; i < patterns.size(); i++) {
				Pattern pat = patterns.get(i);
				if (pat.matcher(label).matches())
					return i;
			}
			return -1;
		}
	}

	public static void filter(
			IMatrixView matrixView,
			FilterDimension dim,
			String pattern,
			AnnotationMatrix annMatrix,
			List<String> values,
			boolean useRegex) {

		LabelProvider labelProvider = null;

		switch (dim) {
			case ROWS:
				labelProvider = new MatrixRowsLabelProvider(matrixView);
				if (!pattern.equalsIgnoreCase("${id}"))
					labelProvider = new AnnotationsPatternProvider(
							labelProvider, annMatrix, pattern);

				matrixView.setVisibleRows(
						filterLabels(
							labelProvider,
							values,
							useRegex,
							matrixView.getVisibleRows()));
				break;

			case COLUMNS:
				labelProvider = new MatrixColumnsLabelProvider(matrixView);
				if (!pattern.equalsIgnoreCase("${id}"))
					labelProvider = new AnnotationsPatternProvider(
							labelProvider, annMatrix, pattern);

				matrixView.setVisibleColumns(
						filterLabels(
							labelProvider,
							values,
							useRegex,
							matrixView.getVisibleColumns()));
				break;
		}
	}

	public static int[] filterLabels(
			LabelProvider labelProvider,
			List<String> values,
			boolean useRegex,
			int[] visibleIndices) {

		LabelFilter filter = null;
		if (useRegex)
			filter = new RegexFilter(values);
		else
			filter = new StringFilter(values);

		final List<Integer> selectedIndices = new ArrayList<Integer>();
		final List<Integer> matchIndices = new ArrayList<Integer>();

		int count = labelProvider.getCount();
		for (int index = 0; index < count; index++) {
			String label = labelProvider.getLabel(index);
			int mi = filter.matchIndex(label);
			if (mi != -1) {
				selectedIndices.add(visibleIndices[index]);
				matchIndices.add(mi);
			}
		}

		Integer[] sortIndices = new Integer[selectedIndices.size()];
		for (int i = 0; i< sortIndices.length; i++)
			sortIndices[i] = i;

		Arrays.sort(sortIndices, new Comparator<Integer>() {
			@Override public int compare(Integer i1, Integer i2) {
				int d1 = matchIndices.get(i1);
				int d2 = matchIndices.get(i2);
				return d1 - d2;
			}
		});

		int[] vIndices = new int[sortIndices.length];
		for (int i = 0; i < vIndices.length; i++)
			vIndices[i] = selectedIndices.get(sortIndices[i]);

		return vIndices;
	}

	/*public static void filterRows(
			IMatrixView matrixView,
			String pattern,
			AnnotationMatrix am,
			List<String> values,
			boolean useRegex) {
		
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
	}*/
}
