package org.gitools.matrix.sort;

import java.util.Arrays;
import java.util.Comparator;
import org.gitools.aggregation.IAggregator;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrixView;

public abstract class MatrixViewSorter {

	public static void sort(IMatrixView matrixView, SortCriteria[] criteria, boolean applyToRows, boolean applyToColumns) {
		if (applyToRows)
			sortRows(matrixView, matrixView.getSelectedColumns(), criteria);

		if (applyToColumns)
			sortColumns(matrixView, matrixView.getSelectedRows(), criteria);
	}

	protected static void sortRows(final IMatrixView matrixView, int[] selColumns, final SortCriteria[] criteriaArray) {

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

				SortCriteria criteria = null;
				int criteriaIndex = 0;

				while (criteriaIndex < criteriaArray.length && aggr1 == aggr2) {
					criteria = criteriaArray[criteriaIndex];
					IAggregator aggregator = criteria.getAggregator();
					int propIndex = criteria.getPropertyIndex();

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

	protected static void sortColumns(final IMatrixView matrixView, int[] selRows, final SortCriteria[] criteriaArray) {

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

				SortCriteria criteria = null;
				int criteriaIndex = 0;

				while (criteriaIndex < criteriaArray.length && aggr1 == aggr2) {
					criteria = criteriaArray[criteriaIndex];
					IAggregator aggregator = criteria.getAggregator();
					int propIndex = criteria.getPropertyIndex();

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
}
