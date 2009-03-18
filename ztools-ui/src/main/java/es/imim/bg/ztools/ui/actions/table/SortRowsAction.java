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

public class SortRowsAction {

	private static final long serialVersionUID = -1582437709508438222L;
	private ITableContents contents;
	private List<SortCriteria> criteriaList;

	public SortRowsAction(ITable table, List<SortCriteria> criteriaList, boolean allRows) {
		
		this.contents = table.getContents();
		this.criteriaList = criteriaList;
		
		//cols
		int colCount;
		int[] columns = null;
		if(table.getSelectedColumns().length == 0) {
			colCount = table.getVisibleColumns().length;
			columns = new int[colCount];
			for (int c = 0; c < colCount; c++)
				columns[c] = c;
		} else
			columns = table.getSelectedColumns();

		//rows
		Integer[] rows = null;
		final int rowCount;
		if(allRows) {
			rowCount = contents.getRowCount();
			rows = new Integer[rowCount];
			for (int j = 0; j < rowCount; j++)
				rows[j] = j;
		}
		else {
			rowCount = table.getRowCount();
			rows = new Integer[rowCount];
			int[] visibleRows = table.getVisibleRows();
			for (int j = 0; j < rowCount; j++)
				rows[j] = visibleRows[j];
		}

		Integer[] sortedRowIndices = sortRows(columns, rows);
		int[] visibleSortedRows = new int[rowCount];
		for (int k = 0; k < rowCount; k++)
			visibleSortedRows[k] = sortedRowIndices[k];
		table.setVisibleRows(visibleSortedRows);
	}
		

	private Integer[] sortRows(final int[] selection,
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
		
		final int N = selection.length;
		DoubleFactory1D df = DoubleFactory1D.dense;
		final DoubleMatrix1D row1 = df.make(N);
		final DoubleMatrix1D row2 = df.make(N);
		
		Arrays.sort(indices, new Comparator<Integer>() {		
			@Override
			public int compare(Integer idx1, Integer idx2) {
				
				int level = 0;
				double aggr1 = 0, aggr2 = 0;

				
				while (aggr1 == aggr2 && level < criterias) {
						
					for (int i = 0; i < N; i++) {
						int col = selection[i];
						
						Object value1 = contents.getCellValue(idx1, col, properties.get(level));
						double v1 = TableUtils.doubleValue(value1);
	
						if (!Double.isNaN(v1)) {
							row1.set(i, v1);
						}
						
						Object value2 = contents.getCellValue(idx2, col, properties.get(level));
						double v2 = TableUtils.doubleValue(value2);
	
						if (!Double.isNaN(v2)) {
							row2.set(i, v2);
						}
					}
					aggr1 = aggregators.get(level).aggregate(row1);
					aggr2 = aggregators.get(level).aggregate(row2);
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
