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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import jsc.distributions.MannWhitneyU;
import jsc.independentsamples.MannWhitneyTest;
import jsc.tests.H1;
import org.apache.commons.lang.ArrayUtils;
import org.gitools.aggregation.IAggregator;
import org.gitools.aggregation.SumAbsAggregator;
import org.gitools.label.AnnotationsPatternProvider;
import org.gitools.label.LabelProvider;
import org.gitools.label.MatrixColumnsLabelProvider;
import org.gitools.label.MatrixRowsLabelProvider;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.filter.MatrixViewLabelFilter;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.sort.ValueSortCriteria.SortDirection;

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

				IAggregator aggregator = new SumAbsAggregator();
				int propIndex = matrixView.getSelectedPropertyIndex();

									//TODO: figure out how to get the attribute (which data matrix is shown)
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
		criteriaArray[0] = new ValueSortCriteria(0, new SumAbsAggregator(), SortDirection.DESCENDING);

		for (int i = numRows-1; i >= 0; i--) {
			//selRows[i] = i;
			int[] exclusiveRow = new int[1];
			exclusiveRow[0] = i;
			sortColumnsByValue(matrixView, exclusiveRow, criteriaArray);
		}




				/*
ENSG00000075218
ENSG00000198625
ENSG00000141510
ENSG00000172115
ENSG00000113328
				 *
TP53
MDM2
MDM4
CCNG1
CHEK1
CCNE1
RRM2
TSC2
BID
=========
CDKN2B
INHBE
INHBC
SMAD4
EP300
DCN
SKP1
SMAD9
SMURF1
INHBA
				 *
ENSG00000147883
ENSG00000139269
ENSG00000175189
ENSG00000100393
ENSG00000011465
ENSG00000141646
ENSG00000113558
ENSG00000120693
ENSG00000198742
ENSG00000122641

		 */

	}

	private static void sortColumnsByMutualExclusion(IMatrixView matrixView, String pattern, AnnotationMatrix am, List<String> values, boolean regExChecked) {


		throw new UnsupportedOperationException("Not yet implemented");
	}


	public static void sortByValue(IMatrixView matrixView, ValueSortCriteria[] criteria, boolean applyToRows, boolean applyToColumns) {
		if (applyToRows)
			sortRowsByValue(matrixView, matrixView.getSelectedColumns(), criteria);

		if (applyToColumns)
			sortColumnsByValue(matrixView, matrixView.getSelectedRows(), criteria);
	}

	protected static void sortRowsByValue(final IMatrixView matrixView, int[] selColumns, final ValueSortCriteria[] criteriaArray) {

		if (criteriaArray == null || criteriaArray.length == 0)
			return;

		int numRows = matrixView.getRowCount();
		final Integer[] indices = new Integer[numRows];
		for (int i = 0; i < numRows; i++)
			indices[i] = i;

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

		Arrays.sort(indices, comparator);

		final int[] visibleRows = matrixView.getVisibleRows();
		final int[] sortedVisibleRows = new int[numRows];

		for (int i = 0; i < numRows; i++)
			sortedVisibleRows[i] = visibleRows[indices[i]];

		matrixView.setVisibleRows(sortedVisibleRows);
	}

	protected static void sortColumnsByValue(final IMatrixView matrixView, int[] selRows, final ValueSortCriteria[] criteriaArray) {

		if (criteriaArray == null || criteriaArray.length == 0)
			return;

		int numColumns = matrixView.getColumnCount();
		final Integer[] indices = new Integer[numColumns];
		for (int i = 0; i < numColumns; i++)
			indices[i] = i;

		if (selRows == null || selRows.length == 0) {
			selRows = new int[matrixView.getRowCount()];
			for (int i = 0; i < selRows.length; i++)
				selRows[i] = i;
		}

		final int[] selectedRows = selRows;
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

		Arrays.sort(indices, comparator);

		final int[] visibleColumns = matrixView.getVisibleColumns();
		final int[] sortedVisibleColumns = new int[numColumns];

		for (int i = 0; i < numColumns; i++)
			sortedVisibleColumns[i] = visibleColumns[indices[i]];

		matrixView.setVisibleColumns(sortedVisibleColumns);
	}

	public static void sortByLabel(IMatrixView matrixView,
			boolean sortRows,
			SortDirection rowsDirection,
			boolean sortCols,
			SortDirection colsDirection) {

		sortByLabel(matrixView,
				sortRows, "${id}", null, rowsDirection,
				sortCols, "${id}", null, colsDirection);
	}

	public static void sortByLabel(
			IMatrixView matrixView,
			boolean sortRows,
			String rowsPattern,
			AnnotationMatrix rowsAnnMatrix,
			SortDirection rowsDirection,
			boolean sortCols,
			String colsPattern,
			AnnotationMatrix colsAnnMatrix,
			SortDirection colsDirection) {
		
		if (sortRows) {
			LabelProvider labelProvider = new MatrixRowsLabelProvider(matrixView);
			if (!rowsPattern.equalsIgnoreCase("${id}"))
				labelProvider = new AnnotationsPatternProvider(
						labelProvider, rowsAnnMatrix, rowsPattern);

			matrixView.setVisibleRows(
					sortLabels(
						labelProvider,
						rowsDirection,
						matrixView.getVisibleRows()));
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
						matrixView.getVisibleColumns()));
		}
	}

	public static int[] sortLabels(
			final LabelProvider labelProvider,
			SortDirection direction,
			int[] visibleIndices) {

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

		Arrays.sort(indices, comparator);

		int[] vIndices = new int[count];
		for (int i = 0; i < count; i++)
			vIndices[i] = visibleIndices[indices[i]];
		return vIndices;
	}






	/*public static void sortByLabel(IMatrixView matrixView, SortDirection sortDirection, boolean applyToRows, boolean applyToColumns) {
		if (applyToRows)
			sortRowsByLabel(matrixView, sortDirection);

		if (applyToColumns)
			sortColumnsByLabel(matrixView, sortDirection);
	}

	protected static void sortRowsByLabel(final IMatrixView matrixView, SortDirection direction) {

		int numRows = matrixView.getRowCount();
		final Integer[] indices = new Integer[numRows];
		for (int i = 0; i < numRows; i++)
			indices[i] = i;

		final int dirSign = direction == SortDirection.ASCENDING ? 1 : -1;

		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override public int compare(Integer idx1, Integer idx2) {
				String label1 = matrixView.getRowLabel(idx1);
				String label2 = matrixView.getRowLabel(idx2);
				return label1.compareTo(label2) * dirSign;
			}
		};

		Arrays.sort(indices, comparator);

		final int[] visibleRows = matrixView.getVisibleRows();
		final int[] sortedVisibleRows = new int[numRows];

		for (int i = 0; i < numRows; i++)
			sortedVisibleRows[i] = visibleRows[indices[i]];

		matrixView.setVisibleRows(sortedVisibleRows);
	}

	protected static void sortColumnsByLabel(final IMatrixView matrixView, SortDirection direction) {

		int numColumns = matrixView.getColumnCount();
		final Integer[] indices = new Integer[numColumns];
		for (int i = 0; i < numColumns; i++)
			indices[i] = i;

		final int dirSign = direction == SortDirection.ASCENDING ? 1 : -1;

		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override public int compare(Integer idx1, Integer idx2) {
				String label1 = matrixView.getColumnLabel(idx1);
				String label2 = matrixView.getColumnLabel(idx2);
				return label1.compareTo(label2) * dirSign;
			}
		};

		Arrays.sort(indices, comparator);

		final int[] visibleColumns = matrixView.getVisibleColumns();
		final int[] sortedVisibleColumns = new int[numColumns];

		for (int i = 0; i < numColumns; i++)
			sortedVisibleColumns[i] = visibleColumns[indices[i]];

		matrixView.setVisibleColumns(sortedVisibleColumns);
	}*/
}
