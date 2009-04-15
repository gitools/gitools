package es.imim.bg.ztools.ui.actions.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import es.imim.bg.ztools.aggregation.IAggregator;
import es.imim.bg.ztools.table.ITable;
import es.imim.bg.ztools.table.ITableContents;
import es.imim.bg.ztools.table.TableUtils;
import es.imim.bg.ztools.table.sort.SortCriteria;

public class SortColumnsAction {
	
	ITableContents contents;
	List<SortCriteria> criteriaList;
	
	
	public SortColumnsAction(ITable table, List<SortCriteria> criteriaList,
			boolean allCols) {
		
		this.contents = table.getContents();
		this.criteriaList = criteriaList;
		
		//rows
		int rowCount;
		int[] rows = null;
		if(table.getSelectedRows().length == 0) {
			rowCount = table.getVisibleRows().length;
			rows = new int[rowCount];
			for (int r = 0; r < rowCount; r++)
				rows[r] = r;
		} else
			rows = table.getSelectedRows();

		
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
			colCount = table.getColumnCount();
			cols = new Integer[colCount];
			int[] visibleCols = table.getVisibleColumns();
			for (int j = 0; j < colCount; j++)
				cols[j] = visibleCols[j];
		}

		Integer[] sortedColIndices = sortCols(rows, cols);
		int[] visibleSortedCols = new int[colCount];
		for (int k = 0; k < colCount; k++)
			visibleSortedCols[k] = sortedColIndices[k];
		table.setVisibleColumns(visibleSortedCols);
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
						double v1 = TableUtils.doubleValue(value1);
	
						if (!Double.isNaN(v1)) {
							col1.set(i, v1);
						}
						
						Object value2 = contents.getCellValue(row, idx2, properties.get(level));
						double v2 = TableUtils.doubleValue(value2);
	
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
