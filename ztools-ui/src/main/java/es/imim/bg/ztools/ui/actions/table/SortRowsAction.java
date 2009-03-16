package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;

import es.imim.bg.ztools.table.TableUtils;
import es.imim.bg.ztools.table.element.IElementProperty;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.aggregation.IAggregation;
import es.imim.bg.ztools.ui.aggregation.LogSumAggregation;
import es.imim.bg.ztools.ui.aggregation.MedianAggregation;
import es.imim.bg.ztools.ui.aggregation.MultAggregation;
import es.imim.bg.ztools.ui.aggregation.SumAggregation;
import es.imim.bg.ztools.ui.dialogs.SortRowsDialog;
import es.imim.bg.ztools.ui.dialogs.SortRowsDialog.AggregationType;
import es.imim.bg.ztools.ui.dialogs.SortRowsDialog.SortCriteria;
import es.imim.bg.ztools.ui.dialogs.SortRowsDialog.SortDirection;
import es.imim.bg.ztools.ui.model.table.ITable;
import es.imim.bg.ztools.ui.model.table.ITableContents;

public class SortRowsAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;
	private ITable table;
	private ITableContents contents;
	private List<SortCriteria> criteriaList;

	public SortRowsAction() {
		super("Sort rows by ...");	
		setDesc("Sort rows by ...");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		this.table = getTable();
		if (table == null)
			return;
		
		this.contents = table.getContents();
		
		//select properties
		List<IElementProperty> cellProps = table.getCellAdapter().getProperties();
		ListIterator<IElementProperty> i = cellProps.listIterator();
		Object[] props = new Object[cellProps.size()];
		int counter = 0;
		while (i.hasNext()) {
			IElementProperty ep = i.next();
			props[counter] = ep.getName();
			counter++;
		}
				
		SortRowsDialog d = new SortRowsDialog(AppFrame.instance(), props);
		this.criteriaList = d.getValueList();
		boolean allCols = d.considerAllColumns();
		boolean allRows = d.considerAllRows();
		
		if(criteriaList != null) {
			//cols
			int colCount;
			int[] columns = null;
			if(table.getSelectedColumns().length == 0 || allCols) {
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
			AppFrame.instance()
				.setStatusText("Rows sorted.");
		}
	}

	private Integer[] sortRows(final int[] selectedColumns,
								final Integer[] indices) {
		
		final List<IAggregation> aggregations = new ArrayList<IAggregation>();
		final List<Integer> properties = new ArrayList<Integer>();
		final List<Integer> directions = new ArrayList<Integer>();
		
		final int criterias = criteriaList.size();
		
		for (int i = 0; i < criteriaList.size(); i++) {
			SortCriteria sortCriteria = criteriaList.get(i);
			
			properties.add(sortCriteria.getPropertyIndex());
			
			SortDirection sd = sortCriteria.getDirection();
			Integer direction = (sd.equals(SortDirection.ASC)) ? 1 : -1;
			directions.add(direction);
						
			AggregationType at = sortCriteria.getAggregation();
			switch (at) {
			case MULTIPLICATION:
				aggregations.add(new MultAggregation());
				break;
			case LOGSUM:
				aggregations.add(new LogSumAggregation());
				break;
			case MEDIAN:
				aggregations.add(new MedianAggregation());
				break;
			case SUM:
				aggregations.add(new SumAggregation());
				break;
			}
		}
		
		final int N = selectedColumns.length;
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
						int col = selectedColumns[i];
						
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
					aggr1 = aggregations.get(level).aggregate(row1);
					aggr2 = aggregations.get(level).aggregate(row2);
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
