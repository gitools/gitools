package org.gitools.ui.actions.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import org.gitools.aggregation.IAggregator;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.sort.SortCriteria;
import org.gitools.model.matrix.IMatrix;
import org.gitools.model.matrix.IMatrixView;

public class SortByValueActionColumnsDelegate {
	
	IMatrix contents;
	List<SortCriteria> criteriaList;
	
	public SortByValueActionColumnsDelegate(IMatrixView matrixView, List<SortCriteria> criteriaList,
			boolean allCols) {
		
		this.contents = matrixView.getContents();
		this.criteriaList = criteriaList;
		
		//rows
		int rowCount;
		int[] rows = null;
		if(matrixView.getSelectedRows().length == 0) {
			rowCount = matrixView.getVisibleRows().length;
			rows = new int[rowCount];
			for (int r = 0; r < rowCount; r++)
				rows[r] = r;
		} else
			rows = matrixView.getSelectedRows();

		
		//cols
		Integer[] cols = null;
		final int colCount;
		if(allCols) {
			colCount = contents.getColumnCount();
			cols = new Integer[colCount];
			for (int j = 0; j < colCount; j++)
				cols[j] = j;
		}
		else {
			colCount = matrixView.getColumnCount();
			cols = new Integer[colCount];
			int[] visibleCols = matrixView.getVisibleColumns();
			for (int j = 0; j < colCount; j++)
				cols[j] = visibleCols[j];
		}

		Integer[] sortedColIndices = sortCols(rows, cols);
		int[] visibleSortedCols = new int[colCount];
		for (int k = 0; k < colCount; k++)
			visibleSortedCols[k] = sortedColIndices[k];
		matrixView.setVisibleColumns(visibleSortedCols);
	}
	

	private Integer[] sortCols(final int[] selectedRows,
								final Integer[] indices) {
		
		final List<IAggregator> aggregators = new ArrayList<IAggregator>();
		final List<Integer> properties = new ArrayList<Integer>();
		final List<Integer> directions = new ArrayList<Integer>();
		
		final int criterias = criteriaList.size();
		
		for (int i = 0; i < criteriaList.size(); i++) {
			SortCriteria sortCriteria = criteriaList.get(i);
			
			properties.add(sortCriteria.getPropertyIndex());
			directions.add(sortCriteria.getDirection().getFactor());
			aggregators.add(sortCriteria.getAggregator());
		}
		
		final int N = selectedRows.length;
		DoubleFactory1D df = DoubleFactory1D.dense;
		final DoubleMatrix1D col1 = df.make(N);
		final DoubleMatrix1D col2 = df.make(N);
	
		
		Arrays.sort(indices, new Comparator<Integer>() {		
			@Override
			public int compare(Integer idx1, Integer idx2) {
				
				int level = 0;
				double aggr1 = 0, aggr2 = 0;
	
				
				while (aggr1 == aggr2 && level < criterias) {
						
					for (int i = 0; i < N; i++) {
						int row = selectedRows[i];
						
						Object value1 = contents.getCellValue(row , idx1, properties.get(level));
						double v1 = MatrixUtils.doubleValue(value1);
	
						if (!Double.isNaN(v1)) {
							col1.set(i, v1);
						}
						
						Object value2 = contents.getCellValue(row, idx2, properties.get(level));
						double v2 = MatrixUtils.doubleValue(value2);
	
						if (!Double.isNaN(v2)) {
							col2.set(i, v2);
						}
					}
					aggr1 = aggregators.get(level).aggregate(col1);
					aggr2 = aggregators.get(level).aggregate(col2);
					level++;
				}
	
				int res = (int) Math.signum(aggr1 - aggr2);
				level--;
				return res*directions.get(level);
			}
		});
		return indices;
	}
}
