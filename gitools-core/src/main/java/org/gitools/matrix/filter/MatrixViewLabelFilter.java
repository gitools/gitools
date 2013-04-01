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

import org.gitools.label.AnnotationsPatternProvider;
import org.gitools.label.LabelProvider;
import org.gitools.label.MatrixColumnsLabelProvider;
import org.gitools.label.MatrixRowsLabelProvider;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;

import java.util.*;
import java.util.regex.Pattern;

public class MatrixViewLabelFilter
{

    public enum FilterDimension
    {
        ROWS, COLUMNS
    }

    private static interface LabelFilter
    {
        int matchIndex(String label);
    }

    private static class StringFilter implements LabelFilter
    {
        private Map<String, Integer> values;

        public StringFilter(List<String> values)
        {
            this.values = new HashMap<String, Integer>();
            for (int i = 0; i < values.size(); i++)
            {
                String v = values.get(i).trim();
                if (!v.isEmpty())
                {
                    this.values.put(v, i);
                }
            }
        }

        @Override
        public int matchIndex(String label)
        {
            Integer index = values.get(label);
            return index != null ? index : -1;
        }
    }

    private static class RegexFilter implements LabelFilter
    {
        private List<Pattern> patterns;

        public RegexFilter(List<String> values)
        {
            patterns = new ArrayList<Pattern>(values.size());
            for (String value : values)
                if (!value.trim().isEmpty())
                {
                    patterns.add(Pattern.compile(value));
                }
        }

        @Override
        public int matchIndex(String label)
        {
            for (int i = 0; i < patterns.size(); i++)
            {
                Pattern pat = patterns.get(i);
                if (pat.matcher(label).matches())
                {
                    return i;
                }
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
            boolean useRegex)
    {

        LabelProvider labelProvider = null;

        switch (dim)
        {
            case ROWS:
                labelProvider = new MatrixRowsLabelProvider(matrixView);
                if (!pattern.equalsIgnoreCase("${id}"))
                {
                    labelProvider = new AnnotationsPatternProvider(
                            labelProvider, annMatrix, pattern);
                }

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
                {
                    labelProvider = new AnnotationsPatternProvider(
                            labelProvider, annMatrix, pattern);
                }

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
            int[] visibleIndices)
    {

        LabelFilter filter = null;
        if (useRegex)
        {
            filter = new RegexFilter(values);
        }
        else
        {
            filter = new StringFilter(values);
        }

        final List<Integer> selectedIndices = new ArrayList<Integer>();
        final List<Integer> matchIndices = new ArrayList<Integer>();

        int count = labelProvider.getCount();
        for (int index = 0; index < count; index++)
        {
            String label = labelProvider.getLabel(index);
            int mi = filter.matchIndex(label);
            if (mi != -1)
            {
                selectedIndices.add(visibleIndices[index]);
                matchIndices.add(mi);
            }
        }

        Integer[] sortIndices = new Integer[selectedIndices.size()];
        for (int i = 0; i < sortIndices.length; i++)
            sortIndices[i] = i;

        Arrays.sort(sortIndices, new Comparator<Integer>()
        {
            @Override
            public int compare(Integer i1, Integer i2)
            {
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
