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

package org.gitools.matrix.sort;

import edu.upf.bg.aggregation.IAggregator;
import edu.upf.bg.aggregation.SumAbsAggregator;
import org.apache.commons.lang.ArrayUtils;
import org.gitools.label.AnnotationsPatternProvider;
import org.gitools.label.LabelProvider;
import org.gitools.label.MatrixColumnsLabelProvider;
import org.gitools.label.MatrixRowsLabelProvider;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.filter.MatrixViewLabelFilter;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.sort.ValueSortCriteria.SortDirection;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class MatrixViewSorter {


	public static void sortByMutualExclusion(final IMatrixView matrixView,
												String pattern,
												AnnotationMatrix am,
												List<String> values,
												boolean regExChecked,
												boolean applyToRows,
												boolean applyToColumns) {
		if (applyToRows)
			sortRowsByMutualExclusion(matrixView, pattern, am, values, regExChecked);

		if (applyToColumns)
			sortColumnsByMutualExclusion(matrixView, pattern, am, values, regExChecked);
	}
	
	protected static void sortRowsByMutualExclusion(final IMatrixView matrixView,
												String pattern,
												AnnotationMatrix am,
												List<String> values,
												boolean regExChecked) {

		int[] selColumns = matrixView.getSelectedColumns();

		LabelProvider labelProvider = new MatrixRowsLabelProvider(matrixView);
		labelProvider = new AnnotationsPatternProvider(labelProvider, am, pattern);
		int[] visibleRows = matrixView.getVisibleRows();
		int[] selRows = MatrixViewLabelFilter.filterLabels(labelProvider,
															values,
															regExChecked,
															visibleRows);


		int numRows = selRows.length;
		final Integer[] indices = new Integer[numRows];
		for (int i = 0; i < selRows.length; i++)
			indices[i] = ArrayUtils.indexOf(visibleRows, selRows[i]);

		if (selColumns == null || selColumns.length == 0) {
			selColumns = new int[matrixView.getColumnCount()];
			for (int i = 0; i < selColumns.length; i++)
				selColumns[i] = i;
		}




		final int[] selectedColumns = selColumns;
		final double[] valueBuffer = new double[selectedColumns.length];

		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override public int compare(Integer idx1, Integer idx2) {

				double aggr1 = 0.0;
				double aggr2 = 0.0;

				//int criteriaIndex = 0;

				//while (criteriaIndex < criteriaArray.length && aggr1 == aggr2) {
				//criteria = criteriaArray[criteriaIndex];

				IAggregator aggregator = SumAbsAggregator.INSTANCE;
				int propIndex = matrixView.getSelectedPropertyIndex();

				SortDirection sortDirection = SortDirection.DESCENDING;

				aggr1 = aggregateValue(matrixView, selectedColumns, idx1, propIndex, aggregator, valueBuffer);
				aggr2 = aggregateValue(matrixView, selectedColumns, idx2, propIndex, aggregator, valueBuffer);

				//	criteriaIndex++;
				//}

				int res = (int) Math.signum(aggr1 - aggr2);
				return res * sortDirection.getFactor();
			}

			private double aggregateValue(
					IMatrixView matrixView,
					int[] selectedColumns,
					int idx,
					int propIndex,
					IAggregator aggregator,
					double[] valueBuffer) {

				for (int i = 0; i < selectedColumns.length; i++) {
					int col = selectedColumns[i];

					Object valueObject = matrixView.getCellValue(idx, col, propIndex);
					valueBuffer[i] = MatrixUtils.doubleValue(valueObject);
				}

				return aggregator.aggregate(valueBuffer);
			}
		};

		Arrays.sort(indices, comparator);

		//put the chosen indices at the top of the matrix
		int[] rowIndices = new int[indices.length];
		for (int i = 0; i < indices.length; i++) {
			rowIndices[i] = visibleRows[indices[i]];
		}

		for (int i = 0; i < rowIndices.length; i++) {
			int rowPos = ArrayUtils.indexOf(visibleRows, rowIndices[i]);
			int replacingRow = rowIndices[i];
			for (int j = i; j <= rowPos; j++) {
				int rowToReplace = visibleRows[j];
				visibleRows[j] = replacingRow;
				replacingRow = rowToReplace;
			}
		}



		final int[] sortedVisibleRows = visibleRows;
		


		matrixView.setVisibleRows(sortedVisibleRows);

		ValueSortCriteria[] criteriaArray =
						new ValueSortCriteria[1];
		int index = matrixView.getSelectedPropertyIndex();
		criteriaArray[0] = new ValueSortCriteria(index, SumAbsAggregator.INSTANCE, SortDirection.DESCENDING);

		for (int i = numRows-1; i >= 0; i--) {
			//selRows[i] = i;
			int[] exclusiveRow = new int[1];
			exclusiveRow[0] = i;
			sortColumnsByValue(matrixView, null, exclusiveRow, criteriaArray);
		}


	}

	private static void sortColumnsByMutualExclusion(IMatrixView matrixView, String pattern, AnnotationMatrix am, List<String> values, boolean regExChecked) {


		throw new UnsupportedOperationException("Mutually exclusive sorting for columns is not yet implemented");
	}


	public static void sortByValue(IMatrixView matrixView, ValueSortCriteria[] criteria, boolean applyToRows, boolean applyToColumns) {
		if (applyToRows)
			sortRowsByValue(matrixView, matrixView.getSelectedColumns(), matrixView.getSelectedRows(), criteria);

		if (applyToColumns)
			sortColumnsByValue(matrixView, matrixView.getSelectedColumns(), matrixView.getSelectedRows(), criteria);
	}

	protected static void sortRowsByValue(final IMatrixView matrixView, int[] selColumns, int[] selRows, final ValueSortCriteria[] criteriaArray) {

		if (criteriaArray == null || criteriaArray.length == 0)
			return;

        if (selRows == null || selRows.length == 0) {
            selRows = new int[matrixView.getRowCount()];
            for (int i = 0; i < selRows.length; i++)
                selRows[i] = i;
        }
		int numRows = selRows.length;

        /*final Integer[] indices = new Integer[numRows];
          for (int i = 0; i < numRows; i++)
              indices[i] = i;      */

		if (selColumns == null || selColumns.length == 0) {
			selColumns = new int[matrixView.getColumnCount()];
			for (int i = 0; i < selColumns.length; i++)
				selColumns[i] = i;
		}

		final int[] selectedColumns = selColumns;
        final Integer[] selectedRows = ArrayUtils.toObject(selRows);
		final double[] valueBuffer = new double[selectedColumns.length];

		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override public int compare(Integer idx1, Integer idx2) {

				double aggr1 = 0.0;
				double aggr2 = 0.0;

				ValueSortCriteria criteria = null;
				int criteriaIndex = 0;

				while (criteriaIndex < criteriaArray.length && aggr1 == aggr2) {
					criteria = criteriaArray[criteriaIndex];
					IAggregator aggregator = criteria.getAggregator();
					int propIndex = criteria.getAttributeIndex();

					aggr1 = aggregateValue(matrixView, selectedColumns, idx1, propIndex, aggregator, valueBuffer);
					aggr2 = aggregateValue(matrixView, selectedColumns, idx2, propIndex, aggregator, valueBuffer);

					criteriaIndex++;
				}

				int res = (int) Math.signum(aggr1 - aggr2);
				return res * criteria.getDirection().getFactor();
			}

			private double aggregateValue(
					IMatrixView matrixView,
					int[] selectedColumns,
					int idx,
					int propIndex,
					IAggregator aggregator,
					double[] valueBuffer) {

				for (int i = 0; i < selectedColumns.length; i++) {
					int col = selectedColumns[i];

					Object valueObject = matrixView.getCellValue(idx, col, propIndex);
					valueBuffer[i] = MatrixUtils.doubleValue(valueObject);
				}

				return aggregator.aggregate(valueBuffer);
			}
		};

		Arrays.sort(selectedRows, comparator);

		final int[] visibleRows = matrixView.getVisibleRows();
		final int[] sortedVisibleRows = new int[visibleRows.length];
        for (int i = 0; i < visibleRows.length; i++)
            sortedVisibleRows[i] = visibleRows[i];

		for (int i = 0; i < numRows; i++)
			sortedVisibleRows[selRows[i]] = visibleRows[selectedRows[i]];

		matrixView.setVisibleRows(sortedVisibleRows);
	}

	protected static void sortColumnsByValue(final IMatrixView matrixView, int[] selColumns, int[] selRows, final ValueSortCriteria[] criteriaArray) {

		if (criteriaArray == null || criteriaArray.length == 0)
			return;

        if (selColumns == null || selColumns.length == 0) {
            selColumns = new int[matrixView.getColumnCount()];
            for (int i = 0; i < selColumns.length; i++)
                selColumns[i] = i;
        }

		int numColumns = selColumns.length;

		if (selRows == null || selRows.length == 0) {
			selRows = new int[matrixView.getRowCount()];
			for (int i = 0; i < selRows.length; i++)
				selRows[i] = i;
		}

		final int[] selectedRows = selRows;
        final Integer[] selectedColumns = ArrayUtils.toObject(selColumns);
		final double[] valueBuffer = new double[selectedRows.length];

		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override public int compare(Integer idx1, Integer idx2) {

				double aggr1 = 0.0;
				double aggr2 = 0.0;

				ValueSortCriteria criteria = null;
				int criteriaIndex = 0;

				while (criteriaIndex < criteriaArray.length && aggr1 == aggr2) {
					criteria = criteriaArray[criteriaIndex];
					IAggregator aggregator = criteria.getAggregator();
					int propIndex = criteria.getAttributeIndex();

					aggr1 = aggregateValue(matrixView, selectedRows, idx1, propIndex, aggregator, valueBuffer);
					aggr2 = aggregateValue(matrixView, selectedRows, idx2, propIndex, aggregator, valueBuffer);

					criteriaIndex++;
				}

				int res = (int) Math.signum(aggr1 - aggr2);
				return res * criteria.getDirection().getFactor();
			}

			private double aggregateValue(
					IMatrixView matrixView,
					int[] selectedRows,
					int idx,
					int propIndex,
					IAggregator aggregator,
					double[] valueBuffer) {

				for (int i = 0; i < selectedRows.length; i++) {
					int row = selectedRows[i];

					Object valueObject = matrixView.getCellValue(row, idx, propIndex);
					valueBuffer[i] = MatrixUtils.doubleValue(valueObject);
				}

				return aggregator.aggregate(valueBuffer);
			}
		};

		Arrays.sort(selectedColumns, comparator);

		final int[] visibleColumns = matrixView.getVisibleColumns();
		final int[] sortedVisibleColumns = new int[visibleColumns.length];
		for (int i = 0; i < visibleColumns.length; i++)
			sortedVisibleColumns[i] = visibleColumns[i];

        for (int i = 0; i < numColumns; i++)
            sortedVisibleColumns[selColumns[i]] = visibleColumns[selectedColumns[i]];

		matrixView.setVisibleColumns(sortedVisibleColumns);
	}

	public static void sortByLabel(IMatrixView matrixView,
			boolean sortRows,
			SortDirection rowsDirection,
            boolean rowsNumeric,
			boolean sortCols,
			SortDirection colsDirection,
            boolean colsNumeric) {

		sortByLabel(matrixView,
				sortRows, "${id}", null, rowsDirection, rowsNumeric,
				sortCols, "${id}", null, colsDirection, colsNumeric);
	}

    //TODO: sort by label with all selected properties!
	public static void sortByLabel(
			IMatrixView matrixView,
			boolean sortRows,
			String rowsPattern,
			AnnotationMatrix rowsAnnMatrix,
			SortDirection rowsDirection,
            boolean rowsNumeric,
			boolean sortCols,
			String colsPattern,
			AnnotationMatrix colsAnnMatrix,
			SortDirection colsDirection,
            boolean colsNumeric) {
		
		if (sortRows) {
			LabelProvider labelProvider = new MatrixRowsLabelProvider(matrixView);
			if (!rowsPattern.equalsIgnoreCase("${id}"))
				labelProvider = new AnnotationsPatternProvider(
						labelProvider, rowsAnnMatrix, rowsPattern);

			matrixView.setVisibleRows(
					sortLabels(
						labelProvider,
						rowsDirection,
						matrixView.getVisibleRows(),
                        rowsNumeric));
		}

		if (sortCols) {
			LabelProvider labelProvider = new MatrixColumnsLabelProvider(matrixView);
			if (!colsPattern.equalsIgnoreCase("${id}"))
				labelProvider = new AnnotationsPatternProvider(
						labelProvider, colsAnnMatrix, colsPattern);

			matrixView.setVisibleColumns(
					sortLabels(
						labelProvider,
						colsDirection,
						matrixView.getVisibleColumns(),
                        colsNumeric));
		}
	}

	public static int[] sortLabels(
			final LabelProvider labelProvider,
			SortDirection direction,
			int[] visibleIndices,
            boolean numeric) {

		int count = labelProvider.getCount();
		Integer[] indices = new Integer[count];
		for (int i = 0; i < count; i++)
			indices[i] = i;

		final int dirSign = direction == SortDirection.ASCENDING ? 1 : -1;

		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override public int compare(Integer idx1, Integer idx2) {
				String label1 = labelProvider.getLabel(idx1);
				String label2 = labelProvider.getLabel(idx2);
				return label1.compareTo(label2) * dirSign;
			}
		};

        Comparator<Integer> numericComparator = new Comparator<Integer>() {
            @Override public int compare(Integer idx1, Integer idx2) {
                Double v1;
                Double v2;

                try {
                    v1 = Double.parseDouble(labelProvider.getLabel(idx1));
                } catch (NumberFormatException e) {
                    v1 = Double.NaN;
                }

                try {
                    v2 = Double.parseDouble(labelProvider.getLabel(idx2));
                } catch (NumberFormatException e) {
                    v2 = Double.NaN;
                }

                if (v1.isNaN() || v2.isNaN())
                    return v1.compareTo(v2);
                else
                    return v1.compareTo(v2) * dirSign;
            }
        };

		if (numeric)
            Arrays.sort(indices,numericComparator);
        else
            Arrays.sort(indices, comparator);

		int[] vIndices = new int[count];
		for (int i = 0; i < count; i++)
			vIndices[i] = visibleIndices[indices[i]];
		return vIndices;
	}

}